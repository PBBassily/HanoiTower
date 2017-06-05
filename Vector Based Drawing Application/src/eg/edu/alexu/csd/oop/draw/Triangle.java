package eg.edu.alexu.csd.oop.draw;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Triangle implements Shape {
	private Point position;
	private Map<String, Double> properties;
	private Color stroke, fill;
	private final int ThreeSides=3;
	private final int SizeOfThree =3;

	public Triangle() {
		 properties =new HashMap<String, Double>();
		 properties.put("PointY-b",null);
		 properties.put("PointY-a",null);
		 properties.put("PointX-a",null);
		 properties.put("PointX-b",null);
		 position= new Point();
		stroke = Color.BLACK;
		fill = Color.BLACK;
	}

	@Override
	public void setPosition(Point position) {
		
		this.position = position;
	}

	@Override
	public Point getPosition() {
		
		return position;
	}

	@Override
	public void setProperties(Map<String, Double> properties) {
		
		this.properties = properties;
	}

	@Override
	public Map<String, Double> getProperties() {
		
		return properties;
	}

	@Override
	public void setColor(Color color) {
		
		this.stroke = color;

	}

	@Override
	public Color getColor() {
		return stroke;
	}

	@Override
	public void setFillColor(Color color) {
		this.fill = color;

	}

	@Override
	public Color getFillColor() {
		// TODO Auto-generated method stub
		return fill;
	}

	@Override
	public void draw(Graphics canvas) {
		// TODO Auto-generated method stub
		// Class used to define the shapes to be drawn

		Graphics2D graphSettings = (Graphics2D) canvas;
		// Antialiasing cleans up the jagged lines and defines rendering
		// rules

		graphSettings.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		// Defines the line width of the stroke

		graphSettings.setStroke(new BasicStroke(5));
		graphSettings.setComposite(AlphaComposite.getInstance(
				AlphaComposite.SRC_OVER, 1.0f));
		
		int[] x=new int[SizeOfThree];
		int[] y=new int[SizeOfThree];
		double value;
		value=properties.get("PointX-a");
		x[0]=(int) value;
		value=properties.get("PointY-a");
		y[0]=(int) value;
		value=properties.get("PointX-b");
		x[1]=(int) value;
		value=properties.get("PointY-b");
		y[1]=(int) value;
		x[2]=getPosition().x;
		y[2]=getPosition().y;
		
		graphSettings.setPaint(getColor());
		graphSettings.drawPolygon(x, y, ThreeSides);
		graphSettings.setPaint(getFillColor());
		graphSettings.fillPolygon(x, y, ThreeSides);
		
		
	}

	public Object clone() throws CloneNotSupportedException {
		Shape o = new Triangle();
		Map<String,Double> mapToBeCloned = getProperties();
		Map<String,Double> map = new HashMap<String, Double>();
		for(Entry<String,Double> entry :mapToBeCloned.entrySet()){
			map.put(entry.getKey(), entry.getValue());
		}
		o.setColor(getColor());
		o.setFillColor(getFillColor());
		o.setProperties(map);
		o.setPosition(getPosition());
		return o;
}}
