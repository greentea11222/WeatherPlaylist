package com.example.demo.service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.WeatherDetail;
import com.example.demo.dto.WeatherResponse;

@Service
public class WeatherService {
	public WeatherResponse getWeather(String cityName) {
		
		//リスポンスのインスタンスを生成
		WeatherResponse response = new WeatherResponse();
		
		//引数の都市名を設定
		response.setCityName(cityName);
		
		//天気インスタンス1を作成
		WeatherDetail detail1 = new WeatherDetail();
		detail1.setTime(LocalTime.of(10, 0));
		detail1.setStatus("SUNNY");
		detail1.setTemperature(25.5);
		
		//天気インスタンス2を生成
		WeatherDetail detail2 = new WeatherDetail();
		detail2.setTime(LocalTime.of(13, 0));
		detail2.setStatus("RAINY");
		detail2.setTemperature(19.0);
		
		//リストを生成
		List<WeatherDetail> list = new ArrayList<WeatherDetail>();
		
		//リストに詰める
		list.add(detail1);
		list.add(detail2);
		
		//リスポンスに設定
		response.setWeatherList(list);
		
		return response;
	}
}
