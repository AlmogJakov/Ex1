package ex1.src;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;

public class WGraph_DS implements weighted_graph, Serializable {
	private static final long serialVersionUID = 1L;
	private HashMap<Integer, node_info> nodes;
	private HashMap<node_info, HashMap<node_info, Double>> edges; // HashMap<Integer, node_data>
	private int MC;
	private int degree;

	/* Constructor */
	public WGraph_DS() {
		this.nodes = new HashMap<>();
		this.edges = new HashMap<>();
		this.MC = 0;
		this.degree = 0;
	}

	/* 
	 * Copy Constructor (Hard Copy) */
	/** This constructor is mainly used by copy() method in WGraph_Algo class
	 * The implement this method is by Hashtables.
	 * Since we want to copy (deep copy) the graph we will need a new Hashtable representing
	 * the old 'nodes' Hashtable and another Hashtable representing the old 'edges' Hashtable.
	 * In addition, we use an Hashtable to synchronize between an old vertex with the new vertex
	 * that represents it (so that we can isomorphically copy neighbor lists from the old graph
	 * to the new graph).
	 * In the first loop initialize the Hashtables:
	 * 		first add each old vertex with the new vertex represents it to the Hashtable 
	 * 		(so we can access from an old vertex to a new vertex representing it).
	 * 		then add the new vertices to the new Hashtable represents 'nodes', 'edges'.
	 *  then perform iterations on the neighbors of the vertices of the old graph
	 *  and isomorphically add to the new graph the same list of neighbors with the new vertices
	 *  representing them.
	 *  Finally copy the 'MC', 'degree' variables. 
	 *  (MC = The number of actions required to create the graph [G.nodeSize() + G.edgeSize()]).
	 */
	public WGraph_DS(weighted_graph G) {
		this();
		if (G != null) {
			HashMap<node_info, node_info> oldToNew = new HashMap<>();
			G.getV().forEach(node -> {
				oldToNew.put(node, new NodeInfo((NodeInfo) node));
				nodes.put(oldToNew.get(node).getKey(), oldToNew.get(node));
				edges.put(oldToNew.get(node), new HashMap<>());
			});
			G.getV().forEach(node -> {
				nodes.put(node.getKey(), oldToNew.get(node));
				G.getV(node.getKey()).forEach(n -> {
					edges.get(oldToNew.get(node)).put(oldToNew.get(n), G.getEdge(node.getKey(), n.getKey()));
				});
				edges.put(node, new HashMap<>());
			});
			this.MC = G.nodeSize() + G.edgeSize();
			this.degree = G.edgeSize() * 2;
		}
	}

	/**
	 * About getNode(int key) method: this method return the node_data by the
	 * node_id. the method return null if none as requested.
	 * @param key - the node_id
     * @return the node_data by the node_id, null if none.
	 */
	@Override
	public node_info getNode(int key) {
		if (nodes.containsKey(key))
			return nodes.get(key);
		else
			return null;
	}

	/**
	 * About hasEdge(int node1, int node2) method: This method return true if (if
	 * and only if) there is an edge between node1 and node2. the method throw
	 * RuntimeException if node1 or node2 aren't in the graph. the method return
	 * false if node1==node2 (There is no edge from a Node to itself). the
	 * implementation of the method is simply by checking if node1 and node2
	 * neighbors using 'containsKey' method in 'edges' Hashmap [O(1)].
	 * @param node1
     * @param node2
     * @return
	 */
	@Override
	public boolean hasEdge(int node1, int node2) {
		if (!nodes.containsKey(node1) || !nodes.containsKey(node2))
			return false;
		if (node1 == node2)
			return false;
		node_info one = nodes.get(node1);
		node_info two = nodes.get(node2);
		return edges.get(one).containsKey(two);
	}

	/**
	 * About getEdge(int node1, int node2) method: this method return the weight if
	 * the edge (node1, node1). the method return -1 if node1 or node2 aren't in the
	 * graph. the method return -1 if node1==node2 (There is no edge from a Node to
	 * itself). the method return -1 in case there is no such edge. the
	 * implementation of the method is simply by 'get' method in 'edges' Hashmap
	 * [O(1)].
	 * @param node1
     * @param node2
     * @return
	 */
	@Override
	public double getEdge(int node1, int node2) {
		if (!nodes.containsKey(node1) || !nodes.containsKey(node2) || node1 == node2 || !this.hasEdge(node1, node2))
			return -1;
		node_info one = nodes.get(node1);
		node_info two = nodes.get(node2);
		return edges.get(one).get(two);
	}

	/**
	 * About addNode(int key) method:
	 * this method add a new node to the graph with the given key.
	 * if there is already a node with such a key no action performed.
	 * the method is implemented by adding the node to 'nodes' 'edges' Hashmaps [O(1)].
	 * if the method is implemented we update the MC (Mode Count).
	 * Note!:
	 * 		There is no use in initializing a Node with internal variables before
	 *  	adding it to a graph, because when it is added to a graph the variables
	 *  	are reset to the default.
	 * @param key
	 */
	@Override
	public void addNode(int key) {
		if (nodes.containsKey(key))
			return;
		node_info temp = new NodeInfo(key);
		nodes.put(key, temp);
		edges.put(temp, new HashMap<node_info, Double>());
		MC++;
	}

	/**
	 * About connect(int node1, int node2, double w) method: this method connect an
	 * edge between node1 and node2, with an edge with weight>=0. the method
	 * implemented only if node1 or node2 in the graph, node1!=node2 and w>=0. 
	 * if the edge node1-node2 already exists the method updates the weight of the
	 * edge. (in case the existing edge weight == w no action performed). if there
	 * is no such edge the method adds each Node to the list of neighbors [Hashmap]
	 * of the other Node [O(1)] and updates 'degree' variable. if any action
	 * performed the method updates 'MC' variable.
	 */
	public void connect(int node1, int node2, double w) {
		if (!nodes.containsKey(node1) || !nodes.containsKey(node2) || node1 == node2 || w < 0)
			return;
		node_info one = nodes.get(node1);
		node_info two = nodes.get(node2);
		if (edges.get(one).get(two) != null) {
			if (edges.get(one).get(two) == w)
				return;
		} else
			this.degree += 2;
		edges.get(one).put(two, w);
		edges.get(two).put(one, w);
		MC++; // added an edge OR edge exists but weight of the edge updates
	}

	/**
	 * About getV() method: This method return a pointer (shallow copy) for the
	 * collection representing all the nodes in the graph. the method is implemented
	 * by returning 'nodes' HashMap values.
	 * @return Collection<node_data>
	 */
	@Override
	public Collection<node_info> getV() {
		return nodes.values();
	}

	/**
	 * About getV(int node_id) method: This method returns a collection containing
	 * all the nodes connected to node_id. the method throw RuntimeException if
	 * node_id isn't in the graph. The implementation of the method is simply by
	 * returning KeySet of node_id neighbors.
	 * @return Collection<node_data>
	 */
	@Override
	public Collection<node_info> getV(int node_id) {
		if (!nodes.containsKey(node_id))
			throw new RuntimeException("Seems like this node isn't in this graph!");
		node_info node = nodes.get(node_id);
		return edges.get(node).keySet();
	}

	/**
	 * About removeNode(int key) method: this method delete the node (with the given
	 * ID) from the graph - and removes all edges which starts or ends at this node.
	 * the method return null if the Node isn't in the graph as requested. otherwise
	 * the method initializes a pointer to the Node and performs an iteration on all
	 * the neighbors of the Node [O(n)] and removes his connected edges one by one
	 * (using removeEdge(int node1, int node2) method while each step updates the MC
	 * (Mode Count)). afterwards the method removes the Node itself [O(1)] and
	 * update the MC (Mode Count) once again. finally the method returns the pointer
	 * (data).
	 * @return the data of the removed node (null if none).
     * @param key
	 */
	@Override
	public node_info removeNode(int key) {
		if (!nodes.containsKey(key))
			return null;
		node_info pointer = nodes.get(key);
		this.getV(key).forEach(e -> {
			edges.get(e).remove(pointer);
			this.degree -= 2;
			MC++;
		});
		this.nodes.remove(key);
		this.edges.remove(pointer);
		MC++;
		return pointer;
	}

	/**
	 * About removeEdge(int node1, int node2) method: This method delete the edge
	 * from the graph. the method throw RuntimeException if node1 or node2 aren't in
	 * the graph. if node1 and node2 are neighbors the method removes the edge
	 * between them by removing each Node from the list of neighbors [Hashmap] of
	 * the other one [O(1)]. afterwards the method update Degree variable used for
	 * counting the edges and also update the MC (Mode Count).
	 * @param node1
     * @param node2
	 */
	@Override
	public void removeEdge(int node1, int node2) {
		if (!nodes.containsKey(node1) || !nodes.containsKey(node2))
			return;
		node_info one = nodes.get(node1);
		node_info two = nodes.get(node2);
		if (this.hasEdge(node1, node2)) {
			edges.get(one).remove(two);
			edges.get(two).remove(one);
			this.degree -= 2;
			MC++;
		}
	}

	/**
	 * About nodeSize() method: this method return the number of vertices (nodes) in
	 * the graph. The implementation of the method is simply by returning 'nodes'
	 * size [O(1)].
	 * @return
	 */
	@Override
	public int nodeSize() {
		return nodes.size();
	}

	/**
	 * About edgeSize() method: this method return the number of edges
	 * (undirectional graph). The implementation of the method is simply by
	 * returning node neighbors size [Hashmap > O(1)].
	 * @return
	 */
	@Override
	public int edgeSize() {
		return this.degree / 2;
	}

	/**
	 * About getMC() method: this method return the Mode Count - for testing changes
	 * in the graph. The implementation of the method is simply by returning 'MC'
	 * variable.
	 * @return
	 */
	@Override
	public int getMC() {
		return this.MC;
	}

	/**
	 * About equals(Object g) method: this is an auxiliary method mainly for testing
	 * copy()/save()/load() methods. the method returns false if the input object
	 * isn't instanceof weighted_graph. otherwise calls equals(weighted_graph g)
	 * method [below].
	 * @param g
	 * @return boolean
	 */
	@Override
	public boolean equals(Object g) {
		if (g instanceof weighted_graph) {
			return this.equals((weighted_graph) g);
		}
		return false;
	}

	/**
	 * About equals(weighted_graph g) method:
	 * this method compares this graph with the input graph.
	 * if this nodes size != g nodes size or this edges size != g edges size - return false
	 * (the graphs obviously not equals).
	 * otherwise the method performs iteration on all g vertices:
	 * 		for each vertex - the method check if this graph contains vertex with same key
	 * 		and checks equality using nodesEquals(node_info a, node_info b) method.
	 * 		if g vertex neighbors size != this vertex neighbors size - return false
	 * 		(the graphs obviously not equals).
	 * 		otherwise, the method perform iteration on the vertex neighbors and checks existence
	 * 		and equality of each neighbor using nodesEquals(node_info a, node_info b) method.
	 * 		in addition, the method checks equality of edges weight.
	 * 		# There is no need to check equation between MC variables because the purpose of the test
	 * 		# is equality between the graphs and not for the number of operations performed on them.
	 * @param g
	 * @return boolean
	 */
	public boolean equals(weighted_graph g) {
		if (this.nodes.size() != g.getV().size() || this.edgeSize() != g.edgeSize())
			return false;
		node_info tempOne, tempTwo;
		for (node_info node : g.getV()) {
			tempOne = nodes.get(node.getKey());
			if (tempOne == null || this.nodesEquals(tempOne, node))
				return false;
			if (g.getV(node.getKey()).size() != edges.get((tempOne)).size())
				return false;
			for (node_info n : g.getV(node.getKey())) {
				tempTwo = nodes.get(n.getKey());
				if (tempTwo == null || this.nodesEquals(tempTwo, n))
					return false;
				if (g.getEdge(node.getKey(), n.getKey()) != this.getEdge(tempOne.getKey(), tempTwo.getKey()))
					return false;
			}
		}
		return true;
	}

	/**
	 * About nodesEquals(node_info a, node_info b) method: this method compares
	 * node_info a with node_info b. The implementation of the method is simply by
	 * checking equality of each node variables.
	 * @param a
	 * @param b
	 * @return boolean
	 */
	public boolean nodesEquals(node_info a, node_info b) {
		return a.getKey() != b.getKey() || a.getTag() != b.getTag() || !a.getInfo().equals(b.getInfo());
	}

	/* toString() method */
	/* The implementation of this method is mainly for testing purposes */
	/* The method is overridden so that the interface does not need to be changed */
	/*@Override
	public String toString() { // Print by keys printGraph
		System.out.println(" ______________________________________________");
		// System.out.println("| #############################################");
		System.out.println("| ############### Print By Keys ###############");
		nodes.forEach((k, v) -> {
			System.out.print("| NodeKey = " + v.getKey() + ". NeighborsKeys = ");
			this.getV(k).forEach(node -> {
				System.out.print(node.getKey());
				System.out.print(" (weight:" + edges.get(v).get(node) + "), ");
			});
			System.out.println();
		});
		System.out.println("| ############### Print By Node ###############");
		nodes.forEach((k, v) -> {
			System.out.print("| NodeKey = " + v + ". NeighborsKeys = ");
			this.getV(k).forEach(node -> {
				System.out.print(node);
				System.out.print(" (weight:" + edges.get(v).get(node) + "), ");
			});
			System.out.println();
		});
		// System.out.println("| #############################################");
		System.out.println("|______________________________________________");
		return null;
	}*/

	private class NodeInfo implements node_info, Serializable {
		private static final long serialVersionUID = 1L;
		private int key;
		private double tag;
		private String Info;

		/* Copy Constructor */
		public NodeInfo(int key) {
			this.tag = -1;
			this.Info = "";
			this.key = key;
		}

		/* Copy Constructor */
		/* This constructor is mainly used by deep copy methods */
		/* (copying is implemented also on the key) */
		public NodeInfo(NodeInfo n) {
			this.key = n.key;
			this.tag = n.tag;
			this.Info = n.Info;
		}

		/**
		 * About getKey() method: this method return the key (id) associated with this
		 * node.
		 * @return
		 */
		@Override
		public int getKey() {
			return this.key;
		}

		/**
		 * About setInfo(String s) method: this method allows changing the remark (meta
		 * data) associated with this node.
		 * @return
		 */
		@Override
		public String getInfo() {
			return this.Info;
		}

		/**
		 * About setInfo(String s) method: this method allows changing the remark (meta
		 * data) associated with this node.
		 * @param s
		 */
		@Override
		public void setInfo(String s) {
			this.Info = s;
		}

		/**
		 * About getTag() method: this method returns the temporal data (aka distance,
		 * color, or state) which can be used be algorithms
		 * @return
		 */
		@Override
		public double getTag() {
			return this.tag;
		}

		/**
		 * About setTag(double t) method: this method allow setting the "tag" value for
		 * temporal marking an node (common practice for marking by algorithms).
		 * @param t - the new value of the tag
		 */
		@Override
		public void setTag(double t) {
			this.tag = t;
		}
	}
}