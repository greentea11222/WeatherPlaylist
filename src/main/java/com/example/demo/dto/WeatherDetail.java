package com.example.demo.dto;

import java.time.LocalTime;

import lombok.Data;

@Data
public class WeatherDetail {
	//時間
	private LocalTime time;
	
	//天気
	private String status;
	
	//気温
	private double temperature;
}
