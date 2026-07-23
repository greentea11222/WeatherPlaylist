package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.dto.WeatherResponse;

@Service
public class WeatherService {
	
	private final RestTemplate restTemplate = new RestTemplate();
	
	public WeatherResponse getWeather(String cityName) {
		// 都市名(cityName)に応じて、緯度(lat)と経度(lon)を切り替える
		// 大文字、小文字の違いを無視するために、toLowerCase()を使用する
		double latitude;
		double longitude;
		
		switch (cityName.toLowerCase()) {
		case "osaka" -> {
			latitude = 34.6937;
			longitude = 135.5023;
		}
		case "sapporo" -> {
			latitude = 43.0618;
			longitude = 141.3545;
		}
		case "okinawa" -> {
			latitude = 26.2124;
			longitude = 127.6809;
		}
		default -> { //デフォルトは東京
			latitude = 35.6785;
			longitude = 139.6823;
		}
		}
		
		// 緯度と経度を埋め込んだURLを作成する
		String url = String.format(
				"https://api.open-meteo.com/v1/forecast?latitude=%.4f&longitude=%.4f&current_temperature_2m,weather_code&timezone=Asia/Tokyo",
				latitude,
				longitude);
		
		// APIを呼び出して結果を返す
		return restTemplate.getForObject(url, WeatherResponse.class);
	}
}
