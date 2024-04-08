package edu.ucalgary.oop;

/**
 * @author Dainielle Puno <a href = "mailto:dainielle.puno@ucalgary.ca">
 * 		dainielle.puno@ucalgary.ca</a>
 * @author Praveen De Silva <a href = "mailto:weeraddana.desilva@ucalgary.ca">
 * 		weeraddana.desilva@ucalgary.ca
 * @author Salma Ineflas <a href = "mailto:salma.ineflas1@ucalgary.ca">
 * 		salma.ineflas1@ucalgary.ca</a>
 * @author Shahzeb Ahmed <a href = "mailto:shahzeb.ahmed1@ucalgary.ca">
 * 		shahzeb.ahmed1@ucalgary.ca</a>
 * This class manages user interacions for ordering furni
 * @version 1.4
 * @since 1.0
 */

/*
 * UserInput is a class that handles user interactions for placing furniture orders, allowing 
 * users to specify the category, its type, and the number items requested. This class provides
 * functionality for continous order processing to accomodate multiple orders and provides user
 * to exit the program during the ordering process.
 * 
 * INSTRUCTIONS FOR RUNNING THE CODE:
 * 
 */


import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;
 
public class UserInput {
	 
	private String itemNumber;	
	private String category;
	private String type;
	private static Scanner scanner;
	private static String[] finalList;
	private static Printing output;
	private static Map<String, String[]> categoryTypes;


	/**
     * Constructs a UserInput object with the specified furniture category, its type, and number of items requested.
     * Inputs are trimmed to remove leading and trailing whitespace.
     *
     * @param category The category of the furniture item
     * @param type The type of the furniture item within the specified category
     * @param itemNumber The number of furniture items requested
     */

 
	public UserInput(String category, String type, String itemNumber) {
		this.category = category.trim();
		this.type = type.trim();
		this.itemNumber = itemNumber.trim();
	}
 
	static {
		// map furniture categories to their types
		categoryTypes = new HashMap<>();
		categoryTypes.put("chair", new String[]{"Mesh", "Task", "Kneeling", "Executive", "Ergonomic"});
		categoryTypes.put("desk", new String[]{"Traditional", "Adjustable", "Standing"});
		categoryTypes.put("filing", new String[]{"Small", "Medium", "Large"});
		categoryTypes.put("lamp", new String[]{"Desk", "Swing Arm", "Study"});
	}

	/**
     * The main method initiates the application, allowing the user to place furniture orders
     * and potentially reuse surplus items to meet their requirements.
     * The program remains active, processing orders until the user decides to exit the program.
     *
     * @param args Command-line arguments (not used)
     */

	 public static void main(String args[]) {
		Database myJDBC = new Database("jdbc:postgresql://localhost/inventory", "oop", "ucalgary");
		myJDBC.initializeConnection();
		scanner = new Scanner(System.in);
	
		while (true) {
			String category = getCategory();
			String type = getType(category);
			String itemNumber = getItemNumber();
	
			UserInput input = new UserInput(category, type, itemNumber);
			finalList = myJDBC.findInventoryItem(input.category, input.type, input.itemNumber);
			summarizeOrder(input.category, input.type, input.itemNumber, finalList);
			processFinalList(input);
	
			boolean validResponse = false;
			while (!validResponse) {
				System.out.println("Would you like to make another order? (yes/no):");
				String decision = scanner.nextLine().trim().toLowerCase();
	
				if (decision.equals("yes")) {
					validResponse = true; // Allows the outer loop to continue
				} else if (decision.equals("no")) {
					System.out.println("Exiting program...");
					System.out.println("Thank you for using **insert program name**");
					scanner.close();
					System.exit(0); // Exits the program
				} else {
					System.out.println("Invalid input. Please respond with 'yes' or 'no'.");
					// Loop continues until a valid response is given
				}
			}
		}
	}
	
	/**
 	* Capitalizes the first letter of each word in the given string.
 	* Words are assumed to be separated by whitespace.
 	*
 	* @param input The string to be capitalized.
 	* @return The capitalized string.
 	*/
	static String capitalize(String input) {
		if (input == null || input.isEmpty()) {
			return input;
		}
	
		String[] words = input.split("\\s+");
		for (int i = 0; i < words.length; i++) {
			words[i] = words[i].substring(0, 1).toUpperCase() + words[i].substring(1).toLowerCase();
		}
	
		return String.join(" ", words);
	}
	/**
 	* Checks if the given category is valid.
 	*
 	* @param category The category to validate.
 	* @return true if the category is valid, false otherwise.
 	*/
	static boolean validateCategory(String category) {
		return categoryTypes.containsKey(category);
	}
	
	/**
 	* Checks if the given type is valid for the specified category.
 	*
 	* @param category The category of the furniture.
 	* @param type The type of the furniture.
 	* @return true if the type is valid for the category, false otherwise.
 	*/
	static boolean validateType(String category, String type) {
		 String[] availableTypes = categoryTypes.getOrDefault(category, new String[]{});
		 for (String availableType : availableTypes) {
			 if (availableType.equalsIgnoreCase(type)) {
				 return true;
			 }
		 }
		 return false;
	 }
	
	/**
 	* Validates whether the provided string is a positive integer.
 	*
 	* @param number The string to validate as a number.
 	* @return true if the string is a positive integer, false otherwise.
 	*/
	static boolean validateNumber(String number) {
		 try {
			 return Integer.parseInt(number) > 0;
		 } catch (NumberFormatException e) {
			 return false;
		 }
	}
	 /**
 	* Summarizes the order based on user input and inventory availability.
 	* Outputs the summary to the console.
 	*
 	* @param category The category of furniture ordered.
 	* @param type The type of furniture ordered.
 	* @param itemNumber The number of items ordered.
 	* @param finalList An array containing either the item IDs or manufacturer recommendations.
 	*/
	 private static void summarizeOrder(String category, String type, String itemNumber, String[] finalList) {
		System.out.println("\nOrder Summary:");
		System.out.println("Category: " + category);
		System.out.println("Type: " + type);
		System.out.println("Quantity: " + itemNumber);
	
		if (validateInput(finalList[0])) { // Assuming first element indicates item IDs
			System.out.println("Items Ordered:");
			for (int i = 1; i < finalList.length; i++) {
				if (finalList[i] != null && !finalList[i].isEmpty()) {
					System.out.println(" - Item ID: " + finalList[i]);
				}
			}
		} else {
			System.out.println("Note: The requested items are not available. Please see manufacturer recommendations.");
		}
	}
	
	
	/**
 	* Processes the final list of items to create an order form.
 	* Writes the order form to a file.
 	*
 	* @param input The UserInput object containing order details.
 	*/
	private static void processFinalList(UserInput input) {
		 boolean isOrderedItemsID = validateInput(finalList[0]);
		 if (isOrderedItemsID) {
			output = new Printing(input.type + " " + input.category + ", " + input.itemNumber, finalList, null);
		 } else {
			output = new Printing(input.type + " " + input.category + ", " + input.itemNumber, null, finalList);
		 }
		 output.writeFile();
		}
	
	/**
 	* Checks if a given string can be converted to an integer. 
 	*
 	* @param str The string to be evaluated for integer conversion.
 	* @return true if the string can be successfully converted to an integer, false otherwise.
 	*/
	public static boolean validateInput(String str) { 
		try {  
			Integer.parseInt(str);  
			return true;
		 } catch(NumberFormatException e){  
			return false;  
		}  
	}

	
	//Getters


	/**
 	* Prompts the user to enter a furniture category and ensures that the input is valid.
 	* If the input is 'q', the program exits.
 	* Repeats the prompt until a valid category is entered or the program is exited.
 	*
 	* @return The valid furniture category entered by the user.
 	*/
	 private static String getCategory() {
        while (true) {
            System.out.println("Please enter the furniture category (chair, desk, filing or lamp), or type 'q' to quit:");
            String category = scanner.nextLine().trim().toLowerCase();
            if (category.equalsIgnoreCase("q")) {
                System.out.println("Exiting program...");
				System.out.println("Thank you for using **insert program name**");
                System.exit(0);
            }
            if (validateCategory(category)) {
                return category;
            }
            System.out.println("Invalid category entered. Please try again.");
        }
    }

	/**
 	* Prompts the user to enter a furniture type for a given category.
 	* If the input is 'q', the program exits.
 	* Repeats the prompt until a valid type is entered or the program is exited.
 	*
 	* @param category The furniture category for which a type is required.
 	* @return The valid furniture type entered by the user.
 	*/

    private static String getType(String category) {
        while (true) {
            System.out.println("Please enter what type of " + category + " you would like. We have " + String.join(", ", categoryTypes.get(category)) + ", or type 'q' to quit:");
            String type = capitalize(scanner.nextLine().trim());
            if (type.equalsIgnoreCase("q")) {
                System.out.println("Exiting program...");
				System.out.println("Thank you for using **insert program name**");
                System.exit(0);
            }
            if (validateType(category, type)) {
                return type;
            }
            System.out.println("Invalid type for the selected category. Please try again.");
        }
    }

	/**
 	* Prompts the user to enter the number of items required.
 	* If the input is 'q', the program exits.
 	* Repeats the prompt until a valid number is entered or the program is exited.
 	*
 	* @return The valid number of items entered by the user.
 	*/

    private static String getItemNumber() {
        while (true) {
            System.out.println("Please enter the number of items required, or type 'q' to quit:");
            String itemNumber = scanner.nextLine().trim();
            if (itemNumber.equalsIgnoreCase("q")) {
                System.out.println("Exiting program...");
				System.out.println("Thank you for using **insert program name**");
                System.exit(0);
            }
            if (validateNumber(itemNumber)) {
                return itemNumber;
            }
            System.out.println("Invalid quantity. Please enter a positive number.");
        }
    }

	/**
 	* Gets the Printing object used for output.
 	*
 	* @return The Printing object used for output.
 	*/
	public static Printing getOutput() {
    	return output;
	}


}
 