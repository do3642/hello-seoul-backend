package com.helloseoul.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.helloseoul.domain.TouristSpot;
import com.helloseoul.repository.TouristSpotRepository;
import com.helloseoul.util.AddressParser;

@Service
public class TouristSpotService {
	@Autowired
	private TouristSpotRepository touristSpotRepository;
	
	@Value("${tourist.api.key}")
	private String apiKey;
	
	private String apiUrl = "http://openapi.seoul.go.kr:8088/{apiKey}/json/TbVwAttractions/{startIndex}/{endIndex}";
	
	private final Gson gson = new Gson();
	
	public void fetchAndSaveTouristSpots() {
		// URL 호출하고 JSON 응답을 가져오기 위한 작업.
		RestTemplate restTemplate = new RestTemplate();
		
		int startIndex = 1;
		int endIndex = 500;
		
		while (true) {
			// API 호출
			String url = String.format(apiUrl, apiKey, startIndex, endIndex);
			String response = restTemplate.getForObject(url, String.class);	
			
			// JSON 파싱하여 데이터 처리
			JsonObject jsonObject = gson.fromJson(response, JsonObject.class);
			JsonObject data = jsonObject.getAsJsonObject("TbVwAttractions");
			JsonArray rows = data.getAsJsonArray("row");
			
			// 더 이상 가져올 json 데이터가 없으면 종료
			if (rows == null || rows.size() == 0) {
				break;
			}
			
			// 데이터 DB에 저장
			for(int i = 0; i < rows.size(); i++) {
				JsonObject row = rows.get(i).getAsJsonObject();
				TouristSpot spot = gson.fromJson(row, TouristSpot.class);
				
				// 구 이름 추출 및 설정
				String guName = AddressParser.extractGuName(spot.getAddress());
				spot.setGuName(guName);
				
				touristSpotRepository.save(spot);
			}
			
			// 다음 인덱스를 저장하기 위해 업데이트 해줌.
			startIndex += 500;
			endIndex += 500;
		}
	}
	
}
