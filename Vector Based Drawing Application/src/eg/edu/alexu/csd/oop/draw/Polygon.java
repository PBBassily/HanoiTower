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

public class Polygon implements Shape {
	private Point position;
	private Map<String, Double> properties;
	private Color stroke, fill;
	private int x[];
	private int y[];
	private int len;

	public Polygon() {
		 properties =new HashMap<String, Double>();
		 properties.put("Length",null);

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
		
		double h;
		h=properties.get("Length");

			len=(int)h;
		
		
		graphSettings.setPaint(getColor());
		polygon();
		graphSettings.drawPolyline(x, y, 5);
//		graphSettings.setPaint(getFillColor());
//		graphSettings.fillOval(getPosition().x-width/2, getPosition().y-hieght/2, width, hieght);
		

	}

	public Object clone() throws CloneNotSupportedException {
		Shape o = new Polygon();
		o.setColor(getColor());
		o.setFillColor(getFillColor());
		o.setProperties(o.getProperties());
		o.setPosition(getPosition());
		return o;
	}
	public void polygon(){
		double R=len/(2*Math.sin(Math.toRadians(180/5)));
		x=new int[5];
		y=new int[5];
		for(int i=0;i<5;i++){
			x[i]=(int) (getPosition().x + R*Math.cos(Math.toRadians((2.0*Math.PI*(i+1))/5)));
			y[i]=(int) (getPosition().y + R*Math.sin(Math.toRadians(2.0*Math.PI*(i+1)/5)));
		}
	}

}
