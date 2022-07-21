package com.Component;
import com.abc.Component;
import com.abc.Window;
import com.util.Const;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;

public class PlayerComponent extends Component {

	Sprite layer1, layer2, layer3;
	public int width, height;
	public boolean onGround = true;
	
	public PlayerComponent(Sprite layer1, Sprite layer2, Sprite layer3,
						   Color color1, Color color2) {
		this.layer1 = layer1;
		this.layer2 = layer2;
		this.layer3 = layer3;
		this.width = Const.TILE_WIDTH-2;
		this.height = Const.TILE_HEIGHT-2;
		int threshold = 205;
		
		for (int x = 0; x < layer1.image.getHeight(); x++) {
			for (int y = 0; y < layer1.image.getWidth(); y++) {
				Color color = new Color(layer1.image.getRGB(x, y));
				if (color.getRed() > threshold && color.getGreen() > threshold &&
					color.getBlue() > threshold) {
					layer1.image.setRGB(x, y, color1.getRGB());
				}
			}
		}
		for (int x = 0; x < layer2.image.getHeight(); x++) {
			for (int y = 0; y < layer2.image.getWidth(); y++) {
				Color color = new Color(layer2.image.getRGB(x, y));
				if (color.getRed() > threshold && color.getGreen() > threshold &&
						color.getBlue() > threshold) {
					layer2.image.setRGB(x, y, color2.getRGB());
				}
			}
		}
	
	}
	
	private void addJumpForce() {
		gameObject.getComponent(RigidBody.class).velocity.y = Const.JUMP_FORCE;
	}
	
	public void die() {
		gameObject.transform.position.x = 0.0;
		gameObject.transform.position.y = 30.0;
		Window.getWindow().getCurrent_scene().camera.position.x = 0.0;
	}
	
	@Override
	public void update(double dt) {
		if (onGround && Window.getWindow().key_listener.key_is_pressed(KeyEvent.VK_SPACE)) {
			addJumpForce();
			this.onGround = false;
		}
		if (!onGround) {
			gameObject.transform.rotation += 6.5 * dt;
		} else {
			//gameObject.transform.rotation = (int)gameObject.transform.rotation % 360;
			gameObject.transform.rotation = (Math.round(gameObject.transform.rotation / 90)) * 90;
			
			if (gameObject.transform.rotation > 180.0 &&
				gameObject.transform.rotation < 360.0) {
				gameObject.transform.rotation = 0.0;
			} else if (gameObject.transform.rotation > 0.0 &&
						gameObject.transform.rotation < 180.0) {
				gameObject.transform.rotation = 0.0;
			}
			
		}
		
	}
	
	@Override
	public void draw(Graphics2D g2D) {
		AffineTransform transform = new AffineTransform();
		transform.setToIdentity();
		transform.translate(gameObject.transform.position.x, gameObject.transform.position.y);
		transform.rotate(gameObject.transform.rotation, width*gameObject.transform.scale.x/2.0,
				height*gameObject.transform.scale.y/2.0);
		transform.scale(gameObject.transform.scale.x, gameObject.transform.scale.y);
		
		g2D.drawImage(layer1.image, transform, null);
		g2D.drawImage(layer2.image, transform, null);
		g2D.drawImage(layer3.image, transform, null);
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
