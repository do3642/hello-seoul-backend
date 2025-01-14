package com.helloseoul.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.helloseoul.domain.TouristSpot;
import com.helloseoul.service.SpotsService;

@RestController
@RequestMapping("/api")
public class SpotsController {

	private final SpotsService spotsService;
	
	@Autowired
    public SpotsController(SpotsService spotsService) {
        this.spotsService = spotsService;
    }
	
    // 관광지 데이터 요청 API
    @GetMapping("/tourist-spots")
    public List<TouristSpot> getTouristSpots(
    		@RequestParam("lang") String languageCode,
    		@RequestParam("page") int page,
    		@RequestParam("size") int size,
    		@RequestParam(value = "excludeIds", required = false) List<Integer> excludeIds) {
        return spotsService.getRandomTouristSpots(languageCode, page, size,excludeIds);
    }
    
    // 축제 데이터 요청
    @GetMapping("/festivals")
    public List<TouristSpot> getFestivals(
    		@RequestParam("lang") String languageCode,
    		@RequestParam("page") int page,
    		@RequestParam("size") int size,
    		@RequestParam(value = "excludeIds", required = false) List<Integer> excludeIds) {
    	System.out.println(excludeIds);
        return spotsService.getFestivalsByLanguage(languageCode, page, size,excludeIds);
    }
    
    
    



}
