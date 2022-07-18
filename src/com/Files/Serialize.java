package com.Files;

public abstract class Serialize {
	public abstract String serialize(int tabSize);
	
	public String addStringProperty(String name, String value, int tabSize, boolean newLine, boolean coma) {
		return addTabs(tabSize) + "\"" + name + "\": " + "\"" + value + "\"" + addEnding(newLine, coma);
	}
	
	public String addIntProperty(String name, int value, int tabSize, boolean newLine, boolean coma) {
		return addTabs(tabSize) + "\"" + name + "\": " + value + addEnding(newLine, coma);
	}
	
	public String addDoubleProperty(String name, double value, int tabSize, boolean newLine, boolean coma) {
		return addTabs(tabSize) + "\"" + name + "\": " + value + addEnding(newLine, coma);
		
	}
	
	public String addFloatProperty(String name, float value, int tabSize, boolean newLine, boolean coma) {
		return addTabs(tabSize) + "\"" + name + "\": " + value + "f" + addEnding(newLine, coma);
	}
	
	public String addBooleanProperty(String name, boolean value, int tabSize, boolean newLine, boolean coma) {
		return addTabs(tabSize) + "\"" + name + "\": " + value + addEnding(newLine, coma);
	}
	
	public String beginObjectProperty(String name, int tabSize) {
		return addTabs(tabSize) + "\"" + name + "\": {" + addEnding(true, false);
		
	}
	
	public String closeObjectProperty(int tabSize) {
		return addTabs(tabSize) + "}";
	}
	
	public String addEnding(boolean newLine, boolean coma) {
		String str = "";
		if (coma) str += ",";
		if (newLine) str += "\n";
		return str;
	}
	
	public String addTabs(int tabSize) {
		return "\t".repeat(tabSize);
	}
	
}
