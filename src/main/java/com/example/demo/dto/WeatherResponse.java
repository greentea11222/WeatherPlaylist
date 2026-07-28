package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class WeatherResponse {
	private String cityName;
	
	@JsonProperty("current")
	private CurrentWeather current;
}
