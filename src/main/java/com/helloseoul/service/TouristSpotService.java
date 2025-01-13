package com.helloseoul.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
		return touristSpotRepository.findByLanguageCode(languageCode);
	}
	
}
