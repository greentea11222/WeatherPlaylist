package com.example.demo.dto;

import lombok.Data;

@Data
public class WeatherResponse {
	private String cityName;
	
	private CUrrentWeather current;
}
