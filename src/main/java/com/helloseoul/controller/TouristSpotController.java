package com.helloseoul.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.helloseoul.service.TouristSpotService;

@RestController
public class TouristSpotController {

	@Autowired
	private TouristSpotService touristSpotService;
	
	@GetMapping("/save-tourist-spots")
	public String fetchTouristSpots() {
		touristSpotService.fetchAndSaveTouristSpots();
		
		return "관광지가 성공적으로 저장되었습니다!";
	}
	
}
