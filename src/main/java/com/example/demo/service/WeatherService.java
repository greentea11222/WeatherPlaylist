package com.example.demo.service;

import java.util.Locale;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.dto.GeocodingResponse;
import com.example.demo.dto.WeatherResponse;

@Service
public class WeatherService {
	
	//Geocoding APIを使用して、都市名から天気を自動取得する
	public WeatherResponse getWeather(String cityName) {
		
		String query = (cityName != null && !cityName.trim().isEmpty()) ? cityName.trim() : "Tokyo";
		
		RestTemplate restTemplate = new RestTemplate();
		
		//Open-Meteo Geocoding APIのURL
		String geoUrl = String.format("https://geocoding-api.open-meteo.com/v1/search?name=%s&count=1&language=en&format=json", query);
		
		
		
		try {
			GeocodingResponse geoResponse = restTemplate.getForObject(geoUrl, GeocodingResponse.class);
		} catch (Exception e) {
			System.out.println("Geocoding API呼び出しエラー" + e.getMessage());
		}
		
		// 都市名(cityName)に応じて、緯度(lat)と経度(lon)を切り替える
		// 大文字、小文字の違いを無視するために、toLowerCase()を使用する
		double lat = 1;
		double lon = 1;
	
		return getWeatherByCoordinates(lat, lon);
	}
	
	// 緯度と経度を使用してOpen-Meteo APIを呼び出す
	public WeatherResponse getWeatherByCoordinates(double lat, double lon) {
		
		RestTemplate restTemplate = new RestTemplate();
		
		// 緯度と経度を埋め込んだURLを作成する
				String url = String.format(
						Locale.US,
						"https://api.open-meteo.com/v1/forecast?latitude=%.4f&longitude=%.4f&current_weather=true",
						lat,
						lon);

		return restTemplate.getForObject(url, WeatherResponse.class);
	}
}
