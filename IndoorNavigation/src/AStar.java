
import java.util.*;
import java.awt.geom.*;



public class AStar {
	public Map map;
	public Point start;
	public Point goal;
	int index_min;
	Logger log = new Logger();
	
	//heuristic estimate
	//public float H; // heuristic value H cost taken to reach the goal 
	//public float G = 0; // exact value to reach the destination...
	//public float cost;
	private Path shortestPath;
	public Room startroom;
	Set<Room> adj;
	public Room goalroom;
	Room currentRoom;
	double scalefactor = 0.8;
	public Vector<Room> openst;
	public Vector<Room> closedst;
	
	public AStar(Map amap, float sx, float sy, float gx, float gy)
	{
		log.addToLog("Astar has been called");
		this.map = amap;
			
		map.setpreviousnull();
		start = new Point(sx,sy);
		startroom = map.searchroom(start);
		startroom.doors.setDistanceFromStart(0);
		
		goal = new Point(gx,gy);
		goalroom = map.searchroom(goal);
		
		currentRoom = startroom;
		openst = new Vector<Room>();
		closedst = new Vector<Room>();
		
		openst.add(currentRoom);
		log.addToLog("added to openset");
	}
	public Vector<Point> calcpath()
	{
		while(!(openst.isEmpty()))
		{
			double min = openst.firstElement().doors.getPathCost();
			int length = openst.size();
			for(int i = 0; i < length ; i++)
			{
				if(openst.get(i).doors.getPathCost() < min)
				{
					min = openst.get(i).doors.getPathCost();
					index_min = i;
				}
			}
			currentRoom = openst.elementAt(index_min);
			
			if(currentRoom == goalroom) //checks if we reached the goal
			{
				log.addToLog("Reached the goal");
				return getPath(currentRoom.doors);
			}
			openst.remove(currentRoom);
			closedst.add(currentRoom);
			try {
				adj = map.rooms.getNeighbors(currentRoom);
				for(Room neighbor : adj)
				{
					boolean neighborIsBetter;
					//if we have already searched this Node, don't bother and continue to the next one 
					// calculate how long the path is if we choose this neighbor as the next step in the path 
					double neighborDistanceFromStart = (currentRoom.doors.getDistanceFromStart() + map.getDistanceBetween(currentRoom.doors, neighbor.doors));
			         
					if(closedst.contains(neighbor))
					{
						continue;
					}
					//add neighbor to the open set if it is not there
					if(!openst.contains(neighbor)) 
					{
						openst.add(neighbor);
						neighborIsBetter = true;
						//if neighbor is closer to start it could also be better
					} else if(neighborDistanceFromStart < currentRoom.doors.getDistanceFromStart()) 
					{
						neighborIsBetter = true;
					} else 
					{
						neighborIsBetter = false;
					}		
					// set neighbors parameters if it is better
					if (neighborIsBetter) 
					{
						//log.addToLog("neighbor x" + neighbor.doors.x + " ,y " + neighbor.doors.y + " sets room taken" + currentRoom.doors.x + " ,y " + currentRoom.doors.y);
						neighbor.doors.setPrevious(currentRoom.doors);
						neighbor.doors.setDistanceFromStart(neighborDistanceFromStart);
						neighbor.doors.setHeuristicDistanceFromGoal(getEstimatedDistanceToGoal(neighbor.doors.getX(), neighbor.doors.getY(), goalroom.doors.getX(), goalroom.doors.getY()));
						neighbor.doors.setPathCost();
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	private Vector<Point> getPath(Point n) {
        Path path = new Path();
        log.addToLog("Inside the getPath Function");
        while(!(n.getPrevious() == null)) {
                path.prependWayPoint(n);
                n = n.getPrevious();
        }
        path.prependWayPoint(n);
        this.shortestPath = path;
        return shortestPath.getpoints();
}
	public double getEstimatedDistanceToGoal(double startx, double starty,double goalx,double goaly) {         //mahattan distance
        double dx = goalx - startx;
        double dy = goaly - starty;
        
        double result = (float) (Math.sqrt((dx*dx)+(dy*dy)));
        
        
        //float result = (float) (dx*dx)+(dy*dy);
      
		return result;
	}
}
	/*public float pathCost(Point start, Point goal) // calculates the cost of path heuristically and the actual path
	{
		H = (Math.abs(start.x-goal.x) + Math.abs(start.y-goal.y));
		//Point current;
		//if()
		cost = H + G; //IMPLEMENT G
		return cost ;
		}
}
/*	
	public double Fudge(Point a, Point b, double low) // diagonal astar path that extends huristic astar
	{
	
		Point start = openset.firstElement();
		double DX1 = a.x -b.x;
		double DY1 = a.y - b.y;
        double dx2 = start.x - b.x;
        double dy2 = start.y - b.y;
        double cross = DX1*dy2-dx2*DY1;
        if (cross < 0) cross = -cross;
        	return  low *(Math.abs(DX1)+Math.abs(DY1) + cross*0.0002);
	}

	*/