package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CurrentWeather {
	@JsonProperty("temperature_2m")
	private double temperature;
	
	@JsonProperty("weather_code")
	private int weatherCode;
}
