package eg.edu.alexu.csd.oop.draw;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.Map.Entry;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import javax.xml.parsers.ParserConfigurationException;


public class Engine implements DrawingEngine {

	
	LinkedList<Shape> shapesList;
	
	private Stack<Object> undoStack = new Stack<Object>();
	private Stack<Object> redoStack = new Stack<Object>();
	private final int MaxUndoSize = 20;
	private List<Class<? extends Shape>> classList = new LinkedList<Class<? extends Shape>>();
	
	
	public Engine() {
		shapesList = new LinkedList<Shape>();
	}

	@Override
	public void refresh(Graphics canvas) {
		// TODO Auto-generated method stub
		//drawing all shapes
		for (int i = 0; i < shapesList.size(); i++) {
			((Shape) shapesList.get(i)).draw(canvas);
		}
	}

	
	@Override
	public void addShape(Shape shape) {
		// TODO Auto-generated method stu
		undoTracer(shapesList.clone());
		shapesList.add(shape);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void removeShape(Shape shape) {
		// TODO Auto-generated method stub
		for (int i = 0; i < shapesList.size(); i++) {
			if (shapesList.get(i) instanceof Shape
					&& shape.equals(((Shape) shapesList.get(i)))) {
				undoTracer((LinkedList) shapesList.clone());
				shapesList.remove(i);
			}
		}
	}

	
	@Override
	public void updateShape(Shape oldShape, Shape newShape) {
		for (int i = 0; i < shapesList.size(); i++) {
			if (equals(oldShape,shapesList.get(i))) {
				undoTracer(shapesList.clone());
				System.out.println("undo observed");
				shapesList.remove(i);
				shapesList.add(newShape);
				break;
			}
		}
	}

	@Override
	public Shape[] getShapes() {
		// TODO Auto-generated method stub
		Shape[] shapesArray = new Shape[shapesList.size()];
		for (int i = 0; i < shapesList.size(); i++) {
			shapesArray[i] = shapesList.get(i);
		}
		return shapesArray;
	}
	
	@Override
	public List<Class<? extends Shape>> getSupportedShapes() {

		/////////////////manually//////////////////
		classList.add(Ellipse.class);
		classList.add(Rectangle.class);
		classList.add(Line.class);
		classList.add(Triangle.class);
		// ////////////////automatically //////////////
		try {
			findPlugins();
		} catch (Exception e) {
			// log
			System.out.println("cant path ");
		}

		return classList;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void undo() {
		if (!undoStack.isEmpty()) {
			redoStack.push(shapesList.clone());
			shapesList = (LinkedList) undoStack.pop();
		} else {
			//throw new RuntimeException();
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void redo() {
		if (!redoStack.isEmpty()) {
			undoStack.push(shapesList.clone());
			shapesList = (LinkedList) redoStack.pop();

		} else {
		//	throw new RuntimeException();
		}
	}
	
	@Override
	public void save(String path) {
		// TODO Auto-generated method stub
		try {
			 String path2 = path.toLowerCase();
			if((path2.endsWith(".xml"))){
				WriteXML(path);
			}
			else if((path2.endsWith(".json"))){
				WriteJson(path);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void load(String path) {
		// TODO Auto-generated method stub
		try {
			 String path2 = path.toLowerCase();
			if(path2.contains(".xml")){
				LinkedList<Shape> tempList;
				XMLReader xml = new XMLReader();
				tempList= xml.ReadFileXML(path);
			    shapesList.clear();
				shapesList = tempList;
				redoStack.clear();
				undoStack.clear();
			}
			else if(path2.contains("json")){
				LinkedList<Shape> tempList;
				JsonReader  json = new JsonReader();
				tempList =json.JsonFileReader(path);
			    shapesList.clear();
				shapesList = tempList;
				redoStack.clear();
				undoStack.clear();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	
	private void undoTracer(Object o) {
		if (undoStack.size() == MaxUndoSize) {
			undoStack.remove(0);
		}
		undoStack.push(o);
		redoStack.clear();
	}
	
	public boolean undoValid() {
		if (undoStack.isEmpty()) {
			return false;
		}
		return true;
	}

	public boolean redoValid() {
		if (redoStack.isEmpty()) {
			return false;
		}
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public java.util.List<Class<? extends Shape>> findPlugins() {

		List<File> allFiles = new ArrayList<File>();

		String classpath = System.getProperty("java.class.path");
		String[] pathes = classpath.split(System.getProperty("path.separator"));

		for (int i = 0; i < pathes.length; i++) {

			File myfile = new File(pathes[i]);
			if (myfile.isFile()) {

				if (myfile.getName().endsWith(".jar")) {
					allFiles.add(myfile);
				}

			} else if (myfile.isDirectory()) {

				File[] myDirectory = myfile.listFiles(new FilenameFilter() {

					public boolean accept(File directory, String fileName) {

						return fileName.endsWith(".jar");
					}
				});
				for (File f : myDirectory)
					allFiles.add(f);
			}
		}
		for (File file : allFiles) {

			JarFile jarFile = null;
			try {
				jarFile = new JarFile(file);
			} catch (IOException e) {

			}
			Enumeration<JarEntry> entries = jarFile.entries();
			JarEntry entryName = null;
			while (entries.hasMoreElements()) {
				entryName = entries.nextElement();
				if (entryName.getName().endsWith(".class")) {
					String s = entryName.getName();
					s = s.split("\\.")[0];
					s = s.replaceAll("/", ".");
					System.out.println(s);
					Class<?> cls;
					try {
						cls = Class.forName(s);
						if (Shape.class.isAssignableFrom(cls)) {
							classList.add((Class<? extends Shape>) cls);
						}
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		return classList;
	}
	
	public void WriteXML(String path) throws FileNotFoundException, ParserConfigurationException, IOException{
		FileWriter writer = new FileWriter(path);
		Shape[] shapes = getShapes();
		
		writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		writer.write("<Shapes>\n\n");
		writer.write("\t<Numbers>n-" + shapes.length + "</Numbers>\n" );

		for(int i=0; i<shapes.length; i++){
			Shape shape = shapes[i];
			/////////////////////class Name//////////////////////
			String className = shape.getClass().toString();
			String[] temp = className.split("[.]");
			className = temp[temp.length - 1];
			className += ".";
			className += i;
			writer.write("\t<" + className + ">\n");
			/////////////////////class Name//////////////////////
			
			///////////////////////Color/////////////////////////
			Color color = shape.getColor();
			if(color == null){

			}
			else{
				writer.write("\t\t<Color." + i + ">\n");
				writer.write("\t\t\t<Red>r-" + color.getRed() +"</Red>\n");
				writer.write("\t\t\t<Green>g-" + color.getGreen()+"</Green>\n");
				writer.write("\t\t\t<Blue>b-" + color.getBlue() +"</Blue>\n");
				writer.write("\t\t</Color." + i + ">\n");
			}
			///////////////////////Color/////////////////////////
			
			//////////////////////fill Color/////////////////////
			color = shape.getFillColor();
			if(color == null){

			}else{
				writer.write("\t\t<FillColor." + i + ">\n");
				writer.write("\t\t\t<Red>r-" + color.getRed() + "</Red>\n");
				writer.write("\t\t\t<Green>g-" + color.getGreen() + "</Green>\n");
				writer.write("\t\t\t<Blue>b-" + color.getBlue() + "</Blue>\n");
				writer.write("\t\t</FillColor." + i + ">\n");
			}
			/////////////////////fill Color/////////////////////
			
			///////////////////////Position/////////////////////
			Point point = shape.getPosition();
			if(point == null){

			}else{
				writer.write("\t\t<Position." + i + ">\n");
				writer.write("\t\t\t<X>x-" + point.getX() +"</X>\n");
				writer.write("\t\t\t<Y>y-" + point.getY()+"</Y>\n");
				writer.write("\t\t</Position." + i + ">\n");
			}
			///////////////////////Position/////////////////////
			
			///////////////////num of Properties////////////////
			Map<String, Double> map = shape.getProperties();
			int proNum = 0;
			if (map != null){
				proNum = map.size();
			}
			writer.write("\t\t<propertiesNumber." + i + ">p-" + proNum + "</propertiesNumber."+ i + ">\n");
			///////////////////num of Properties////////////////
			
			///////////////////////Properties///////////////////
			if (proNum != 0) {
				writer.write("\t\t<properties." + i + ">\n");
				for (Entry<String, Double> entry : map.entrySet()) {
					String temp1 = entry.getKey().replaceAll("\\s+"," ");
					writer.write("\t\t\t<" + temp1 + ">v-" + entry.getValue() + "</" + entry.getKey() + ">\n");
				}
				writer.write("\t\t</properties." + i + ">\n");
			}
			/////////////////////// Properties///////////////////
			writer.write("\t</" + className + ">\n\n");
		}
		writer.write("</Shapes>\n");
		writer.close();
	}
	
	public  void WriteJson(String path) throws IOException {
		FileWriter writer = new FileWriter(path);
		Shape[] shapes =getShapes();
		int count = 0;
		int count2 = 0;
		writer.write("{\n \"Shapes\":{\n  \"Shape\":[\n");
		for (Shape o : shapes) {
			count++;
			String[] className = o.getClass().toString().split(" ");
			writer.write("   {\n");
			writer.write("   \"Class\": \"" + className[1] + "\",\n");
			if (o.getPosition() == null) {
				writer.write("   \"Postion\": \"null\",\n");
			} else {
				writer.write("   \"Postionx\": \"" + o.getPosition().x
						+ "\",\n");
				writer.write("   \"PostionY\": \"" + o.getPosition().y
						+ "\",\n");
			}
			if (o.getColor() == null) {
				writer.write("   \"Color\": \"null\",\n");
			} else {
				writer.write("   \"ColorRed\": \"" + o.getColor().getRed()
						+ "\",\n");
				writer.write("   \"ColorGreen\": \"" + o.getColor().getGreen()
						+ "\",\n");
				writer.write("   \"ColorBlue\": \"" + o.getColor().getBlue()
						+ "\",\n");
			}
			if (o.getFillColor() == null) {
				writer.write("   \"FillColor\": \"null\",\n");
			} else {
				writer.write("   \"FillColorRed\": \""
						+ o.getFillColor().getRed() + "\",\n");
				writer.write("   \"FillColorGreen\": \""
						+ o.getFillColor().getGreen() + "\",\n");
				writer.write("   \"FillColorBlue\": \""
						+ o.getFillColor().getBlue() + "\",\n");
			}
			if (o.getProperties() == null) {
				writer.write("   \"NumberOfProperties\": \"0\"");
			} else {
				writer.write("   \"NumberOfProperties\": \""
						+ o.getProperties().size() + "\",");
				for (Entry<String, Double> property : o.getProperties()
						.entrySet()) {
					count2++;
					writer.write("\n   \"" + property.getKey() + "\": \""
							+ property.getValue() + "\"");
					if (count2 != o.getProperties().size()) {
						writer.write(",");
					}
				}
			}
			count2 = 0;
			writer.write("\n   }");
			if (count != shapes.length) {
				writer.write(",");
			}
			writer.write("\n");
		}
		count = 0;
		writer.write("  ]\n }\n}");
		writer.close();
	}
	public boolean equals(Shape s1,Shape s2){
		
		if(s1.getProperties().equals(s2.getProperties())&&s1.getColor()==s2.getColor()
			&&s1.getFillColor()==s2.getFillColor()&&s1.getPosition().x==s2.getPosition().x
			&&s2.getPosition().y==s1.getPosition().y){
			return true;
		}
		System.out.println("not equal");
		return false ;
	}
}