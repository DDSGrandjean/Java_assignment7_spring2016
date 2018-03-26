/**
	DYLAN GRANDJEAN,
	Java Assignment A7,
	This program is designed to read a file and display sales reports
	to the user
*/

import java.util.Scanner;       	 //Allow use of Scanner class
import java.io.*;               	 //Allow use of I/O class

public class JavaMatic
{
	/**
		The main method allows the user to enter the number of vending
		machines to process and display.
		@param args Takes an array of String arguments.
		@exception IOException If file cannot be found or opened.
	*/
	public static void main(String[] args) throws IOException
	{
		//Variable declaration
		final int DAYS = 5;			//Number of sales figures / weekdays

		double[][] sales;			  //2D array to hold sales record for the week
		String[] locations;			//array to hold vending machine's location

		int vendingMachines;		//User input - number of vending machines

		//Scanner intantiation
		Scanner keyboard = new Scanner( System.in );

		//Display programmer's name
		System.out.println( "DYLAN GRANDJEAN\n" );

		//Get user input
		System.out.print("How many vending machines? ");
		vendingMachines = keyboard.nextInt();

		//Instantiate arrays length from user input
		sales = new double[vendingMachines][5];
		locations = new String[vendingMachines];

		//Methods calling
		vendingMachines = fillArray(DAYS, locations, sales);
		display(DAYS, vendingMachines, locations, sales);
		average(vendingMachines, locations, sales);
		sortLocations(DAYS, vendingMachines, locations, sales);
		display(DAYS, vendingMachines, locations, sales);

		//Leave a line at the end of the program
		System.out.println();
	}

	/**
		The fillArray method reads input from a file and fills arrays accordingly.
		@param FINAL The number of sales figure / weekdays.
		@param place A String array to hold String inputs from file.
		@param amounts A double 2D array to hold double inputs from file.
		@return The actual length of the arrays.
		@exception IOException If file cannot be found or opened.
	*/
	public static int fillArray(final int FINAL, String[] place, double[][] amounts) throws IOException
	{
		//Declare and instantiate file reader
		File file = new File("JavaMaticSales.txt");
		//Instantiate file scanner
		Scanner dataFile = new Scanner(file);

		String erase;				//Empty String for input line cleaning
		boolean isFull = false;		//True when the array is full
		int arrayLength = 0;		//Hold value for the actual length of the arrays

		//Fill array by reading from the file until conditions are false
		for(int i = 0; i < place.length && !isFull; i++)
		{
			//Get first input
			place[i] = dataFile.nextLine();
			//Increment arrayLength
			arrayLength++;

			//Fill amounts row i with the next FINAL values
			for(int j = 0; j < FINAL; j++)
				amounts[i][j] = dataFile.nextDouble();

			//Check if the file has more data
			if(dataFile.hasNext())
				erase = dataFile.nextLine();
			else if(i < place.length - 1)
			{
				System.out.println("\nData for only " + (i + 1) + " locations available.");
				isFull = true;
			}
		}

		//If file has data left after the arrays are full, display message
		if(dataFile.hasNext())
			System.out.println("\nExtra data found in input file will be ignored.");

		//Close the file
		dataFile.close();

		//Return the actual size of the arrays
		return arrayLength;
	}

	/**
		The display method displays a formatted, non-sorted table of arrays content.
		@param FINAL The number of sales figures / weekdays.
		@param arrayLength The actual size of the arrays.
		@param place A String array of vending machine locations.
		@param amounts A double 2D array of vending machine sales records.
	*/
	public static void display(final int FINAL, int arrayLength, String[] place, double[][] amounts)
	{
		//Display formatted header for the table
		System.out.printf("\n%30s%10s%10s%10s%10s\n", "MON", "TUE", "WED", "THU", "FRI");

		//Display the elements of the arrays
		for(int i = 0; i < arrayLength; i++)
		{
			//Display locations
			System.out.printf("%-20s", place[i]);
			for(int j = 0; j < FINAL; j++)
				//Display all sales for the above location
				System.out.printf("%,10.2f", amounts[i][j]);
			System.out.println();
		}
	}

	/**
		The average method calculates the average sales of each location, and the average
		sales from all location daily. Displays averages, best location, and worst day
		based on sales average.
		@param arrayLength The actual size of the arrays.
		@param place A String array of vending machine locations.
		@param amounts A double 2D array of vending machine sales records.
	*/
	public static void average(int arrayLength, String[] place, double[][] amounts)
	{
		//Variable declaration and instantiation
		String[] weekdays = {"MON", "TUE", "WED", "THU", "FRI"};		//The days of the week
		String bestLocation = "";										//The best sales location
		String worstDay = "";											//The worst day of sales
		double testAverage = Double.MIN_VALUE;							//Value to compare other averages to
		double average;													//Average of location or day
		double totalAverage = 0;										//Overall average

		//Display header
		System.out.println("\nAVERAGES BY LOCATION");
		//Calculate and display the average by location
		for(int i = 0; i < arrayLength; i++)
		{
			//Reset average
			average = 0;

			//Display location
			System.out.printf("    %-20s", place[i]);
			for(int j = 0; j < weekdays.length; j++)
				//Add all of location's sales
				average += amounts[i][j];

			//Test for highest sales
			if(average > testAverage)
			{
				testAverage = average;
				bestLocation = place[i];
			}

			//Increment the overall average
			totalAverage += average;

			//Display the average of sales for the above location
			System.out.printf("%,10.2f\n", (average / weekdays.length));
		}

		//Initialize testAverage
		testAverage = Double.MAX_VALUE;

		//Display header
		System.out.println("\nDAILY AVERAGES");
		//Calculate and displaythe daily average of sales
		for(int j = 0; j < weekdays.length; j++)
		{
			//Reset average
			average = 0;

			//Display the day of the week
			System.out.printf("    %-4s", weekdays[j]);
			for(int i = 0; i < arrayLength; i++)
				//Add all sales for the above day
				average += amounts[i][j];

			//Test for worst average
			if(average < testAverage)
			{
				testAverage = average;
				worstDay = weekdays[j];
			}

			//Display the average for the above day
			System.out.printf("%,10.2f\n", (average / arrayLength));
		}

		//Display the overall average
		System.out.printf("\nOVERALL AVERAGE:      %,.2f\n",
						 (totalAverage / (arrayLength * weekdays.length)));
		//Display the best location
		System.out.printf("\nBEST LOCATION: %s\n", bestLocation);
		//Display the worst day for sales
		System.out.printf("\nWORST DAY:    %s\n", worstDay);
	}

	/**
		The sortLocation method re-orders the locations, and their respective sales for each day, in
		alphabetical order.
		@param FINAL The number of sales figures / weekdays.
		@param arrayLength The actual size of the arrays.
		@param place A String array of vending machine locations.
		@param amounts A double 2D array of vending machine sales records.
	*/
	public static void sortLocations(final int FINAL, int arrayLength, String[] place, double[][] amounts)
	{
		//Variable initialization
		double[][] hold = new double[arrayLength][FINAL];		//Temporarily hold the sales arrays for swap
		String temp;											//Temporarily hold the location names for swap
		boolean swap;											//True if swap occured
		int last = arrayLength - 1;								//Size of the array that needs to be tested

		//Sort the array in alphabetical order
		do
		{
			//Reset swap
			swap = false;

			for(int i = 0; i < last; i++)
			{
				//Compare the first two elements and swap if true
				if(place[i].compareTo(place[i + 1]) > 0)
				{
					//Put values in placeholders
					temp = place[i];
					hold[i] = amounts[i];

					//Assign values of second element to the first
					place[i] = place[i + 1];
					amounts[i] = amounts[i + 1];

					//Assign values of first element to the second
					place[i + 1] = temp;
					amounts[i + 1] = hold[i];

					//A swap occured
					swap = true;
				}
			}
			//Reduce the size of the array to scan
			last--;
		} while(swap);
	}
}
