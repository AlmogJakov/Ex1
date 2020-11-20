package ex1.src;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class WGraph_Algo implements weighted_graph_algorithms {
	public weighted_graph G;

	/* Constructor */
	public WGraph_Algo() {
		this.G = new WGraph_DS();
	}

	/**
	 * About init(weighted_graph g) method: this method init the graph on which this
	 * set of algorithms operates on. if the given graph=null the method init an
	 * empty graph.
	 * @param g
	 */
	@Override
	public void init(weighted_graph g) {
		if (g == null)
			this.G = new WGraph_DS();
		else
			this.G = g;
	}

	/**
	 * About getGraph() method: this method return the underlying graph of which
	 * this class works.
	 * @return
	 */
	@Override
	public weighted_graph getGraph() {
		return this.G;
	}

	/**
	 * About copy() method: this method compute a deep copy of this weighted graph.
	 * the method uses WGraph_DS copy constructor to implement deep copy.
	 * @return
	 */
	@Override
	public weighted_graph copy() {
		return new WGraph_DS(this.G);
	}

	/**
	 * About isConnected() method:
	 * this method returns true if and only if (iff) there is a valid path from EVREY node to each.
	 * The method is based on the BFS algorithm.
	 * In this algorithm we use a data structure of a queue to store the vertices we "visit".
	 * In addition, we use a data structure of a HashMap to store the Nodes we "visited" before.
	 * so that we don't loop this Nodes again.
	 * Initially we use an iterator on a random Node in the graph and adding the Node to the queue.
	 * using the BFS algorithm we progress through the neighbor list of the first Node in the queue:
	 * 	mark the first Node on the queue as current.
	 * 	delete the current Node from the queue. 
	 * 	mark the current Node as visited (by adding the current Node to the HashMap).
	 * 	We loop on the neighbors of the current Node as long as the queue isn't empty:
	 * 		if we found a node that marked as visited => do nothing.
	 * 		else => we add the Node to the queue and mark the Node as visited.
	 * The method will stop when the loop "visits" all the Nodes that
	 * were connected to the first Node (where we started).
	 * If we visited the same number of Nodes as the total number of
	 * Nodes in the graph then indeed the graph is completely linked.
	 * @return
	 */
	@Override
	public boolean isConnected() {
		HashMap<node_info, Boolean> vis = new HashMap<>();
		Queue<node_info> q = new LinkedList<node_info>();
		if (!this.G.getV().iterator().hasNext())
			return vis.size() == this.G.getV().size();
		node_info current = this.G.getV().iterator().next();
		q.add(current);
		vis.put(current, true);
		while (!q.isEmpty()) {
			current = (node_info) q.remove();
			for (node_info node : this.G.getV(current.getKey())) {
				if (!vis.containsKey(node)) {
					q.add(node);
					vis.put(node, true);
				}
			}
		}
		return vis.size() == this.G.getV().size();
	}

	/**
	 * About dijkstra(int src, int dest) method:
	 * This is an auxiliary method method for two methods: (We implemented both of the methods in the same way)
	 * 	  an method to returns the length of the shortest path between src to dest.
	 * 	  an method to returns a List of the shortest path between src to dest.
	 * The method is based on dijkstra's algorithm.
	 * In this algorithm we use a couple of data structures:
	 * 	  an HashMap to store the vertices we "visit".
	 * 	  an PriorityQueue to store the Nodes sorted by their weight value to source Node.
	 * 	  an PriorityQueue to store the the neighbors of the Node on which iterations are performed (sorted).
	 * 	  an HashMap to store the Nodes for building the output path - each node with his parent.
	 * 	  an List for returning the output in the same data structure as requested.
	 * Initially we add all the Nodes to the PriorityQueue with weight = Infinity, and src weight = 0.
	 * using the dijkstra's algorithm we progress through the graph:
	 * while the PriorityQueue isn't empty:
	 * 	 mark the first Node on the queue (lowest weight value to source Node) as current.
	 * 	 delete the current Node from the PriorityQueue.
	 * 	 mark the current Node as visited (by adding the current Node to the HashMap).
	 * 		We loop on the neighbors of the current Node (by their weight) as long as the queue isn't empty:
	 * 		if a node that marked as visited founded  => remove from the neighbors PriorityQueue.
	 * 		else => 
	 * 			remove from the neighbors PriorityQueue.
	 * 			Setting 't' variable = the weight from the current Node to the source Node.
	 * 			update the weight of the current Node in case 't' is lower.
	 * 			add the current Node to the HashMap that stores the Nodes for the output path.
	 * 			// we store the Node as a key while his parent is the value. //
	 * 			// that's means that through a Node we can return to his parent (progress one level) //
	 * 			// (we need to store the shortest path = shortest levels to destination) //
	 * 			mark the Node as visited.
	 * The method will stop when the PriorityQueue is empty.
	 * At the end of the loop:
	 * 	 if the tag value of src Node = Infinity => return null (-1 for length); // can't reach destination.
	 * 	 else => do nothing. // we reach'd destination Node.
	 * Finally we build the path using the HashMap that stores the Nodes for the output path
	 * // each Node is a key while his parent is the value. (so we can build levels from dest to src). //
	 * 	start with destination Node:
	 * 		while node!=null:
	 * 			add the node to the output path List and progress to his parent through the HashMap.
	 * - for returning a List of the shortest path between src to dest:
	 * 	 we reverse the List to get src->dest path instead of dest->src path.
	 * 	 then we return the List as requested.
	 * - for returning the length of the shortest path:
	 * 	 we remove one element of the List for getting the number of edged between src to dest (length).
	 * 	 then we return the size of the List.
	 * @param src
	 * @param dest
	 * @return List<node_info>
	 */
	public List<node_info> dijkstra(int src, int dest) {
		if (this.G.getNode(src) == null || this.G.getNode(dest) == null)
			return null;
		HashMap<node_info, Boolean> vis = new HashMap<>();
		PriorityQueue<node_info> pq = new PriorityQueue<node_info>(new NodesComparator());
		PriorityQueue<node_info> pqNi = new PriorityQueue<node_info>(new NodesComparator());
		HashMap<node_info, node_info> prev = new HashMap<>();
		G.getV().forEach(node -> {
			node.setTag(Integer.MAX_VALUE); // Double.POSITIVE_INFINITY Integer.MAX_VALUE
			pq.add(node);
		});
		pq.add(this.G.getNode(src));
		this.G.getNode(src).setTag(0);
		double t;
		node_info n;
		node_info current = pq.peek();
		vis.put(current, true);
		while (!pq.isEmpty()) {
			current = (node_info) pq.poll();
			// if (current.getKey()==dest) break;
			G.getV(current.getKey()).forEach(node -> {
				pqNi.add(node);
			});
			while (!pqNi.isEmpty()) {
				n = pqNi.poll();
				if (!vis.containsKey(n)) {
					t = current.getTag() + this.G.getEdge(current.getKey(), n.getKey());
					if (t < n.getTag()) {
						pq.remove(n);
						n.setTag(t);
						pq.add(n);
						prev.put(n, current);
					}
				}
			}
			vis.put(current, true);
		}
		if (this.G.getNode(dest).getTag() == Integer.MAX_VALUE)
			return null;
		List<node_info> directions = new LinkedList<node_info>();
		for (node_info node = this.G.getNode(dest); node != null; node = prev.get(node))
			directions.add(node);
		Collections.reverse(directions);
		return directions;
	}

	/**
	 * About shortestPathDist(int src, int dest) method: this method returns the
	 * length of the shortest path between src to dest. if no such path --> returns
	 * -1. This method explained with the dijkstra method above.
	 * @param src - start node
     * @param dest - end (target) node
     * @return
	 */
	@Override
	public double shortestPathDist(int src, int dest) {
		List<node_info> path = dijkstra(src, dest);
		if (path == null || path.size() == 0)
			return -1;
		else
			return path.get(path.size() - 1).getTag();
	}

	/**
	 * About shortestPath(int src, int dest) method: this method returns the the
	 * shortest path between src to dest - as an ordered List of nodes. if no such
	 * path --> returns null. This method explained with the dijkstra method above.
	 * @param src - start node
     * @param dest - end (target) node
     * @return
	 */
	@Override
	public List<node_info> shortestPath(int src, int dest) {
		return dijkstra(src, dest);
	}

	/**
	 * About save(String file) method: this method saves this weighted (undirected)
	 * graph to the given file name. this method returns true - iff the file was
	 * successfully saved.
	 * @param file - the file name (may include a relative path).
     * @return true - iff the file was successfully saved
	 */
	@Override
	public boolean save(String fileOutput) {
		if (fileOutput == null)
			return false;
		try {
			FileOutputStream file = new FileOutputStream(fileOutput);
			ObjectOutputStream oos = new ObjectOutputStream(file);
			oos.writeObject(G);
			oos.close();
			file.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * About load(String file) method: this method load a graph to this graph
	 * algorithm. if the file was successfully loaded - the underlying graph of this
	 * class will be changed (to the loaded one), in case the graph was not loaded
	 * the original graph remain "as is". this method returns true - iff the graph
	 * was successfully loaded.
	 * @param file - file name
     * @return true - iff the graph was successfully loaded.
	 */
	@Override
	public boolean load(String fileInput) {
		if (fileInput == null)
			return false;
		try {
			FileInputStream file = new FileInputStream(fileInput);
			ObjectInputStream ois = new ObjectInputStream(file);
			weighted_graph temp = (weighted_graph) ois.readObject();
			ois.close();
			file.close();
			/*if (temp instanceof weighted_graph) {
				this.G = temp;
				return true;
			}*/
			this.G = temp;
			return true;
		} catch (Exception error) {
			error.printStackTrace();
			return false;
		}
	}

	/**
	 * About equals method: this is an auxiliary method mainly for testing
	 * copy()/save()/load() methods. the method uses 'equals' method in
	 * weighted_graph class to implement comparison. the method returns false if the
	 * input object isn't instanceof weighted_graph_algorithms. otherwise calls
	 * equals(weighted_graph_algorithms g) method [below].
	 * @param g
	 * @return boolean
	 */
	@Override
	public boolean equals(Object g) {
		if (g instanceof weighted_graph_algorithms) {
			return this.equals((weighted_graph_algorithms) g);
		}
		return false;
	}

	/* Explained with the method above */
	public boolean equals(weighted_graph_algorithms g) {
		return this.G.equals(g.getGraph());
	}

	/**
	 * About NodesComparator inner class: this is an auxiliary class for implement
	 * comparison by the tag of each node. this method used by the PriorityQueue
	 * data structures in dijkstra method.
	 */
	class NodesComparator implements Comparator<node_info> {
		@Override
		public int compare(node_info o1, node_info o2) {
			return Double.compare(o1.getTag(), o2.getTag());
		}
	}
}