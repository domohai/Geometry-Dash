package com.dataStructure;
import com.Files.Parser;
import com.Files.Serialize;
import com.util.Vector2D;

public class Transform extends Serialize {
	public Vector2D position;
	public Vector2D scale;
	public double rotation;
	
	public Transform(Vector2D position) {
		this.position = position;
		this.scale = new Vector2D(1.0, 1.0);
		this.rotation = 0.0;
	}
	
	public Transform copy() {
		Transform newTransform = new Transform(this.position.copy());
		newTransform.scale = this.scale.copy();
		newTransform.rotation = this.rotation;
		return newTransform;
	}
	
	@Override
	public String serialize(int tabSize) {
		StringBuilder builder = new StringBuilder();
		// open transform
		builder.append(beginObjectProperty("Transform", tabSize));
		//
		builder.append(beginObjectProperty("Position", tabSize+1));
		builder.append(position.serialize(tabSize+2));
		builder.append(closeObjectProperty(tabSize+1));
		builder.append(addEnding(true, true));
		//
		builder.append(beginObjectProperty("Scale", tabSize+1));
		builder.append(scale.serialize(tabSize+2));
		builder.append(closeObjectProperty(tabSize+1));
		builder.append(addEnding(true, true));
		//
		//builder.append(beginObjectProperty("Rotation", tabSize+1));
		builder.append(addDoubleProperty("Rotation", rotation, tabSize+1, true, false));
		builder.append(closeObjectProperty(tabSize));
		//builder.append(addEnding(true, false));
		
		return builder.toString();
	}
	
	public static Transform deserialize() {
		Parser.consumeBeginObjectProperty("Transform");
		
		Parser.consumeBeginObjectProperty("Position");
		Vector2D pos = Vector2D.deserialize();
		Parser.consumeEndObjectProperty();
		Parser.consume(',');
		
		Parser.consumeBeginObjectProperty("Scale");
		Vector2D scale = Vector2D.deserialize();
		Parser.consumeEndObjectProperty();
		Parser.consume(',');
		
		
		Double rotation = Parser.consumeDoubleProperty("Rotation");
		Parser.consumeEndObjectProperty();
		
		Transform t = new Transform(pos);
		t.scale = scale;
		t.rotation = rotation;
		return t;
	}
}
