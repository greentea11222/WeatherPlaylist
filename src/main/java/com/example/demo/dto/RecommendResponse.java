package com.example.demo.dto;

import java.util.List;

import lombok.Data;

@Data
public class RecommendResponse {
	private String cityName;
	Double temperature;
	private String status;
	private List<Playlist> playlist;
}
