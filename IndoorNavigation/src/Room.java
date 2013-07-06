import java.awt.Rectangle;


public class Room 
{
	public Point doors;
	public Rectangle rooms;
	
	public Room(float left,float right,float top,float bottom,float doorx, float doory)
	{
		rooms = new Rectangle((int)left,(int)right,(int)top,(int)bottom);
		doors = new Point(doorx,doory);
	}
}