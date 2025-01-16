package com.helloseoul.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
	public List<TouristSpot> getTouristSpots(@RequestParam("lang") String languageCode, @RequestParam("page") int page,
			@RequestParam("size") int size,
			@RequestParam(value = "excludeIds", required = false) List<Integer> excludeIds) {
		return spotsService.getRandomTouristSpots(languageCode, page, size, excludeIds);
	}

	// 축제 데이터 요청
	@GetMapping("/festivals")
	public List<TouristSpot> getFestivals(@RequestParam("lang") String languageCode, @RequestParam("page") int page,
			@RequestParam("size") int size,
			@RequestParam(value = "excludeIds", required = false) List<Integer> excludeIds) {
		return spotsService.getFestivalsByLanguage(languageCode, page, size, excludeIds);
	}

	// 계절 데이터 요청
	@GetMapping("/season-spots")
	public List<TouristSpot> getSeason(@RequestParam("seasonKR") String seasonKR) {

		return spotsService.getSeasonalTouristSpots(seasonKR);
	}

	// 검색 요청 처리

	@GetMapping("/search")
	public List<TouristSpot> searchTouristSpots(@RequestParam("search") String searchQuery) {
		
		return spotsService.getSearchList(searchQuery);
		
	}
	
    @GetMapping("/tourist-detail/{contentid}")
    public ResponseEntity<TouristSpot> getTouristSpotDetails(@PathVariable String  contentid) {
        try {
            TouristSpot touristSpot = spotsService.getTouristSpotDetails(contentid); // 서비스에서 상세 정보 조회
   
            if (touristSpot != null) {
                return ResponseEntity.ok(touristSpot); // 데이터가 있으면 200 OK와 함께 반환
            } else {
                return ResponseEntity.notFound().build(); // 데이터가 없으면 404 Not Found 반환
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null); // 예외 발생 시 500 서버 오류 반환
        }
    }

}
