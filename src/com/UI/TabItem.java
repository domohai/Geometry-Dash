package com.UI;

import com.Component.Sprite;
import com.abc.Component;
import com.abc.Window;
import java.awt.Graphics2D;
import java.awt.AlphaComposite;
import java.awt.event.MouseEvent;

public class TabItem extends Component {
	private int x, y, width, height;
	private Sprite sprite;
	public boolean isSelected = false;
	private int bufferX, bufferY;
	private MainContainer parentContainer;
	
	public TabItem(int x, int y, int width, int height,Sprite sprite,
					MainContainer parentContainer) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.sprite = sprite;
		this.parentContainer = parentContainer;
	}
	
	@Override
	public void update(double dt) {
		if (Window.getWindow().mouse_listener.mouse_pressed &&
				Window.getWindow().mouse_listener.mouse_button == MouseEvent.BUTTON1) {
			if (!isSelected && Window.getWindow().mouse_listener.x > this.x
					&& Window.getWindow().mouse_listener.x <= this.x + this.width
					&& Window.getWindow().mouse_listener.y > this.y
					&& Window.getWindow().mouse_listener.y <= this.y + this.height) {
				// this button is clicked
				isSelected = true;
				this.parentContainer.setHotTab(gameObject);
			}
		}
	}
	
	@Override
	public void draw(Graphics2D g2D) {
		if (isSelected) {
			g2D.drawImage(sprite.image, x, y, width, height, null);
		} else {
			float alpha = 0.5f;
			AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
			g2D.setComposite(ac);
			g2D.drawImage(sprite.image, x, y, width, height, null);
			alpha = 1.0f;
			ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
			g2D.setComposite(ac);
		}
	}
	
	@Override
	public String serialize(int tabSize) {
		return null;
	}
	
	@Override
	public Component copy() {
		return null;
	}
}
