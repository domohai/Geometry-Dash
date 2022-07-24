package com.Component;
import com.abc.Component;
import com.abc.GameObject;
import com.util.Vector2D;

enum BoundsType {
	BOX,
	TRIANGLE
}

public abstract class Bounds extends Component {
	public BoundsType type;
	public boolean isSelected;
	
	public abstract double getWidth();
	public abstract double getHeight();
	
	abstract public boolean raycast(Vector2D pos);
	
	public static boolean check_collision(Bounds b1, Bounds b2) {
		
		if (b1.type == b2.type && b1.type == BoundsType.BOX) {
			return BoxBounds.check_collision((BoxBounds)b1, (BoxBounds)b2);
		} else if (b1.type == BoundsType.BOX && b2.type == BoundsType.TRIANGLE) {
			return TriangleBounds.check_collision((BoxBounds)b1, (TriangleBounds)b2);
		} else if (b1.type == BoundsType.TRIANGLE && b2.type == BoundsType.BOX) {
			return TriangleBounds.check_collision((BoxBounds)b2, (TriangleBounds)b1);
		}
		return false;
	}
	
	public static void resolveCollision(Bounds b, GameObject player) {
		if (b.type == BoundsType.BOX) {
			BoxBounds box = (BoxBounds) b;
			box.resolveCollision(player);
		} else if (b.type == BoundsType.TRIANGLE) {
			player.getComponent(PlayerComponent.class).die();
		}
	}
	
}
