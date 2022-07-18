package com.Component;

import com.abc.Component;
import com.abc.Window;
import java.awt.event.MouseEvent;

public class CameraController extends Component {
	private double prevMX, prevMY;
	
	public CameraController() {
		this.prevMX = 0.0;
		this.prevMY = 0.0;
		
	}

	@Override
	public void update(double dt) {
		if (Window.getWindow().mouse_listener.mouse_pressed &&
			Window.getWindow().mouse_listener.mouse_button == MouseEvent.BUTTON3) {
			double dx = (Window.getWindow().mouse_listener.x + Window.getWindow().mouse_listener.dx - prevMX);
			double dy = (Window.getWindow().mouse_listener.y + Window.getWindow().mouse_listener.dy - prevMY);
			
			Window.getWindow().getCurrent_scene().camera.position.x -= dx;
			Window.getWindow().getCurrent_scene().camera.position.y -= dy;
			
		}
		
		prevMX = Window.getWindow().mouse_listener.x + Window.getWindow().mouse_listener.dx;
		prevMY = Window.getWindow().mouse_listener.y + Window.getWindow().mouse_listener.dy;
		
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
