package eg.edu.alexu.csd.oop.draw;

import java.awt.Color;
import java.awt.Point;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Test {

	public static void main(String[] args) {
		Shape s1 = new Ellipse();
		s1.setColor(new Color(0, 1, 2));
		Map<String, Double> properties=new HashMap<String, Double>();
		properties.put("1", 1.0);
		properties.put("2", 2.0);
		s1.setProperties(properties);
		try {
			Shape s2 = (Shape) s1.clone();
			properties.put("3", 3.0);
			s1.setPosition(new Point(0,3));
			System.out.println(s2.getProperties());
			System.out.println(s2.getPosition());
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public static void pluginDrawer(){
		String separator=System.getProperty("file.separator");
		
		JarFile jarFile = null;
		try {
			jarFile = new JarFile("bin"+separator+"plugin.jar");
		} catch (Exception e) {
			System.out.println("cant call class");

		}
		Enumeration<JarEntry> entries = jarFile.entries();
		JarEntry entry = null;
		String entryName="";
		while (entries.hasMoreElements()) {
			entry = entries.nextElement();
			entryName=entry.getName();
			if (entryName.endsWith(".class")) {
				//System.out.println(entryName);
				//s = s.split("\\.")[0];
				//entryName = entryName.replaceAll(System.getProperty("file.separator"), ".");
				//entryName = entryName.split("bin")[1];
				System.out.println(entryName);
				entryName=entryName.replaceAll("/", ".");
				System.out.println(entryName);
				
				Class<?> cls;
				try {
					cls = Class.forName(entryName);
					if (Shape.class.isAssignableFrom(cls)) {
						
					}
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					System.out.println("cant load class");
				}
			}
		}
	}
	public static void plugin2() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException{
		
		String separator=System.getProperty("file.separator");
		String pathToJar="bin"+separator+"plugin.jar";
		
		JarFile jarFile = new JarFile(pathToJar);
		Enumeration<JarEntry> e = jarFile.entries();

		URL[] urls = { new URL("jar:file:" + pathToJar+"!/") };
		URLClassLoader cl = URLClassLoader.newInstance(urls);

		    while (e.hasMoreElements()) {
		        JarEntry je = (JarEntry) e.nextElement();
		        if(je.isDirectory() || !je.getName().endsWith(".class")){
		            continue;
		        }
		    // -6 because of .class
		    String className = je.getName().substring(0,je.getName().length()-6);
		    className = className.replace('/', '.');
		    System.out.println(className);
		    Class<?> c = cl.loadClass(className);
		    Object o = c.newInstance();
		    System.out.println(o.getClass());
		    jarFile.close();
	}

}}
