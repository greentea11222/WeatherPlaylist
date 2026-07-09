package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.Music;
import com.example.demo.dto.Playlist;
import com.example.demo.dto.WeatherResponse;
import com.example.demo.service.WeatherService;

@RestController
@RequestMapping("/api")
public class WeatherController {
	
	//WeatherServiceのインスタンスを自動的に紐づけてくれる
	@Autowired
	private WeatherService weatherService;
	
	@GetMapping("/recommend")
	public List<Playlist> getRecommendPlaylists(@RequestParam("city") String cityName){
		
		//都市名を使って、天気サービスから全体のレスポンスを取得する
		WeatherResponse response = weatherService.getWeather(cityName);
		
		//天気コード（数字）を取得
		int weatherCode = response.getCurrent().getWeatherCode();
		//天気（文字列）
		String currentStatus;
		
		if (weatherCode == 61) {
			currentStatus = "RAINY"; 
		} else {
			currentStatus = "SUNNY";
		}
		
		//全てのプレイリスト一覧を作成
		List<Playlist> allPlaylists = new ArrayList<Playlist>();
		
		Music music1 = new Music();
		music1.setTitle("Adventure");
		music1.setArtist("はるかぜ");
		
		
		//ダミーのプレイリストを作成して一覧に格納
		Playlist list1 = new Playlist();
		list1.setStatus("SUNNY");
		Playlist list2 = new Playlist();
		list2.setStatus("RAINY");
		allPlaylists.add(list1);
		allPlaylists.add(list2);
		
		//全てのプレイリストから、天気が一致するものを絞り込む
		List<Playlist> matchedPlaylists = allPlaylists.stream()
				.filter(playlist -> playlist.getStatus().equals(currentStatus))
				.toList();
		
		//絞り込んだプレイリストを返却
		return matchedPlaylists;
	}
}
