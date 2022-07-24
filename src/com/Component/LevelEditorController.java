package com.Component;
import com.abc.Component;
import com.abc.GameObject;
import com.abc.LevelEditorScene;
import com.abc.Window;
import com.util.Const;
import com.util.Vector2D;
import java.awt.Graphics2D;
import java.awt.AlphaComposite;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;


public class LevelEditorController extends Component {
	private double debounceTime = 0.2;
	private double debounceTimeLeft = 0.0;
	public int gridW, gridH;
	private float worldX, worldY;
	private boolean isEditing = false;
	private List<GameObject> selectedObjects;
	
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
		GameObject obj = gameObject.copy();
		obj.transform.position = new Vector2D(this.worldX * gridW, this.worldY * gridH);
		Window.getWindow().getCurrent_scene().addGameObject(obj);
	}
	
	public void addGameObjectToSelected(Vector2D Mousepos) {
		Mousepos.x += Window.getScene().camera.position.x;
		Mousepos.y += Window.getScene().camera.position.y;
		for (GameObject g : Window.getScene().getAllGameObject()) {
			Bounds b = g.getComponent(Bounds.class);
			if (b != null && b.raycast(Mousepos)) {
				selectedObjects.add(g);
				b.isSelected = true;
				break;
			}
		}
	}
	
	public void clearSelectedObject(Vector2D mousePos) {
		clearSelected();
		addGameObjectToSelected(mousePos);
	}
	
	public void clearSelected() {
		for (GameObject g : selectedObjects) {
			g.getComponent(Bounds.class).isSelected = false;
		}
		selectedObjects.clear();
	}
	
	public void escapeKeyPressed() {
		GameObject newObject = new GameObject("MouseCursor", this.gameObject.transform.copy(), this.gameObject.zIndex);
		newObject.addComponent(this);
		LevelEditorScene scene = (LevelEditorScene) Window.getScene();
		scene.mouseCursor = newObject;
		isEditing = false;
	}
	
	@Override
	public void update(double dt) {
		debounceTimeLeft -= dt;
		if (!isEditing && this.gameObject.getComponent(Sprite.class) != null) {
			this.isEditing = true;
		}
		
		if (isEditing) {
			if (selectedObjects.size() != 0) {
				clearSelected();
			}
			updateSpritePos();
		}

		if (Window.getWindow().mouse_listener.y < Const.TAB_OFFSET_Y &&
			Window.getWindow().mouse_listener.mouse_pressed && Window.getWindow().mouse_listener.mouse_button == MouseEvent.BUTTON1
			&& debounceTimeLeft < 0) {
			// mouse clicked
			if (isEditing) copyGameObjectToScene();
			else if (Window.keyListener().key_is_pressed(KeyEvent.VK_SHIFT)) {
				addGameObjectToSelected(new Vector2D(Window.mouseListener().x, Window.mouseListener().y));
			} else {
				clearSelectedObject(new Vector2D(Window.mouseListener().x, Window.mouseListener().y));
			}
			debounceTimeLeft = debounceTime;
		}
		if (Window.keyListener().key_is_pressed(KeyEvent.VK_ESCAPE)) {
			escapeKeyPressed();
		}
	}
	
	@Override
	public void draw(Graphics2D g2D) {
		Sprite sprite = gameObject.getComponent(Sprite.class);
		if (sprite != null) {
			float alpha = 0.5f;
			AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
			
			g2D.setComposite(ac);
			g2D.drawImage(sprite.image, (int)gameObject.transform.position.x, (int)gameObject.transform.position.y,
					sprite.width, sprite.height, null);
			
			alpha = 1.0f;
			ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
			g2D.setComposite(ac);
			
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
