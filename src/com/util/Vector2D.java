package com.util;
import com.Files.Parser;
import com.Files.Serialize;

public class Vector2D extends Serialize {
	public double x, y;
	public Vector2D(double x, double y) {
		this.x = x;
		this.y = y;
		
	}
	
	public Vector2D copy() {
		return new Vector2D(this.x, this.y);
	}
	
	@Override
	public String serialize(int tabSize) {
		StringBuilder builder = new StringBuilder();
		builder.append(addDoubleProperty("x", x, tabSize, true, true));
		builder.append(addDoubleProperty("y", y, tabSize, true, false));
		return builder.toString();
	}
	
	public static Vector2D deserialize() {
		double x = Parser.consumeDoubleProperty("x");
		Parser.consume(',');
		double y = Parser.consumeDoubleProperty("y");
		return new Vector2D(x, y);
	}
}
