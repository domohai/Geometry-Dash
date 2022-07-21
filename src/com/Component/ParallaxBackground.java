package com.Component;
import com.abc.Component;
import com.abc.GameObject;
import com.abc.Window;
import com.dataStructure.AssetPool;
import com.util.Const;
import java.awt.Graphics2D;

public class ParallaxBackground extends Component {
	public int width, height;
	public Sprite sprite;
	public GameObject[] backgrounds;
	public int timeStep = 0;
	private float speed = 80.0f;
	private Ground ground;
	private boolean followGround;
	
	public ParallaxBackground(String file, GameObject[] backgrounds,
							  Ground ground, boolean followGround) {
		this.sprite = AssetPool.getSprite(file);
		this.width = this.sprite.width;
		this.height = this.sprite.height;
		this.ground = ground;
		this.backgrounds = backgrounds;
		if (followGround) {
			this.speed = Const.PLAYER_SPEED - 35;
		}
		this.followGround = followGround;
	}
	
	@Override
	public void update(double dt) {
		if (backgrounds == null) return;
		this.timeStep++;
		this.gameObject.transform.position.x -= speed * dt;
		this.gameObject.transform.position.x = (float)Math.floor(this.gameObject.transform.position.x);
		if (this.gameObject.transform.position.x < - width) {
			float maxX = 0;
			int otherTimeStep = 0;
			for (GameObject g : backgrounds) {
				if (g.transform.position.x > maxX) {
					maxX = (float) g.transform.position.x;
					otherTimeStep = g.getComponent(ParallaxBackground.class).timeStep;
					
				}
			}
			if (otherTimeStep == this.timeStep) {
				this.gameObject.transform.position.x = maxX + width;
			} else {
				this.gameObject.transform.position.x = (float)(Math.floor(maxX + width) - (dt * speed));
			}
		}
		if (this.followGround) {
			this.gameObject.transform.position.y = ground.gameObject.transform.position.y;
			
		}
	}
	
	@Override
	public void draw(Graphics2D g2D) {
		if (followGround) {
			g2D.drawImage(this.sprite.image, (int)this.gameObject.transform.position.x,
					(int)(this.gameObject.transform.position.y - Window.getWindow().getCurrent_scene().camera.position.y),
						width, height, null);
		} else {
			int height = Math.min((int)(ground.gameObject.transform.position.y -
					Window.getWindow().getCurrent_scene().camera.position.y), Const.SCREEN_HEIGHT);
			g2D.drawImage(this.sprite.image, (int) this.gameObject.transform.position.x,
					(int)this.gameObject.transform.position.y, width, Const.SCREEN_HEIGHT, null);
			g2D.setColor(Const.GROUND_COLOR);
			g2D.fillRect((int)this.gameObject.transform.position.x, height, width, Const.SCREEN_HEIGHT);
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
