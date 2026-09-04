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
		
			//検索結果が存在する場合
			if(geoResponse != null && geoResponse.getResults() != null && !geoResponse.getResults().isEmpty()) {
				GeocodingResponse.CityResult city = geoResponse.getResults().get(0);
				
				//正確な緯度・経度で天気を取得
				WeatherResponse weather = getWeatherByCoordinates(city.getLatitude(), city.getLongitude());
				
				//APIから返ってきた正確な都市名（例："Kyoto","Paris"など)をセット
				if(weather != null) {
					weather.setCityName(city.getName());
				}
				return weather;
			}
		
		} catch (Exception e) {
			System.out.println("Geocoding API呼び出しエラー" + e.getMessage());
		}
		
		//検索で見つからなかった場合やエラー時はデフォルト（東京：35.6895, 139.6917)へフォールバック
		WeatherResponse fallback = getWeatherByCoordinates(35.6895, 139.6917);
		if(fallback != null) {
			fallback.setCityName((query + "(見つからず東京を表示"));
		}
		return fallback;
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
