/*Barbara Emke
 * T00721475
 * COMP 3711
 */
 
import java.util.Random;

public class Comp3711_A3 {

    public static String[] colours = {"blue", "red", "orange", "jungle"};
    public static String[] regions = {"BC", "AB", "SK", "MB", "ON", "QC", "NB", "NS", "PEI", "NL", "NU", "NT", "YT"};
    private static int[][] adjacencyMatrix = {
            {1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1},
            {1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0},
            {0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0},
            {0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 1, 0, 0},
            {0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0},
            {0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0},
            {0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 1, 0},
            {1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1}
        };

    public static void main(String[] args) {
        int[] initialState = new int[13];
		int k = 0;

        // checks if k values was added
        if (args.length > 0) {
            try {
                // Attempt to parse the first command-line argument as an integer
                k = Integer.parseInt(args[0]);

                if (k <= 2) {
                    System.out.println("\nk cannot be less than 2.\n");
					System.exit(0);
                } else if (k =< 4) {
                    // generates initial state with specified number of colours
                    generateInitialState(k, initialState);
                } else {
					System.out.println("\nYou must provide a k value of 3 or 4.");
					System.exit(0);
				}

                // catches not a number exception
            } catch (NumberFormatException e) {
                System.err.println("Invalid Number Format: " + args[0]);
				System.exit(0);
            }
        } else {
            System.out.println("\nYou must provide a k value of 3 or 4.");
			System.exit(0);
        }

        int[] solution = hillClimbingSearch(k, initialState, adjacencyMatrix);

        int cost = calculateCost(solution);

        printSolution(solution, k, cost);
    }

    // Performs the hill climbing search to find map colouration
    public static int[] hillClimbingSearch(int k, int[] initialState, int[][] adjacencyMatrix) {
        int[] currentState = initialState;

        while (true) {
            int[] bestNeighbour = null;
            int bestHeuristic = calculateHeuristic(currentState);

            // checks if solution has been found
            if (bestHeuristic == 0) {
                return currentState; // returns solution
            }

            // loops through all the regions
            for (int region = 0; region < 13; region++) {
                // loops through all possible colours
                for (int colour = 0; colour < k; colour++) {

                    // creates new neighbour
                    int[] neighbour = new int[13];
                    System.arraycopy(currentState, 0, neighbour, 0, 13);
                    neighbour[region] = colour;

                    // calls method to calculate heursitic of neighbour
                    int neighbourHeuristic = calculateHeuristic(neighbour);

                    // if heuristic is better than or equal to current best it becomes current best
                    if (neighbourHeuristic < bestHeuristic) {
                        bestNeighbour = neighbour.clone();
                        bestHeuristic = neighbourHeuristic;
                    }
                }
            }

            // If no better neighbor is found, it is stuck in local minimum
            if (bestNeighbour == null) {
                System.out.println("Attempt unsuccessful, stuck in local minimum.");
                System.exit(0);
            }

            // copies best neighbour into current state
            System.arraycopy(bestNeighbour, 0, currentState, 0, 13);
        }
    }

	//Calculates the heuristic value of a given state
	public static int calculateHeuristic(int[] state){
		int heuristic = 0;
		
		//loops through the adjacency matrix
		for(int i = 0; i < 13; i++){
			for(int j = 0; j < 13; j++){
				
				//checks if i and j are neighbouring regions
				if(i != j && adjacencyMatrix[i][j] == 1){
					
					//checks if the neighboring regions have the same colour
					if(state[i] == state[j]){
						//heuristic gets incremented if they have the same colour
						heuristic++;
					}
				}
			}
		}
		
		return heuristic;
	}
	
	//generates an initial colouration state with k different colours
	public static void generateInitialState(int k, int[] state){
		Random random = new Random(); // Create a Random object
		
		//randomly assigns a number representing a colour to each region index
		for(int i = 0; i < 13; i++){
			state[i] = random.nextInt(k);
		}
	}
	
	//calculates the cost of a given state
	public static int calculateCost(int[] state){
		int[] cost = new int[]{1,2,3,5};
		int value = 0;
		
		for(int i =0; i< 13; i++){
			value += cost[state[i]];
		}
		
		return value;
	}
	
	//Brings solution to map colouring problem
	public static void printSolution(int[] solution, int k, int cost){
		
		//prints out specified k value
		System.out.println("A colouring solution with k = " + k + " is: \n");
		
		//loops through regions printing name and colour
		for(int i = 0; i < 13; i++){
			System.out.println(regions[i] + ": " + colours[solution[i]]);
		}
		
		//prints out cost
		System.out.println("\nThe cost of the solution is: " + cost);
	}
}