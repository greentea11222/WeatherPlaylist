package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.dto.Music;
import com.example.demo.dto.Playlist;
import com.example.demo.dto.WeatherResponse;
import com.example.demo.service.WeatherService;

@Controller
@RequestMapping("/api")
public class WeatherController {
	
	//WeatherServiceのインスタンスを自動的に紐づけてくれる
	@Autowired
	private WeatherService weatherService;
	
	@GetMapping(value = "/recommend-view")
	public String getRecommendView(@RequestParam("city") String cityName, org.springframework.ui.Model model){
		
		//都市名を使って、天気サービスから全体のレスポンスを取得する
		WeatherResponse response = weatherService.getWeather(cityName);
		
		//天気コード（数字）を取得
		int weatherCode = response.getCurrent().getWeatherCode();
		
		//コードから天気を判定
		String currentStatus = switch (weatherCode) {
		
		// 晴れ
		case 0, 1, 2, 3 -> "SUNNY";
		
		// 霧
		case 45, 48 -> "FOGGY";
		
		// 雨
		case 51, 53, 55, 61, 63, 65, 80, 81, 82 -> "RAINY";
		
		// 雪
		case 71, 73, 75, 85, 86 -> "SNOWY";
		
		// その他
		default -> "SUNNY";
		
		};
		//全てのプレイリスト一覧を作成
		List<Playlist> allPlaylists = new ArrayList<Playlist>();
		
		// 1曲ずつ曲のインスタンスを作る
		Music music1 = new Music();
		music1.setTitle("Adventure");
		music1.setArtist("はるかぜ");
		music1.setYoutubeUrl("https://www.youtube.com/results?search_query=Adventure+はるかぜ");
		
		Music music2 = new Music();
		music2.setTitle("Sunny Day");
		music2.setArtist("Blue Sky");
		music2.setYoutubeUrl("https://www.youtube.com/results?search_query=Sunny+Day+Blue+Sky");
		
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
		music3.setArtist("雨");
		music3.setYoutubeUrl("https://www.youtube.com/results?search_query=Rain+雨");
		
		Music music4 = new Music();
		music4.setTitle("雨の午後");
		music4.setArtist("Grayyyy");
		music4.setYoutubeUrl("https://www.youtube.com/results?search_query=雨の午後+Grayyyy");
		
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
		
		// 画面（HTML）にデータを渡す
		model.addAttribute("playlists", matchedPlaylists);
		
		// templates/recommend.htmlを表示する
		return "recommend";
	}
}
