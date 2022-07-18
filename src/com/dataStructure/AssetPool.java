package com.dataStructure;

import com.Component.Sprite;
import com.Component.Spritesheet;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class AssetPool {

	static Map<String, Sprite> sprites = new HashMap<>();
	static Map<String, Spritesheet> spritesheets = new HashMap<>();
	
	public static boolean exist_sprite(String file_path) {
		File file = new File(file_path);
		return AssetPool.sprites.containsKey(file.getAbsolutePath());
	}
	
	public static boolean exist_spritesheet(String file_path) {
		File file = new File(file_path);
		return AssetPool.spritesheets.containsKey(file.getAbsolutePath());
	}
	
	public static Sprite getSprite(String file_path) {
		File file = new File(file_path);
		if (AssetPool.exist_sprite(file.getAbsolutePath())) {
			// user can pass in "assets/pic1.png" or "pic1.png"
			// we use get absolute path to accept the path in any form
			return AssetPool.sprites.get(file.getAbsolutePath());
		} else {
			Sprite sprite = new Sprite(file.getAbsolutePath());
			AssetPool.addSprite(file.getAbsolutePath(), sprite);
		}
		return AssetPool.sprites.get(file.getAbsolutePath());
	}
	
	public static Spritesheet getSpritesheet(String file_path) {
		File file = new File(file_path);
		if (AssetPool.exist_spritesheet(file.getAbsolutePath())) {
			return AssetPool.spritesheets.get(file.getAbsolutePath());
			
		} else {
			System.out.println("Spritesheet" + file_path + "does not exist");
			System.exit(-1);
		}
		return null;
	}
	
	public static void addSprite(String file_path, Sprite sprite) {
		File file = new File(file_path);
		if (!AssetPool.exist_sprite(file.getAbsolutePath())) {
			AssetPool.sprites.put(file.getAbsolutePath(), sprite);
		} else {
			System.out.println("already exist" + file.getAbsolutePath());
			System.exit(-1);
		}
	}
	
	public static void addSpritesheet(String file_path, int tileW, int tileH,
									  int spacing, int col, int size) {
		File file = new File(file_path);
		if (!AssetPool.exist_spritesheet(file.getAbsolutePath())) {
			Spritesheet spritesheet = new Spritesheet(file.getAbsolutePath(), tileW, tileH,
							spacing, col, size);
			AssetPool.spritesheets.put(file.getAbsolutePath(), spritesheet);
			
		}
		
		
	}
	
}
