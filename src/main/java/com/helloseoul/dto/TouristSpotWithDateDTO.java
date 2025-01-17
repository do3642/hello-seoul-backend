package com.helloseoul.dto;

import com.helloseoul.domain.TouristDate;
import com.helloseoul.domain.TouristSpot;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TouristSpotWithDateDTO {
    private TouristSpot touristSpot;
    private TouristDate touristDate;
}
