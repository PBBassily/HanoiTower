package eg.edu.alexu.csd.oop.draw;

import java.awt.Color;
import java.awt.Point;
import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;

public class JsonReader {
	 
	public LinkedList<Shape> JsonFileReader(String path) throws Exception {
		LinkedList<Shape> list = new LinkedList<Shape>();
		Map<String, Double> properties;
		Scanner reader = new Scanner(new File(path));
		Shape o;

		String[] param_val;
		String val, param, loaded = "";
		reader.nextLine();// skip {
		reader.nextLine();// skip "Shapes":{
		reader.nextLine();// skip "Shape":[
		while (true) {
			Point p = new Point();
			// //////////////////////// getting shape
			// class/////////////////////////
			loaded = reader.nextLine();// skip { but stop when finding ]
			if (loaded.contains("]")) {
				break;
			}
			loaded = reader.nextLine();
			param_val = loaded.split("[:]");
			val = param_val[1].replaceAll("[\";, {}]", "");
			Class<?> c = Class.forName(val);
			o = (Shape) c.newInstance();
			// ////////////////////////getting shape
			// position/////////////////////////
			loaded = reader.nextLine();
			param_val = loaded.split("[:]");
			val = param_val[1].replaceAll("[\";, {}]", "");
			if (val.contains("null")) {
				o.setPosition(null);
			} else {
				p.x = Integer.parseInt(val);

				loaded = reader.nextLine();
				param_val = loaded.split("[:]");
				val = param_val[1].replaceAll("[\";, {}]", "");
				p.y = Integer.parseInt(val);

				o.setPosition(p);
			}
			// ////////////////////////getting shape color
			// /////////////////////////
			loaded = reader.nextLine();
			param_val = loaded.split("[:]");
			val = param_val[1].replaceAll("[\";, {}]", "");
			if (val.contains("null")) {
				o.setColor(null);
			} else {
				int red = Integer.parseInt(val);

				loaded = reader.nextLine();
				param_val = loaded.split("[:]");
				val = param_val[1].replaceAll("[\";, {}]", "");
				int green = Integer.parseInt(val);

				loaded = reader.nextLine();
				param_val = loaded.split("[:]");
				val = param_val[1].replaceAll("[\";, {}]", "");
				int blue = Integer.parseInt(val);

				o.setColor(new Color(red, green, blue));
			}
			// ////////////////////////getting shape fill color
			// /////////////////////////
			loaded = reader.nextLine();
			param_val = loaded.split("[:]");
			val = param_val[1].replaceAll("[\";, {}]", "");
			if (val.contains("null")) {
				o.setFillColor(null);
			} else {
				int redFill = Integer.parseInt(val);

				loaded = reader.nextLine();
				param_val = loaded.split("[:]");
				val = param_val[1].replaceAll("[\";, {}]", "");
				int greenFill = Integer.parseInt(val);

				loaded = reader.nextLine();
				param_val = loaded.split("[:]");
				val = param_val[1].replaceAll("[\";, {}]", "");
				int blueFill = Integer.parseInt(val);

				o.setFillColor(new Color(redFill, greenFill, blueFill));
			}
			// ////////////////////////getting shape
			// properties/////////////////////////
			properties = new HashMap<String, Double>();
			loaded = reader.nextLine();
			param_val = loaded.split("[:]");
			val = param_val[1].replaceAll("[\";, {}]", "");
			int proNum = Integer.parseInt(val);
			if (proNum == 0) {
				o.setProperties(null);
			} else {
				for (int i = 0; i < proNum; i++) {
					loaded = reader.nextLine();
					param_val = loaded.split("[:]");
					param = param_val[0].replaceAll("[\";, {}]", "");
					val = param_val[1].replaceAll("[\";, {}]", "");
					if (val.contains("null")) {
						properties.put(param, null);
					} else {
						properties.put(param, Double.parseDouble(val));
					}
				}
				o.setProperties(properties);
			}

			loaded = reader.nextLine();// skip },

			list.add(o);
		}
		reader.close();
		return list;
	}	
}