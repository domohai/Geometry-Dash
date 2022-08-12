package com.Component;
import com.abc.Component;
import com.abc.GameObject;
import com.abc.LevelEditorScene;
import com.abc.Window;
import com.util.Const;
import com.util.Vector2D;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.AlphaComposite;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

enum Direction {
	UP, DOWN, LEFT, RIGHT
}

public class LevelEditorController extends Component {
	private double debounceTime = 0.2;
	private double debounceTimeLeft = 0.0;
	private double debounceKey = 0.05;
	private double debounceKeyLeft = 0.0;
	private boolean shiftKeyDown = false;
	public int gridW, gridH;
	private float worldX, worldY;
	private boolean isEditing = false;
	private List<GameObject> selectedObjects;
	private boolean wasDragged = false;
	private float dragX, dragY, dragW, dragH;
	
	public LevelEditorController(int gridW, int gridH) {
		this.gridW = gridW;
		this.gridH = gridH;
		this.selectedObjects = new ArrayList<>();
	}
	
	public void updateSpritePos() {
		this.worldX = (float)Math.floor((Window.getWindow().mouse_listener.x +
				Window.getWindow().getCurrent_scene().camera.position.x + Window.getWindow().mouse_listener.dx) / gridW);
		this.worldY = (float)Math.floor((Window.getWindow().mouse_listener.y +
				Window.getWindow().getCurrent_scene().camera.position.y + Window.getWindow().mouse_listener.dy) / gridH);
		this.gameObject.transform.position.x = this.worldX * gridW - Window.getWindow().getCurrent_scene().camera.position.x;
		this.gameObject.transform.position.y = this.worldY * gridH - Window.getWindow().getCurrent_scene().camera.position.y;
	}
	
	public void copyGameObjectToScene() {
		GameObject obj = this.gameObject.copy();
		obj.transform.position = new Vector2D(this.worldX * gridW, this.worldY * gridH);
		Window.getWindow().getCurrent_scene().addGameObject(obj);
	}
	
	public void addGameObjectToSelected(Vector2D Mousepos) {
		Mousepos.x += Window.getScene().camera.position.x;
		Mousepos.y += Window.getScene().camera.position.y;
		for (GameObject g : Window.getScene().getAllGameObject()) {
			Bounds b1 = g.getComponent(Bounds.class);
			if ((b1 != null && b1.raycast(Mousepos))) {
				this.selectedObjects.add(g);
				b1.isSelected = true;
				break;
			}
		}
	}
	
	public void clearSelectedObject(Vector2D mousePos) {
		clearSelected();
		this.addGameObjectToSelected(mousePos);
	}
	
	public void clearSelected() {
		for (GameObject g : this.selectedObjects) {
			g.getComponent(Bounds.class).isSelected = false;
		}
		this.selectedObjects.clear();
	}
	
	public void escapeKeyPressed() {
		GameObject newObject = new GameObject("MouseCursor", this.gameObject.transform.copy(), this.gameObject.zIndex);
		newObject.addComponent(this);
		LevelEditorScene scene = (LevelEditorScene) Window.getScene();
		scene.mouseCursor = newObject;
		isEditing = false;
	}
	
	public void moveObject(Direction direction, float scale) {
		Vector2D distance = new Vector2D(0,0);
		switch (direction) {
			case UP:
				distance.y = - Const.TILE_HEIGHT * scale;
				break;
			case DOWN:
				distance.y = Const.TILE_HEIGHT * scale;
				break;
			case LEFT:
				distance.x = - Const.TILE_WIDTH * scale;
				break;
			case RIGHT:
				distance.x = Const.TILE_WIDTH * scale;
				break;
			default:
				System.out.println("Unknown direction!");
				System.exit(-1);
				break;
		}
		for (GameObject g : this.selectedObjects) {
			g.transform.position.x += distance.x;
			g.transform.position.y += distance.y;
			float gridX = (float) ((Math.floor(g.transform.position.x / Const.TILE_WIDTH) + 1)  * Const.TILE_WIDTH);
			float gridY = (float) (Math.floor(g.transform.position.y / Const.TILE_HEIGHT) * Const.TILE_HEIGHT);
			if (g.transform.position.x < gridX + 1 && g.transform.position.x > gridX - 1) {
				g.transform.position.x = gridX;
			}
			if (g.transform.position.y < gridY + 1 && g.transform.position.y > gridY - 1) {
				g.transform.position.y = gridY;
			}
			TriangleBounds b = g.getComponent(TriangleBounds.class);
			if (b != null) {
				b.calculateTransform();
			}
		}
	}
	
	public List<GameObject> boxCast(float x, float y, float w, float h) {
		float x0 = x + (float) Window.getScene().camera.position.x;
		float y0 = y + (float) Window.getScene().camera.position.y;
		List<GameObject> Objects = new ArrayList<>();
		for (GameObject g : Window.getScene().getAllGameObject()) {
			Bounds b = g.getComponent(Bounds.class);
			if (b != null) {
				if (g.transform.position.x + b.getWidth() <= (x0 + w) &&
				g.transform.position.y + b.getHeight() <= (y0 + h) &&
				g.transform.position.x >= x0 && g.transform.position.y >= y0) {
					Objects.add(g);
				}
			}
		}
		return Objects;
	}
	
	public void duplicate() {
		for (GameObject g : this.selectedObjects) {
			Window.getScene().addGameObject(g.copy());
		}
	}
	
	public void rotate(float degree) {
		for (GameObject g : this.selectedObjects) {
			g.transform.rotation += degree;
			TriangleBounds b = g.getComponent(TriangleBounds.class);
			if (b != null) {
				b.calculateTransform();
			}
		}
	}
	
	@Override
	public void update(double dt) {
		this.debounceTimeLeft -= dt;
		this.debounceKeyLeft -= dt;
		if (!this.isEditing && this.gameObject.getComponent(Sprite.class) != null) {
			this.isEditing = true;
		}
		if (this.isEditing) {
			if (this.selectedObjects.size() != 0) {
				clearSelected();
			}
			updateSpritePos();
		}
		if (Window.getWindow().mouse_listener.y < Const.TAB_OFFSET_Y &&
			Window.getWindow().mouse_listener.mouse_pressed && Window.getWindow().mouse_listener.mouse_button == MouseEvent.BUTTON1
			&& debounceTimeLeft < 0 && !wasDragged) {
			this.debounceTimeLeft = this.debounceTime;
			// mouse clicked
			if (this.isEditing) copyGameObjectToScene();
			else if (Window.keyListener().key_is_pressed(KeyEvent.VK_SHIFT)) {
				addGameObjectToSelected(new Vector2D(Window.mouseListener().x, Window.mouseListener().y));
			} else {
				clearSelectedObject(new Vector2D(Window.mouseListener().x, Window.mouseListener().y));
			}
		} else if (!Window.mouseListener().mouse_pressed && this.wasDragged) {
			wasDragged = false;
			clearSelected();
			List<GameObject> Objects = boxCast(dragX, dragY, dragW, dragH);
			for (GameObject g : Objects) {
				this.selectedObjects.add(g);
				Bounds b = g.getComponent(Bounds.class);
				if (b != null) b.isSelected = true;
			}
		}
		if (Window.keyListener().key_is_pressed(KeyEvent.VK_ESCAPE)) {
			escapeKeyPressed();
		}
		if (Window.keyListener().key_is_pressed(KeyEvent.VK_SHIFT)) {
			this.shiftKeyDown = true;
		} else {
			this.shiftKeyDown = false;
		}
		if (debounceKeyLeft <= 0.0) {
			if (Window.keyListener().key_is_pressed(KeyEvent.VK_RIGHT)) {
				moveObject(Direction.RIGHT, this.shiftKeyDown ? 0.2f : 1.0f);
			} else if (Window.keyListener().key_is_pressed(KeyEvent.VK_LEFT)) {
				moveObject(Direction.LEFT, this.shiftKeyDown ? 0.2f : 1.0f);
			} else if (Window.keyListener().key_is_pressed(KeyEvent.VK_UP)) {
				moveObject(Direction.UP, this.shiftKeyDown ? 0.2f : 1.0f);
			} else if (Window.keyListener().key_is_pressed(KeyEvent.VK_DOWN)) {
				moveObject(Direction.DOWN, this.shiftKeyDown ? 0.2f : 1.0f);
			}
			if (Window.keyListener().key_is_pressed(KeyEvent.VK_V)) {
				if (Window.keyListener().key_is_pressed(KeyEvent.VK_CONTROL)) {
					this.duplicate();
				}
			}
			if (Window.keyListener().key_is_pressed(KeyEvent.VK_R)) {
				TriangleBounds b = this.gameObject.getComponent(TriangleBounds.class);
				if (b != null) {
					b.calculateTransform();
				}
				this.rotate(90.0f);
			} else if (Window.keyListener().key_is_pressed(KeyEvent.VK_E)) {
				this.rotate(-90.0f);
			}
			if (Window.keyListener().key_is_pressed(KeyEvent.VK_BACK_SPACE)) {
				for (GameObject g : this.selectedObjects) {
					Window.getScene().removeObject(g);
				}
				selectedObjects.clear();
			}
			debounceKeyLeft = debounceKey;
		}
	}
	
	@Override
	public void draw(Graphics2D g2D) {
		if (this.isEditing) {
			Sprite sprite = gameObject.getComponent(Sprite.class);
			if (sprite != null) {
				float alpha = 0.3f;
				AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
				g2D.setComposite(ac);
				g2D.drawImage(sprite.image, (int) gameObject.transform.position.x, (int) gameObject.transform.position.y,
						sprite.width, sprite.height, null);
				alpha = 1.0f;
				ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
				g2D.setComposite(ac);
			}
		} else if (Window.mouseListener().mouse_dragged &&
		Window.mouseListener().mouse_button == MouseEvent.BUTTON1) {
			this.wasDragged = true;
			g2D.setColor(new Color(1.0f, 1.0f, 1.0f, 0.3f));
			dragX = (float) Window.mouseListener().x;
			dragY = (float) Window.mouseListener().y;
			dragW = (float) Window.mouseListener().dx;
			dragH = (float) Window.mouseListener().dy;
			if (dragW < 0) {
				dragW *= -1.0f;
				dragX -= dragW;
			}
			if (dragH < 0) {
				dragH *= -1.0f;
				dragY -= dragH;
			}
			g2D.fillRect((int)dragX, (int)dragY, (int)dragW, (int)dragH);
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
