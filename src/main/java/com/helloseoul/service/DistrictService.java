package com.helloseoul.service;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.helloseoul.domain.DistrictEntity;
import com.helloseoul.repository.DistrictRepository;

@Service
public class DistrictService {
	
	@Autowired
	 private DistrictRepository districtRepository;
	 
	 RestTemplate restTemplate = new RestTemplate();
	 
	 @Value("${tourist.api.key}")
	 private String apiKey;
	 
	   public void fetchAndSaveDistricts() throws URISyntaxException {
	       String apiUrl = String.format("http://apis.data.go.kr/B551011/KorService1/areaCode1?numOfRows=25&MobileOS=ETC&MobileApp=AppTest&serviceKey=%s&_type=json&areaCode=1", apiKey);
	       URI uri = new URI(apiUrl); 
	       String response = restTemplate.getForObject(uri, String.class);
	       
	       Gson gson = new Gson();
	       JsonObject jsonObject = gson.fromJson(response, JsonObject.class);
	       
	       JsonObject responseBody = jsonObject.getAsJsonObject("response");
	       // 'body' 안에 있는 'items' -> 'item' 배열 추출
	       JsonObject body = responseBody.getAsJsonObject("body");
	       JsonObject items = body.getAsJsonObject("items");
	       JsonArray itemArray = items.getAsJsonArray("item");  // item 배열을 가져옵니다.
	       
	       // items 안의 각 item을 처리
	       for (int i = 0; i < itemArray.size(); i++) {
	           JsonObject item = itemArray.get(i).getAsJsonObject();
	           
	           // item에서 필요한 데이터 추출
	           String code = item.get("code").getAsString();
	           String name = item.get("name").getAsString();
	           
	           // DTO 객체로 매핑 후 저장
	           DistrictEntity entity = new DistrictEntity();
	           entity.setCode(code);
	           entity.setName(name);
	           
	           districtRepository.save(entity);
	        }
	    }

}
