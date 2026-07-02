package com.example.demo.dto;

import java.util.List;

import lombok.Data;

@Data
public class Playlist {
	//プレイリストタイトル
	private String title;
	
	//プレイリストに紐づく天気
	private String status;
	
	//曲のリスト
	private List<Music> musicList;
}
