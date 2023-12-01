/*Barbara Emke
 * T00721475
 * COMP 3711
 */
 

public class Comp3711_A5_Question5 {
	
    public static void main(String[] args) {
		//creates new perceptron and train_test object
        Forward_Propagation perceptron = new Forward_Propagation();
        Train_Test trainer = new Train_Test(perceptron);

		//gets the perceptron to be trained 1000 times
        System.out.println("Training the perceptron...\n");
        trainer.train(1000);

		//defines test instance
        double[] instance5 = {0, 0, 0};  // Off, Off, Off
        trainer.test(instance5);	//calls to get output of test instance
    }
}

class Forward_Propagation {
	
	//defines the inputs for the training data
	private double[][] inputs = {
        {0, 0, 1},
        {1, 1, 1},
        {1, 0, 1},
        {0, 1, 1}
    };

	//Defines the outputs for the training data
    private double[][] outputs = {
        {0},
        {1},
        {1},
        {0}
    };

	//Variable for the perceptron weights and bias
    private double[] weights;
    private double bias;

	/*
	Purpose: Constructor method for the Forward_Propagation class. Initializes weights and 
		bias using the initializeWeightsAndBias method.
	*/
    public Forward_Propagation() {
        initializeWeightsAndBias();
    }

	/*
	Purpose: Initializes the weights and bias using random values.
	*/
    private void initializeWeightsAndBias() {
        weights = new double[inputs[0].length];
		
		//Creates a random weight for each input into the perceptron
		System.out.println("\nInitial weights and bias");
        for (int i = 0; i < weights.length; i++) {
            weights[i] = Math.random();
			System.out.println("w" + (i+1) + ": \t" + weights[i]); //prints out initial random weights
        }
        bias = Math.random();
		System.out.println("bias: \t" + bias + "\n"); //prints out initial random bias
		
    }

	/*
	Parameters:
		input (double[]): The input data for which the output needs to be calculated.
	Output:
		double[]: The output calculated based on the input and current weights and bias.
	Purpose: Calculates the output for the given input using the current weights and bias.
	*/
    public double[] calculateOutput(double[] input) {
        double[] output = new double[input.length];
		//calculates the output of the input
        for (int i = 0; i < input.length; i++) {
            output[i] = sigmoid(input[i] * weights[i] + bias);
        }
        return output;
    }

	/*
	Parameters:
		x (double): The input to the sigmoid function.
	Output:
		double: The output of the sigmoid function for the given input.
	Purpose: Computes the sigmoid function for the given input.
	*/
    private double sigmoid(double x) {
        return 1 / (1 + Math.exp(-x));
    }

	/*
	Output:
		double[][]: The input data for the perceptron.
	Purpose: Returns the input data for the perceptron.
	*/
    public double[][] getInputs() {
        return inputs;
    }

	/*
	Output:
		double[][]: The output data for the perceptron.
	Purpose: Returns the output data for the perceptron.
	*/
    public double[][] getOutputs() {
        return outputs;
    }

	/*
	Output:
		double[]: The weights of the perceptron.
	Purpose: Returns the weights of the perceptron.
	*/
    public double[] getWeights() {
        return weights;
    }

	/*
	Output:
		double: The bias of the perceptron.
	Purpose: Returns the bias of the perceptron.
	*/
    public double getBias() {
        return bias;
    }

	/*
	Parameters:
		bias (double): The new bias value to be set.
	Purpose: Sets a new bias value for the perceptron.
	*/
    public void setBias(double bias) {
        this.bias = bias;
    }
}

class Train_Test {
	
	private Forward_Propagation perceptron;

	/*
	Parameters:
		perceptron (Forward_Propagation): The perceptron to be used for training and testing.
	Purpose: This is a constructor method for the Train_Test class. It initializes the perceptron 
		field of the Train_Test instance with the provided perceptron object.
	*/
    public Train_Test(Forward_Propagation perceptron) {
        this.perceptron = perceptron;
    }
	
	/*
	Parameters:
		iterations (int): The number of training iterations.
	Purpose: This method is used to train the perceptron for a specified number of iterations. 
		It iterates over the training data, calculates the error, and updates the weights and 
		bias accordingly to improve the model.
	*/
    public void train(int iterations) {
		
		//performs training the number of times specified in function call
        for (int i = 0; i < iterations; i++) {
			
			//calculates outputs and errors for the weights of the perceptron
            for (int j = 0; j < perceptron.getInputs().length; j++) {
                double[] input = perceptron.getInputs()[j];
                double[] expectedOutput = {perceptron.getOutputs()[j][0]};
                double[] output = perceptron.calculateOutput(input);
                double error = expectedOutput[0] - output[0];
				
                updateWeightsAndBias(input, error);	//calls function to update weights
            }
        }
    }

	/*
	Parameters:
		input (double[]): The input data.
		error (double): The error in prediction for the given input.
	Purpose: This private method updates the weights and bias of the perceptron based on the 
		calculated error and input. It's a crucial step in training the perceptron and adjusting 
		its parameters to reduce prediction errors.
	*/
    private void updateWeightsAndBias(double[] input, double error) {
		
		//updates all the weights based on input and error of output
        for (int i = 0; i < perceptron.getWeights().length; i++) {
            perceptron.getWeights()[i] += error * input[i];
        }
		
		//sets new bias given error
        perceptron.setBias(perceptron.getBias() + error);
    }

	/*
	Parameters:
		input (double[]): The input data for testing.
	Purpose: This method is used to test the trained perceptron using the provided input. It 
		calculates the output using the trained perceptron, determines whether the output represents 
		"FAKE" or "REAL", and prints the prediction for the given input.
	*/	
    public void test(double[] input) {
        double[] output = perceptron.calculateOutput(input);	//Calls function to calculate output
		double finalOutput = 0;
		
		//calculates final output
		System.out.println("\nFinal weights and bias");
		for (int i = 0; i < perceptron.getWeights().length; i++) {
			finalOutput += perceptron.getWeights()[i] * output[i];
			System.out.println("w" + (i+1) + ": \t" + perceptron.getWeights()[i]); //prints out final weights
        }
		System.out.println("bias: \t" + perceptron.getBias() + "\n"); //prints out final bias
		
		//prediction is Real if output < 0.5 else prediction is FAKE
        String prediction = (finalOutput < 0.5) ? "FAKE" : "REAL";
		System.out.println("Instance 5: " + input[0] + ", " + input[1] + ", " + input[2]);
        System.out.println("Predicted output for instance 5: " + finalOutput + " = " + prediction);
    }
}