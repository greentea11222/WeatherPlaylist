package com.example.demo.dto;

import java.util.List;

import lombok.Data;

@Data
public class WeatherResponse {
	private String cityName;
	
	private List<WeatherDetail> weatherList;
}
