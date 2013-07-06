 
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Map;

	public class Graph<T> implements Iterable<T>
	{
	    public Map<T,Set<T>> nodeset; //Each element inside the nodeset is a node containing its edges.
	    public Graph()
	    {
	    	nodeset = new HashMap<T,Set<T>>();
	    }
	    public boolean addNode(T node) //adds a new node with 0 edges
	    {
	    	if (nodeset.containsKey(node))
	    		return false;
	    	nodeset.put(node, new HashSet<T>());
	    	return true;
	    }
	    public void addEdge(T start, T dest) throws Exception { //adds the a new edge to the node specified
	   	 if (!nodeset.containsKey(start) || !nodeset.containsKey(dest))
	   		 throw new Exception("No such nodes in the graph.");
	   	 
	   	 nodeset.get(start).add(dest);
	   	 nodeset.get(dest).add(start);
	   	}
	    public void removeEdge(T start, T dest) throws Exception //removes edges to the node specified
	    {
	   	 	if (!nodeset.containsKey(start) || !nodeset.containsKey(dest))
	   	 		throw new Exception("No such nodes in the graph.");
	   	 	
	   	 	nodeset.get(start).remove(dest);
	   	 	nodeset.get(dest).remove(start);
	   	}
	    public Set<T> getNeighbors(T node) throws Exception //returns a collection of edges for the node specified
	    {
	    	Set<T> neighbors = nodeset.get(node);
	   	 	if (neighbors == null)
	   	 		throw new Exception("No such node in the graph.");

	   	 	return neighbors;
	   	}      
		@Override
		public Iterator<T> iterator() {
			// TODO Auto-generated method stub
			return null;
		}
		public Set<Entry<T,Set<T>>> getEntry()
		{
			return nodeset.entrySet();
		}
		public int size() {
			// TODO Auto-generated method stub
			return nodeset.size();
		}
}
