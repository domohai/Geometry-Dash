package com.abc;
import com.Files.Serialize;
import java.awt.Graphics2D;

public abstract class Component<T> extends Serialize {
	
	public GameObject gameObject;
	
	public void update(double dt) {
		return;
	}
	public void draw(Graphics2D g2D) {
		return;
	}
	public void start() {
		return;
	}
	public abstract Component copy();

}
