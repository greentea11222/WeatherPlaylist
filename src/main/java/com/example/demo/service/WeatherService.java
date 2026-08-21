package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.dto.WeatherResponse;

@Service
public class WeatherService {
	
	private final RestTemplate restTemplate = new RestTemplate();
	
	public WeatherResponse getWeather(String cityName) {
		
		String cityKey = (cityName != null ) ? cityName.trim().toUpperCase() : "TOKYO";
		
		
		// 都市名(cityName)に応じて、緯度(lat)と経度(lon)を切り替える
		// 大文字、小文字の違いを無視するために、toLowerCase()を使用する
		double lat;
		double lon;
		
		switch (cityKey) {
		case "OSAKA" -> { lat = 34.6937; lon = 135.5023; }
		case "SAPPORO" -> { lat = 43.0618; lon = 141.3545; }
		case "OKINAWA", "NAHA" -> { lat = 26.2124; lon = 127.6809; }
		case "KYOTO" -> { lat = 35.0116; lon = 135.7681; }
		default -> { //デフォルトは東京
			lat = 35.6785;
			lon = 139.6823;
		}
		}
		
		// 緯度と経度を埋め込んだURLを作成する
		String url = String.format(
				"https://api.open-meteo.com/v1/forecast?latitude=%.4f&longitude=%.4f&current=temperature_2m,weather_code&timezone=Asia/Tokyo",
				lat,
				lon);
		
		// APIを呼び出して結果を返す
		return restTemplate.getForObject(url, WeatherResponse.class);
	}
}
