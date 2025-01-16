package com.helloseoul.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.helloseoul.domain.DistrictEntity;
import com.helloseoul.domain.TouristSpot;
import com.helloseoul.repository.DistrictRepository;
import com.helloseoul.repository.TouristSpotRepository;

@Service
public class SpotsService {
	
	
    @Autowired
    private TouristSpotRepository touristSpotRepository;

    @Autowired
    private DistrictRepository districtRepository;

    
    public List<TouristSpot> getRandomTouristSpots(String languageCode, int page, int size, List<Integer> excludeIds) {
        // 랜덤 4개 관광지 데이터 가져오기 (구 이름도 함께)
//        Pageable pageable = PageRequest.of(0, 4);  // 첫 페이지, 4개 아이템
    	Pageable pageable = PageRequest.of(page, size);
        List<TouristSpot> touristSpots = touristSpotRepository.findRandomTouristSpotsByLanguage(languageCode,excludeIds, pageable);

        // 구 이름을 한번에 가져오는 방법을 추가하거나, 여기서 각 관광지마다 구 이름을 설정
        return touristSpots.stream()
                .map(touristSpot -> {
                    Optional<DistrictEntity> district = districtRepository.findByCode(touristSpot.getSigungucode());
                    district.ifPresent(value -> touristSpot.setGuName(value.getName())); // 구 이름 설정
                    return touristSpot;
                })
                .collect(Collectors.toList());
    }
    
    public List<TouristSpot> getFestivalsByLanguage(String languageCode, int page, int size, List<Integer> excludeIds) {
    	Pageable pageable = PageRequest.of(page, size);
        List<TouristSpot> festivals = touristSpotRepository.findFestivalsByLanguage(languageCode,excludeIds, pageable);

        return festivals.stream()
                .map(touristSpot -> {
                    Optional<DistrictEntity> district = districtRepository.findByCode(touristSpot.getSigungucode());
                    district.ifPresent(value -> touristSpot.setGuName(value.getName())); // 구 이름 설정
                    return touristSpot;
                })
                .collect(Collectors.toList());
    }

    
    public List<TouristSpot> getSeasonalTouristSpots(String seasonKR) {
        List<Integer> targetMonths = new ArrayList<>();

        // 계절별 월 범위 설정
        switch (seasonKR) {
            case "봄": // 3, 4, 5월
                targetMonths = Arrays.asList(3, 4, 5);
                break;
            case "여름": // 6, 7, 8월
                targetMonths = Arrays.asList(6, 7, 8);
                break;
            case "가을": // 9, 10, 11월
                targetMonths = Arrays.asList(9, 10, 11);
                break;
            case "겨울": // 12, 1, 2월
                targetMonths = Arrays.asList(12, 1, 2);
                break;
            default:
                throw new IllegalArgumentException("Invalid season: " + seasonKR);
        }

        // 페이징 처리
        Pageable pageable = PageRequest.of(0, 6);  // 첫 번째 페이지, 6개 항목

        // DB에서 해당 범위의 월을 포함한 레코드 조회
        return touristSpotRepository.findSeasonalTouristSpotsByMonths(targetMonths, pageable);
    }

    public List<TouristSpot> getSearchList(String searchQuery) {
        return touristSpotRepository.findTouristSpotsBySearchQuery(searchQuery);
    }
    
    
    
    // 상세 정보 조회 메소드
    public TouristSpot getTouristSpotDetails(String  contentid) {
        Optional<TouristSpot> touristSpot = touristSpotRepository.findByContentid(contentid);
        // 관광지 정보가 있으면 반환, 없으면 null 반환
        return touristSpot.orElse(new TouristSpot());
    }

    
}
