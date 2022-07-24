package com.abc;
import com.util.Vector2D;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public abstract class Scene {
	String name;
	public Camera camera;
	List<GameObject> gameObjects;
	Renderer renderer;
	
	public Scene(String name) {
		this.name = name;
		this.gameObjects = new ArrayList<>();
		this.camera = new Camera(new Vector2D(0.0, 0.0));
		this.renderer = new Renderer(this.camera);
		
	}
	
	public void init() {
	
	}
	public List<GameObject> getAllGameObject() {
		return gameObjects;
	}
	
	public void addGameObject(GameObject g) {
		gameObjects.add(g);
		renderer.submit(g);
		for (Component c : g.getAllComponent()) {
			c.start();
		}
	}
	public abstract void update(double deltatime);
	public abstract void draw(Graphics2D g2D);
	
	
}
