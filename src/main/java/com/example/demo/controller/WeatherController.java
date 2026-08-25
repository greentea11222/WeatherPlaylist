package com.example.demo.controller;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
	public String getRecommendView(
			@RequestParam(value = "city", required = false, defaultValue = "Tokyo") String cityName, 
			@RequestParam(value = "lat", required = false) Double lat,
			@RequestParam(value = "lon", required = false) Double lon,
			org.springframework.ui.Model model){
		
		WeatherResponse response;
		
		// 緯度・経度が直接送られてきた場合はそれを使用し、それ以外の場合は都市名から取得
		if(lat != null && lon != null) {
			response = weatherService.getWeatherByCoordinates(lat, lon);
			cityName = "現在地";
		} else {
			response = weatherService.getWeather(cityName);
		}
		
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
		
		// 現在の時刻を取得して時間帯を判定する
		LocalTime now = LocalTime.now();
		int hour = now.getHour();
		
		String timeOfDay;
		if (hour >= 5 && hour < 11) {
			//朝
			timeOfDay = "MORNING";
		} else if (hour >= 11 && hour < 18) {
			//昼
			timeOfDay = "DAY";
		} else {
			//夜
			timeOfDay = "NIGHT";
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
		
		// -------------------------------------------
		// 雨用①：寒い雨のしっとり曲
		// -------------------------------------------
		Music music3 = new Music();
		music3.setTitle("Rain");
		music3.setArtist("雨");
		music3.setYoutubeUrl("https://www.youtube.com/embed/mPZkdNFkNps");
		
		List<Music> rainyColdMusicList = new ArrayList<>();
		rainyColdMusicList.add(music3);
		
		Playlist list3 = new Playlist();
		list3.setTitle("冷たい雨の日に聴きたいジャズ");
		list3.setStatus("RAINY");
		list3.setMood("COLD");
		list3.setMusicList(rainyColdMusicList);
		allPlaylists.add(list3);
		
		// -------------------------------------------
		// 霧用
		// -------------------------------------------		
		Music music4 = new Music();
		music4.setTitle("Misty Road");
		music4.setArtist("Lo-Fi Chill");
		music4.setYoutubeUrl("https://www.youtube.com/embed/Dx5qFacd3-E");
		
		List<Music> foggyMusicList = new ArrayList<>();
		foggyMusicList.add(music4);
		
		Playlist list4 = new Playlist();
		list4.setTitle("幻想的な霧のロファイ");
		list4.setStatus("FOGGY");
		list4.setMood("MILD");
		list4.setMusicList(foggyMusicList);
		allPlaylists.add(list4);
		
		// -------------------------------------------
		// 雪用
		// -------------------------------------------		
		Music music5 = new Music();
		music5.setTitle("White Magic");
		music5.setArtist("しんしん");
		music5.setYoutubeUrl("https://www.youtube.com/embed/q76bMs-NwRk");
		
		List<Music> snowyMusicList = new ArrayList<>();
		snowyMusicList.add(music5);
		
		Playlist list5 = new Playlist();
		list5.setTitle("しんしんと降る雪のバラード");
		list5.setStatus("SNOWY");
		list5.setMood("COLD");
		list5.setMusicList(snowyMusicList);
		allPlaylists.add(list5);
		
		//全てのプレイリストから、天気が一致するものを絞り込む
		List<Playlist> matchedPlaylists = allPlaylists.stream()
				.filter(playlist -> playlist.getStatus().equals(currentStatus))
				.filter(playlist -> playlist.getMood().equals(moodTag))
				.toList();
		
		//もし天気・気温に完全一致するものがない場合は、天気の一致だけで探す
		if (matchedPlaylists.isEmpty()) {
			matchedPlaylists = allPlaylists.stream()
					.filter(playlist -> playlist.getStatus().equals(currentStatus))
					.collect(Collectors.toList());
		}
		
		//天気と気温に応じたおでかけアドバイスを作成
		String adviceMessage;
		if (currentStatus.equals("RAINY")) {
			adviceMessage = "☔️雨が降っています。傘を持って出かけましょう！"; 
		} else if (currentStatus.equals("SNOWY")) {
			adviceMessage = "❄️雪が降っています。足元に気をつけて、しっかり防寒して出かけましょう。";
		} else if (temperature >= 28.0) {
			adviceMessage = "🥤厳しい暑さになりそうです。こまめな水分補給と熱中症対策を！";
		} else if (temperature <= 10.0) {
			adviceMessage = "🧥肌寒い日になりそうです。暖かい上着を持ってお出かけください。";
		} else if (timeOfDay.equals("MORNING") && currentStatus.equals("SUNNY")) {
			adviceMessage = "☕️爽やかな朝です！気持ちの良い一日のスタートを";
		} else if (timeOfDay.equals("NIGHT")) {
			adviceMessage = "🌙今夜は冷え込む可能性があります。暖かくしてお過ごしください。";
		} else {
			adviceMessage = "✨お出かけにぴったりの心地よいお天気です！素敵な1日を！";
		}
		
		// 抽出されたプレイリストや曲をランダムにシャッフルする
		if (!matchedPlaylists.isEmpty()) {
			// プレイリスト自体をランダムに並び替え
			Collections.shuffle(matchedPlaylists);
			
			// 各プレイリストの中身の曲もランダムに並び替え
			for (Playlist p : matchedPlaylists) {
				if (p.getMusicList() != null) {
					Collections.shuffle(p.getMusicList());
				}
			}
		}
		
		// 画面（HTML）にデータを渡す
		model.addAttribute("cityName", cityName);
		model.addAttribute("temperature", temperature);
		model.addAttribute("currentStatus", currentStatus);
		model.addAttribute("timeOfDay", timeOfDay);
		model.addAttribute("adviceMessage", adviceMessage);
		model.addAttribute("playlists", matchedPlaylists);
		
		// templates/recommend.htmlを表示する
		return "recommend";
	}
}
