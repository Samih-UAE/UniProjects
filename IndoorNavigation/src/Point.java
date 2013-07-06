
public class Point  {
        /* Nodes that this is connected to */
        private double distanceFromStart;
        private double heuristicDistanceFromGoal;
        Point previous;
        double x;
        double y;
        public double PathCost;
        
        Point(double x2, double y2) {
                this.x =  x2;
                this.y =  y2;
        }
        public void setPathCost()// total cost of G + H
        {
        	PathCost =  this.heuristicDistanceFromGoal + this.distanceFromStart;
        }
        public double getPathCost() // returns path cost(G+H)
        {
        	return PathCost;
        }
        public double getDistanceFromStart() {
                return distanceFromStart;
        }

        public void setDistanceFromStart(double f) {
                this.distanceFromStart = f;
        }

        public Point getPrevious() {
                return previous;
        }
        public void setPrevious(Point previous) {
                this.previous = previous;
        }
        
        public double getHeuristicDistanceFromGoal() {
                return heuristicDistanceFromGoal;
        }
        public void setHeuristicDistanceFromGoal(double heuristicDistanceFromGoal) {
                this.heuristicDistanceFromGoal = heuristicDistanceFromGoal;
        }

        public double getX() {
                return x;
        }
        public double getY() {
                return y;
        }
}
