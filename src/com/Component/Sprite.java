package com.Component;
import com.Files.Parser;
import com.abc.Component;
import com.dataStructure.AssetPool;
import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;

public class Sprite extends Component {
	public String file_path;
	public int width, height;
	public BufferedImage image = null;
	public boolean isSubSprite = false;
	public int row, col, index;
	
	public Sprite(String file_path) {
		this.file_path = file_path;
		try {
			File file = new File(file_path);
			if (AssetPool.exist_sprite(file_path)) {
				throw new Exception("Asset already exist: " + file_path);
			}
			this.image = ImageIO.read(file);
			this.width = this.image.getWidth();
			this.height = this.image.getHeight();
		} catch(Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	public Sprite(BufferedImage image, String file_path) {
		this.image = image;
		this.width = image.getWidth();
		this.height = image.getHeight();
		this.file_path = file_path;
	}
	
	public Sprite(BufferedImage image, int row, int col, int index, String file_path) {
		this.image = image;
		this.width = image.getWidth();
		this.height = image.getHeight();
		this.row = row;
		this.col = col;
		this.index = index;
		this.isSubSprite = true;
		this.file_path = file_path;
	}
	
	@Override
	public void draw(Graphics2D g2D) {
		AffineTransform transform = new AffineTransform();
		transform.setToIdentity();
		transform.translate(gameObject.transform.position.x, gameObject.transform.position.y);
		transform.rotate(Math.toRadians(gameObject.transform.rotation), (width*gameObject.transform.scale.x)/2.0,
				height*gameObject.transform.scale.y/2.0);
		transform.scale(gameObject.transform.scale.x, gameObject.transform.scale.y);
		g2D.drawImage(image, transform, null);
	}
	
	@Override
	public Component copy() {
		if (!isSubSprite) return new Sprite(this.image, file_path);
		return new Sprite(this.image, this.row, this.col, this.index, file_path);
	}
	
	@Override
	public String serialize(int tabSize) {
		StringBuilder builder = new StringBuilder();
		
		builder.append(beginObjectProperty("Sprite", tabSize));
		builder.append(addBooleanProperty("isSubSprite", isSubSprite, tabSize+1, true, true));
		if (isSubSprite) {
			builder.append(addStringProperty("File path", file_path, tabSize+1, true, true));
			builder.append(addIntProperty("Row", row, tabSize+1, true, true));
			builder.append(addIntProperty("Column", col, tabSize+1, true, true));
			builder.append(addIntProperty("Index", index, tabSize+1, true, false));
			builder.append(closeObjectProperty(tabSize));
			return builder.toString();
		}
		builder.append(addStringProperty("File path", file_path, tabSize+1, true, false));
		builder.append(closeObjectProperty(tabSize));
		return builder.toString();
	}
	
	public static Sprite deserialize() {
		boolean isSubSprite = Parser.consumeBooleanProperty("isSubSprite");
		Parser.consume(',');
		String file_path = Parser.consumeStringProperty("File path");
		if (isSubSprite) {
			Parser.consume(',');
			Parser.consumeIntProperty("Row");
			Parser.consume(',');
			Parser.consumeIntProperty("Column");
			Parser.consume(',');
			int index = Parser.consumeIntProperty("Index");
			if (!AssetPool.exist_spritesheet(file_path)) {
				System.out.println("Spritesheet " + file_path + "has not been loaded");
				System.exit(-1);
			}
			Parser.consumeEndObjectProperty();
			return (Sprite)AssetPool.getSpritesheet(file_path).sprites.get(index).copy();
		}
		if (!AssetPool.exist_sprite(file_path)) {
			System.out.println("Sprite " + file_path + "has not been loaded");
			System.exit(-1);
		}
		Parser.consumeEndObjectProperty();
		return (Sprite)AssetPool.getSprite(file_path).copy();
	}
	
}
