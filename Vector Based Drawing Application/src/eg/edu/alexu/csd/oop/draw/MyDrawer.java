package eg.edu.alexu.csd.oop.draw;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.swing.JFileChooser;


@SuppressWarnings("serial")
public class MyDrawer extends JFrame {
	
	private URL url;
	private ImageIcon icon;
	private int currentAction;
	private JButton lineBut, ellipseBut, rectBut ,triangle;
	private JButton redoBut,undoBut;
	private JButton brushBut,plugin;
	private Point drawStart;
	static Shape shape;
	private Shape oldShape;
	private JComboBox<String> comboBox;
	private int background = 4;
	private boolean isLocated = false;
	private boolean isModified = false;
	private JMenuBar menuBar = new JMenuBar();
	private Engine engine = new Engine();

	// Going to be used to monitor what shape to draw next

	public static void main(String[] args) {
		new MyDrawer();
	}

	public MyDrawer() {
		 		
		// Define the defaults for the JFrame
		this.setSize(550, 550);
		this.setTitle("Java Paint");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//to put buttons box
		JPanel buttonPanel = new JPanel();
		
		// Swing box that will hold all the buttons
		Box theBox = Box.createHorizontalBox();
		
		// Make all the buttons in makeMeButtons by passing the button icon.
		triangle = makeMeButtons("Triangle.png", 1);
		lineBut = makeMeButtons("Line.png", 2);
		ellipseBut = makeMeButtons("Ellipse.png", 3);
		rectBut = makeMeButtons("Rectangle.png", 4);
		
		/////////////////////////////////undoButtonConfigiration///////////////////////////
		undoBut = new JButton();
		try {
			url=new URL(MyDrawer.class.getResource("/resources/")+"Undo.png");
		} catch (MalformedURLException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		icon = new ImageIcon(url);
		undoBut.setIcon(icon);
		undoBut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				engine.undo();
				notifyComboBox();
			}
		});
		/////////////////////////////////redoButtonConfigiration///////////////////////////
		redoBut = new JButton();
		try {
			url=new URL(MyDrawer.class.getResource("/resources/")+"Redo.png");
		} catch (MalformedURLException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		icon = new ImageIcon(url);
		redoBut.setIcon(icon);
		redoBut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				engine.redo();
				notifyComboBox();
			}
		});
		/////////////////////////////////refreshButtonConfigiration///////////////////////////
		brushBut = new JButton();
		try {
			url=new URL(MyDrawer.class.getResource("/resources/")+"Refresh.png");
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		icon = new ImageIcon(url);
		brushBut.setIcon(icon);
		brushBut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				notifyComboBox();
				repaint();
			}
		});
		/////////////////////////////////pluginButtonConfigiration///////////////////////////
		plugin = new JButton();
		try {
			url=new URL(MyDrawer.class.getResource("/resources/")+"Plugin.png");
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		icon = new ImageIcon(url);
		plugin.setIcon(icon);
		plugin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final JFileChooser pluginChooser = new JFileChooser();
				pluginChooser.setCurrentDirectory(new File(System
						.getProperty("user.home")));
				int result = pluginChooser.showOpenDialog(null);
				if (result == JFileChooser.APPROVE_OPTION) {
					File selectedPlugin = pluginChooser.getSelectedFile();
					System.out.println("Selected file: "
							+ selectedPlugin.getAbsolutePath());
					
					try {
						plugin(selectedPlugin.getAbsolutePath());
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});

		
		// Add the buttons to the box

		theBox.add(brushBut);
		theBox.add(triangle);
		theBox.add(lineBut);
		theBox.add(ellipseBut);
		theBox.add(rectBut);
		theBox.add(undoBut);
		theBox.add(redoBut);
		theBox.add(plugin);

		// Add the box of buttons to the panel

		buttonPanel.add(theBox);
		buttonPanel.setBackground(null);

		// Position the buttons in the bottom of the frame

		getContentPane().add(buttonPanel, BorderLayout.SOUTH);

		// Make the drawing area take up the rest of the frame

		getContentPane().add(new DrawingBoard(), BorderLayout.CENTER);
		
		/////////////////////////////////MenuBarConfigiration//////////////////////
		setJMenuBar(menuBar);
		
		/////////////////////////////////FileMenuConfigiration//////////////////////
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		//save item
		JMenuItem mnNewMenu = new JMenuItem("Save");
		mnNewMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new save();
			}
		});
		mnFile.add(mnNewMenu);
		//loaditem
		JMenuItem mntmLoad = new JMenuItem("Load");
		mntmLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new load();
			}
		});
		mnFile.add(mntmLoad);
		/////////////////////////////////TextureMenuConfigiration//////////////////////
		JMenu mnBackground = new JMenu("Texture");
		menuBar.add(mnBackground);
		//item1
		JMenuItem Grid = new JMenuItem("Grid");
		Grid.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				background = 0;
			}
		});
		mnBackground.add(Grid);
		//item2
		JMenuItem Paper = new JMenuItem("Paper");
		Paper.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				background = 1;
			}
		});
		mnBackground.add(Paper);
		//item3
		JMenuItem Ancient = new JMenuItem("Ancient");
		Ancient.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				background = 2;
			}
		});
		mnBackground.add(Ancient);
		//item4
		JMenuItem CrumpledPaper = new JMenuItem("Crumpled Paper");
		CrumpledPaper.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				background = 3;
			}
		});

		mnBackground.add(CrumpledPaper);
		//item5
		JMenuItem Defualt = new JMenuItem("Defualt");
		Defualt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				background = 4;
			}
		});
		mnBackground.add(Defualt);
		/////////////////////////////////UpdateButtonConfigiration//////////////////////
		JButton update = new JButton("Update");
		update.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					oldShape = (Shape) shape.clone();
					new ShapeUpdater();
					isModified = true;
					isLocated = true;
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});
		menuBar.add(update);
		/////////////////////////////////ComboBoxConfigiration//////////////////////
		comboBox = new JComboBox<String>();
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {
					shape = (Shape) engine.getShapes()[comboBox.getSelectedIndex()].clone();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
				}
			}
		});
		
		//add comboBox last thing in menuBar
		menuBar.add(comboBox);

		// Show the frame
		this.setVisible(true);
		
		//Show at center
		this.setLocationRelativeTo(null);
	}
	
	// Make all the buttons in makeMeColorButton by passing the icon and actionNum
	// actionNum represents each shape to be drawn
	public JButton makeMeButtons(String iconFile, final int actionNum) {
		JButton theBut = new JButton();
		try {
			url=new URL(MyDrawer.class.getResource("/resources/")+iconFile);
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		 ImageIcon icon = new ImageIcon(url);
		theBut.setIcon(icon);
		theBut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				currentAction = actionNum;
				shape = getObject(currentAction);
				new ShapeSetter();
				new DrawingBoard();
			}
		});
		return theBut;
	}

	private class DrawingBoard extends JComponent {
		public DrawingBoard() {
			this.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					Shape clonedShape=null;
					drawStart = new Point(e.getX(), e.getY());
					if (isLocated) {
						shape.setPosition(drawStart);
						try {
							clonedShape =(Shape) shape.clone();
						} catch (CloneNotSupportedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						if (!isModified) {
							engine.addShape(clonedShape);
						} else {
							engine.updateShape(oldShape, clonedShape);
							isModified = false;
						}
						
						isLocated = false;
						notifyComboBox();

					}
				}

				public void mouseReleased(MouseEvent e) {
					repaint();
				}
			});
		}

		public void paint(Graphics g) {
			BufferedImage image = putBackground(background);
			g.drawImage(image, 0, 0, null);
			repaint();

			try {
				engine.refresh(g);
				repaint();
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(0);
			}
		}
	}

	private BufferedImage putBackground(int n) {
		BufferedImage image;
		try {
			switch (n) {
			case 0:
				url=new URL(MyDrawer.class.getResource("/resources/")+"01.png");
				image=ImageIO.read(url);
				return image;
			case 1:
				url=new URL(MyDrawer.class.getResource("/resources/")+"00.png");
				image=ImageIO.read(url);
				return image;
			case 2:
				url=new URL(MyDrawer.class.getResource("/resources/")+"02.jpg");
				image=ImageIO.read(url);
				return image;
			case 3:
				url=new URL(MyDrawer.class.getResource("/resources/")+"Crumpled.png");
				image=ImageIO.read(url);
				return image;
			default:
				image = ImageIO.read(new File(""));
				return null;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return null;
		}
	}

	private Shape getObject(int action) {
		Shape obj;
		switch (action) {
		case 1:
			obj = new Triangle();
			break;
		case 2:
			obj = new Line();
			break;
		case 3:
			obj = new Ellipse();
			break;
		default:
			obj = new Rectangle();
			break;
		}
		isLocated = true;
		return obj;
	}

	public class save extends JFrame {

		private JTextField path;
		private JLabel save;
		private String myPath;
		private JButton enterSave;

		public save() {
			setSize(300, 150);
			setLocationRelativeTo(null);
			setLayout(new FlowLayout());
			this.setTitle("Save");
			this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			this.setVisible(true);
			save = new JLabel("Enter your save path");
			path = new JTextField(10);
			enterSave = new JButton("Enter");
			enterSave.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					myPath = path.getText();
					System.out.println("Save");
					engine.save(myPath);
					dispose();
				}
			});
			add(save);
			add(path);
			add(enterSave);
		}
	}

	public class load extends JFrame {

		private JTextField path;
		private JLabel load;
		private String myPath;
		private JButton enterLoad;

		public load() {
			setSize(300, 150);
			setLayout(new FlowLayout());
			setLocationRelativeTo(null);
			this.setTitle("Load");
			this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			this.setVisible(true);
			load = new JLabel("Enter your load path");
			path = new JTextField(10);
			enterLoad = new JButton("Enter");
			enterLoad.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					myPath = path.getText();
					engine.load(myPath);
					repaint();
					// comboBoxListUpdater();
					dispose();
					notifyComboBox();
				}
			});
			add(load);
			add(path);
			add(enterLoad);
		}
	}

	// it will add new shapes in StringLinkedList
	// i guess um gonna call it from refresh button
	// it doesn't require a shapeUpdater
	//
	public String[] comboBoxListUpdater() {

		Shape[] arr = engine.getShapes();
		String[] list = new String[arr.length];
		for (int i = 0; i < arr.length; i++) {
			String className = arr[i].getClass().toString();
			//System.out.println(className);
			int j = i + 1;
			if (className.contains("Ellipse")) {
				className = j + " " + "Ellipse";
			} else if (className.contains("Rectangle")) {
				className = j + " " + "Rectangle";
			} else if (className.contains("Line")) {
				className = j + " " + "Line";
			} else if (className.contains("Triangle")) {
				className = j + " " + "Triangle";
			} else {
				className = j + " " + "Plugin";
			}
			list[i] = className;
		}
		return list;
	}
	public void notifyComboBox(){
		comboBox.removeAllItems();
		String[] list = comboBoxListUpdater();
		for (int i = 0; i < list.length; i++) {
			comboBox.addItem(list[i]);
		}
		comboBox.setSelectedIndex(list.length - 1);
	}

public  void plugin(String pathToJar) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException{
		
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
		        Class<?> c = cl.loadClass(className);
		        if(Shape.class.isAssignableFrom(c)){
		    		shape=(Shape) c.newInstance();
		    		if(shape.getProperties()==null||shape.getProperties().size()==0){
		    			engine.addShape((Shape) shape);
		    	}
		    	else{
		    	new ShapeSetter();
		    	isLocated=true;}
		        }
		   }
		   notifyComboBox();
		   jarFile.close();

}

	}

