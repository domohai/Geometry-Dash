package com.Component;
import com.abc.Component;
import com.abc.GameObject;
import com.abc.Window;
import com.util.Const;
import com.util.Vector2D;
import java.awt.Graphics2D;
import java.awt.AlphaComposite;
import java.awt.event.MouseEvent;


public class SnapToGrid extends Component {
	
	private double debounceTime = 0.2;
	private double debounceTimeLeft = 0.0;
	public int gridW, gridH;
	
	public SnapToGrid(int gridW, int gridH) {
		this.gridW = gridW;
		this.gridH = gridH;
		
	}
	
	@Override
	public void update(double dt) {
		debounceTimeLeft -= dt;
		
		if (this.gameObject.getComponent(Sprite.class) != null) {
			float x = (float)Math.floor((Window.getWindow().mouse_listener.x +
					Window.getWindow().getCurrent_scene().camera.position.x + Window.getWindow().mouse_listener.dx) / gridW);
			float y = (float)Math.floor((Window.getWindow().mouse_listener.y +
					Window.getWindow().getCurrent_scene().camera.position.y + Window.getWindow().mouse_listener.dy) / gridH);
			this.gameObject.transform.position.x = x * gridW - Window.getWindow().getCurrent_scene().camera.position.x;
			this.gameObject.transform.position.y = y * gridH - Window.getWindow().getCurrent_scene().camera.position.y;
			
			if (Window.getWindow().mouse_listener.y < Const.TAB_OFFSET_Y &&
				Window.getWindow().mouse_listener.mouse_pressed && Window.getWindow().mouse_listener.mouse_button == MouseEvent.BUTTON1
				&& debounceTimeLeft < 0) {
				GameObject obj = gameObject.copy();
				obj.transform.position = new Vector2D(x * gridW, y * gridH);
				Window.getWindow().getCurrent_scene().addGameObject(obj);
				debounceTimeLeft = debounceTime;
			}
			
		}
	}
	
	@Override
	public void draw(Graphics2D g2D) {
		Sprite sprite = gameObject.getComponent(Sprite.class);
		if (sprite != null) {
			float alpha = 0.5f;
			AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
			
			g2D.setComposite(ac);
			g2D.drawImage(sprite.image, (int)gameObject.transform.position.x, (int)gameObject.transform.position.y,
					sprite.width, sprite.height, null);
			
			alpha = 1.0f;
			ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
			g2D.setComposite(ac);
			
		}
		
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
