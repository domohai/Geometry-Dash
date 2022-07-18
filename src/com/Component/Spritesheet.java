package com.Component;
import com.dataStructure.AssetPool;
import java.util.ArrayList;
import java.util.List;

public class Spritesheet {
	public List<Sprite> sprites;
	public int tileW, tileH, spacing;
	
	public Spritesheet(String file_path, int tileW, int tileH, int spacing, int cols, int size) {
		this.tileW = tileW;
		this.tileH = tileH;
		this.spacing = spacing;
		
		sprites = new ArrayList<>();
		Sprite parent = AssetPool.getSprite(file_path);
		int count = 0, row = 0;
		while(count < size) {
			for (int col = 0; col < cols; col++) {
				int x = (col * tileW) + (col * spacing);
				int y = (row * tileH) + (row * spacing);
				sprites.add(new Sprite(parent.image.getSubimage(x, y, tileW, tileH),
							row, col, count, file_path));
				
				count++;
				// necessary?
				if (count > size-1) break;
			}
			row++;
		}
	}
	
}
