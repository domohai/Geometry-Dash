package com.abc;

import com.Component.*;
import com.Files.Parser;
import com.UI.MainContainer;
import com.dataStructure.AssetPool;
import com.dataStructure.Transform;
import com.util.Const;
import com.util.Vector2D;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class LevelEditorScene extends Scene {
	
	public GameObject player, ground;
	private Grid grid;
	private CameraController cameraController;
	public GameObject mouseCursor;
	private MainContainer editingButtons;
	
	public LevelEditorScene(String name) {
		super(name);
	}
	
	@Override
	public void init() {
		initAssetsPool();
		editingButtons = new MainContainer();
		
		grid = new Grid();
		cameraController = new CameraController();
		editingButtons.start();
		
		mouseCursor = new GameObject("mouse", new Transform(new Vector2D(0.0, 0.0)), 10);
		mouseCursor.addComponent(new SnapToGrid(Const.TILE_WIDTH, Const.TILE_HEIGHT));
		
		
		player = new GameObject("game object", new Transform(new Vector2D(300.0, 300.0)), 0);
		Spritesheet spritesheet1 = AssetPool.getSpritesheet("assets/layerOne.png");
		Spritesheet spritesheet2 = AssetPool.getSpritesheet("assets/layerTwo.png");
		Spritesheet spritesheet3 = AssetPool.getSpritesheet("assets/layerThree.png");
		PlayerComponent playerCom = new PlayerComponent(spritesheet1.sprites.get(0), spritesheet2.sprites.get(0),
				spritesheet3.sprites.get(0), Color.WHITE, Color.GREEN);
		player.addComponent(playerCom);
		
		ground = new GameObject("ground", new Transform(new Vector2D(0, Const.GROUND_Y)), 1);
		ground.addComponent(new Ground());
		ground.setNonserializable();
		player.setNonserializable();
		
		addGameObject(player);
		addGameObject(ground);
		
		
	}
	
	public void initAssetsPool() {
		AssetPool.addSpritesheet("assets/layerOne.png", Const.TILE_WIDTH, Const.TILE_HEIGHT, 2, 13, 13*5);
		AssetPool.addSpritesheet("assets/layerTwo.png", Const.TILE_WIDTH, Const.TILE_HEIGHT, 2, 13, 13*5);
		AssetPool.addSpritesheet("assets/layerThree.png", Const.TILE_WIDTH, Const.TILE_HEIGHT, 2, 13, 13*5);
		AssetPool.addSpritesheet("assets/groundSprites.png", Const.TILE_WIDTH, Const.TILE_HEIGHT, 2, 6, 12);
		AssetPool.addSpritesheet("assets/buttonSprites.png", 60, 60, 2, 2, 2);
		AssetPool.addSpritesheet("assets/ui/tabs.png", Const.TAB_WIDTH, Const.TAB_HEIGHT, 2, 6, 6);
		AssetPool.addSpritesheet("assets/spikes.png", Const.TILE_WIDTH, Const.TILE_HEIGHT, 2, 6, 4);
		AssetPool.addSpritesheet("assets/bigSprites.png", Const.TILE_WIDTH*2, Const.TILE_HEIGHT*2, 2, 2, 4);
		AssetPool.addSpritesheet("assets/smallBlocks.png", Const.TILE_WIDTH, Const.TILE_HEIGHT, 2, 6, 1);
		AssetPool.addSpritesheet("assets/portal.png", 44, 85, 2, 2, 2);
		
	}
	
	@Override
	public void update(double deltatime) {
		/*
		if (player.transform.position.x - camera.position.x > Const.CAMERA_OFFSET_X) {
			camera.position.x = player.transform.position.x - Const.CAMERA_OFFSET_X;
		}
		if (player.transform.position.y - camera.position.y > Const.CAMERA_OFFSET_Y) {
			camera.position.y = player.transform.position.y - Const.CAMERA_OFFSET_Y;
		}
		 */
		
		if (camera.position.y > Const.CAMERA_OFFSET_GROUND_Y) {
			camera.position.y = Const.CAMERA_OFFSET_GROUND_Y+70;
		}
		
		for (GameObject g : gameObjects) {
			g.update(deltatime);
		}
		
		cameraController.update(deltatime);
		grid.update(deltatime);
		editingButtons.update(deltatime);
		mouseCursor.update(deltatime);
		//
		if (Window.getWindow().key_listener.key_is_pressed(KeyEvent.VK_ENTER)) {
			// press enter to save data
			export("data");
		} else if (Window.getWindow().key_listener.key_is_pressed(KeyEvent.VK_L)) {
			// press L to load data
			importData("data");
		} else if (Window.getWindow().key_listener.key_is_pressed(KeyEvent.VK_P)) {
			// press P to switch to Level scene
			Window.getWindow().change_scene(GameState.LEVEL_SCENE);
		}
	}
	
	private void importData(String file_path) {
		Parser.openFile(file_path);
		GameObject go = Parser.parseGameObject();
		while (go != null) {
			addGameObject(go);
			go = Parser.parseGameObject();
		}
	}
	
	private void export(String name) {
		try {
			FileOutputStream fos = new FileOutputStream("Data/" + name + ".zip");
			ZipOutputStream zos = new ZipOutputStream(fos);
			zos.putNextEntry(new ZipEntry(name + ".json"));
			int i = 0;
			for (GameObject g : gameObjects) {
				String str = g.serialize(0);
				if (str.compareTo("") != 0) {
					zos.write(str.getBytes());
					if (i != gameObjects.size() - 1) {
						zos.write(",\n".getBytes());
					}
				}
				i++;
			}
			zos.closeEntry();
			zos.close();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	@Override
	public void draw(Graphics2D g2D) {
		g2D.setColor(Color.BLACK);
		g2D.fillRect(0, 0, Const.SCREEN_WIDTH, Const.SCREEN_HEIGHT);
		renderer.render(g2D);
		grid.draw(g2D);
		editingButtons.draw(g2D);
		
		// should be draw last
		mouseCursor.draw(g2D);
	}
	
	
}
