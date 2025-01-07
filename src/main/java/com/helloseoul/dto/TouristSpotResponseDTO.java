package com.helloseoul.dto;

import java.util.List;

import lombok.Data;

@Data
public class TouristSpotResponseDTO {
	private int listTotalCount;
	private List<TouristSpotDTO> row;
}
