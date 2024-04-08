package edu.ucalgary.oop;

import java.io.*;

/**
 * This class is responsible for generating and printing order forms
 * for inventory items into an output file named "output.txt". It includes
 * functionality for suggesting manufacturers if the requested items
 * are not available in the inventory.
 *
 * @version 1.4
 * @since 1.0
 * @author Dainielle Puno, Praveen De Silva, Salma Ineflas, Shahzeb Ahmed
 */
public class Printing {

    private BufferedWriter buffWrite;
    private FileWriter fileWrite;
    private String requestDetails;
    private String[] inventoryItemIDs; // Updated to reflect InventoryItem context
    private String[] suggestedManufacturers; // Spelling corrected
    private int totalCost;

    /**
     * Constructor for the Printing class.
     * Initializes the request details, inventory item IDs, and suggested manufacturers.
     *
     * @param requestDetails The details of the request.
     * @param inventoryItemIDs The IDs of the inventory items.
     * @param suggestedManufacturers The names of suggested manufacturers if items are unavailable.
     */
    public Printing(String requestDetails, String[] inventoryItemIDs, String[] suggestedManufacturers) {
        this.requestDetails = requestDetails;
        this.inventoryItemIDs = inventoryItemIDs;
        this.suggestedManufacturers = suggestedManufacturers;
    }

    /**
     * Writes the order form to an output file named "output.txt".
     * If the requested items are available, it details the items and total cost.
     * Otherwise, it suggests alternative manufacturers.
     *
     * @return A string summary of the written content or an error message.
     */
    public String writeFile() {
        try {
            File file = new File("output.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            fileWrite = new FileWriter(file, false); // Overwrite existing file
            buffWrite = new BufferedWriter(fileWrite);

            String toWrite = buildOrderContent(); // Consolidate writing logic
            buffWrite.write(toWrite);
            
            System.out.println("Order form created successfully.");
            return toWrite;
        } catch(IOException ioe) {
            ioe.printStackTrace();
            return "An error occurred during file writing.";
        } finally { 
            try {
                if (buffWrite != null) {
                    buffWrite.close();
                }
            } catch (Exception ex) {
                System.out.println("Error in closing the BufferedWriter: " + ex);
                return "An error occurred while closing file resources.";
            }
        }
    }

    /**
     * Builds the string content for the order form or manufacturer suggestions.
     *
     * @return The content to be written to the output file.
     */
    private String buildOrderContent() {
        StringBuilder contentBuilder = new StringBuilder();
        if (inventoryItemIDs != null && inventoryItemIDs.length > 0) {
            totalCost = Integer.parseInt(inventoryItemIDs[0]); // Assuming first ID is actually total cost (based on original logic)
            contentBuilder.append("Inventory Order Form ->\n\n\n")
                .append("Faculty Name:\nContact:\nDate:\n\n\n")
                .append("Original Request: ").append(requestDetails).append("\n\n\nItems Ordered:\n");
            for (int i = 1; i < inventoryItemIDs.length; i++) {
                if (inventoryItemIDs[i] != null) {
                    contentBuilder.append("ID: ").append(inventoryItemIDs[i]).append("\n");
                }
            }
            contentBuilder.append("\n\nTotal Cost: $").append(totalCost);
        } else {
            contentBuilder.append("Order cannot be fulfilled based on current inventory. Suggested manufacturers are ")
                .append(String.join(", ", suggestedManufacturers)).append(".");
        }
        return contentBuilder.toString();
    }

    // Getters and Setters
        /**
     * Retrieves the detailed description of the request.
     *
     * @return A string representing the request details.
     */
    public String getRequestDetails() {
        return requestDetails;
    }

    /**
     * Updates the detailed description of the request.
     *
     * @param requestDetails A string containing the new details of the request.
     */
    public void setRequestDetails(String requestDetails) {
        this.requestDetails = requestDetails;
    }

    /**
     * Retrieves the array of IDs for the inventory items.
     *
     * @return An array of strings containing the inventory item IDs.
     */
    public String[] getInventoryItemIDs() {
        return inventoryItemIDs;
    }

    /**
     * Sets the array of IDs for the inventory items.
     *
     * @param inventoryItemIDs An array of strings containing the new inventory item IDs.
     */
    public void setInventoryItemIDs(String[] inventoryItemIDs) {
        this.inventoryItemIDs = inventoryItemIDs;
    }

    /**
     * Retrieves the names of suggested manufacturers when items are unavailable.
     *
     * @return An array of strings containing the names of the suggested manufacturers.
     */
    public String[] getSuggestedManufacturers() {
        return suggestedManufacturers;
    }

    /**
     * Sets the names of suggested manufacturers for when items are unavailable.
     *
     * @param suggestedManufacturers An array of strings containing the names of manufacturers to suggest.
     */
    public void setSuggestedManufacturers(String[] suggestedManufacturers) {
        this.suggestedManufacturers = suggestedManufacturers;
    }

}
