
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JTextField;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.Vector;


public class Map extends JApplet implements ActionListener  {
	public Vector<Point> strokes;
	File file,room;
	Graphics g;
	Vector<Point> bestpath = new Vector<Point>();
	public Vector<Line2D> lines;
	boolean flag = false;
	float thickness = 3.0f;
	Vector<Point> Maps;
	BasicStroke basic = new BasicStroke(thickness);
	Logger log = new Logger();
	JTextField startx = new JTextField();
	JTextField starty = new JTextField();
	JTextField goaly = new JTextField();
	JTextField goalx = new JTextField();
	JButton navigate = new JButton("navigate");
	JButton zoomup = new JButton("Zoom in");
	JButton zoomdown = new JButton("Zoom out");
	AStar path;
	double scalefac = 1.0;
	Graph<Room> rooms = new Graph<Room>();
	
	public Map(){
		strokes = new Vector<Point>();
		lines = new Vector<Line2D>();
		file = new File("Outline.txt");
		room = new File("roomsjava.txt");
		try
		{
			Scanner scanner = new Scanner(file);
			scanner.useDelimiter(",");
			while(scanner.hasNextLine()) // read all the points from the file and store them into strokes
			{
				String line = scanner.nextLine();
				double x = Double.parseDouble(line.split(",")[0]);
				double y = Double.parseDouble(line.split(",")[1]);
				strokes.add(new Point(x,y));
			}
			
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}
		int length = strokes.size();
		 for(int j = 0; j < length; j= j+2)
		 {
			 Line2D floors = new Line2D.Double(strokes.get(j).x,strokes.get(j).y,strokes.get(j+1).x,strokes.get(j+1).y);
			 lines.add(floors);
		 }
		try {
			Scanner scanner = new Scanner(room);
			scanner.useDelimiter(",");
			while(scanner.hasNextLine()) // stores all the Rooms and their location into the Room class
			{
				String line = scanner.nextLine();
				float x1 = Float.parseFloat(line.split(",")[0]);
				float y1 = Float.parseFloat(line.split(",")[1]);
				float x2 = Float.parseFloat(line.split(",")[2]);
				float y2 = Float.parseFloat(line.split(",")[3]);
				float doorx = Float.parseFloat(line.split(",")[4]);
				float doory = Float.parseFloat(line.split(",")[5]);
				Room corridor = new Room(x1,y1,x2,y2,doorx,doory);
				rooms.addNode(corridor);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// adds all the edges to all the Room Nodes
		collisiondetect();
	}
	private void collisiondetect() {
		// TODO Auto-generated method stub
	try
	{
		for(Entry<Room, Set<Room>> key : rooms.getEntry())
		{
			for(Entry<Room, Set<Room>> k : rooms.getEntry())
			{
					rooms.addEdge(key.getKey(), k.getKey());
			}
		}
		//log.addToLog(" " + rooms.getNeighbors(data.get(0)));
		for(Iterator<Entry<Room,Set<Room>>> i = rooms.getEntry().iterator(); i.hasNext(); )
		{
			Entry<Room,Set<Room>> m = i.next();
			for(Iterator<Room> entry = m.getValue().iterator(); entry.hasNext(); )
			{
				Room k = entry.next();
				double distance = Math.sqrt((Math.pow((m.getKey().doors.y - k.doors.y),2) + (Math.pow((m.getKey().doors.x - k.doors.x),2))));
				if(distance == 0 || distance > 90)
				{
					entry.remove();
				} 
			}
		}
		for(Iterator<Entry<Room,Set<Room>>> i = rooms.getEntry().iterator(); i.hasNext(); )
		{
			Entry<Room,Set<Room>> m = i.next();
			Rectangle area = new Rectangle((int)(m.getKey().doors.x-65),(int)(m.getKey().doors.y - 65),130,130);
			for(Iterator<Room> entry = m.getValue().iterator(); entry.hasNext(); )
			{
				Room k = entry.next();
				boolean flagline = true;
				//double s1 = (m.getKey().doors.y - k.doors.y)/(m.getKey().doors.x - k.doors.x);
				for(int p = 8 ; p < strokes.size() ; p = p+2)
				{
					if(flagline){
					if((area.contains(strokes.get(p).x, strokes.get(p).y)) || (area.contains(strokes.get(p+1).x, strokes.get(p+1).y)))
					{
						
						double d = (m.getKey().doors.x - k.doors.x)*(strokes.get(p).y - strokes.get(p+1).y) - (m.getKey().doors.y - k.doors.y) * (strokes.get(p).x - strokes.get(p+1).x);
						if(d == 0) 
						{
							if(m.getKey().doors.y == k.doors.y && m.getKey().doors.y == strokes.get(p).y && m.getKey().doors.y == strokes.get(p+1).y)
							{
								if(m.getKey().doors.x <= strokes.get(p).x && k.doors.x >= strokes.get(p).x)
								{
									entry.remove();
									flagline = false;
									break;
								}
								if(m.getKey().doors.x <= strokes.get(p+1).x && k.doors.x >= strokes.get(p+1).x)
								{
									entry.remove();
									flagline = false;
									break;
								}
								if(strokes.get(p).x <= m.getKey().doors.x && strokes.get(p+1).x >= m.getKey().doors.x)
								{
									entry.remove();
									flagline = false;
									break;
								}
								if(strokes.get(p).x <= k.doors.x && strokes.get(p+1).x >= k.doors.x)
								{
									entry.remove();
									flagline = false;
									break;
								}
							}
							if(m.getKey().doors.x == k.doors.x && m.getKey().doors.x == strokes.get(p).x && m.getKey().doors.x == strokes.get(p+1).x)
							{
								if(m.getKey().doors.y <= strokes.get(p).y && k.doors.y >= strokes.get(p).y)
								{
									entry.remove();
									flagline = false;
									break;
								}
								if(m.getKey().doors.y <= strokes.get(p+1).y && k.doors.y >= strokes.get(p+1).y)
								{
									entry.remove();
									flagline = false;
									break;
								}
								if(strokes.get(p).y <= m.getKey().doors.y && strokes.get(p+1).y >= m.getKey().doors.y)
								{
									entry.remove();
									flagline = false;
									break;
								}
								if(strokes.get(p).y <= k.doors.y && strokes.get(p+1).y >= k.doors.y)
								{
									entry.remove();
									flagline = false;
									break;
								}
							}
						}
						
						double x = ((strokes.get(p).x - strokes.get(p+1).x)*(m.getKey().doors.x*k.doors.y-m.getKey().doors.y*k.doors.x) - (m.getKey().doors.x - k.doors.x)*(strokes.get(p).x*strokes.get(p+1).y-strokes.get(p).y*strokes.get(p+1).x))/d;
						
						double y = ((strokes.get(p).y - strokes.get(p+1).y)*(m.getKey().doors.x*k.doors.y-m.getKey().doors.y*k.doors.x) - (m.getKey().doors.y - k.doors.y)*(strokes.get(p).x*strokes.get(p+1).y-strokes.get(p).y*strokes.get(p+1).x))/d;
						
						double minx,miny,maxx,maxy;
						if(strokes.get(p).x > strokes.get(p+1).x)
						{
							minx = strokes.get(p+1).x; 
							maxx = strokes.get(p).x;
						}
						else
						{
							minx = strokes.get(p).x;
							maxx = strokes.get(p+1).x;
						}
						if(strokes.get(p).y > strokes.get(p+1).y)
						{
							miny = strokes.get(p+1).y;
							maxy = strokes.get(p).y;
						}
						else
						{
							miny = strokes.get(p).y;
							maxy = strokes.get(p+1).y;
						}
						double minlinex,minliney,maxlinex,maxliney;
						if(m.getKey().doors.x > k.doors.x)
						{
							minlinex = k.doors.x;
							maxlinex = m.getKey().doors.x;
						}
						else
						{
							minlinex = m.getKey().doors.x;
							maxlinex = k.doors.x;
						}
						if(m.getKey().doors.y > k.doors.y)
						{
							minliney = k.doors.y;
							maxliney = m.getKey().doors.y;
						}
						else
						{
							minliney = m.getKey().doors.y;
							maxliney = k.doors.y;
						}
						if(((x >= minx && x <= maxx) && (y >= miny && y <= maxy)) && ((x >= minlinex && x <= maxlinex) && (y >= minliney && y <= maxliney)))
						{
							entry.remove();
							flagline = false;	
						}	
					}
					}
					
				}
			}
		}
		} catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	//function to start the AStar navigation. 
	public void navigate(float sx, float sy, float gx, float gy){
		path = new AStar(this, sx , sy , gx, gy);
		bestpath = path.calcpath();
		repaint();
	}
	//returns the cost of the edge from Node 1 to Node 2
	public double getDistanceBetween(Point a,Point b)
	{
		if((a.getX() == b.getX()) || (a.getY() == b.getY()) )
		{
			return 40 * scalefac;
		}
		else{
			double distance = Math.sqrt((Math.pow((a.y - b.y),2) + (Math.pow((a.x - b.x),2))));
			return (distance * scalefac);
		} 
		
		
	}
	//returns which room the start(user) and goal(friend) are in. 
	public Room searchroom(Point start)
	{
		for(Iterator<Entry<Room,Set<Room>>> i = rooms.getEntry().iterator(); i.hasNext(); )
		{
			Entry<Room,Set<Room>> m = i.next();
			if(m.getKey().rooms.contains((start.getX() * (1/scalefac)),(start.getY() * (1/scalefac))))
			{	
				return m.getKey();
			}
		}
		return null;
	}
	//The paint function draws all the lines and the path once the bestpath is found.
	public void paint(Graphics g)
	{
		 
		 Graphics2D g1 = (Graphics2D) g;
		 g.clearRect(0, 0,1500 ,500);
		 
		 int length = strokes.size();
		 for(int i = 0; i < length; i= i+2)
		 {
			 g1.drawLine((int)(strokes.get(i).x * scalefac),(int)(strokes.get(i).y * scalefac),(int)(strokes.get(i+1).x * scalefac),(int)(strokes.get(i+1).y * scalefac));
			 g1.setStroke(basic);
		 }
		 if(flag){
			 length = bestpath.size();
			 g1.drawString("User",(int) (path.start.x * scalefac),(int)(path.start.y * scalefac));
			 g1.drawString("Friend",(int)(path.goal.x * scalefac),(int)(path.goal.y * scalefac));
			 for(int i = 0; i < length-1; i++)
			 {
				g1.setColor(Color.BLUE);
				g1.drawLine((int)(bestpath.get(i).x * scalefac),(int)(bestpath.get(i).y * scalefac),(int)(bestpath.get(i+1).x * scalefac),(int)(bestpath.get(i+1).y * scalefac));
			 }
			 log.addToLog("path drawn");
			 
			 
		 }
		
	}
	public void init()
	{
		Container contains = getContentPane();
		contains.setLayout(null);
		navigate.setSize(100, 50);
		navigate.setLocation(500, 550);
		contains.add(navigate);
		startx.setSize(50,50);
		startx.setLocation(210,550);
		starty.setSize(50,50);
		starty.setLocation(270,550);
		goalx.setSize(50, 50);
		goalx.setLocation(350, 550);
		goaly.setSize(50, 50);
		goaly.setLocation(410, 550);
		contains.add(startx);
		contains.add(goalx);
		contains.add(starty);
		contains.add(goaly);
		zoomup.setSize(100,50);
		zoomdown.setSize(100,50);
		zoomup.setLocation(650,550);
		zoomdown.setLocation(800,550);
		contains.add(zoomup);
		contains.add(zoomdown);
		zoomup.addActionListener(this);
		zoomdown.addActionListener(this);
		startx.addActionListener(this);
		goalx.addActionListener(this);
		starty.addActionListener(this);
		goaly.addActionListener(this);
		navigate.addActionListener(this);
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stud
		Logger log = new Logger();
		   // Map firstfloor = new Map();
		   // start = new Point(10,190);
		   // goal = new Point(10,300);
		   // firstfloor.setStartandEnd(start, goal);	
		  //  firstfloor.navigate();
		    log.addToLog("navigation called");
		   
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		float sx = Float.parseFloat(startx.getText());
		float sy = Float.parseFloat(starty.getText());
		float gx = Float.parseFloat(goalx.getText());
		float gy = Float.parseFloat(goaly.getText());
		
		if(e.getSource() == navigate)
		{
			log.addToLog("Button pressed");
			navigate(sx,sy,gx,gy);
			flag = true;
			log.addToLog("Flag is now true");
			repaint();
		}
		if(e.getSource() == zoomup)
		{
			if(scalefac < 1.0)
			{
				scalefac += 0.2;
				repaint();
			}
		}
		if(e.getSource() == zoomdown)
		{
			if(scalefac > 0.6)
			{
				scalefac -= 0.2;
				repaint();
			}
			
		}	
	}
	public void setpreviousnull() {
		// TODO Auto-generated method stub
		for(Iterator<Entry<Room,Set<Room>>> i = rooms.getEntry().iterator(); i.hasNext(); )
		{
			Entry<Room,Set<Room>> m = i.next();
			m.getKey().doors.setPrevious(null);	
		}
	}
}
