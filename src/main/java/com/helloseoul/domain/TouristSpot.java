package com.helloseoul.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.gson.annotations.SerializedName;

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
	
	@SerializedName("LANG_CODE_ID")
	private String langCodeId; // 언어코드
	
	@SerializedName("POST_SJ")
	private String postSj; // 관광지 이름
	
	@SerializedName("ADDRESS")
	private String address; // 주소(지번주소)
	
	@SerializedName("NEW_ADDRESS")
	private String newAddress; // 주소(도로명주소)
	
	@SerializedName("CMMN_TELNO")
	private String cmmnTelno; // 전화번호
	
	@SerializedName("CMMN_HMPG_URL")
	private String cmmnHmpgUrl; // 홈페이지 URL
	
	@SerializedName("CMMN_USE_TIME")
	@Column(length = 1000)
	private String cmmnUseTime; // 운영시간
	
	@SerializedName("CMMN_BSNDE")
	@Column(length = 1000)
	private String cmmnBsnde; // 운영요일
	
	@SerializedName("CMMN_RSTDE")
	@Column(length = 1000)
	private String cmmnRstde; // 휴무일
	
	@SerializedName("SUBWAY_INFO")
	@Column(length = 1000)
	private String subwayInfo; // 오시는길(교통정보)
	
	@SerializedName("TAG")
	@Column(length = 1000)
	private String tag; // 태그
	
	@SerializedName("BF_DESC")
	@Column(length = 1000)
	private String bfDesc; // 장애인 편의시설
	
	private String img; // 이미지
	

}
