package com.abc;
import com.Component.*;
import com.Files.Parser;
import com.dataStructure.AssetPool;
import com.dataStructure.Transform;
import com.util.Const;
import com.util.Vector2D;
import java.awt.Graphics2D;
import java.awt.Color;

public class LevelScene extends Scene {
	public GameObject player;
	public BoxBounds playerBounds;
	
	public LevelScene(String name) {
		super(name);
	}
	
	@Override
	public void init() {
		initAssetsPool();
		
		player = new GameObject("game object", new Transform(new Vector2D(200.0, 200.0)), 0);
		Spritesheet spritesheet1 = AssetPool.getSpritesheet("assets/layerOne.png");
		Spritesheet spritesheet2 = AssetPool.getSpritesheet("assets/layerTwo.png");
		Spritesheet spritesheet3 = AssetPool.getSpritesheet("assets/layerThree.png");
		
		PlayerComponent playerCom = new PlayerComponent(spritesheet1.sprites.get(0), spritesheet2.sprites.get(0),
				spritesheet3.sprites.get(0), Color.WHITE, Color.GREEN);
		player.addComponent(playerCom);
		
		player.addComponent(new RigidBody(new Vector2D(Const.PLAYER_SPEED, 0)));
		
		playerBounds = new BoxBounds(Const.TILE_WIDTH, Const.TILE_HEIGHT);
		player.addComponent(playerBounds);
		
		//addGameObject(player);
		renderer.submit(player);
		initBackgrounds();
		
		importData("data");
	}
	
	public void initBackgrounds() {
		GameObject ground;
		ground = new GameObject("ground", new Transform(new Vector2D(0, Const.GROUND_Y)),1);
		ground.addComponent(new Ground());
		addGameObject(ground);
		int numBackground = 7;
		GameObject[] backgrounds = new GameObject[numBackground];
		GameObject[] groundBgs = new GameObject[numBackground];
		for (int i = 0; i < numBackground; i++) {
			ParallaxBackground bg = new ParallaxBackground("assets/backgrounds/bg01.png",
					backgrounds, ground.getComponent(Ground.class), false);
			int x = i * bg.sprite.width;
			int y = 0;
			GameObject g = new GameObject("background", new Transform(new Vector2D(x, y)), -10);
			g.setUi(true);
			g.addComponent(bg);
			backgrounds[i] = g;
			
			ParallaxBackground groundBg = new ParallaxBackground("assets/grounds/ground01.png",
						groundBgs, ground.getComponent(Ground.class), true);
			x = i * groundBg.sprite.width;
			y = bg.sprite.height;
			GameObject groundGo = new GameObject("groundBg", new Transform(new Vector2D(x, y)), -9);
			groundGo.addComponent(groundBg);
			groundGo.setUi(true);
			groundBgs[i] = groundGo;
			addGameObject(g);
			addGameObject(groundGo);
			
		}
		
	}
	
	public void initAssetsPool() {
		AssetPool.addSpritesheet("assets/layerOne.png", Const.TILE_WIDTH, Const.TILE_HEIGHT, 2, 13, 13*5);
		AssetPool.addSpritesheet("assets/layerTwo.png", Const.TILE_WIDTH, Const.TILE_HEIGHT, 2, 13, 13*5);
		AssetPool.addSpritesheet("assets/layerThree.png", Const.TILE_WIDTH, Const.TILE_HEIGHT, 2, 13, 13*5);
		AssetPool.addSpritesheet("assets/groundSprites.png", Const.TILE_WIDTH, Const.TILE_HEIGHT, 2, 6, 12);
		AssetPool.addSpritesheet("assets/buttonSprites.png", 60, 60, 2, 2, 2);
		
	}
	
	@Override
	public void update(double deltatime) {
		if (player.transform.position.x - camera.position.x > Const.CAMERA_OFFSET_X) {
			camera.position.x = player.transform.position.x - Const.CAMERA_OFFSET_X;
		}
		if (player.transform.position.y - camera.position.y > Const.CAMERA_OFFSET_Y) {
			camera.position.y = player.transform.position.y - Const.CAMERA_OFFSET_Y;
		}
		if (camera.position.y > Const.CAMERA_OFFSET_GROUND_Y) {
			camera.position.y = Const.CAMERA_OFFSET_GROUND_Y;
		}
		
		player.update(deltatime);
		player.getComponent(PlayerComponent.class).onGround = false;
		for (GameObject g : gameObjects) {
			g.update(deltatime);
			Bounds b = g.getComponent(Bounds.class);
			if (g != player && b != null) {
				if (Bounds.check_collision(playerBounds, b)) {
					Bounds.resolveCollision(b, this.player);
				}
			}
		}
		
	}
	
	@Override
	public void draw(Graphics2D g2D) {
		//g2D.setColor(Color.BLACK);
		g2D.setColor(Const.GB_COLOR);
		g2D.fillRect(0, 0, Const.SCREEN_WIDTH, Const.SCREEN_HEIGHT);
		renderer.render(g2D);
		
	}
	
	private void importData(String file_path) {
		Parser.openFile(file_path);
		GameObject go = Parser.parseGameObject();
		while (go != null) {
			addGameObject(go);
			go = Parser.parseGameObject();
		}
	}
}
