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
		this.currentTab = this.tabs.get(3);
		
		addTabObjects();
		
	}
	
	private void addTabObjects() {
		Spritesheet buttonSprites = AssetPool.getSpritesheet("assets/buttonSprites.png");
		Spritesheet blockSprites = AssetPool.getSpritesheet("assets/groundSprites.png");
		Spritesheet spikeSprites = AssetPool.getSpritesheet("assets/spikes.png");
		Spritesheet bigSprites = AssetPool.getSpritesheet("assets/bigSprites.png");
		Spritesheet smallBlocks = AssetPool.getSpritesheet("assets/smallBlocks.png");
		Spritesheet portalSprites = AssetPool.getSpritesheet("assets/portal.png");
		
		for (int i = 0; i < blockSprites.sprites.size(); i++) {
			Sprite currentSprite = blockSprites.sprites.get(i);
			int x = Const.BUTTON_OFFSET_X + (currentSprite.col * Const.BUTTON_WIDTH) +
					(currentSprite.col * Const.BUTTON_SPACING_HZ);
			int y = Const.BUTTON_OFFSET_Y + (currentSprite.row * Const.BUTTON_HEIGHT) +
					(currentSprite.row * Const.BUTTON_SPACING_VT);
			// first tab container
			GameObject obj = new GameObject("Gen", new Transform(new Vector2D(x, y)), 10);
			obj.setUi(true);
			obj.setNonserializable();
			obj.addComponent(currentSprite.copy());
			MenuItem menuItem = new MenuItem(x, y, Const.BUTTON_WIDTH, Const.BUTTON_HEIGHT,
					buttonSprites.sprites.get(0), buttonSprites.sprites.get(1));
			obj.addComponent(menuItem);
			obj.addComponent(new BoxBounds(Const.TILE_WIDTH, Const.TILE_HEIGHT));
			this.tabMaps.get(this.tabs.get(0)).add(obj);
			
			//menuItems.add(obj);
			// second tab container
			if (i < smallBlocks.sprites.size()) {
				obj = new GameObject("Gen", new Transform(new Vector2D(x, y)), 10);
				obj.setUi(true);
				obj.setNonserializable();
				menuItem = menuItem.copy();
				obj.addComponent(smallBlocks.sprites.get(i));
				obj.addComponent(menuItem);
				if (i == 0) {
					obj.addComponent(new BoxBounds(Const.TILE_WIDTH, 16));
				}
				this.tabMaps.get(tabs.get(1)).add(obj);
			}
			// add fourth tab
			if (i < spikeSprites.sprites.size()) {
				obj = new GameObject("Gen", new Transform(new Vector2D(x, y)), 10);
				obj.setUi(true);
				obj.setNonserializable();
				menuItem = menuItem.copy();
				obj.addComponent(spikeSprites.sprites.get(i));
				obj.addComponent(menuItem);
				// TODO:: add triangle bounds here
				
				this.tabMaps.get(tabs.get(3)).add(obj);
				
			}
			// add fifth tab
			if (i == 0) {
				obj = new GameObject("Gen", new Transform(new Vector2D(x, y)), 10);
				obj.setUi(true);
				obj.setNonserializable();
				menuItem = menuItem.copy();
				obj.addComponent(menuItem);
				obj.addComponent(bigSprites.sprites.get(i));
				obj.addComponent(new BoxBounds(Const.TILE_WIDTH*2, 56));
				this.tabMaps.get(tabs.get(4)).add(obj);
			}
			
			// add sixth tab
			if (i < portalSprites.sprites.size()) {
				obj = new GameObject("Gen", new Transform(new Vector2D(x, y)), 10);
				obj.setUi(true);
				obj.setNonserializable();
				menuItem = menuItem.copy();
				obj.addComponent(menuItem);
				obj.addComponent(portalSprites.sprites.get(i));
				obj.addComponent(new BoxBounds(44, 85));
				
				// TODO: create portal component
				
				this.tabMaps.get(tabs.get(5)).add(obj);
			}
		}
	}
	
	@Override
	public void start() {
		for (GameObject g : tabs) {
			for (GameObject g1 : tabMaps.get(g)) {
				for (Component c : g1.getAllComponent()) {
					c.start();
				}
			}
		}
	}
	
	@Override
	public void update(double dt) {
		for (GameObject g : this.tabMaps.get(currentTab)) {
			g.update(dt);
		}
	}
	
	@Override
	public void draw(Graphics2D g2D) {
		g2D.drawImage(this.containerBg.image, 0, Const.CONTAINER_OFFSET_Y, this.containerBg.width, this.containerBg.height, null);
		
		for (GameObject g : this.tabMaps.get(currentTab)) {
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
