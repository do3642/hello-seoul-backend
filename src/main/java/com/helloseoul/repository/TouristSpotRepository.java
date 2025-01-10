package com.helloseoul.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.helloseoul.domain.TouristSpot;

@Repository
public interface TouristSpotRepository extends JpaRepository<TouristSpot, Integer>{
	// 언어 코드로 검색하며 페이지네이션 지원
	Page<TouristSpot> findByLanguageCode(String languageCode, Pageable pageable);
}
