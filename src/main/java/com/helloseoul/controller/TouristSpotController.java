package com.helloseoul.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.helloseoul.domain.TouristSpot;
import com.helloseoul.service.DistrictService;
import com.helloseoul.service.TouristSpotService;

@RestController
public class TouristSpotController {

	    @Autowired
	    private TouristSpotService touristSpotService;
	    
	    @Autowired
	    private DistrictService districtService;
		
	    // 데이터 저장 시: 언어 코드 필수
	    @PostMapping("/api/touristspot")
	    public ResponseEntity<String> fetchAndSaveTouristSpots(@RequestParam String languageCode) {
	        try {
	            touristSpotService.fetchAndSaveTouristSpots(languageCode);
	            return ResponseEntity.ok("데이터 저장이 완료되었습니다! 홈으로 돌아가려면 버튼을 누르세요.");
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("데이터 저장 중 오류가 발생했습니다.");
	        }
	    }
	    
	    // 데이터 조회 시: 해당 언어 코드에 맞는 모든 관광지 데이터 반환(페이지네이션 포함)
	    @GetMapping("/api/touristspotdata")
	    public ResponseEntity<Page<TouristSpot>> getTouristSpots(
	    		@RequestParam String languageCode,
	    		@RequestParam(defaultValue = "0") int page,
	    		@RequestParam(defaultValue = "10") int size
	    		) {
	    	try {
	    		// 모든 관광지 가져오기
	    		Page<TouristSpot> touristSpots = touristSpotService.getTouristSpotsByLanguage(languageCode, page, size);
	    		return ResponseEntity.ok(touristSpots);
	    	} catch (Exception e) {
	    		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	    	}
	    }
	    
	    // 데이터 조회 시 : 언어코드에 맞는 전체 관광지 데이터 반환(페이지네이션 x)
	    @GetMapping("/api/alltouristspotdata")
	    public ResponseEntity<List<TouristSpot>> getAllTouristSpots (@RequestParam String languageCode) {
	    	List<TouristSpot> spots = touristSpotService.getTouristSpot(languageCode);
	    	return ResponseEntity.ok(spots);
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
	}
