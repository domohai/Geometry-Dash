package com.Component;
import com.abc.Component;
import com.abc.GameObject;

enum BoundsType {
	BOX,
	TRIANGLE
}

public abstract class Bounds extends Component {
	public BoundsType type;
	
	public abstract double getWidth();
	public abstract double getHeight();
	
	public static boolean check_collision(Bounds b1, Bounds b2) {
		if (b1.type == b2.type && b1.type == BoundsType.BOX) {
			return BoxBounds.check_collision((BoxBounds)b1, (BoxBounds)b2);
		}
		return false;
	}
	
	public static void resolveCollision(Bounds b, GameObject player) {
		if (b.type == BoundsType.BOX) {
			BoxBounds box = (BoxBounds) b;
			box.resolveCollision(player);
		}
	}
	
}
