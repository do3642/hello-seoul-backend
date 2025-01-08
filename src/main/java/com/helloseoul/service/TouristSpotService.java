package com.helloseoul.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
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
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${tourist.api.key}")
	private String apiKey;
	
	public void fetchAndSaveTouristSpots() {
		String[] apiUrls = {
			String.format("http://apis.data.go.kr/B551011/KorService1/areaCode1?MobileOS=ETC&MobileApp=AppTest&serviceKey=%s&areaCode=1", apiKey),
			String.format("http://apis.data.go.kr/B551011/EngService1/areaBasedList1?MobileOS=ETC&MobileApp=AppTest&serviceKey=%s&areaCode=1", apiKey),
			String.format("http://apis.data.go.kr/B551011/JpnService1/areaBasedList1?MobileOS=ETC&MobileApp=AppTest&serviceKey=%s&areaCode=1", apiKey),
			String.format("http://apis.data.go.kr/B551011/ChsService1/areaBasedList1?MobileOS=ETC&MobileApp=AppTest&serviceKey=%s&areaCode=1", apiKey),
			String.format("http://apis.data.go.kr/B551011/ChtService1/areaBasedList1?MobileOS=ETC&MobileApp=AppTest&serviceKey=%s&areaCode=1", apiKey),
		};
		
		String[] languageCodes = {"ko", "en", "ja", "chs", "cht"};
		
		for(int i = 0; i < apiUrls.length; i++) {
			String apiUrl = apiUrls[i];
			String languageCode = languageCodes[i];
			
			String response = restTemplate.getForObject(apiUrl, String.class);
			
			Gson gson = new Gson();
			TouristSpotDTO[] touristSpots = gson.fromJson(response, TouristSpotDTO[].class);
			
			for(TouristSpotDTO dto : touristSpots) {
				TouristSpot touristSpot = new TouristSpot(dto, languageCode);
				
				int areaCode = dto.getAreacode();
				
			}
		}
	}
	
	
	
}