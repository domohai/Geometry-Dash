package com.Component;
import com.abc.Camera;
import com.abc.Component;
import com.abc.Window;
import com.util.Const;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.BasicStroke;
import java.awt.geom.Line2D;


public class Grid extends Component {
	
	Camera camera;
	public int gridW, gridH;
	private int numYlines = 31, numXlines = 20;
	
	public Grid() {
		this.camera = Window.getWindow().getCurrent_scene().camera;
		this.gridW = Const.TILE_WIDTH;
		this.gridH = Const.TILE_HEIGHT;
		
	}

	@Override
	public void update(double dt) {
	
	}
	
	@Override
	public void draw(Graphics2D g2D) {
		
		g2D.setStroke(new BasicStroke(1.0f));
		g2D.setColor(new Color(0.3f, 0.3f, 0.3f, 0.5f));
		
		double bottom = Math.min(Const.SCREEN_HEIGHT, Const.GROUND_Y - camera.position.y);
		double startX = Math.floor(camera.position.x / gridW) * gridW - camera.position.x;
		double startY = Math.floor(camera.position.y / gridH) * gridH - camera.position.y;
		
		for (int col = 0; col < numYlines; col++) {
			g2D.draw(new Line2D.Double(startX, 0, startX, bottom));
			startX += gridW;
		
		}
		for (int row = 0; row < numXlines; row++) {
			if (camera.position.y + startY < Const.GROUND_Y) {
				g2D.draw(new Line2D.Double(0, startY, Const.SCREEN_WIDTH, startY));
				startY += gridH;
			}
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
