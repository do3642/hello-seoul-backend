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
	
	// contenttypeid가 12 또는 15인 데이터만 찾기
    List<TouristSpot> findByContenttypeidIn(List<String> contentTypeIds);
    

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
    
    
    // 계절에 맞는 관광지 필터링 (전체 데이터에서 한글 계절 정보로 필터링)
    @Query(value = "SELECT * FROM tourist_spot " +
                   "WHERE title LIKE CONCAT('%', :season, '%') " +  // title에서 계절 텍스트 포함 여부 확인
                   "ORDER BY RAND()", nativeQuery = true)
    List<TouristSpot> findSeasonalTouristSpots(@Param("season") String season,  // 계절 정보
                                               Pageable pageable);

}
