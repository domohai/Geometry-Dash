package com.Component;
import com.Files.Parser;
import com.abc.Component;
import com.util.Const;
import com.util.Vector2D;
import java.awt.Graphics2D;

public class TriangleBounds extends Bounds {
	private float base, height, halfW, halfH;
	private float enclosingR;
	private float x1, x2, x3, y1, y2, y3;
	
	public TriangleBounds(float base, float height) {
		this.type = BoundsType.TRIANGLE;
		this.base = base;
		this.height = height;
		this.halfW = this.base / 2.0f;
		this.halfH = this.height / 2.0f;
		this.enclosingR = Math.max(this.halfW, this.halfH);
	}
	
	@Override
	public void start() {
		calculateTransform();
		
	}
	
	public static boolean check_collision(BoxBounds b1, TriangleBounds t2) {
		if (t2.broadPhase(b1)) {
			System.out.println("abcabc");
			return t2.narrowPhase(b1);
		}
		return false;
	}
	
	private boolean broadPhase(BoxBounds b1) {
		float bR = b1.enclosingR;
		float tR = this.enclosingR;
		float centerX = this.x2;
		float centerY = this.y2 + halfH;
		float playerCenterX = (float) (b1.gameObject.transform.position.x + b1.halfW);
		float playerCenterY = (float) (b1.gameObject.transform.position.y + b1.halfH);
		
		Vector2D distance = new Vector2D(playerCenterX - centerX, playerCenterY - centerY);
		float magSquared = (float) ((distance.x * distance.x) + (distance.y * distance.y));
		float RSquared = ((bR + tR) * (bR + tR));
		return magSquared <= RSquared;
	}
	
	private boolean narrowPhase(BoxBounds b1) {
		Vector2D p1 = new Vector2D(x1, y1);
		Vector2D p2 = new Vector2D(x2, y2);
		Vector2D p3 = new Vector2D(x3, y3);
		
		// origin is the center of boxbounds
		Vector2D origin = new Vector2D(b1.gameObject.transform.position.x + (b1.width / 2.0f),
				b1.gameObject.transform.position.y + (b1.height / 2.0f));
		float rAngle = (float) Math.toRadians(b1.gameObject.transform.rotation);
		p1 = rotatePoint(rAngle, p1, origin);
		p2 = rotatePoint(rAngle, p2, origin);
		p3 = rotatePoint(rAngle, p3, origin);
		
		
		return true;
	}
	
	private boolean boxIntersectLine(Vector2D p1, Vector2D p2, int depth, BoxBounds bounds, Vector2D pos) {
		if (depth > 5) {
			System.out.println("Max depth exceeded");
			return true;
		}
		
	}
	
	private void calculateTransform() {
		double angle = Math.toRadians(gameObject.transform.rotation);
		Vector2D p1 = new Vector2D(gameObject.transform.position.x, gameObject.transform.position.y + height);
		Vector2D p2 = new Vector2D(gameObject.transform.position.x + halfW, gameObject.transform.position.y);
		Vector2D p3 = new Vector2D(gameObject.transform.position.x + base, gameObject.transform.position.y + height);
		Vector2D origin = new Vector2D(gameObject.transform.position.x + (Const.TILE_WIDTH / 2.0), gameObject.transform.position.y + (Const.TILE_HEIGHT / 2.0));
		
		p1 = rotatePoint(angle, p1, origin);
		p2 = rotatePoint(angle, p2, origin);
		p3 = rotatePoint(angle, p3, origin);
		
		this.x1 = (float) p1.x;
		this.y1 = (float) p1.y;
		this.x2 = (float) p2.x;
		this.y2 = (float) p2.y;
		this.x3 = (float) p3.x;
		this.y3 = (float) p3.y;
		
	}
	
	private Vector2D rotatePoint(double angle, Vector2D p, Vector2D o) {
		double cos = Math.cos(angle);
		double sin = Math.sin(angle);
		Vector2D newVector = new Vector2D(p.x, p.y);
		newVector.x -= o.x;
		newVector.y -= o.y;
		float newX = (float) ((newVector.x * cos) - (newVector.y * sin));
		float newY = (float) ((newVector.x * sin) + (newVector.y * cos));
		return new Vector2D(newX, newY);
	}
	
	@Override
	public void draw(Graphics2D g2D) {
	
	}
	
	@Override
	public double getWidth() {
		return this.base;
	}
	
	@Override
	public double getHeight() {
		return this.height;
	}
	
	@Override
	public String serialize(int tabSize) {
		StringBuilder builder = new StringBuilder();
		builder.append(beginObjectProperty("TriangleBounds", tabSize));
		builder.append(addDoubleProperty("Base", this.base, tabSize+1, true, true));
		builder.append(addDoubleProperty("Height", this.height, tabSize+1, true, false));
		builder.append(closeObjectProperty(tabSize));
		
		return builder.toString();
	}
	
	public static TriangleBounds deserialize() {
		//Parser.consume(',');
		double base = Parser.consumeDoubleProperty("Base");
		Parser.consume(',');
		double height = Parser.consumeDoubleProperty("Height");
		Parser.consumeEndObjectProperty();
		
		return new TriangleBounds((float) base, (float) height);
	}
	
	@Override
	public Component copy() {
		return new TriangleBounds(this.base, this.height);
	}
}
