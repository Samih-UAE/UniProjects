import java.util.*;

public class Path {
        // pathpoints contains the list of the coordinates that make up the path.
        private Vector<Point> pathpoints = new Vector<Point>();
        
        public Path() {
        }
        
        public int getLength() {
                return pathpoints.size();
        }
        public Vector<Point> getpoints()
        {
        	return pathpoints;
        }

        public Point getpathpoints(int index) {
                return pathpoints.get(index);
        }


        /**
         * Get the x-coordinate for the pathpoint at the given index.
         */
        public double getX(int index) {
                return getpathpoints(index).getX();
        }


        /**
         * Get the y-coordinate for the waypoint at the given index.
         */
        public double getY(int index) {
                return getpathpoints(index).getY();
        }


        /**
         * Append/add the pathpoint to the path.  
         */
        public void appendpathpoints(Point n) {
                pathpoints.add(n);
        }


        /**
         * Add a waypoint to the beginning of the path.  
         */
        public void prependWayPoint(Point n) {
                pathpoints.add(0, n);
        }


        /**
         * Check if this path contains the pathpoints
         checks and returns true or false if it contains the pathpoints or not...
         */
        public boolean contains(int x, int y) {
                for(Point point : pathpoints) {
                        if (point.getX() == x && point.getY() == y)
                                return true;
                }
                return false;
        }


}