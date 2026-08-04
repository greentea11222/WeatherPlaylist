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
	public String getRecommendView(@RequestParam(value = "city", defaultValue = "Tokyo") String cityName, org.springframework.ui.Model model){
		
		//都市名を使って、天気サービスから全体のレスポンスを取得する
		WeatherResponse response = weatherService.getWeather(cityName);
		
		//天気コード（数字）を取得
		int weatherCode = response.getCurrent().getWeatherCode();
		
		// 気温を取得
		double temperature = response.getCurrent().getTemperature();
		
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
		
		//気温から雰囲気(mood)を判定
		String moodTag;
		if (temperature >= 25.0) {
			moodTag = "HOT";
		} else if (temperature <= 10.0) {
			moodTag = "COLD";
		} else {
			moodTag = "MILD";
		}
		
		//全てのプレイリスト一覧を作成
		List<Playlist> allPlaylists = new ArrayList<Playlist>();
		
		// -------------------------------------------
		// 晴れ用①：暑い日のサマーポップス
		// -------------------------------------------
		Music music1 = new Music();
		music1.setTitle("Adventure");
		music1.setArtist("はるかぜ");
		music1.setYoutubeUrl("https://www.youtube.com/embed/S_MOd40872I");
		
		// 曲のリストを作成し、作った曲を詰める
		List<Music> sunnyHotMusicList = new ArrayList<>();
		sunnyHotMusicList.add(music1);
		
		// プレイリストのインスタンスを作り、タイトル・天気・曲リストをセットする
		Playlist list1 = new Playlist();
		list1.setTitle("真夏の爽快ポップス");
		list1.setStatus("SUNNY");
		list1.setMood("HOT");
		list1.setMusicList(sunnyHotMusicList);
		allPlaylists.add(list1);
		
		// -------------------------------------------
		// 晴れ用②：快適・心地よい日のポップス
		// -------------------------------------------
		Music music2 = new Music();
		music2.setTitle("Sunny Day");
		music2.setArtist("Blue Sky");
		music2.setYoutubeUrl("https://www.youtube.com/results?search_query=Sunny+Day+Blue+Sky");
		
		List<Music> sunnyMildMusicList = new ArrayList<>();
		sunnyMildMusicList.add(music2);
		
		// プレイリストのインスタンスを作り、タイトル・天気・曲リストをセットする
		Playlist list2 = new Playlist();
		list2.setTitle("お散歩ポップス");
		list2.setStatus("SUNNY");
		list2.setMood("MILD");
		list2.setMusicList(sunnyMildMusicList);
		allPlaylists.add(list2);
		
		
		// 1曲ずつ曲のインスタンスを作る
		Music music3 = new Music();
		music3.setTitle("Rain");
		music3.setArtist("雨");
		music3.setYoutubeUrl("https://www.youtube.com/embed/mPZkdNFkNps");
		
		Music music4 = new Music();
		music4.setTitle("雨の午後");
		music4.setArtist("Grayyyy");
		music4.setYoutubeUrl("https://www.youtube.com/results?search_query=雨の午後+Grayyyy");
		
		// 曲のリストを作成し、作った曲を詰める
		List<Music> rainyMusicList = new ArrayList<>();
		rainyMusicList.add(music3);
		rainyMusicList.add(music4);
		
		
		// 霧の日用プレイリスト
		Music music5 = new Music();
		music5.setTitle("Misty Road");
		music5.setArtist("Lo-Fi Chill");
		music5.setYoutubeUrl("https://www.youtube.com/embed/Dx5qFacd3-E");
		
		List<Music> foggyMusicList = new ArrayList();
		foggyMusicList.add(music5);
		
		Playlist list3 = new Playlist();
		list3.setTitle("幻想的な霧のロファイ");
		list3.setStatus("FOGGY");
		list3.setMusicList(foggyMusicList);
		allPlaylists.add(list3);
		
		// 雪の日用
		Music music6 = new Music();
		music6.setTitle("White Magic");
		music6.setArtist("しんしん");
		music6.setYoutubeUrl("https://www.youtube.com/embed/q76bMs-NwRk");
		
		List<Music> snowyMusicList = new ArrayList<>();
		snowyMusicList.add(music6);
		
		Playlist list4 = new Playlist();
		list4.setTitle("しんしんと降る雪のバラード");
		list4.setStatus("SNOWY");
		list4.setMusicList(snowyMusicList);
		allPlaylists.add(list4);
		
		//全てのプレイリストから、天気が一致するものを絞り込む
		List<Playlist> matchedPlaylists = allPlaylists.stream()
				.filter(playlist -> playlist.getStatus().equals(currentStatus))
				.toList();
		
		// 画面（HTML）にデータを渡す
		model.addAttribute("cityName", cityName);
		model.addAttribute("temperature", temperature);
		model.addAttribute("currentStatus", currentStatus);
		model.addAttribute("playlists", matchedPlaylists);
		
		// templates/recommend.htmlを表示する
		return "recommend";
	}
}
