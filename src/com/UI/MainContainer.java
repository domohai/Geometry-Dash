package com.UI;
import com.Component.BoxBounds;
import com.Component.Sprite;
import com.Component.Spritesheet;
import com.abc.Component;
import com.abc.GameObject;
import com.abc.Window;
import com.dataStructure.AssetPool;
import com.dataStructure.Transform;
import com.util.Const;
import com.util.Vector2D;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainContainer extends Component {
	public Sprite containerBg;
	public List<GameObject> menuItems;
	public List<GameObject> tabs;
	public Map<GameObject, List<GameObject>> tabMaps;
	public GameObject currentTab;
	
	public MainContainer() {
		this.menuItems = new ArrayList<>();
		this.tabs = new ArrayList<>();
		this.tabMaps = new HashMap<>();
		this.containerBg = AssetPool.getSprite("assets/ui/menuContainerBackground.png");
		init();
	}
	
	public void init() {
		Spritesheet blockSprites = AssetPool.getSpritesheet("assets/groundSprites.png");
		Spritesheet buttonSprites = AssetPool.getSpritesheet("assets/buttonSprites.png");
		Spritesheet tabSprites = AssetPool.getSpritesheet("assets/ui/tabs.png");
		
		for (int i = 0; i < tabSprites.sprites.size(); i++) {
			Sprite currentTab = tabSprites.sprites.get(i);
			int x = Const.TAB_OFFSET_X + (currentTab.col * Const.TAB_WIDTH) + (currentTab.col * Const.TAB_HORIZONTAL_SPACING);
			int y = Const.TAB_OFFSET_Y;
			GameObject tabobj = new GameObject("Tab", new Transform(new Vector2D(x, y)), 10);
			tabobj.setUi(true);
			tabobj.addComponent(currentTab);
			this.tabs.add(tabobj);
			this.tabMaps.put(tabobj, new ArrayList<>());
			
			Window.getWindow().getCurrent_scene().addGameObject(tabobj);
		}
		
		
		
		for (int i = 0; i < blockSprites.sprites.size(); i++) {
			Sprite currentSprite = blockSprites.sprites.get(i);
			int x = Const.BUTTON_OFFSET_X + (currentSprite.col * Const.BUTTON_WIDTH) +
					(currentSprite.col * Const.BUTTON_SPACING_HZ);
			int y = Const.BUTTON_OFFSET_Y + (currentSprite.row * Const.BUTTON_HEIGHT) +
					(currentSprite.row * Const.BUTTON_SPACING_VT);
			
			GameObject obj = new GameObject("obj", new Transform(new Vector2D(x, y)), -1);
			obj.addComponent(currentSprite.copy());
			MenuItem menuItem = new MenuItem(x, y, Const.BUTTON_WIDTH, Const.BUTTON_HEIGHT,
										buttonSprites.sprites.get(0), buttonSprites.sprites.get(1));
			obj.addComponent(menuItem);
			obj.addComponent(new BoxBounds(Const.TILE_WIDTH, Const.TILE_HEIGHT));
			
			menuItems.add(obj);
		}
		
	}
	
	@Override
	public void start() {
		for (GameObject g : menuItems) {
			for (Component c : g.getAllComponent()) {
				c.start();
			}
		}
	}
	
	@Override
	public void update(double dt) {
		for (GameObject g : this.menuItems) {
			g.update(dt);
		}
	}
	
	@Override
	public void draw(Graphics2D g2D) {
		g2D.drawImage(this.containerBg.image, 0, Const.CONTAINER_OFFSET_Y, this.containerBg.width, this.containerBg.height, null);
		
		for (GameObject g : this.menuItems) {
			g.draw(g2D);
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
