package com.helloseoul.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TouristSpotDTO {
	
	private String title;
	
	private String addr1;
	
	private String addr2;
	
	private int areacode;
	
	private String tel;
	
	private String contentid;
	
	private String contenttypeid;
	
	private String firstimage;
	
	private String firstimage2;
	
	private double mapx;
	
	private double mapy;

}

