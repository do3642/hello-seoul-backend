package com.helloseoul.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.helloseoul.domain.TouristDate;

@Repository
public interface TouristDateRepository extends JpaRepository<TouristDate, Integer> {
	// contentid 목록만 가져오는 쿼리 메서드
    @Query("SELECT t.contentid FROM TouristDate t")
    List<String> findAllContentIds();
}

