package com.helloseoul.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.helloseoul.domain.TouristSpot;
import com.helloseoul.dto.TouristSpotDTO;
import com.helloseoul.dto.TouristSpotResponseDTO;
import com.helloseoul.repository.TouristSpotRepository;

@Service
public class TouristSpotService {
	
	@Autowired
	private TouristSpotRepository touristSpotRepository;
	
	@Value("${api.key}")
	private String apiKey;
	
	private String apiUrl = "http://openapi.seoul.go.kr:8088/{apiKey}/json/TbVwAttractions/{startIndex}/{endIndex}/";
	
	public void fetchAndSaveTouristSpots(int startIndex, int endIndex) {
		RestTemplate restTemplate = new RestTemplate();
		
		ResponseEntity<TouristSpotResponseDTO> response = restTemplate.getForEntity(apiUrl, apiKey, TouristSpotResponseDTO.class, startIndex, endIndex);
		
		if(response.getBody() != null) {
			TouristSpotResponseDTO data = response.getBody();
			int totalCount = data.getListTotalCount();
			
			for(TouristSpotDTO dto : data.getRow()) {
				TouristSpot spot = new TouristSpot();
				spot.setLanCodeId(dto.getLanCodeId());
				spot.setPostSj(dto.getPostSj());
				spot.setAddress(dto.getAddress());
				spot.setNewAddress(dto.getNewAddress());
				spot.setCmmnTelno(dto.getCmmnTelno());
				spot.setCmmnHmpgUrl(dto.getCmmnHmpgUrl());
				spot.setCmmnUseTime(dto.getCmmnUseTime());
				spot.setCmmnBsnde(dto.getCmmnBsnde());
				spot.setCmmnRstde(dto.getCmmnRstde());
				spot.setSubwayInfo(dto.getSubwayInfo());
				spot.setTag(dto.getTag());
				spot.setBfDesc(dto.getBfDesc());
				
				//DB 저장
				touristSpotRepository.save(spot);
			}
		}
		
	}
	
	public void fetchAllTouristSpots() {
		int startIndex = 1;
		int endIndex = 500;
		int totalDataCount = Integer.MAX_VALUE;
		
		// 전체 데이터를 가져올 때까지 반복
		while(startIndex <= totalDataCount) {
			fetchAndSaveTouristSpots(startIndex, endIndex);
			startIndex += 500;
			endIndex += 500;
			
			// 마지막 페이지 처리 (데이터가 500개 미만일 경우)
			if (endIndex > totalDataCount) {
				endIndex = totalDataCount;
			}
		}
	}
	

}
