package com.Component;
import com.abc.Component;
import com.util.Const;
import com.util.Vector2D;

public class RigidBody extends Component {

	public Vector2D velocity;
	
	public RigidBody(Vector2D velocity) {
		this.velocity = velocity;
		
	}
	@Override
	public void update(double dt) {
		gameObject.transform.position.x += velocity.x * dt;
		gameObject.transform.position.y += velocity.y * dt;
		
		velocity.y += Const.GRAVITY * dt;
		if (Math.abs(velocity.y) > Const.MAX_VELOCITY) {
			velocity.y = Math.signum(velocity.y) * Const.MAX_VELOCITY;
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
