package com.helloseoul.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    
    public List<TouristSpot> getRandomTouristSpots(String languageCode) {
        // 랜덤 4개 관광지 데이터 가져오기 (구 이름도 함께)
        Pageable pageable = PageRequest.of(0, 4);  // 첫 페이지, 4개 아이템
        List<TouristSpot> touristSpots = touristSpotRepository.findRandomTouristSpotsByLanguage(languageCode, pageable);

        // 구 이름을 한번에 가져오는 방법을 추가하거나, 여기서 각 관광지마다 구 이름을 설정
        return touristSpots.stream()
                .map(touristSpot -> {
                    Optional<DistrictEntity> district = districtRepository.findByCode(touristSpot.getSigungucode());
                    district.ifPresent(value -> touristSpot.setGuName(value.getName())); // 구 이름 설정
                    return touristSpot;
                })
                .collect(Collectors.toList());
    }
}
