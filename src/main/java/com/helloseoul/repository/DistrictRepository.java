package com.helloseoul.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.helloseoul.domain.DistrictEntity;


@Repository
public interface DistrictRepository extends JpaRepository<DistrictEntity, Integer> {
	Optional<DistrictEntity> findByCode(String areaCode);
}
