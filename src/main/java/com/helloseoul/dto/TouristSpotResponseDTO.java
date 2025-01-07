package com.helloseoul.dto;

import java.util.List;

public class TouristSpotResponseDTO {
	private List<TouristSpotDTO> row;
	
	public List<TouristSpotDTO> getRows() {
		return row;
	}
	
	public void setRows(List<TouristSpotDTO> row) {
		this.row = row;
	}

}
