package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.dto.WeatherResponse;

@Service
public class WeatherService {
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
		
		//外部のお天気API（Open-Meteo)のURL
		String url = "https://api.open-meteo.com/v1/forecast?latitude=35.6785&longitude=139.6823&current=temperature_2m,weather_code&timezone=Asia/Tokyo";
		
		//通信を行うためのRestTemplateインスタンスを生成
		RestTemplate restTemplate = new RestTemplate();
		
		//URLにアクセスしてJSONを取得し、WeatherResponseの構造に合わせて自動変換する
		WeatherResponse response = restTemplate.getForObject(url, WeatherResponse.class);
		
		//APIからは都市名が返ってこないので、引数で渡された都市名を設定する
		if (response != null) {
			response.setCityName(cityName);
		}
		
		return response;
	}
}
