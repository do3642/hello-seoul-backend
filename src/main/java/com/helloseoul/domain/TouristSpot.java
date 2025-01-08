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
	
	private String languageCode; // 언어코드
	
	private String title; // 관광지 이름
	
	private String addr1; // 주소
	
	private String addr2; // 상세주소
	
	private String tel; // 전화번호
	
	private String contentid; // 콘텐츠ID
	
	private String contenttypeid; // 콘텐츠타입 ID
	
	private String firstimage; // 대표이미지(원본)(500*333)
	
	private String firstimage2; // 대표 이미지(썸네일)(150*100)
	
	private double mapx; // GPS X좌표
	
	private double mapy; // GPS Y좌표
	

}
