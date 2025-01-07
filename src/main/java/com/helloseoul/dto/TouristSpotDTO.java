package com.helloseoul.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class TouristSpotDTO {
	
	private String lanCodeId;
	private String postSj;
	private String address;
	private String newAddress;
	private String tel;
	private String url;
	private String time;
	private String day;
	private String restDay;
	private String subwayInfo;
	private String tag;
	private String BF_DESC;
	
	@NoArgsConstructor
	@AllArgsConstructor
	public class TouristSpotResponseDTO {
		private List<TouristSpotDTO> row;
	}
}

