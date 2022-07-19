package com.UI;
import com.Component.SnapToGrid;
import com.Component.Sprite;
import com.abc.Component;
import com.abc.GameObject;
import com.abc.LevelEditorScene;
import com.abc.Window;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

public class MenuItem extends Component {
	private int x, y, width, height;
	private Sprite button, hovered, image;
	public boolean isSelected = false;
	private int bufferX, bufferY;
	
	public MenuItem(int x, int y, int width, int height, Sprite button, Sprite hovered) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.button = button;
		this.hovered = hovered;
		
	}
	
	@Override
	public void start() {
		image = gameObject.getComponent(Sprite.class);
		this.bufferX = (int)(this.width / 2.0 - image.width / 2.0);
		this.bufferY = (int)(this.height / 2.0 - image.height / 2.0);
		
	}
	
	@Override
	public void update(double dt) {
		if (!isSelected && Window.getWindow().mouse_listener.x > this.x
		 && Window.getWindow().mouse_listener.x <= this.x + this.width
		 && Window.getWindow().mouse_listener.y > this.y
		 && Window.getWindow().mouse_listener.y <= this.y + this.height) {
			if (Window.getWindow().mouse_listener.mouse_pressed &&
				Window.getWindow().mouse_listener.mouse_button == MouseEvent.BUTTON1) {
				// this button is clicked
				isSelected = true;
				GameObject obj = gameObject.copy();
				obj.removeComponent(MenuItem.class);
				
				LevelEditorScene scene = (LevelEditorScene) Window.getWindow().getCurrent_scene();
				SnapToGrid snap = scene.mouseCursor.getComponent(SnapToGrid.class);
				obj.addComponent(snap);
				scene.mouseCursor = obj;
				
			}
		}
	}
	
	@Override
	public void draw(Graphics2D g2D) {
		g2D.drawImage(this.button.image, this.x, this.y, this.width, this.height, null);
		g2D.drawImage(this.image.image, this.x+bufferX, this.y+bufferY, image.width, image.height, null);
		if (isSelected) {
			g2D.drawImage(hovered.image, this.x, this.y, this.width, this.height, null);
		}
		
	}
	
	@Override
	public MenuItem copy() {
		return new MenuItem(this.x, this.y, this.width, this.height, (Sprite) this.button.copy(), (Sprite) this.hovered.copy());
	}
	
	@Override
	public String serialize(int tabSize) {
		return "";
	}
}
