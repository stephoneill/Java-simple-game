package mainProjectv2;

import java.util.Scanner;

import java.util.Random;
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;
import java.io.FileWriter;   // Import the FileWriter class
import java.io.BufferedWriter;   // Import the FileWriter class
import java.util.ArrayList; // import the ArrayList class
import java.util.Collections; // import the Collections class
import java.util.InputMismatchException;
import java.util.List;
import java.io.FileReader;



import java.io.IOException;

public class Play {
	
	protected static ArrayList<Player> players = new ArrayList<Player>(); // to contain all players
	
	public static void main(String[] args) {
		
		// firstly reading in the leaderboard file to recreate past players, for a persistent leaderboard
		try (Scanner s = new Scanner(new FileReader("leaderboard.txt"))) {
			int lineNumber = 0; // counter for line number in leaderboard file
			int index = 0; // counter for the index in the arraylist 'players'
			while (s.hasNext()) {
		    	String temp = s.nextLine(); // save whole line as a string, parse later
		    	
		    	// only when it reaches the table entries (third line of file), recreate the players that already are on the leaderboard
		    	if (lineNumber >= 3){ 
		    	
		    		String name = temp.substring(0, temp.indexOf("|"));
		    		String temppoints = (temp.substring(temp.indexOf("|")+1, temp.length())).replaceAll("\\s+","");
		    		int points = Integer.parseInt(temppoints);
		    		
		    		// create instance of relevant subclass of Player (VIP or limited)
		    		if (name.contains("(VIP)")) {
		    			name = name.substring(0, name.indexOf("(")); // remove the '(VIP)' part of string.
		    			name = name.replaceAll("\\s+",""); // remove whitespaces.
		    			players.add(new VIPPlayer(name));			    			
		    		} else {
		    			name = name.replaceAll("\\s+","");
		    			players.add(new LimitedPlayer(name));
		    		}
		    		players.get(index).setPoints(points);
		    		index += 1;
		    		
		    	}
		    	
		    	lineNumber += 1;

		    }
			
		} catch (FileNotFoundException e1) {
			// do nothing, file will just be created later on in the program.
		}
			
		while(true) { // keep program looping until it reaches the return statement
			Scanner reader = new Scanner(System.in);
			System.out.println("Please choose an option: \n1. New Player \n2. Quit ");
			
			int choice = 0;
			while(true) { // ensure the scanner has an integer input
				try {
					choice = reader.nextInt();
					if(choice == 1 || choice == 2) {
						break; 
					}
					System.out.println("Error: valid options are 1 or 2. Please try again.");
					continue;
				} catch(InputMismatchException e) {
					System.out.println("Error: only accepts integer input. Please try again.");
					reader.next();
				}
				
				
			}
				
			if(choice == 1) {
				Player player;
				System.out.println("Are you a VIP member?: (Y/N)");
				
				
				
				while(true) {
					String vipYN = reader.next();
					vipYN = vipYN.replaceAll("\\s+",""); // remove any white spaces
					if(vipYN.equals("y") || vipYN.equals("Y")) {
						player = new VIPPlayer();
						break;
					} else if (vipYN.equals("n") || vipYN.equals("N")) {
						player = new LimitedPlayer();
						break;
					} else {
						System.out.println("Error: please enter 'Y' or 'N'.");
						
					}
				}
				player = enterName(player);
				players.add(player);
				
				int game = 0;
				
				while(game != -1) {
					game = chooseGame(player);
					switch(game) {
					case 1:
						rps(player);
						break;
					case 2:
						powerball(player);
						break;
					case 3:
						coinflip(player);
						break;
					}
				}
				
			} else if (choice == 2) {
				try {
					showLeaderboard();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}	
				return;
			} else {
				System.out.println("Error: Invalid choice. Please select from the two available options.");

			}
			
			
		}
	}
		
	
	public static Player enterName(Player player) {
		Scanner reader = new Scanner(System.in);
		System.out.println("Please enter a name (No more than 15 characters): ");
		String name = reader.nextLine();
		name = name.replaceAll("\\s+",""); // remove any white spaces
		
		
		while(true) {
			if(name.isEmpty()) {
				System.out.println("Error: name cannot be null. Please try again."); // name must contain at least 1 character
			} else if(name.length() > 15){
				System.out.println("Error: Name must be no longer than 15 letters."); // for a reasonable limit on name length, and for formatting purposes also in leaderboard
			} else {
				break;
			}
			name = reader.nextLine();
			name = name.replaceAll("\\s+",""); // remove any white spaces
		}
	
		player.setName(name);
		return player;
	}


	
	public static int chooseGame(Player player) {
		System.out.println("--------------------------------------------------");
		Scanner reader = new Scanner(System.in);
		System.out.println("Hello " + player.getName() + ". Please choose a game, or -1 to quit.\n1. Rock, Paper, Scissors (First to 3) \n2. Powerball (Difficult) \n3. Coinflip (First to 3)");
		int g = reader.nextInt(); 
		
		return g; // return chosen game to the main function (1, 2 or 3)
	}
		
		
	
	public static void showLeaderboard() throws FileNotFoundException {
		Collections.sort(players); // sorts the arraylist by points (descending)
		
		// Writing up new leaderboard file
		try {
		      File f = new File("leaderboard.txt");
		      f.createNewFile();
		} catch (IOException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		}
		try {
		      FileWriter myWriter = new FileWriter("leaderboard.txt");
		      myWriter.write("---------- Player Leaderboard ---------\n");
		      String format = "%1$-20s|%2$-20s\n";
		      myWriter.write(String.format(format, "Player", "Points"));
		      myWriter.write(String.format(format, "____________________", "____________________"));
		      myWriter.close();
		} catch (IOException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		}
		
		System.out.println("\nThank you for playing. Goodbye!");
			
		try {
		      // loop through the sorted arraylist 'players' in order to populate leaderboard with each player's name and points
		      FileWriter myWriter = new FileWriter("leaderboard.txt", true);
		      BufferedWriter bw = new BufferedWriter(myWriter);
		      String format = "%1$-20s|%2$-20s\n";
		      for (int i=0; i < players.size(); i++) {
		    	  // VIP player get 'VIP' next to their name in the leaderboard
		    	  if(players.get(i) instanceof VIPPlayer) {
		    		  bw.write(String.format(format, players.get(i).getName() + " (VIP)", players.get(i).getPoints()));
		    	  } else {
		    		  bw.write(String.format(format, players.get(i).getName(), players.get(i).getPoints()));
		    	  }
		    	  
		      }
		      bw.close();
			  myWriter.close();
			  
		} catch (IOException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		}
		
		// Finally, print contents of this file to the console:
		File file = new File("leaderboard.txt");
		Scanner sc = new Scanner(file);
	
		while (sc.hasNextLine()) {
			System.out.println(sc.nextLine());
		}
		return; // end run of program
	
	}
	
	public static void rps(Player player) {
		System.out.println("~~~ Welcome to Rock, Paper, Scissors! ~~~ \nFirst to 3 wins 10 points. Good luck!");
		Scanner reader = new Scanner(System.in);
		int yourpoints = 0;
		int comppoints = 0;
		int round = 1;
		while(yourpoints < 3 & comppoints < 3) {
			System.out.println("\nRound "+ round + ". When you're ready, enter one of: Rock/Paper/Scissors");
			String yourplay = reader.nextLine();
			yourplay = yourplay.replaceAll("\\s+",""); // remove any white spaces
			if (yourplay.equals("rock")) {
				yourplay = "Rock";
			}
			
			Random rand = new Random();
			int rand_int = rand.nextInt(3);
			String compplay = "None";
			if(rand_int == 0) {
				compplay = "Rock";
			} else if (rand_int == 1) {
				compplay = "Paper";
			} else if (rand_int == 2){
				compplay = "Scissors";
			} 
			
			System.out.println(compplay); // print computer's choice
			
			
			if ((yourplay.equals("Rock") & compplay.equals("Scissors")) || (yourplay.equals("Scissors") & compplay.equals("Paper")) || (yourplay.equals("Paper") & compplay.equals("Rock"))) {
				System.out.println("\nYour point...");
				yourpoints += 1;
			} else if ((compplay.equals("Rock") & yourplay.equals("Scissors")) || (compplay.equals("Scissors") & yourplay.equals("Paper")) || (compplay.equals("Paper") & yourplay.equals("Rock"))) {
				System.out.println("\nComputer's point!");
				comppoints += 1;
			} else if (compplay.equals(yourplay)){
				System.out.println("\nDraw, let's try that again...");
			} else {
				System.out.println("Please enter a valid input!");
			}
			System.out.println("Your score: " + String.valueOf(yourpoints));
			System.out.println("My score: " + String.valueOf(comppoints));
			round += 1;
		
		}
		
		
		if (yourpoints > comppoints) {
			int points;
			if (player instanceof VIPPlayer) {
				points = 30;
			} else {
				points = 10; // ie. limited players
			}
			player.addPoints(points);
			System.out.println("\nCongratulations " + player.getName() + "! You win! You have won "+ points +" points. You now have a total of " + player.getPoints() + " points.");
			
		} else if (comppoints > yourpoints) {
			System.out.println("\nGame over...Computer wins. You have won 0 points. You still have a total of " + player.getPoints() + " points");
		} 
		System.out.println("\nFinal scores:\nYour score: " + String.valueOf(yourpoints));
		System.out.println("My score: " + String.valueOf(comppoints));
		
		return;
		
		
		
	}
	
	public static void powerball(Player player) {
		System.out.println("~~~ Welcome to Powerball! ~~~");
		Scanner input = new Scanner(System.in);
		ArrayList<Integer> userNumbers = new ArrayList<Integer>(); // to store user inputs
		
		boolean invalid = false;
		System.out.println("Please enter 5 numbers between 1 and 69:");
		while(userNumbers.size() < 5) { // checking all input numbers are within range
			invalid = false;
			for (int i=0; i < 5; i++) {
				int tempNum = input.nextInt();
				if ((tempNum < 1) || (tempNum > 69)) {
					invalid = true;
				} else {
					userNumbers.add(tempNum);
				}
			}
			if(invalid == true) {
				System.out.println("Error: only accepts numbers between 1 and 69 (inclusive). Please try again.");
			}
		
		}
		
		
		
		System.out.println("Now enter 1 number between 1 and 26:");
		String temp = input.nextLine(); // needed to consume the next line
		
		
				
		int userPowerball = 0;
		
		while(userPowerball == 0) { // checking input is within correct range
			int tempPowerball = Integer.parseInt(input.nextLine());
			if(tempPowerball < 1 || tempPowerball > 26) {
				System.out.println("Error: only accepts numbers between 1 and 26 (inclusive). Please try again.");
			} else {
				userPowerball = tempPowerball;
			}
			
		}
		
		System.out.println("~~~ RESULTS ~~~ \nYour chosen main numbers were:");
		for(int i=0; i < userNumbers.size(); i++) {
			System.out.print(userNumbers.get(i) + " ");
		}
		System.out.println("\nYour Powerball: "+ userPowerball);
		
		
		// generate the 5 main numbers and 1 Powerball
		System.out.print("\nThe numbers selected by the machine are... ");
		ArrayList<Integer> selectedNumbers = new ArrayList<Integer>();
		Random rand = new Random();
		for(int i=0; i < 5; i++) {
			int tempNum = rand.nextInt(69)+1; // 1 to 69 inclusive
			selectedNumbers.add(tempNum);
			System.out.print(tempNum + " ");
		}
		
		int selectedPowerball = rand.nextInt(26)+1; // 1 to 26 inclusive
		System.out.println("\nAnd the Powerball is.... " + Integer.toString(selectedPowerball));
		
		// Identify what the user got correct using a nested loop, and making sure not to count matches twice.
		ArrayList<Integer> matched = new ArrayList<Integer>(); // to contain matched numbers
		for(int i = 0; i < selectedNumbers.size(); i++) {
			for(int j=0; j < userNumbers.size(); j++) {
				if(selectedNumbers.get(i)==userNumbers.get(j)) {
					matched.add(userNumbers.get(j));
					
					selectedNumbers.set(i, 0); // set to 0 so that it doesn't get matched again (equivalent to removing from list).
					userNumbers.set(j, -1); // set to -1 so that it doesn't get matched again AND doesn't get matched with the 0's in the other list.
				}
			}
		}
				
			
			
			
			System.out.print("Of the main numbers, you matched " + Integer.toString(matched.size()) + " number(s). ");
			if(matched.size() != 0) {
				System.out.print("(");
				for(int j=0; j< matched.size(); j++) {
					System.out.print(matched.get(j) + " ");
				}
				System.out.println(")");
			}
			if (selectedPowerball == userPowerball) {
				System.out.println("You matched the Powerball!");
			}
			
			
			int odds = 0;
			
			if((matched.size() == 5) & (selectedPowerball == userPowerball)) {
				odds = 292_201_338;
			} else if (matched.size() == 5) {
				odds = 11_688_054;
			} else if ((matched.size() == 4) & (selectedPowerball == userPowerball)) {
				odds = 913_129;
			} else if(matched.size() == 4) {
				odds = 36_525;
			} else if ((matched.size() == 3) & (selectedPowerball == userPowerball)) {
				odds = 14_494;
			} else if(matched.size() == 3) {
				odds = 580;
			} else if ((matched.size() == 2) & (selectedPowerball == userPowerball)) {
				odds = 701;
			} else if ((matched.size() == 1) & (selectedPowerball == userPowerball)) {
				odds = 92;
			} else if (selectedPowerball == userPowerball) {
				odds = 38;
			}
			
			
			if(odds != 0) {
				System.out.println("Congratulations " + player.getName() + "!!! This was a 1 in " + Integer.toString(odds) + " chance of winning, therefore you win: " + Integer.toString(odds) + " points!");
				player.addPoints(odds);
				System.out.println("You now have " + player.getPoints() + " points.");
			} else {
				System.out.println("You have won no points. Better luck next time!");
			}
			return;
			
		}
		
		
		
		
	
	
	

	public static void coinflip(Player player){
		System.out.println("~~~ Welcome to Coinflip game! ~~~ \nFirst to 3 wins 10 points. Good luck!");
		Scanner reader = new Scanner(System.in);
		int yourpoints = 0;
		int comppoints = 0;
		int round = 1;
		while(yourpoints < 3 & comppoints < 3) {
			System.out.println("\nRound "+ round + ". When you're ready, enter one of: Heads/Tails or H/T");
			String yourplay = reader.nextLine();
			Random rand = new Random();
			int rand_int = rand.nextInt(2);
			String compplay = "None";
			// generate random number (0 or 1) and assign each to a side of the coin
			if(rand_int == 0) {
				compplay = "Heads";
			} else if (rand_int == 1) {
				compplay = "Tails";
			}
			
			System.out.println(compplay);
			
			
			// If user input H/T, convert to Heads/Tails
			if(yourplay.equals("H") || yourplay.equals("h") || yourplay.equals("heads")){
				yourplay = "Heads";
			} else if (yourplay.equals("T") || yourplay.equals("t") || yourplay.equals("tails")) {
				yourplay = "Tails";
			}
			
		
			
			// If user guessed correct, they get 1 point, else computer gets 1 point. First to 3. 
			if ((yourplay.equals("Heads") & compplay.equals("Heads")) || (yourplay.equals("Tails") & compplay.equals("Tails"))) {
				System.out.println("Correct guess! You got it.");
				yourpoints += 1;
			} else if (!yourplay.equals("Heads") & !yourplay.equals("Tails")) {
				System.out.println("Error: please enter a valid input!");
			} else {
				System.out.println("Wrong guess! Computer's point.");
				comppoints += 1;
			}
			System.out.println("Your score: " + String.valueOf(yourpoints));
			System.out.println("My score: " + String.valueOf(comppoints));
			round += 1;
		
		}
		
		int points;
		if (yourpoints > comppoints) {
			if(player instanceof VIPPlayer) {
				points = 30;
			} else {
				points = 10; // ie. limited players
			}
			player.addPoints(points);
			System.out.println("\nCongratulations " + player.getName() + "! You win! You have won "+ points +" points. You now have a total of " + player.getPoints() + " points.");
			
		} else if (comppoints > yourpoints) {
			System.out.println("\nGame over...Computer wins. You have won 0 points. You still have a total of " + player.getPoints() + " points");
		} 
		System.out.println("\nFinal scores:\nYour score: " + String.valueOf(yourpoints));
		System.out.println("My score: " + String.valueOf(comppoints));
		
		return;
		
	}
	

	



}
