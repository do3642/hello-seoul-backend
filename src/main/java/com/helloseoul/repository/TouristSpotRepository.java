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
    @Query(value = "SELECT * FROM tourist_spot " +
                   "WHERE language_code = :languageCode " +
                   "AND contenttypeid = CASE " +
                   "WHEN :languageCode = 'kor' THEN 12 " +
                   "WHEN :languageCode IN ('eng', 'jpn', 'chs') THEN 76 " +
                   "ELSE 12 END " +
                   "AND (COALESCE(:excludeIds) IS NULL OR id NOT IN :excludeIds) " +
                   "ORDER BY RAND()", nativeQuery = true)
    List<TouristSpot> findRandomTouristSpotsByLanguage(@Param("languageCode") String languageCode, 
            @Param("excludeIds") List<Integer> excludeIds, 
            Pageable pageable);
    
    // 언어 코드에 따라 contenttypeid 조건을 다르게 설정하는 쿼리
    @Query(value = "SELECT * FROM tourist_spot " +
                   "WHERE language_code = :languageCode " +
                   "AND contenttypeid = CASE " +
                   "WHEN :languageCode = 'kor' THEN 15 " +
                   "WHEN :languageCode IN ('eng', 'jpn', 'chs') THEN 85 " +
                   "ELSE 15 END " +
                   "AND (COALESCE(:excludeIds) IS NULL OR id NOT IN :excludeIds) " +
                   "ORDER BY RAND()", nativeQuery = true)
    List<TouristSpot> findFestivalsByLanguage(@Param("languageCode") String languageCode,
    		@Param("excludeIds") List<Integer> excludeIds,
    		Pageable pageable);
}
