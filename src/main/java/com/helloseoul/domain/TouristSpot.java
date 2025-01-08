package com.helloseoul.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class TouristSpot {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String guName; // 구 이름
	private String lanCodeId; // 언어코드
	private String postSj; // 관광지 이름
	private String address; // 주소(지번주소)
	private String newAddress; // 주소(도로명주소)
	private String cmmnTelno; // 전화번호
	private String cmmnHmpgUrl; // 홈페이지 URL
	private String cmmnUseTime; // 운영시간
	private String cmmnBsnde; // 운영요일
	private String cmmnRstde; // 휴무일
	private String subwayInfo; // 오시는길(교통정보)
	private String tag; // 태그
	private String bfDesc; // 장애인 편의시설
	private String img; // 이미지
	

}
