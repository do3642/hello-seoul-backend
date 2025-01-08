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
	
	private final Gson gson = new Gson();
	
	public void fetchAndSaveTouristSpots() {
		// URL 호출하고 JSON 응답을 가져오기 위한 작업.
		RestTemplate restTemplate = new RestTemplate();
		
		int startIndex = 1;
		int endIndex = 1000;
		
		// 반복 여부를 결정하는 변수
		boolean hasMoreData = true;
		
		while (hasMoreData) {
	        try {
	            // API 호출
	            String apiUrl = String.format("http://openapi.seoul.go.kr:8088/%s/json/TbVwAttractions/%d/%d", apiKey, startIndex, endIndex);
	            // RestTemplate을 사용하여 Api 응답을 받아옴.
	            String response = restTemplate.getForObject(apiUrl, String.class);    

	            // JSON 파싱하여 데이터 처리
	            JsonObject jsonObject = gson.fromJson(response, JsonObject.class);
	            
	            // "TbVwAttractions"이 null인지 체크
	            JsonObject data = jsonObject.getAsJsonObject("TbVwAttractions");
	            if (data == null) {
	                System.out.println("Warning: 'TbVwAttractions' is missing or null in the response. Skipping this response.");
	                break;  // 'TbVwAttractions'가 없으면 건너뛰고 계속 진행
	            }
	            
	            // 총 데이터 개수를 가져옴.
	            int totalCount = data.get("list_total_count").getAsInt();
	            System.out.println("Total data count : " + totalCount);

	            // "row" 배열을 가져오기 전에 null 체크
	            JsonArray rows = data.getAsJsonArray("row");
	            if (rows == null || rows.size() == 0) {
	                System.out.println("Warning: No rows found for startIndex: " + startIndex + " to endIndex: " + endIndex);
	                break;  // 'row'가 없으면 건너뛰고 계속 진행
	            }

	            // 데이터 DB에 저장
	            for (int i = 0; i < rows.size(); i++) {
	                JsonObject row = rows.get(i).getAsJsonObject();
	                
	                // null 체크 후 DB에 저장
	                if (row != null) {
	                    TouristSpot spot = gson.fromJson(row, TouristSpot.class);
	                    
	                    // 구 이름 추출 및 설정
	                    String guName = AddressParser.extractGuName(spot.getAddress());
	                    spot.setGuName(guName);
	                    
	                    touristSpotRepository.save(spot);
	                } else {
	                    System.out.println("Warning: Row is null at index " + i);
	                }
	            }
	            
	            // 더 이상 가져올 데이터가 없으면 반복 종료
	            if(endIndex >= totalCount) {
	            	hasMoreData = false;
	            	System.out.println("Data fetching completed successfully.");
	            } else {
	            	
	            	// 다음 인덱스를 저장하기 위해 업데이트 해줌.
	            	startIndex += 1000;
	            	endIndex += 1000;
	            }
	            
	        } catch (Exception e) {
	            // 예외가 발생한 경우, 에러 메시지를 출력하고 계속 진행
	            System.err.println("Error occurred during data processing: " + e.getMessage());
	            e.printStackTrace();
	        }

	    }
	}
}