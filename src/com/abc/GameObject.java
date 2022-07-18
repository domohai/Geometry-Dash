package com.abc;
import com.Files.Parser;
import com.Files.Serialize;
import com.dataStructure.Transform;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public class GameObject extends Serialize {
	private List<Component> components;
	private String name;
	public Transform transform;
	private boolean serializable = true;
	public boolean isUi = false;
	public int zIndex;
	
	public GameObject(String name, Transform transform, int zIndex) {
		this.name = name;
		this.transform = transform;
		this.components = new ArrayList<>();
		this.zIndex = zIndex;
	}
	
	public void addComponent(Component c) {
		components.add(c);
		c.gameObject = this;
	}
	
	public void setUi(boolean val) {
		this.isUi = val;
	}
	
	public GameObject copy() {
		GameObject newG = new GameObject("new G", this.transform.copy(), this.zIndex);
		for (Component c : components) {
			Component copy = c.copy();
			if (copy != null) {
				newG.addComponent(copy);
			}
		}
		
		return newG;
	}
	
	public List<Component> getAllComponent() {
		return this.components;
	}
	
	public void setNonserializable() {
		serializable = false;
	}
	
	// confused
	public <T extends Component> T getComponent(Class<T> componentClass) {
		for (Component x : components) {
			// technically if componentClass is equal to class of x
			if (componentClass.isAssignableFrom(x.getClass())) {
				try {
					return componentClass.cast(x);
				} catch (ClassCastException e) {
					e.printStackTrace();
					System.exit(-1);
				}
			}
		}
		return null;
	}
	
	public <T extends Component> void removeComponent(Class<T> componentClass) {
		for (Component c : components) {
			if (componentClass.isAssignableFrom(c.getClass())) {
				components.remove(c);
				return;
			}
		}
	
	}
	
	public void update(double deltatime) {
		for (Component c : components) {
			c.update(deltatime);
		}
	}
	
	public void draw(Graphics2D g2D) {
		for (Component c : components) {
			c.draw(g2D);
		}
	}
	
	@Override
	public String serialize(int tabSize) {
		if (!serializable) return "";
		StringBuilder builder = new StringBuilder();
		// gameobject
		builder.append(beginObjectProperty("GameObject", tabSize));
		// transform
		builder.append(transform.serialize(tabSize+1));
		builder.append(addEnding(true, true));
		
		builder.append(addStringProperty("Name", name, tabSize+1, true, true));
		//
		if (components.size() > 0) {
			builder.append(addIntProperty("zIndex", this.zIndex, tabSize+1, true, true));
			builder.append(beginObjectProperty("Components", tabSize+1));
		} else {
			builder.append(addIntProperty("zIndex", this.zIndex, tabSize+1, true, false));
		}
		int i = 0;
		for (Component c : components) {
			String str = c.serialize(tabSize+2);
			if (str.compareTo("") != 0) {
				builder.append(str);
				if (i != components.size()-1) {
					builder.append(addEnding(true, true));
				} else {
					builder.append(addEnding(true, false));
				}
			}
			i++;
		}
		if (components.size() > 0) {
			builder.append(closeObjectProperty(tabSize+1));
		}
		builder.append(addEnding(true, false));
		builder.append(closeObjectProperty(tabSize));
		return builder.toString();
	}
	
	public static GameObject deserialize() {
		Parser.consumeBeginObjectProperty("GameObject");
		Transform transform = Transform.deserialize();
		Parser.consume(',');
		String name = Parser.consumeStringProperty("Name");
		Parser.consume(',');
		int zIndex = Parser.consumeIntProperty("zIndex");
		GameObject go = new GameObject(name, transform, zIndex);
		if (Parser.peek() == ',') {
			Parser.consume(',');
			Parser.consumeBeginObjectProperty("Components");
			go.addComponent(Parser.parseComponent());
			while (Parser.peek() == ',') {
				Parser.consume(',');
				go.addComponent(Parser.parseComponent());
			}
			Parser.consumeEndObjectProperty();
		}
		Parser.consumeEndObjectProperty();
		return go;
	}
	
}
