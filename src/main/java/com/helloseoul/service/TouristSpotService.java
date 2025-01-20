package com.helloseoul.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.helloseoul.domain.DistrictEntity;
import com.helloseoul.domain.TouristDate;
import com.helloseoul.domain.TouristSpot;
import com.helloseoul.dto.TouristSpotDTO;
import com.helloseoul.repository.DistrictRepository;
import com.helloseoul.repository.TouristDateRepository;
import com.helloseoul.repository.TouristSpotRepository;

@Service
public class TouristSpotService {
	
	@Autowired
	private TouristSpotRepository touristSpotRepository;
	
	@Autowired
	private DistrictRepository districtRepository;
	
	@Autowired
    private TouristDateRepository touristDateRepository;
	
	RestTemplate restTemplate = new RestTemplate();
	
	@Value("${tourist.api.key}")
	private String apiKey;
	
	private String getApiUrl(String languageCode) {
		switch(languageCode.toLowerCase()) {
			case "eng" :
				return String.format("http://apis.data.go.kr/B551011/EngService1/areaBasedList1?numOfRows=10000&MobileOS=ETC&MobileApp=AppTest&serviceKey=%s&_type=json&areaCode=1", apiKey);
			case "jpn" :
				return String.format("http://apis.data.go.kr/B551011/JpnService1/areaBasedList1?numOfRows=10000&MobileOS=ETC&MobileApp=AppTest&serviceKey=%s&_type=json&areaCode=1", apiKey);
			case "chs" :
				return String.format("http://apis.data.go.kr/B551011/ChsService1/areaBasedList1?numOfRows=10000&MobileOS=ETC&MobileApp=AppTest&serviceKey=%s&_type=json&areaCode=1", apiKey);
			default: // "kor"
				return String.format("http://apis.data.go.kr/B551011/KorService1/areaBasedList1?numOfRows=10000&MobileOS=ETC&MobileApp=AppTest&serviceKey=%s&_type=json&areaCode=1", apiKey);
		}
	}
	
	// 언어 코드에 맞는 데이터를 저장
	public void fetchAndSaveTouristSpots(String languageCode) {
		String apiUrl = getApiUrl(languageCode);
		
		try {
			URI apiUri = new URI(apiUrl);
			String response = restTemplate.getForObject(apiUri, String.class);
			
			Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(response, JsonObject.class);
            JsonObject responseBody = jsonObject.getAsJsonObject("response");
            JsonObject body = responseBody.getAsJsonObject("body");
            JsonObject items = body.getAsJsonObject("items");
            JsonArray itemArray = items.getAsJsonArray("item");
            
            if (itemArray == null || itemArray.size() == 0) {
                return;
            }
            
            for (int j = 0; j < itemArray.size(); j++) {
                JsonObject item = itemArray.get(j).getAsJsonObject();
                TouristSpotDTO dto = gson.fromJson(item, TouristSpotDTO.class);
                TouristSpot touristSpot = new TouristSpot();

                touristSpot.setSigungucode(dto.getSigungucode());
                touristSpot.setTitle(dto.getTitle());
                touristSpot.setAddr1(dto.getAddr1());
                touristSpot.setAddr2(dto.getAddr2());
                touristSpot.setTel(dto.getTel());
                touristSpot.setContentid(dto.getContentid());
                touristSpot.setContenttypeid(dto.getContenttypeid());
                touristSpot.setFirstimage(dto.getFirstimage());
                touristSpot.setFirstimage2(dto.getFirstimage2());
                touristSpot.setMapx(dto.getMapx());
                touristSpot.setMapy(dto.getMapy());

                touristSpot.setLanguageCode(languageCode);

                String sigunguCode = dto.getSigungucode();
                Optional<DistrictEntity> districtOptional = districtRepository.findByCode(sigunguCode);
                if (districtOptional.isPresent()) {
                    String districtName = districtOptional.get().getName();
                    touristSpot.setGuName(districtName);
                } else {
                    touristSpot.setGuName("Unknown");
                    System.out.println("District not found for areaCode: " + sigunguCode);
                }

                touristSpotRepository.save(touristSpot);
            }
			
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	public Page<TouristSpot> getTouristSpotsByLanguage(String languageCode, int page, int size) {
		// DB에서 언어 코드에 맞는 데이터를 조회.
		// 페이지네이션이 적용된 관광지 데이터를 반환
		return touristSpotRepository.findByLanguageCode(languageCode, PageRequest.of(page, size));
	}
	
	public List<TouristSpot> getTouristSpot(String languageCode) {
		return touristSpotRepository.findByLanguageCodeAndContentTypeId(languageCode);
	}

	
	public void fetchAndSaveTouristDateDetails() {
	    System.out.println("1. TouristSpot 데이터 가져오기 시작...");

	    // contenttypeid가 12 또는 15인 관광지 데이터만 가져옵니다.
	    List<String> contentTypeIds = Arrays.asList("12", "15");
	    List<TouristSpot> touristSpots = touristSpotRepository.findByContenttypeidIn(contentTypeIds);

	    if (touristSpots.isEmpty()) {
	        System.out.println("No tourist spots found with contenttypeid 12 or 15.");
	        return;
	    }

	    // 2. 이미 touristDate에 저장된 contentid 목록을 가져옵니다.
	    List<String> existingContentIds = touristDateRepository.findAllContentIds();

	    // 3. touristSpot 중에서 아직 touristDate에 저장되지 않은 contentid만 필터링
	    List<TouristSpot> newTouristSpots = touristSpots.stream()
	            .filter(spot -> !existingContentIds.contains(spot.getContentid()))
	            .collect(Collectors.toList());

	    if (newTouristSpots.isEmpty()) {
	        System.out.println("모든 TouristSpot이 이미 touristDate에 저장되어 있습니다.");
	        return;
	    }

	    // 4. 각 touristSpot에 대해 반복하여 API 호출 및 저장
	    for (TouristSpot spot : newTouristSpots) {
	        String contentid = spot.getContentid();
	        String contenttypeid = spot.getContenttypeid();

	        // 5. API URL 구성
	        String apiUrl = String.format("http://apis.data.go.kr/B551011/KorService1/detailIntro1?serviceKey=%s&numOfRows=1&pageNo=1&MobileOS=ETC&MobileApp=AppTest&_type=json&contentId=%s&contentTypeId=%s", apiKey, contentid, contenttypeid);

	        try {
	            System.out.println("6. API 호출: " + apiUrl);
	            // 6. API 호출
	            URI apiUri = new URI(apiUrl);
	            String response = restTemplate.getForObject(apiUri, String.class);

	            System.out.println("7. API 응답 받기 완료");

	            // 응답을 바로 출력하여 실제 구조를 확인합니다.
	            System.out.println("API 응답: " + response);

	            // 8. API 응답을 파싱하여 데이터 추출
	            Gson gson = new Gson();
	            JsonElement jsonElement = gson.fromJson(response, JsonElement.class);

	            if (jsonElement.isJsonObject()) {
	                JsonObject jsonObject = jsonElement.getAsJsonObject();
	                JsonObject responseBody = jsonObject.getAsJsonObject("response");

	                // 응답에서 'body' 객체가 있는지 확인
	                if (responseBody != null && responseBody.has("body")) {
	                    JsonObject body = responseBody.getAsJsonObject("body");
	                    JsonObject items = body.getAsJsonObject("items");

	                    if (items != null && items.has("item")) {
	                        JsonArray itemArray = items.getAsJsonArray("item");

	                        // 배열이 비어있지 않으면 첫 번째 아이템을 처리
	                        if (itemArray != null && itemArray.size() > 0) {
	                            JsonObject item = itemArray.get(0).getAsJsonObject();

	                            String eventstartdate = item.has("eventstartdate") ? item.get("eventstartdate").getAsString() : null;
	                            String eventenddate = item.has("eventenddate") ? item.get("eventenddate").getAsString() : null;
	                            String opendate = item.has("opendate") ? item.get("opendate").getAsString() : null;
	                            String useseason = item.has("useseason") ? item.get("useseason").getAsString() : null;

	                            System.out.println("8. 추출된 데이터: ");
	                            System.out.println("Event Start Date: " + eventstartdate);
	                            System.out.println("Event End Date: " + eventenddate);
	                            System.out.println("Open Date: " + opendate);
	                            System.out.println("Use Season: " + useseason);

	                            // 9. touristDateRepository에 저장
	                            TouristDate touristDate = new TouristDate();
	                            touristDate.setContentid(contentid);
	                            touristDate.setContenttypeid(contenttypeid);
	                            touristDate.setEventstartdate(eventstartdate);
	                            touristDate.setEventenddate(eventenddate);
	                            touristDate.setOpendate(opendate);
	                            touristDate.setUseseason(useseason);

	                            System.out.println("10. touristDateRepository에 저장 중...");
	                            touristDateRepository.save(touristDate);
	                            System.out.println("11. touristDateRepository에 저장 완료");
	                        }
	                    } else {
	                        System.out.println("API 응답에 'items' 또는 'item' 데이터가 없습니다.");
	                    }
	                } else {
	                    System.out.println("API 응답에 'body' 데이터가 없습니다.");
	                }
	            } else {
	                System.out.println("응답 데이터가 JsonObject 형식이 아닙니다. 실제 형식: " + jsonElement);
	            }

	        } catch (Exception e) {
	            System.out.println("에러 발생: " + e.getMessage());
	            e.printStackTrace();
	        }
	    }
	}


	

  }


