package eg.edu.alexu.csd.oop.draw;

import java.awt.Color;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLReader {

	private String thePath;

	public LinkedList<Shape> ReadFileXML(String path) throws ParserConfigurationException, SAXException, IOException,
			ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException,
		IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		thePath = path;
		Color color = null;
		Color fillColor;
		Point position;
		NodeList nodeList = null;
		Map<String, Double> properties = new HashMap<>();

		File xmlFile = new File(path);
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document xmlDoc = docBuilder.parse(xmlFile);
		
		LinkedList<Shape> shapes = new LinkedList<Shape>();
		int num = getNum();
		List<String> nodes;
		nodes = getNodes(path, "*", true);

		int counter = 0;

		for (int i = 0; i < num; i++) {
			// this for loop because nodes contains tags that i don't want
			for (counter = 0; counter < nodes.size(); counter++) {
				String s = "";
				s += i;
				if (nodes.get(counter).contains(s)) {
					nodeList = xmlDoc.getElementsByTagName(nodes.get(counter));
					break;
				}
			}
			for (int j = 0; j < nodeList.getLength(); j++) {
				Node node = nodeList.item(j);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					String[] temp2 = nodes.get(counter).split("[.]");
					// reflection
					if(temp2[0].equals("Moon")){
						temp2[0] = "plugin." + temp2[0];
					}
					String fullPathOfTheClass = "eg.edu.alexu.csd.oop.draw." + temp2[0];
					Class<?> cls = Class.forName(fullPathOfTheClass);
					Shape shape = (Shape) cls.newInstance();
					
					////////////////////////// get color////////////////////////////
					int[] colors = new int[3];
					try{
						NodeList list = xmlDoc.getElementsByTagName("Color." + i);
						Node node1 = list.item(0);
						
						Element element1 = (Element) node1;
						// red
						String s = element1.getElementsByTagName("Red").item(0).getTextContent();
						String[] temp = s.split("[-]");
						colors[0] = Integer.parseInt(temp[1]);

						// green
						s = element1.getElementsByTagName("Green").item(0).getTextContent();
						temp = s.split("[-]");
						colors[1] = Integer.parseInt(temp[1]);
						
						// blue
						s = element1.getElementsByTagName("Blue").item(0).getTextContent();
						temp = s.split("[-]");
						colors[2] = Integer.parseInt(temp[1]);
						color = new Color(colors[0], colors[1], colors[2]);
					}catch(Exception e){
						color = null;
					}
					////////////////////////// get color////////////////////////////

					////////////////////////// get fillColor////////////////////////////
					
					try {
						NodeList list = xmlDoc.getElementsByTagName("FillColor." + i);

						Node node1 = list.item(0);
						Element element1 = (Element) node1;
						// red
						String s = element1.getElementsByTagName("Red").item(0).getTextContent();
						String[] temp = s.split("[-]");
						colors[0] = Integer.parseInt(temp[1]);

						// green
						s = element1.getElementsByTagName("Green").item(0).getTextContent();
						temp = s.split("[-]");
						colors[1] = Integer.parseInt(temp[1]);

						// blue
						s = element1.getElementsByTagName("Blue").item(0).getTextContent();
						temp = s.split("[-]");
						colors[2] = Integer.parseInt(temp[1]);
						fillColor = new Color(colors[0], colors[1], colors[2]);
					} catch (Exception e) {
						fillColor = null;
					}
					////////////////////////// get fillColor////////////////////////////

					////////////////////////// get Position/////////////////////////////
					Point p = new Point();
					try {
						NodeList list = xmlDoc.getElementsByTagName("Position." + i);

						Node node1 = list.item(0);
						Element element1 = (Element) node1;
						// X
						String s = element1.getElementsByTagName("X").item(0).getTextContent();
						String[] temp = s.split("[-]");
						Double x = Double.parseDouble(temp[1]);
						p.x = x.intValue();

						// Y
						s = element1.getElementsByTagName("Y").item(0).getTextContent();
						temp = s.split("[-]");
						Double y = Double.parseDouble(temp[1]);
						p.y = y.intValue();
						position = new Point(p);
					} catch (Exception e) {
						position = null;
					}
					////////////////////////// get Position/////////////////////////////

					////////////////////////////// proNum///////////////////////////////
					NodeList list = xmlDoc.getElementsByTagName("Shapes");
					Node node1 = list.item(0);
					Element element1 = (Element) node1;
					String ss = "propertiesNumber." + i;
					String s = element1.getElementsByTagName(ss).item(0).getTextContent();
					temp2 = s.split("[-]");
					int numPro = Integer.parseInt(temp2[1]);
					//////////////////////////////// proNum//////////////////////////////

					////////////////////////// get Properties/////////////////////////////
						Map<String, Double> m = shape.getProperties();
						try {
							list = xmlDoc.getElementsByTagName("properties." + i);

							node1 = list.item(0);
							element1 = (Element) node1;
							if (numPro == 0) {
								
							} else {
								for (Map.Entry<String, Double> entry : m.entrySet()) {

									String tempString = element1.getElementsByTagName(entry.getKey()).item(0).getTextContent();
									String[] val = tempString.split("[-]"); 
									
									entry.setValue(Double.valueOf(val[1]));
									m.put(entry.getKey(), entry.getValue());
								}
								properties = m;
							}
						} catch (Exception e) {
							m = null;
						}

					////////////////////////// get Properties/////////////////////////////
					// set shape
					shape.setColor(color);
					shape.setFillColor(fillColor);
					shape.setProperties(properties);
					shape.setPosition(position);
					
					shapes.add(shape);
				}
			}
		}
		return shapes;
	}

	private List<String> getNodes(String fileName, String nodeName, boolean flag)
			throws ParserConfigurationException, SAXException, IOException {

		try {
			List<String> nodes = new ArrayList<String>();
			// Create a factory
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			// Use the factory to create a builder
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(fileName);
			// Get a list of all elements in the document
			NodeList list = doc.getElementsByTagName("*");
			for (int i = 0; i < list.getLength(); i++) {
				// Get element
				Element element = (Element) list.item(i);
				nodes.add(element.getNodeName());
			}
			return nodes;
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}

	private int getNum() throws ParserConfigurationException, SAXException, IOException {

		File xmlFile = new File(thePath);
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document xmlDoc = docBuilder.parse(xmlFile);

		NodeList list = xmlDoc.getElementsByTagName("Shapes");
		Node node1 = list.item(0);
		Element element1 = (Element) node1;
		String s = element1.getElementsByTagName("Numbers").item(0).getTextContent();
		String[] temp2 = s.split("[-]");
		int num = Integer.parseInt(temp2[1]);
		return num;
	}
}