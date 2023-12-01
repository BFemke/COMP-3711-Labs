/*Barbara Emke
 * T00721475
 * COMP 3711
 */

public class Comp3711_A1 {

	public static void main(String[] args) {
		char current_location = args[0].charAt(0); //Grabs current location input
		
		//Gets states from input true = clean, false = dirty
		boolean A_status = Boolean.parseBoolean(args[1]);
		boolean B_status = Boolean.parseBoolean(args[2]);
		boolean C_status = Boolean.parseBoolean(args[3]);
		boolean D_status = Boolean.parseBoolean(args[4]);
		
		System.out.println("\nCurrent Location = " + current_location + "\n" + 
				"Square A status = " + A_status + "\n" +
				"Square B status = " + B_status + "\n" +
				"Square C status = " + C_status + "\n" +
				"Square D status = " + D_status + "\n");
		
		/* Function to determine next action for model based agent in a vacuum cleaner environment
		 */
		char next_location = 'A';
		
		//Checks if all squares are clean then it doesn't move
		if(A_status && B_status && C_status && D_status) 
		{
			next_location = current_location;
		}
		
		//Checks if current square is dirty
		else if((current_location=='A' && !A_status) || (current_location=='B' && !B_status) || (current_location=='C' && !C_status) || (current_location=='D' && !D_status))
		{
			next_location = current_location;
		}
		
		//Handles next moves for current location A if previous ifs were not caught
		else if(current_location == 'A')
		{
			//Checks horizontal first because of priority
			if(!B_status)
			{
				next_location = 'B';
			}
			//If either C or D is dirty to goes to its vertical neighbour in this case  C
			else if(!C_status || !D_status)
			{
				next_location = 'C';
			}
		}
		
		//Handles next moves for current location B if previous ifs were not caught
		else if(current_location == 'B')
		{
			//Checks horizontal first because of priority
			if(!A_status)
			{
				next_location = 'A';
			}
			//If either C or D is dirty to goes to its vertical neighbour in this case  D
			else if(!C_status || !D_status)
			{
				next_location = 'D';
			}
		}
		//Handles next moves for current location C if previous ifs were not caught
		else if(current_location == 'C')
		{
			//Checks horizontal first because of priority
			if(!D_status)
			{
				next_location = 'D';
			}
			//If either A or B is dirty to goes to its vertical neighbour in this case  A
			else if(!A_status || !B_status)
			{
				next_location = 'A';
			}
		}
		//Handles next moves for current location D if previous ifs were not caught
		else if(current_location == 'D')
		{
			//Checks horizontal first because of priority
			if(!D_status)
			{
				next_location = 'C';
			}
			//If either A or B is dirty to goes to its vertical neighbour in this case  B
			else if(!A_status || !B_status)
			{
				next_location = 'B';
			}
		}
		/*
		 * I would like to note that I interpreted condition 6 as saying if the diagonal square is dirty
		 * the vacuum would move vertical first so that when it runs again the priority would be to go horizontal and get that 
		 * square cleaned. I also assumed that the decision function would be run after every turn therefore the diagonal and
		 * vertical square being dirty can be joined in one condition since it results in the same action.
		 */
		
		//Print chosen action
		System.out.println("\nAction - Next Location = " + next_location);
	}

}