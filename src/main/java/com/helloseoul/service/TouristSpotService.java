package com.helloseoul.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.helloseoul.domain.DistrictEntity;
import com.helloseoul.domain.TouristSpot;
import com.helloseoul.dto.TouristSpotDTO;
import com.helloseoul.repository.DistrictRepository;
import com.helloseoul.repository.TouristSpotRepository;

@Service
public class TouristSpotService {
	
	@Autowired
	private TouristSpotRepository touristSpotRepository;
	
	@Autowired
	private DistrictRepository districtRepository;
	
	RestTemplate restTemplate = new RestTemplate();
	
	@Value("${tourist.api.key}")
	private String apiKey;
	
	public void fetchAndSaveTouristSpots() {
		String[] apiUrls = {
			String.format("http://apis.data.go.kr/B551011/KorService1/areaBasedList1?numOfRows=5000&MobileOS=ETC&MobileApp=AppTest&serviceKey=%s&_type=json&areaCode=1&pageNo=", apiKey)
			
		};
		
		String[] languageCodes = {"kor"};
		
		for (int i = 0; i < apiUrls.length; i++) { 
			String baseApiUrl = apiUrls[i];
			String languageCode = languageCodes[i];
			int pageNo = 1;
			
			while (true) {
				try {
					// URL을 URI 객체로 변환
					URI apiUri = new URI(baseApiUrl + pageNo);
					
					// API 호출
					String response = restTemplate.getForObject(apiUri, String.class);
					
					// JSON 파싱하여 DTO 객체 배열로 변환
					Gson gson = new Gson();
					JsonObject jsonObject = gson.fromJson(response, JsonObject.class);
					JsonObject responseBody = jsonObject.getAsJsonObject("response");
					JsonObject body = responseBody.getAsJsonObject("body");
					JsonObject items = body.getAsJsonObject("items");
					JsonArray itemArray = items.getAsJsonArray("item");
									
					if (itemArray == null || itemArray.size() == 0) {
						break;
					}
					
					// 데이터를 DB에 저장
					for (int j = 0; j < itemArray.size(); j++) {
						JsonObject item = itemArray.get(j).getAsJsonObject();
						
						TouristSpotDTO dto = gson.fromJson(item, TouristSpotDTO.class);
						
						TouristSpot touristSpot = new TouristSpot();
						
						// DTO 데이터를 TouristSpot에 매핑
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
				        
				        // 언어 코드 설정
				        touristSpot.setLanguageCode(languageCode);;
						
						// 구 이름 설정
						String sigunguCode = dto.getSigungucode();
						Optional<DistrictEntity> districtOptional = districtRepository.findByCode(sigunguCode);
						if (districtOptional.isPresent()) {
						    String districtName = districtOptional.get().getName();
						    touristSpot.setGuName(districtName);
						} else {
						    // 구체적인 예외 처리 또는 기본값 설정
						    touristSpot.setGuName("Unknown");
						    System.out.println("District not found for areaCode: " + sigunguCode);
						}
						// DB에 저장
						touristSpotRepository.save(touristSpot);
					}
					
					// 페이지 번호 증가
					pageNo++;
					
				} catch (URISyntaxException e) {
					e.printStackTrace();
					break;  // URI 파싱 오류 시 루프 종료
				}
			}
		}
	}
}
