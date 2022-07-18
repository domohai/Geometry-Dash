package com.Component;
import com.Files.Parser;
import com.abc.Component;
import com.abc.GameObject;
import com.util.Vector2D;

public class BoxBounds extends Bounds {
	public double width, height;
	public double halfW, halfH;
	public Vector2D center = new Vector2D(0.0, 0.0);
	
	public BoxBounds(double width, double height) {
		this.width = width;
		this.height = height;
		this.halfW = this.width / 2.0;
		this.halfH = this.height / 2.0;
		this.type = BoundsType.BOX;
	}
	
	@Override
	public void start() {
		this.calculateCenter();
		
	}
	
	public void calculateCenter() {
		this.center.x = this.gameObject.transform.position.x + this.halfW;
		this.center.y = this.gameObject.transform.position.y + this.halfH;
	}
	
	public static boolean check_collision(BoxBounds b1, BoxBounds b2) {
		b1.calculateCenter();
		b2.calculateCenter();
		double dx = b2.center.x - b1.center.x;
		double dy = b2.center.y - b1.center.y;
		double combineHalfW = b1.halfW + b2.halfW;
		double combineHalfH = b1.halfH + b2.halfH;
		if (Math.abs(dx) <= combineHalfW) {
			return Math.abs(dy) <= combineHalfH;
		}
		return false;
	}
	
	public void resolveCollision(GameObject player) {
		BoxBounds playerBounds = player.getComponent(BoxBounds.class);
		playerBounds.calculateCenter();
		this.calculateCenter();
		
		double dx = this.center.x - playerBounds.center.x;
		double dy = this.center.y - playerBounds.center.y;
		double combineHalfW = playerBounds.halfW + this.halfW;
		double combineHalfH = playerBounds.halfH + this.halfH;
		
		double overlapX = combineHalfW - Math.abs(dx);
		double overlapY = combineHalfH - Math.abs(dy);
		
		if (overlapX >= overlapY) {
			if (dy >= 0.4) { // 0.0
				// collide on top of player
				player.getComponent(PlayerComponent.class).onGround = true;
				player.transform.position.y = gameObject.transform.position.y - playerBounds.getHeight();
				player.getComponent(RigidBody.class).velocity.y = 0.0;
			} else {
				// collide on bottom of player
				player.getComponent(PlayerComponent.class).die();
			}
		} else {
			// collide on the right or left of the player
			if (dx <= 35.0 && dy <= 35.0) { //&& dy <= 1.0
				player.getComponent(RigidBody.class).velocity.y = 0.0;
				player.getComponent(PlayerComponent.class).onGround = true;
				
			} else {
				player.getComponent(PlayerComponent.class).die();
			}
		}
	}
	
	@Override
	public void update(double dt) {
	
	}
	
	@Override
	public Component copy() {
		return new BoxBounds(this.width, this.height);
	}
	
	
	@Override
	public String serialize(int tabSize) {
		StringBuilder builder = new StringBuilder();
		builder.append(beginObjectProperty("BoxBounds", tabSize));
		builder.append(addDoubleProperty("Width", this.width, tabSize+1, true, true));
		builder.append(addDoubleProperty("Height", this.height, tabSize+1, true, false));
		builder.append(closeObjectProperty(tabSize));
		
		return builder.toString();
	}
	
	public static BoxBounds deserialize() {
		double width = Parser.consumeDoubleProperty("Width");
		Parser.consume(',');
		double height = Parser.consumeDoubleProperty("Height");
		Parser.consumeEndObjectProperty();
		
		return new BoxBounds(width, height);
	}
	
	@Override
	public double getWidth() {
		return this.width;
	}
	
	@Override
	public double getHeight() {
		return this.height;
	}
}
