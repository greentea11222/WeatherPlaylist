package com.example.demo.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GeocodingResponse {
	
	@JsonProperty("results")
	private List<CityResult> results;
	
	@Data
	private static class CityResult{
		private String name;
		private double latitude;
		private double longitude;
		private String country;
	}
}
