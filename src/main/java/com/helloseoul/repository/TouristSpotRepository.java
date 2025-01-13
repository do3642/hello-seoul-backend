package com.helloseoul.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.helloseoul.domain.TouristSpot;

@Repository
public interface TouristSpotRepository extends JpaRepository<TouristSpot, Integer> {

    // language_code에 맞는 랜덤 관광지 4개를 가져오는 쿼리
    @Query(value = "SELECT * FROM tourist_spot WHERE language_code = :languageCode ORDER BY RAND()", nativeQuery = true)
    List<TouristSpot> findRandomTouristSpotsByLanguage(@Param("languageCode") String languageCode, Pageable pageable);
}
