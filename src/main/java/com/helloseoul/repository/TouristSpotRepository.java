package com.helloseoul.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.helloseoul.domain.TouristSpot;
import com.helloseoul.dto.TouristSpotWithDateDTO;

@Repository
public interface TouristSpotRepository extends JpaRepository<TouristSpot, Integer> {
	
	// 언어 코드로 검색하며 페이지네이션 지원
	Page<TouristSpot> findByLanguageCode(String languageCode, Pageable pageable);
	
	// 언어 코드별 전체 관광지 데이터 조회
	List<TouristSpot> findByLanguageCode(String languageCode);
	   // 언어 코드 및 contenttypeid 조건에 따른 관광지 데이터 조회
    @Query("SELECT t FROM TouristSpot t WHERE t.languageCode = :languageCode " +
           "AND ((:languageCode = 'kor' AND t.contenttypeid IN (12, 15)) " +
           "OR (:languageCode <> 'kor' AND t.contenttypeid IN (76, 85)))")
    List<TouristSpot> findByLanguageCodeAndContentTypeId(@Param("languageCode") String languageCode);

	// contenttypeid가 12 또는 15인 데이터만 찾기
	List<TouristSpot> findByContenttypeidIn(List<String> contentTypeIds);

	// language_code에 맞는 랜덤 관광지 4개를 가져오는 쿼리
	@Query(value = "SELECT * FROM tourist_spot " + "WHERE language_code = :languageCode " + "AND contenttypeid = CASE "
			+ "WHEN :languageCode = 'kor' THEN 12 " + "WHEN :languageCode IN ('eng', 'jpn', 'chs') THEN 76 "
			+ "ELSE 12 END " + "AND (COALESCE(:excludeIds) IS NULL OR id NOT IN :excludeIds) "
			+ "ORDER BY RAND()", nativeQuery = true)
	List<TouristSpot> findRandomTouristSpotsByLanguage(@Param("languageCode") String languageCode,
			@Param("excludeIds") List<Integer> excludeIds, Pageable pageable);

	// 언어 코드에 따라 contenttypeid 조건을 다르게 설정하는 쿼리
	@Query(value = "SELECT * FROM tourist_spot " + "WHERE language_code = :languageCode " + "AND contenttypeid = CASE "
			+ "WHEN :languageCode = 'kor' THEN 15 " + "WHEN :languageCode IN ('eng', 'jpn', 'chs') THEN 85 "
			+ "ELSE 15 END " + "AND (COALESCE(:excludeIds) IS NULL OR id NOT IN :excludeIds) "
			+ "ORDER BY RAND()", nativeQuery = true)
	List<TouristSpot> findFestivalsByLanguage(@Param("languageCode") String languageCode,
			@Param("excludeIds") List<Integer> excludeIds, Pageable pageable);

//    // 계절에 맞는 관광지 필터링 (전체 데이터에서 한글 계절 정보로 필터링)
//    @Query(value = "SELECT * FROM tourist_spot " +
//                   "WHERE title LIKE CONCAT('%', :season, '%') " +  // title에서 계절 텍스트 포함 여부 확인
//                   "ORDER BY RAND()", nativeQuery = true)
//    List<TouristSpot> findSeasonalTouristSpots(@Param("season") String season,  // 계절 정보
//                                               Pageable pageable);
	@Query(value = "SELECT t.* FROM tourist_spot t " + "JOIN tourist_date td ON t.contentid = td.contentid " + "WHERE ("
			+ "    FLOOR((" + "        CASE "
			+ "            WHEN YEAR(STR_TO_DATE(td.eventstartdate, '%Y%m%d')) = YEAR(STR_TO_DATE(td.eventenddate, '%Y%m%d')) THEN "
			+ "                (MONTH(STR_TO_DATE(td.eventstartdate, '%Y%m%d')) + MONTH(STR_TO_DATE(td.eventenddate, '%Y%m%d'))) / 2 "
			+ "            ELSE "
			+ "                ((12 - MONTH(STR_TO_DATE(td.eventstartdate, '%Y%m%d'))) + MONTH(STR_TO_DATE(td.eventenddate, '%Y%m%d'))) / 2 + MONTH(STR_TO_DATE(td.eventstartdate, '%Y%m%d')) "
			+ "        END" + "    )) IN :targetMonths" + ") "
			+ "AND (MONTH(STR_TO_DATE(td.eventstartdate, '%Y%m%d')) IS NOT NULL "
			+ "AND MONTH(STR_TO_DATE(td.eventenddate, '%Y%m%d')) IS NOT NULL) " + "ORDER BY RAND()", nativeQuery = true)
	List<TouristSpot> findSeasonalTouristSpotsByMonths(@Param("targetMonths") List<Integer> targetMonths,
			Pageable pageable);

	// 검색 데이터 반환
	@Query(value = "SELECT * FROM tourist_spot WHERE LOWER(title) LIKE LOWER(CONCAT('%', :searchQuery, '%')) AND language_code = 'kor'", nativeQuery = true)
	List<TouristSpot> findTouristSpotsBySearchQuery(@Param("searchQuery") String searchQuery);

	// contentid로 관광지와 해당 날짜 정보를 조회합니다. TouristDate 정보가 없을 경우에도 TouristSpot은 조회됩니다.
	@Query("SELECT new com.helloseoul.dto.TouristSpotWithDateDTO(ts, td) FROM TouristSpot ts " +
	       "LEFT JOIN TouristDate td ON ts.contentid = td.contentid " +
	       "WHERE ts.contentid = :contentid")
	Optional<TouristSpotWithDateDTO> findTouristSpotWithDateByContentid(@Param("contentid") String contentid);

	// 구 이름이나 관광지 이름에 대해 LIKE 검색을 하고 페이징 처리
	Page<TouristSpot> findByTitleContainingOrGuNameContaining(String keyword, Pageable pageable);

}
