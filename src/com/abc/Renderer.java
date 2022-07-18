package com.abc;
import com.dataStructure.Transform;
import com.util.Vector2D;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Renderer {
	Map<Integer, List<GameObject>> gameObjects;
	//List<GameObject> gameObjects;
	Camera camera;
	
	public Renderer(Camera camera) {
		this.camera = camera;
		this.gameObjects = new HashMap<>();
	}
	
	public void render(Graphics2D g2D) {
		int minZindex = Integer.MAX_VALUE;
		int maxZindex = Integer.MIN_VALUE;
		for (Integer i : gameObjects.keySet()) {
			if (i < minZindex) minZindex = i;
			if (i > maxZindex) maxZindex = i;
		}
		int currentZindex = minZindex;
		while (currentZindex <= maxZindex) {
			if (gameObjects.get(currentZindex) == null) {
				currentZindex++;
				continue;
			}
			for (GameObject g : gameObjects.get(currentZindex)) {
				if (g.isUi) {
					g.draw(g2D);
				} else {
					Transform oldTransform = new Transform(g.transform.position);
					oldTransform.rotation = g.transform.rotation;
					oldTransform.scale = new Vector2D(g.transform.scale.x,
							g.transform.scale.y);
					
					g.transform.position = new Vector2D(g.transform.position.x - camera.position.x,
							g.transform.position.y - camera.position.y);
					
					g.draw(g2D);
					g.transform = oldTransform;
				}
			}
			currentZindex++;
		}
	}
	
	public void submit(GameObject gameObject) {
		gameObjects.computeIfAbsent(gameObject.zIndex, k -> new ArrayList<>());
		gameObjects.get(gameObject.zIndex).add(gameObject);
		
	}
	
	

}
