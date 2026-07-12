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
	
	@GetMapping(value = "/recommend", produces = "application/json;charset=UTF-8")
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
		
		// 1曲ずつ曲のインスタンスを作る
		Music music1 = new Music();
		music1.setTitle("Adventure");
		music1.setArtist("はるかぜ");
		
		Music music2 = new Music();
		music2.setTitle("Sunny Day");
		music2.setArtist("Blue Sky");
		
		// 曲のリストを作成し、作った曲を詰める
		List<Music> sunnyMusicList = new ArrayList<>();
		sunnyMusicList.add(music1);
		sunnyMusicList.add(music2);
		
		// プレイリストのインスタンスを作り、タイトル・天気・曲リストをセットする
		Playlist list1 = new Playlist();
		list1.setTitle("晴れやか爽快ポップス");
		list1.setStatus("SUNNY");
		list1.setMusicList(sunnyMusicList);
		allPlaylists.add(list1);
		
		// 1曲ずつ曲のインスタンスを作る
		Music music3 = new Music();
		music3.setTitle("Rain");
		music3.setArtist("たっぽう");
		
		Music music4 = new Music();
		music4.setTitle("雨の午後");
		music4.setArtist("Grayyyy");
		
		// 曲のリストを作成し、作った曲を詰める
		List<Music> rainyMusicList = new ArrayList<>();
		rainyMusicList.add(music3);
		rainyMusicList.add(music4);
		
		// プレイリストのインスタンスを作り、タイトル・天気・曲リストをセットする
		Playlist list2 = new Playlist();
		list2.setTitle("雨の日の曲");
		list2.setStatus("RAINY");
		list2.setMusicList(rainyMusicList);
		allPlaylists.add(list2);
		
		//全てのプレイリストから、天気が一致するものを絞り込む
		List<Playlist> matchedPlaylists = allPlaylists.stream()
				.filter(playlist -> playlist.getStatus().equals(currentStatus))
				.toList();
		
		//絞り込んだプレイリストを返却
		return matchedPlaylists;
	}
}
