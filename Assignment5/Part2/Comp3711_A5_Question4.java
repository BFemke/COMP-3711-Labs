/*Barbara Emke
 * T00721475
 * COMP 3711
 */
 
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Comp3711_A5_Question4 {

	public static void main(String[] args) {
		// Load the dataset from a CSV file
		List<String[]> dataset = loadDataset("dataset.csv");

		//Define the list of attribute names
		List<String> attributes = new ArrayList<>();
		// Retrieve the String[] from the ArrayList that houses the attributes
        String[] retrievedStringArray = dataset.get(0);
		
		//Iterates through the array adding the attributes to the ArrayList
		for (String str : retrievedStringArray) {
            attributes.add(str);
        }
		dataset.remove(0);
		
		//removes output from attributes to be used
		int lastIndex = attributes.size() - 1;
		attributes.remove(lastIndex);
			
		// Build the decision tree
		Node root = buildDecisionTree(dataset, attributes);

		// Print the decision tree
		System.out.println();
		printDecisionTree(root, "");
    }
	
	/*
	Parameters:
		dataset (List<String[]>): The dataset represented as a list of String arrays.
		attributes (List<String>): The list of attribute names.
	Outputs:
		Node: The root node of the decision tree.
	*/
	private static Node buildDecisionTree(List<String[]> dataset, List<String> attributes){
		// Calculate the entropy of the root node
		double entropy = calculateEntropy(dataset);

		// Find the attribute with the highest information gain
		String bestAttribute = null;
		double highestInformationGain = 0;
		int bestAttributeIndex = -1;

		for (String attribute : attributes) {
			int attributeIndex = attributes.indexOf(attribute);
			double informationGain = calculateInformationGain(dataset, attributeIndex);

			//checks if it is currently the best splitting attribute
			if (informationGain > highestInformationGain) {
				bestAttribute = attribute;
				highestInformationGain = informationGain;
				bestAttributeIndex = attributeIndex;
			}
		}

		// If all the attributes have been used or the entropy is zero, return a leaf node
		if (bestAttribute == null || entropy == 0) {
			return new Node(dataset.get(0)[dataset.get(0).length - 1]);
		}

		// Create a new node with the best attribute
		Node root = new Node(bestAttribute);

		// Split the data into subsets based on the best attribute
		Map<String, List<String[]>> subsets = new HashMap<>();
		for (String[] example : dataset) {
			String value = example[bestAttributeIndex];
			
			//Gets current list with that value or makes another
			List<String[]> subset = subsets.getOrDefault(value, new ArrayList<>());
			
			//Trims the best attribute out of the dataset so attribute indices continue to match
			String[] trimmedExample = removeElement(example, bestAttributeIndex);
			subset.add(trimmedExample);
			subsets.put(value, subset);
		}

		// Recursively build decision trees for each subset
		for (String value : subsets.keySet()) {
			List<String> remainingAttributes = new ArrayList<>(attributes);
			
			//removes best attribute from list to check
			remainingAttributes.remove(bestAttribute);
			Node child = buildDecisionTree(subsets.get(value), remainingAttributes);
			root.getChildren().put(value, child);	//adds children to parent node
		}

		return root;
	}

	/*
		Parameters:
		arr (String[]): The original array from which an element is to be removed.
		index (int): The index of the element to be removed from the array.
	Return Value:
		String[]: A new array that is a modified version of the original array arr, with the 
	*/
	private static String[] removeElement(String[] arr, int index) {
		String[] result = new String[arr.length - 1];
		System.arraycopy(arr, 0, result, 0, index);
		System.arraycopy(arr, index + 1, result, index, arr.length - index - 1);
		return result;
	}

	
	/*
	Parameters:
		String fileName: The name of the file to be read (CSV file).
	Return Value:
		List<String[]>: A list of arrays of strings, where each array represents a record from the CSV file.
	*/
    private static List<String[]> loadDataset(String fileName) {
        List<String[]> dataset = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                dataset.add(values);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataset;
    }
	
	/*
	Parameters:
		List<String[]> data: The dataset as a list of String arrays.
	Return Value:
		double: The calculated entropy for the given dataset.
	*/
	private static double calculateEntropy(List<String[]> data){
		int totalExamples = data.size();
        int positiveExamples = 0;
		double entropy = 0;
		
		//Finds all the yes's of the data group
        for (String[] example : data) {
            if (example[example.length - 1].equals("Yes")) {
                positiveExamples++;
            }
        }

		//calculates the probabilities of yes and no fro the group
        double positiveProbability = (double) positiveExamples / totalExamples;
        double negativeProbability = 1 - positiveProbability;

		//Calculates entropy with help from log2 function
		if(positiveProbability == 0 || negativeProbability == 0){
			entropy = 0;
		}
		else {
			entropy = -positiveProbability * log2(positiveProbability)
						   - negativeProbability * log2(negativeProbability);
		}

        return entropy;
	}
	
	/* 
	Parameters:
		List<String[]> data: The dataset as a list of String arrays.
		int attributeIndex: The index of the attribute for which to calculate the information gain.
	Return Value:
		double: The calculated information gain for the specified attribute based on its index.
	*/
	private static double calculateInformationGain(List<String[]> data, int attributeIndex){
		double parentEntropy = calculateEntropy(data);

        // Calculate the entropy of each subset based on the attribute
        Map<String, Double> subsetEntropies = new HashMap<>();
		
		//Finds all unique values ant iterates through them
        for (String value : subsets(data, attributeIndex)) {
			//gets all entries that pertain to a particular value
            List<String[]> subset = subset(data, attributeIndex, value);
            double subsetEntropy = calculateEntropy(subset);	//calculates value entropy
            subsetEntropies.put(value, subsetEntropy);
        }

        // Calculate the weighted average of the subset entropies
        double weightedEntropy = 0;
        for (String value : subsetEntropies.keySet()) {
            double weight = (double) subset(data, attributeIndex, value).size() / data.size(); //gets weighting for each value
            weightedEntropy += weight * subsetEntropies.get(value);	//adds on the weighted entropy to total weighted entropy value
        }

        // Calculate the information gain
        double informationGain = parentEntropy - weightedEntropy;

        return informationGain;
	}
	
    // Method to calculate log base 2
    private static double log2(double value) {
        return Math.log(value) / Math.log(2);
    }


	//Creates the nodes for the ID3 tree
    private static class Node {
        private String attribute;
        private Map<String, Node> children;

        public Node(String attribute) {
            this.attribute = attribute;
            this.children = new HashMap<>();
        }

        public String getAttribute() {
            return attribute;
        }

        public Map<String, Node> getChildren() {
            return children;
        }
    }
	
	/*
	Parameters:
		data: The dataset as a list of String arrays.
		attributeIndex: The index of the attribute for which we want to find subsets.
	Returns:
		A list of unique values for the specified attribute in the dataset.
	*/
	private static List<String> subsets(List<String[]> data, int attributeIndex) {
        List<String> subsets = new ArrayList<>();
		//Finds all unique values of the attribute at attributeIndex
        for (String[] example : data) {
            String value = example[attributeIndex];
			//If not already added, adds new value to set
            if (!subsets.contains(value)) {
                subsets.add(value);
            }
        }
        return subsets;
    }

	/*
	Parameters:
		data: The dataset as a list of String arrays.
		attributeIndex: The index of the attribute based on which we want to create a subset.
		value: The value of the specified attribute for which we want to create the subset.
	Returns:
		A subset of the dataset containing all rows where the attribute at attributeIndex matches the given value.
	*/
    private static List<String[]> subset(List<String[]> data, int attributeIndex, String value) {
        List<String[]> subset = new ArrayList<>();
        for (String[] example : data) {
			//If it matches the same value it is added to the set
            if (example[attributeIndex].equals(value)) {
                subset.add(example);
			}
        }
        return subset;
    }
	

	/*
	Parameters:
		root (Node): The root of the decision tree or subtree to be printed.
		prefix (String): The prefix string used to format the tree structure.
	*/
	public static void printDecisionTree(Node root, String prefix) {
		if (root == null)
			return;

		System.out.println(prefix + "└── " + root.getAttribute());

		// Print child nodes and their associated attribute values
		Map<String, Node> children = root.getChildren();
		for (Map.Entry<String, Node> entry : children.entrySet()) {
			Node child = entry.getValue();
			System.out.println(prefix + "    ├── " + entry.getKey());
			printDecisionTree(child, prefix + "    │   ");
		}
	}
}