/*Barbara Emke
 * T00721475
 * COMP 3711
 */
 
import java.util.*;

public class Comp3711_A2 {

	public static void main(String[] args) {
		
		int cost1 = 0, cost2 = 0, cost3 = 0;
		long time1 = 0, time2 = 0, time3 = 0;
		
		//Sets the path values between nodes for testing
		int[][] cost_matrix = {{0,0,0,6,1,0,0,0,0,0},
							{5,0,2,0,0,0,0,0,0,0},
							{9,3,0,0,0,0,0,0,0,0},
							{0,0,1,0,2,0,0,0,0,0},
							{6,0,0,0,0,2,0,0,0,0},
							{0,0,0,7,0,0,0,0,0,0},
							{0,0,0,0,2,0,0,0,0,0},
							{0,9,0,0,0,0,0,0,0,0},
							{0,0,0,5,0,0,0,0,0,0},
							{0,0,0,0,0,8,7,0,0,0}};
		
		//Sets huerstic values for nodes for testing purposes
		int[] heuristic_vector = {5,7,3,4,6,8,5,0,0,0};
		
		//creates priority queue for node class using f as priority value
		PriorityQueue<Node> frontier = new PriorityQueue<>(Comparator.comparingInt(node -> node.f));

		//adds start node to priority queue
        Node startNode = new Node(0, null, 5);
        frontier.add(startNode);
		
		//Vectors to hold the paths to goals
		Vector<String> pathG1 = new Vector<String>();
		Vector<String> pathG2 = new Vector<String>();
		Vector<String> pathG3 = new Vector<String>();
		
		//creates a new vector to hold goal states anad adds goal states
		Vector<Integer> goal_states = new Vector<Integer>(3);
		for(int i = 0; i < cost_matrix.length; i++){
			//checks if element is a goal state (heuristic is 0)
			if(heuristic_vector[i] == 0){
				//adds element to vector
				goal_states.addElement(i);
			}
		}
		
		//creates vector of visited nodes
		Vector<Node> visited_nodes = new Vector<Node>();
		
		//creates vector of visited states
		Vector<Integer> visited_states = new Vector<Integer>();
		
		//starts timer to find execution times
		long startTime = System.nanoTime();
		
		while(!frontier.isEmpty()){
			//gets next node to explore
			Node nextNode = frontier.poll();
			int state = nextNode.state;
			
			//adds node to visited_node list if not yet visited
			if(!visited_states.contains(state)){
				visited_nodes.addElement(nextNode);
				visited_states.addElement(state);
			}
			//else restart loop to get new node from frontier
			else{
				continue;
			}
			
			//Finds all paths from current node to add to frontier
			for(int i = 0; i < cost_matrix.length; i++){
				//adds new node to frontier if there is a path from current node
				if(cost_matrix[i][state] != 0){
					int cost = cost_matrix[i][state]; //gets cost for path
					int f = heuristic_vector[i] + cost + nextNode.f - heuristic_vector[state]; //calculates f-value for frontier node
					frontier.add(new Node(i, nextNode, f)); //adds new frontier node
				}
			}
			
			//checks if goal state is reached
			if(goal_states.contains(state)){
				//gets path for G1 goal
				if(state == 7)
				{
					nextNode.name = "G1";
					findPath(pathG1, nextNode);
					cost1 = nextNode.f;
					
					//gets execution time for G1
					long endTime = System.nanoTime();
					time1 = endTime - startTime;
				}
				//gets path for G2 goal
				else if(state == 8)
				{
					nextNode.name = "G2";
					findPath(pathG2, nextNode);
					cost2 = nextNode.f;
					
					//gets execution time for G2
					long endTime = System.nanoTime();
					time2 = endTime - startTime;
				}
				//gets path for G3 goal
				else if(state == 9)
				{
					nextNode.name = "G3";
					findPath(pathG3, nextNode);
					cost3 = nextNode.f;
					
					//gets execution time for G3
					long endTime = System.nanoTime();
					time3 = endTime - startTime;
				}
					
			}
			
		}
		
		//prints output of first path
		System.out.print("Path 1 to reach G1 [A");
		printPath(pathG1);
		System.out.print("]; A* score = " + cost1 + "; time required = " + time1 + " nanoseconds\n");
		
		//prints output of second path
		System.out.print("Path 2 to reach G2 [A");
		printPath(pathG2);
		System.out.print("]; A* score = " + cost2 + "; time required = " + time2 + " nanoseconds\n");
		
		//prints output of third path
		System.out.print("Path 3 to reach G3 [A");
		printPath(pathG3);
		System.out.print("]; A* score = " + cost3 + "; time required = " + time3 + " nanoseconds\n");
		
		
		//Prints which path has the smallest cost
		if(cost1 < cost2 && cost1 < cost3){
			System.out.println("Path 1 is the cheapest to reach a goal state");
		}
		else if(cost2 < cost1 && cost2 < cost3){
			System.out.println("Path 2 is the cheapest to reach a goal state");
		}
		else{
			System.out.println("Path 3 is the cheapest to reach a goal state");
		}
		
	}
	
	//Creates node class to be held in priority queue
	static class Node {
        int state;
        Node parent;
        int f;
		String name;

        Node(int state, Node parent, int f) {
            this.state = state;
            this.parent = parent;
			this.f = f;
			int charCode = 65 + state;
			name = String.valueOf((char)charCode);
        }
    }
	
	//finds the path to the goal by backtracking the parents of the nodes
	public static void findPath(Vector<String> path, Node node){
		//Recursive function exit case (when a node does not have a parent)
		if(node.parent == null){
			return;
		}
		findPath(path, node.parent); //recursively calls function to find all node parents
		path.add(node.name);	//adds nodes in path in order
	}
	
	public static void printPath(Vector<String> path){
		
		//prints the path
		for (String state : path) {
            System.out.print(" -> "+state);
        }
	}

}