package com.Component;
import com.Files.Parser;
import com.abc.Component;
import com.abc.Window;
import com.util.Const;
import com.util.Vector2D;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

public class TriangleBounds extends Bounds {
	private float base, height, halfW, halfH;
	private float enclosingR;
	private float x1, x2, x3, y1, y2, y3;
	private final int inside = 0; // 0000
	private final int LEFT = 1; // 0001
	private final int RIGHT = 2; // 0010
	private final int BOTTOM = 4; // 0100
	private final int TOP = 8; // 1000
	
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
			//System.out.println("abcabc");
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
		
		
		return boxIntersectLine(p1, p2, 0, b1, b1.gameObject.transform.position) ||
				boxIntersectLine(p2, p3, 0, b1, b1.gameObject.transform.position) ||
				boxIntersectLine(p1, p3, 0, b1, b1.gameObject.transform.position);
	}
	
	private boolean boxIntersectLine(Vector2D p1, Vector2D p2, int depth, BoxBounds bounds, Vector2D pos) {
		if (depth > 5) {
			System.out.println("Max depth exceeded");
			return true;
		}
		int code1 = computeRegionCode(p1, bounds);
		int code2 = computeRegionCode(p2, bounds);
		// check if the line is completely inside or outside
		// or half in and half out
		if (code1 == 0 && code2 == 0) {
			// completely inside
			return true;
		} else if ((code1 & code2) != 0) {
			// outside
			return false;
		} else if (code1 == 0 || code2 == 0) {
			// one point inside, one point outside
			return true;
		}
		int xMax = (int)(pos.x + bounds.width);
		int xMin = (int)(pos.x);
		// calculate slope
		float m = (float) ((p2.y - p1.y) / (p2.x - p1.x));
		float b = (float) (p2.y - (m * p2.x));
		if ((code1 & LEFT) == LEFT) {
			p1.x = xMin + 1;
		} else if ((code1 & RIGHT) == RIGHT) {
			p1.x = xMax - 1;
		}
		p1.y = (m * p1.x) + b;
		//
		if ((code2 & LEFT) == LEFT) {
			p2.x = xMin + 1;
		} else if ((code2 & RIGHT) == RIGHT) {
			p2.x = xMax - 1;
		}
		p2.y = (m * p2.x) + b;
		return boxIntersectLine(p1,p2, depth+1, bounds, pos);
	}
	
	private int computeRegionCode(Vector2D p, BoxBounds b) {
		int code = inside;
		Vector2D topLeft = b.gameObject.transform.position;
		
		// check if the point is on the left or right of bounds
		if (p.x < topLeft.x) {
			code |= LEFT;
		} else if (p.x > topLeft.x + b.width) {
			code |= RIGHT;
		}
		// check if the point is above or below the box
		if (p.y < topLeft.y) {
			code |= TOP;
		} else if (p.y > topLeft.y + b.height) {
			code |= BOTTOM;
		}
		return code;
	}
	
	public void calculateTransform() {
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
		if (isSelected) {
			g2D.setColor(Color.GREEN);
			g2D.setStroke(Const.THICK_LINE);
			g2D.draw(new Line2D.Float((float)(this.x1 - Window.getScene().camera.position.x)
					, (float)(this.y1 - Window.getScene().camera.position.y)
					, (float)(this.x2 - Window.getScene().camera.position.x)
					, (float)(this.y2 - Window.getScene().camera.position.y)));
			g2D.draw(new Line2D.Double(this.x1 - Window.getScene().camera.position.x
					, this.y1 - Window.getScene().camera.position.y
					, this.x3 - Window.getScene().camera.position.x
					, this.y3 - Window.getScene().camera.position.y));
			g2D.draw(new Line2D.Double(this.x3 - Window.getScene().camera.position.x
					, this.y3 - Window.getScene().camera.position.y
					, this.x2 - Window.getScene().camera.position.x
					, this.y2 - Window.getScene().camera.position.y));
			
			g2D.setStroke(Const.LINE);
		}
	}
	
	@Override
	public double getWidth() {
		return this.base;
	}
	
	@Override
	public double getHeight() {
		return this.height;
	}
	
	public float dot(Vector2D v1, Vector2D v2) {
		return (float) (v1.x * v2.x + v1.y * v2.y);
	}
	
	@Override
	public boolean raycast(Vector2D pos) {
		// Compute vectors
		Vector2D v0 = new Vector2D(x3 - x1, y3 - y1);
		Vector2D v1 = new Vector2D(x2 - x1, y2 - y1);
		Vector2D v2 = new Vector2D(pos.x - x1, pos.y - y1);
		
		// Compute dot products
		float dot00 = dot(v0, v0);
		float dot01 = dot(v0, v1);
		float dot02 = dot(v0, v2);
		float dot11 = dot(v1, v1);
		float dot12 = dot(v1, v2);
		
		// Compute barycentric coordinates
		float invDenom = 1.0f / (dot00 * dot11 - dot01 * dot01);
		float u = (dot11 * dot02 - dot01 * dot12) * invDenom;
		float v = (dot00 * dot12 - dot01 * dot02) * invDenom;
		
		// Check if point is in triangle
		return ((u >= 0.0f) && (v >= 0.0f) && (u + v < 1.0f));
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
