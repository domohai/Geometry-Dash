package com.Component;
import com.abc.*;
import com.util.Const;
import java.awt.Graphics2D;
import java.awt.Color;

public class Ground extends Component {
	
	@Override
	public void update(double dt) {
		if (!Window.getWindow().in_editor) {
			LevelScene scene = (LevelScene) Window.getWindow().getCurrent_scene();
			GameObject player = scene.player;
			
			if (player.transform.position.y + player.getComponent(BoxBounds.class).height
					> gameObject.transform.position.y) {
				player.transform.position.y = gameObject.transform.position.y -
						player.getComponent(BoxBounds.class).height;
				
				player.getComponent(PlayerComponent.class).onGround = true;
			
			}
			gameObject.transform.position.x = scene.camera.position.x - 10;
		} else {
			gameObject.transform.position.x = Window.getWindow().getCurrent_scene().camera.position.x - 10;
		}
	}
	
	@Override
	public void draw(Graphics2D g2D) {
		g2D.setColor(Color.WHITE);
		
		g2D.drawRect((int)gameObject.transform.position.x-50, (int)gameObject.transform.position.y,
				Const.SCREEN_WIDTH+100, Const.SCREEN_HEIGHT);
		
		
	}
	
	
	@Override
	public Component copy() {
		return null;
	}
	@Override
	public String serialize(int tabSize) {
		return "";
	}
	
}
