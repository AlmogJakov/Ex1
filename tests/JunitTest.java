package ex1.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;

import ex1.src.WGraph_Algo;
import ex1.src.WGraph_DS;
import ex1.src.node_info;
import ex1.src.weighted_graph;
import ex1.src.weighted_graph_algorithms;

class JunitTest {
	/* Most tests are performed by the following graph */
	public weighted_graph graph() {
		weighted_graph g = new WGraph_DS();
		g.addNode(0);
		g.addNode(1);
		g.addNode(2);
		g.addNode(3);
		g.addNode(4);
		g.addNode(5);
		g.addNode(6);
		g.addNode(7);
		g.addNode(8);
		g.addNode(9);
		g.addNode(10);
		g.addNode(11);
		g.addNode(12);
		g.addNode(13);
		g.addNode(14);
		g.addNode(15);
		g.connect(0, 1,8);
		g.connect(0, 2,7);
		g.connect(0, 3,2);
		g.connect(1, 2,3);
		g.connect(3, 4,1);
		g.connect(5, 4,3);
		g.connect(4, 2,0);
		g.connect(7, 2,5);
		g.connect(5, 6,6);
		g.connect(7, 6,1);
		g.connect(7, 1,2);
		g.connect(8, 9,4.5);
		g.connect(9, 10,1.3);
		g.connect(12, 13,1);
		g.connect(12, 14,0);
		g.connect(15, 14,0);
		g.connect(13, 15,0);
	/*	
	   Graph illustration:
	         w=2
		(3) ---- (0)
		 |        | \
		 |        |  \
	  w=1|     w=7|   \ w=8
		 |        |    \ 
		 |        |     \
		 |  w=0   |  w=3 \
		(4) ---- (2) ---- (1)
		 |          \      |
		 |           \     |
	  w=3|         w=5\    |w=2
		 |             \   |
		 |              \  |
		(5) ---- (6) ---- (7)
	  	     w=6      w=1
	  	
	  	(8)----(9)----(10)
	  	   w=4.5  w=1.3
	  	
	  	(11)
	  	
	  	      w=7.5
	  	(12)----------(13)
	  	 |             |
	  w=0|             |w=0
	  	 |             |
	  	(14)----------(15)
	  	       w=0
	  	*/
		
		return g;
    }
	weighted_graph Graph = graph();
	
	/* ########################################################################################## */
	/* ############################## weighted_graph class Tests ################################ */
	/* ########################################################################################## */
	
	@Test
	public void getNodeTest() {
		node_info a = Graph.getNode(20);
		assertNull(a);
	}
	
	@Test
	public void hasEdgeTest() {
		Graph.getNode(20);
		/*assertThrows(RuntimeException.class, // if returning exception for incorrect input.
	            ()->{Graph.hasEdge(20, 0);});*/
		assertTrue(Graph.hasEdge(2, 0));
	}
	
	@Test
	public void getEdgeTest() {
		assertEquals(Graph.getEdge(0, 0), -1 ,0);
		assertEquals(Graph.getEdge(11, 0), -1 ,0);
		assertEquals(Graph.getEdge(20, 30), -1 ,0);
		assertEquals(Graph.getEdge(0, 3), 2 ,0);
	}
	
	@Test
	public void addNodeTest() {
		int exceptedNodesSize = Graph.nodeSize();
		Graph.addNode(0);
		assertEquals(exceptedNodesSize, Graph.nodeSize());
		exceptedNodesSize++;
		Graph.addNode(30);
		Graph.connect(30, 11,1);
		assertEquals(exceptedNodesSize, Graph.nodeSize());
		Graph.removeNode(30);
	} //4
	
	@Test
	public void connectTest() {
		int exceptedEdgesSize = Graph.edgeSize();
		Graph.connect(10, 11,1);
		exceptedEdgesSize++;
		assertEquals(exceptedEdgesSize, Graph.edgeSize());
		Graph.connect(10, 11,1);
		assertEquals(exceptedEdgesSize, Graph.edgeSize());
		Graph.connect(10, 11,3);
		assertEquals(exceptedEdgesSize, Graph.edgeSize());
		Graph.removeEdge(10, 11);
	} //3
	
	@Test
	public void getVTest() {
		assertEquals(Graph.getV().size(),16);
		assertEquals(new WGraph_DS().getV().size(),0);
	}
	
	@Test
	public void getNodeVTest() {
		assertEquals(Graph.getV(2).size(),4);
		assertThrows(RuntimeException.class,
	            ()->{Graph.getV(40);});
		assertEquals(Graph.getV(11).size(),0);
		assertEquals(Graph.getV(10).size(),1);
	}
	
	@Test
	public void removeNodeTest() {
		node_info pointer = Graph.removeNode(11);
		assertEquals(pointer.getKey(),11);
		Graph.addNode(11);
		assertEquals(Graph.removeNode(40), null);
	} //2
	
	@Test
	public void removeEdgeTest() {
		int exceptedEdgesSize = Graph.edgeSize();
		/*assertThrows(RuntimeException.class,  // if returning exception for incorrect input.
	            ()->{Graph.removeEdge(40, 6);;});*/
		assertEquals(Graph.edgeSize(), exceptedEdgesSize);
		exceptedEdgesSize--;
		Graph.removeEdge(9, 10);
		assertEquals(Graph.edgeSize(), exceptedEdgesSize);
		Graph.connect(9, 10,1);
	} //2
	
	@Test
	public void nodeSizeTest() {
		assertEquals(Graph.nodeSize(), 16);
	}
	
	@Test
	public void edgeSizeTest() {
		assertEquals(Graph.edgeSize(), 17);
	}
	
	@Test
	public void getMCTest() {
		assertEquals(Graph.getMC(),33);
		Graph.removeNode(9);
		assertEquals(Graph.getMC(),36);
		Graph.addNode(9);
		Graph.connect(8, 9,4);
		Graph.connect(9, 10,1);
		assertEquals(Graph.getMC(),39);
	}
	
	/* ######################################################################################### */
	/* ####################### weighted_graph_algorithms class Tests ########################### */
	/* ######################################################################################### */
	@Test
	public void copyTest() {
		System.out.println();
		System.out.println("################ \"Copy\" function test #################");
		weighted_graph_algorithms gg = new WGraph_Algo();
		gg.init(Graph);
		weighted_graph temp = gg.copy();
		
		if (Graph.getNode(11)==temp.getNode(11))
			System.out.println("Wrong hard copy! Objects have the same reference!");
		assertNotSame(Graph.getNode(11),temp.getNode(11));
		
		if (!Graph.equals(temp)) {
			System.out.println("The graphs are equals! - Should be True! Wrong Answer!");
		} else System.out.print("The graphs are equals! Correct Answer!");
		System.out.println();
		assertEquals(Graph,temp);
		
		temp.removeEdge(2, 7);
		if (Graph.equals(temp)) {
			System.out.println("The graphs aren't equals! - Should be False! Wrong Answer!");
		} else System.out.print("The graphs aren't equals! Correct Answer!");
		System.out.println();
		assertNotEquals(Graph,temp);
		
		temp.connect(2, 7, 6);
		if (Graph.equals(temp)) {
			System.out.println("The graphs aren't equals! - Should be False! Wrong Answer!");
		} else System.out.print("The graphs aren't equals! Correct Answer!");
		System.out.println();
		assertNotEquals(Graph,temp);
		
		temp.connect(2, 7, 5);
		temp.removeNode(10);
		if (Graph.equals(temp)) {
			System.out.println("The graphs aren't equals! - Should be False! Wrong Answer!");
		} else System.out.print("The graphs aren't equals! Correct Answer!");
		System.out.println();
		assertNotEquals(Graph,temp);
	}
	
	@Test
	public void isConnectedTest() {
		weighted_graph_algorithms gb = new WGraph_Algo();
		gb.init(Graph);
		boolean answer;
		System.out.println();
		System.out.println("############## \"Connected\" function test ###############");
		System.out.print("isConnected Answer: ");
		System.out.print(gb.isConnected());
		answer = gb.isConnected();
		if (answer==false) System.out.print(" >> Correct Answer!");
		else System.out.print(" >> Wrong Answer! - Should be False!");
		System.out.println();
		assertEquals(answer,false);
		
		weighted_graph_algorithms gg = new WGraph_Algo();
		answer = gg.isConnected();
		System.out.print("isConnected Answer: ");
		System.out.print(answer);
		if (answer==true) System.out.print(" >> Correct Answer!");
		else System.out.print(" >> Wrong Answer! - Should be True!");
		System.out.println();
		assertEquals(answer,true);
		
		weighted_graph temp = new WGraph_DS();
		gg.init(temp);
		answer = gg.isConnected();
		System.out.print("isConnected Answer: ");
		System.out.print(answer);
		if (answer==true) System.out.print(" >> Correct Answer!");
		else System.out.print(" >> Wrong Answer! - Should be True!");
		System.out.println();
		assertEquals(answer,true);
		
		temp = gb.copy();
		temp.removeNode(8);
		temp.removeNode(9);
		temp.removeNode(10);
		temp.removeNode(11);
		temp.removeNode(12);
		temp.removeNode(13);
		temp.removeNode(14);
		temp.removeNode(15);
		gg.init(temp);
		answer = gg.isConnected();
		System.out.print("isConnected Answer: ");
		System.out.print(answer);
		if (answer==true) System.out.print(" >> Correct Answer!");
		else System.out.print(" >> Wrong Answer! - Should be True!");
		System.out.println();
		assertEquals(answer,true);
		
		gg = new WGraph_Algo();
		answer = gg.isConnected();
		if (answer==true) System.out.print(" >> Correct Answer!");
		else System.out.print(" >> Wrong Answer! - Should be True!");
		System.out.println();
		assertEquals(answer,true);
		
		weighted_graph zz = new WGraph_DS();
		zz.addNode(0);
		gg.init(zz);
		answer = gg.isConnected();
		if (answer==true) System.out.print(" >> Correct Answer!");
		else System.out.print(" >> Wrong Answer! - Should be True!");
		System.out.println();
		assertEquals(answer,true);
	}
	
	@Test
	public void shortestPathtestLengthTest() {
		weighted_graph_algorithms gb = new WGraph_Algo();
		gb.init(Graph);
		double answer = gb.shortestPathDist(0, 0);
		System.out.println();
		System.out.println("########## \"shortestPathLength\" function test ###########");
		System.out.print("Shortest path from 0 to 0: ");
		answer = gb.shortestPathDist(0, 0);
		System.out.print(answer);
		if (answer==0) System.out.print(" >> Correct Answer!");
		else System.out.print(" >> Wrong! - Should be 0!");
		System.out.println();
		assertEquals(answer, 0, 0);
		
		System.out.print("Shortest path from 20 to 6: ");
		answer = gb.shortestPathDist(20, 6);
		System.out.print(answer);
		if (answer==-1) System.out.print(" >> Correct Answer!");
		else System.out.print(" >> Wrong! - Should be -1!");
		System.out.println();
		assertEquals(answer, -1, 0);
		
		System.out.print("Shortest path from 6 to 8: ");
		answer = gb.shortestPathDist(6, 8);
		System.out.print(answer);
		if (answer==-1) System.out.print(" >> Correct Answer!");
		else System.out.print(" >> Wrong! - Should be -1!");
		System.out.println();
		assertEquals(answer, -1, 0);
		
		System.out.print("Shortest path from 0 to 11: ");
		answer = gb.shortestPathDist(0, 11);
		System.out.print(answer);
		if (answer==-1) System.out.print(" >> Correct Answer!");
		else System.out.print(" >> Wrong! - Should be -1!");
		System.out.println();
		assertEquals(answer, -1, 0);
		
		System.out.print("Shortest path from 2 to 4: ");
		answer = gb.shortestPathDist(4, 2);
		System.out.print(answer);
		if (answer==0) System.out.print(" >> Correct Answer!");
		else System.out.print(" >> Wrong! - Should be 0!");
		System.out.println();
		assertEquals(answer, 0, 0);
		
		System.out.print("Shortest path from 2 to 7: ");
		answer = gb.shortestPathDist(7, 2);
		System.out.print(answer);
		if (answer==5) System.out.print(" >> Correct Answer!");
		else System.out.print(" >> Wrong! - Should be 5!");
		System.out.println();
		assertEquals(answer, 5, 0);
		
		System.out.print("Shortest path from 8 to 10: ");
		answer = gb.shortestPathDist(8, 10);
		System.out.print(answer);
		if (answer==5.8) System.out.print(" >> Correct Answer!");
		else System.out.print(" >> Wrong! - Should be 5.8!");
		System.out.println();
		assertEquals(answer, 5.8, 0);
		
		System.out.print("Shortest path from 0 to 6: ");
		answer = gb.shortestPathDist(0, 6);
		System.out.print(answer);
		if (answer==9) System.out.print(" >> Correct Answer!");
		else System.out.print(" >> Wrong! - Should be 9!");
		System.out.println();
		assertEquals(answer, 9, 0);
		
		System.out.print("Shortest path from 0 to 6: ");
		answer = gb.shortestPathDist(13, 14);
		System.out.print(answer);
		if (answer==0) System.out.print(" >> Correct Answer!");
		else System.out.print(" >> Wrong! - Should be 9!");
		System.out.println();
		assertEquals(answer, 0, 0);
		
		System.out.print("Shortest path from 8 to 9: ");
		answer = gb.shortestPathDist(8, 9);
		System.out.print(answer);
		if (answer==4.5) System.out.print(" >> Correct Answer!");
		else System.out.print(" >> Wrong! - Should be 4.5!");
		System.out.println();
		assertEquals(answer, 4.5, 0);
	}
	
	@Test
	public void shortestPathtestTest() {
		weighted_graph_algorithms gb = new WGraph_Algo();
		gb.init(Graph);
		System.out.println();
		System.out.println("############# \"shortestPath\" function test ##############");
		List<node_info> directions = gb.shortestPath(0, 0);
		System.out.print("The Path from 0 to 0: ");
		String answer = "";
		System.out.print("Answer:");
		for (Iterator<node_info> iterator = directions.iterator(); iterator.hasNext();)
			answer = answer + " "+iterator.next().getKey()+" >";
		System.out.print(answer);
		if (answer.equals(" 0 >")) System.out.print("> Correct Answer!");
		else System.out.print("> Wrong Answer!");
		System.out.println();
		assertEquals(" 0 >", answer);
		
		directions = gb.shortestPath(20, 6);
		System.out.print("The Path from 20 to 6: ");
		System.out.print("Answer: ");
		if (directions==null) System.out.print("null >> Correct Answer!");
		else System.out.print("not null >> Wrong Answer!");
		System.out.println();
		assertEquals(directions, null);
		
		directions = gb.shortestPath(8, 6);
		System.out.print("The Path from 6 to 8: ");
		System.out.print("Answer: ");
		if (directions==null) System.out.print("null >> Correct Answer!");
		else System.out.print("not null >> Wrong Answer!");
		System.out.println();
		assertEquals(directions, null);
		
		directions = gb.shortestPath(11, 0);
		System.out.print("The Path from 0 to 11: ");
		System.out.print("Answer: ");
		if (directions==null) System.out.print("null >> Correct Answer!");
		else System.out.print("not null >> Wrong Answer!");
		System.out.println();
		assertEquals(directions, null);
		
		directions = gb.shortestPath(4, 2);
		System.out.print("The Path from 2 to 4: ");
		answer = "";
		System.out.print("Answer:");
		for (Iterator<node_info> iterator = directions.iterator(); iterator.hasNext();)
			answer = answer + " "+iterator.next().getKey()+" >";
		System.out.print(answer);
		if (answer.equals(" 4 > 2 >")) System.out.print("> Correct Answer!");
		else System.out.print("> Wrong Answer!");
		System.out.println();
		assertEquals(" 4 > 2 >", answer);
		
		directions = gb.shortestPath(2, 7);
		System.out.print("The Path from 2 to 7: ");
		answer = "";
		System.out.print("Answer:");
		for (Iterator<node_info> iterator = directions.iterator(); iterator.hasNext();)
			answer = answer + " "+iterator.next().getKey()+" >";
		System.out.print(answer);
		if (answer.equals(" 2 > 7 >")||answer.equals(" 2 > 1 > 7 >")) System.out.print("> Correct Answer!");
		else System.out.print("> Wrong Answer!");
		System.out.println();
		assertEquals(" 2 > 7 >", answer);
		
		directions = gb.shortestPath(8, 10);
		System.out.print("The Path from 8 to 10: ");
		answer = "";
		System.out.print("Answer:");
		for (Iterator<node_info> iterator = directions.iterator(); iterator.hasNext();)
			answer = answer + " "+iterator.next().getKey()+" >";
		System.out.print(answer);
		if (answer.equals(" 8 > 9 > 10 >")) System.out.print("> Correct Answer!");
		else System.out.print("> Wrong Answer!");
		System.out.println();
		assertEquals(" 8 > 9 > 10 >", answer);
		
		directions = gb.shortestPath(0, 6);
		System.out.print("The Path from 0 to 6: ");
		answer = "";
		System.out.print("Answer:");
		for (Iterator<node_info> iterator = directions.iterator(); iterator.hasNext();)
			answer = answer + " "+iterator.next().getKey()+" >";
		System.out.print(answer);
		if (answer.equals(" 0 > 3 > 4 > 2 > 7 > 6 >")) System.out.print("> Correct Answer!");
		else System.out.print("> Wrong Answer!");
		System.out.println();
		assertEquals(" 0 > 3 > 4 > 2 > 7 > 6 >", answer);
		
		directions = gb.shortestPath(13, 14);
		System.out.print("The Path from 13 to 14: ");
		answer = "";
		System.out.print("Answer:");
		for (Iterator<node_info> iterator = directions.iterator(); iterator.hasNext();)
			answer = answer + " "+iterator.next().getKey()+" >";
		System.out.print(answer);
		if (answer.equals(" 13 > 15 > 14 >")) System.out.print("> Correct Answer!");
		else System.out.print("> Wrong Answer!");
		System.out.println();
		assertEquals(" 13 > 15 > 14 >", answer);
	}
	
	@Test
	public void saveAndloadGraphTest() {
		System.out.println();
		System.out.println("############## \"Save&Load\" functions test ###############");
		weighted_graph_algorithms Old = new WGraph_Algo();
		Old.init(Graph);
		weighted_graph_algorithms New = new WGraph_Algo();
		assertFalse(Old.save(null));
		assertFalse(New.load(null));
		assertTrue(Old.save("results.txt"));
		assertTrue(New.load("results.txt"));
		if(Old.equals(New)) System.out.println("Correct Save&Load functions!");
		else System.out.println("Wrong in Save&Load functions!");
		assertEquals(Old, New);
	}
	
	/* ######################################################################################### */
	/* #################################### RunTime Test ####################################### */
	/* ######################################################################################### */
	
	/* This test was copied from task 0 (credit to Boaz) */
	@Test
	public void RunTimeTest() throws InterruptedException, IOException {
		System.out.println(" _______________________________________________________________");
		System.out.println("| RunTimeTest in progress...                                    |");
		System.out.println("| Note! Some computers cannot run this test!                    |");
		System.out.println("| (required to reduce the number of edges less then 10 million) |");
		System.out.println("|_______________________________________________________________|");
		long start = new Date().getTime();
		Random r = new Random();
		int vSize = 1000000;
		int eSize = 10000000;
		weighted_graph testG = new WGraph_DS();
	    for (int i = 0; i<vSize; i++) testG.addNode(i);
	    while(testG.edgeSize()<eSize) {
	    	testG.connect(r.nextInt(vSize), r.nextInt(vSize), r.nextInt(10));
	    }
	    testG.toString();
	    long end = new Date().getTime();
        double dt = (end-start)/1000.0;
        assertTrue(dt<30);
        System.out.println("RunTimeTest Done! result: "+dt);
	}
}