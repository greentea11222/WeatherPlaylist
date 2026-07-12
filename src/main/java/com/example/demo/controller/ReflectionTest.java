package com.example.demo.controller;

import java.lang.reflect.Field;

import com.example.demo.dto.Music;

public class ReflectionTest {
	public static void main(String[] args) throws Exception{
		
		Music music = new Music();
		music.setTitle("曲名");
		System.out.println("①普通に取得:" + music.getTitle());
		
		Class<?> clazz = music.getClass();
		Field titleField = clazz.getDeclaredField("title");
		titleField.setAccessible(true);
		titleField.set(music, "リフレクションで書き換えた秘密の曲名");
		System.out.println("②魔法で書き換え後:" + music.getTitle());
	}
}
