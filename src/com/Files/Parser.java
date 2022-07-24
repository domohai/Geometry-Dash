package com.Files;
import com.Component.BoxBounds;
import com.Component.Sprite;
import com.Component.TriangleBounds;
import com.abc.Component;
import com.abc.GameObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Parser {
	private static int offset = 0;
	private static int line = 1;
	private static byte[] bytes;
	
	public static void openFile(String filename) {
		File file = new File("Data/" + filename + ".zip");
		if (!file.exists()) return;
		//offset = 0;
		//line = 1;
		try {
			ZipFile zipFile = new ZipFile("Data/" + filename + ".zip");
			ZipEntry jsonfile = new ZipEntry(zipFile.getEntry(filename + ".json"));
			InputStream stream = zipFile.getInputStream(jsonfile);
			Parser.bytes = stream.readAllBytes();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	private static boolean isDigit(char c) {
		return c >= '0' && c <= '9';
	}
	
	public static int parseInt() {
		skipSpace();
		char c;
		StringBuilder builder = new StringBuilder();
		while (!atEnd() && (isDigit(peek()) || peek() == '-')) {
			c = advance();
			builder.append(c);
		}
		return Integer.parseInt(builder.toString());
	}
	
	public static double parseDouble() {
		skipSpace();
		char c;
		StringBuilder builder = new StringBuilder();
		while (!atEnd() && (isDigit(peek()) || peek() == '-' || peek() == '.')) {
			c = advance();
			builder.append(c);
		}
		return Double.parseDouble(builder.toString());
	}
	
	public static float parseFloat() {
		float f = (float) parseDouble();
		consume('f');
		return f;
	}
	
	public static String parseString() {
		skipSpace();
		char c;
		StringBuilder builder = new StringBuilder();
		consume('"');
		while (!atEnd() && peek() != '"') {
			c = advance();
			builder.append(c);
		}
		consume('"');
		return builder.toString();
	}
	
	public static boolean parseBoolean() {
		skipSpace();
		StringBuilder builder = new StringBuilder();
		if (!atEnd() && (peek() == 't')) {
			builder.append("true");
			consume('t');
			consume('r');
			consume('u');
			consume('e');
		} else if (!atEnd() && peek() == 'f') {
			builder.append("false");
			consume('f');
			consume('a');
			consume('l');
			consume('s');
			consume('e');
		} else {
			System.out.println("Expecting boolean but got " + peek() + "at line: " + Parser.line);
			System.exit(-1);
		}
		return builder.toString().compareTo("true") == 0;
	}
	
	public static void skipSpace() {
		while (!atEnd() && (peek() == ' ' || peek() == '\n' || peek() == '\t' || peek() == '\r')) {
			if (peek() == '\n') Parser.line++;
			Parser.advance();
		}
	}
	
	public static void consume(char c) {
		char actual = peek();
		if (actual != c) {
			System.out.println("Error: expected " + c + "not " + actual + "at line: " + line);
			System.exit(-1);
		}
		offset++;
	}
	
	public static char advance() {
		char c = (char)bytes[offset];
		offset++;
		return c;
	}
	
	public static void consumeBeginObjectProperty(String name) {
		skipSpace();
		checkString(name);
		skipSpace();
		consume(':');
		skipSpace();
		consume('{');
	}
	
	public static void consumeEndObjectProperty() {
		skipSpace();
		consume('}');
	}
	
	public static Component parseComponent() {
		String componentTitle = Parser.parseString();
		skipSpace();
		Parser.consume(':');
		skipSpace();
		Parser.consume('{');
		
		switch (componentTitle) {
			case "Sprite":
				return Sprite.deserialize();
			case "BoxBounds":
				return BoxBounds.deserialize();
			case "TriangleBounds":
				return TriangleBounds.deserialize();
			default:
				System.out.println("Can not find component: " + componentTitle + "at line " + Parser.line);
				System.exit(-1);
				break;
		}
		return null;
	}
	
	public static GameObject parseGameObject() {
		if (bytes.length == 0 || atEnd()) return null;
		if (peek() == ',') Parser.consume(',');
		skipSpace();
		if (atEnd()) return null;
		return GameObject.deserialize();
	}
	
	public static String consumeStringProperty(String name) {
		skipSpace();
		checkString(name);
		consume(':');
		return parseString();
	}
	
	public static int consumeIntProperty(String name) {
		skipSpace();
		checkString(name);
		skipSpace();
		consume(':');
		return parseInt();
	}
	
	public static float consumeFloatProperty(String name) {
		skipSpace();
		checkString(name);
		skipSpace();
		consume(':');
		return parseFloat();
	}
	
	public static double consumeDoubleProperty(String name) {
		skipSpace();
		checkString(name);
		skipSpace();
		consume(':');
		return parseDouble();
	}
	
	public static boolean consumeBooleanProperty(String name) {
		skipSpace();
		checkString(name);
		skipSpace();
		consume(':');
		return parseBoolean();
	}
	
	private static void checkString(String str) {
		String title = Parser.parseString();
		if (title.compareTo(str) != 0) {
			System.out.println("Expecting " + str + "not " + title + "at line: " + Parser.line);
			System.exit(-1);
		}
	}
	
	public static char peek() {
		return (char)bytes[offset];
	}
	
	public static boolean atEnd() {
		return offset == bytes.length;
	}

}
