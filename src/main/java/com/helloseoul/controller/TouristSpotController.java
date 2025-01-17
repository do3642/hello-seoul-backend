package com.helloseoul.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.helloseoul.service.DistrictService;
import com.helloseoul.service.TouristSpotService;

@RestController
public class TouristSpotController {

	    @Autowired
	    private TouristSpotService touristSpotService;
	    
	    @Autowired
	    private DistrictService districtService;
		
	    @PostMapping("/api/touristspot")
	    public ResponseEntity<String> fetchAndSaveTouristSpots(@RequestParam String languageCode) {
	        try {
	            touristSpotService.fetchAndSaveTouristSpots(languageCode);
	            return ResponseEntity.ok("데이터 저장이 완료되었습니다! 홈으로 돌아가려면 버튼을 누르세요.");
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("데이터 저장 중 오류가 발생했습니다.");
	        }
	    }

	    @PostMapping("/api/districts")
	    public ResponseEntity<String> fetchAndSaveDistricts() {
	        try {
	            districtService.fetchAndSaveDistricts();
	            return ResponseEntity.ok("지역 데이터 저장이 완료되었습니다!");
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("지역 데이터 저장 중 오류가 발생했습니다.");
	        }
	    }
	    
	    @PostMapping("/api/touristdateSave")
	    public ResponseEntity<String> fetchAndSaveTouristDateDetails() {
	        try {
	            touristSpotService.fetchAndSaveTouristDateDetails();
	            return ResponseEntity.ok("날짜 관련 데이터 저장 성공!");
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("날짜 관련 데이터 저장 중 오류가 발생했습니다.");
	        }
	    }
	    
	    
	    
	}

