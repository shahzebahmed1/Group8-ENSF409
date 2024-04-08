package edu.ucalgary.oop;

/**
 * Manages the details of individual inventory items, including type, availability of components, and cost.
 * @version 1.3
 * @since 1.0
 * @author Dainielle Puno, Praveen De Silva, Salma Ineflas, Shahzeb Ahmed
 */
public class InventoryItem {
    private String category;
    private boolean[] componentStatus;
    private String itemId;
    private int cost;

    /**
     * Initializes a new inventory item with details about its category, identification, cost, and component status.
     * @param category the category or specific type of the inventory item.
     * @param itemId the unique identifier of the item.
     * @param cost the monetary value associated with the item.
     * @param componentStatus indicates the status (usable/not usable) of each component of the item.
     */
    public InventoryItem(String category, String itemId, int cost, boolean[] componentStatus){
        this.category = category;
        this.itemId = itemId;
        this.cost = cost;
        this.componentStatus = componentStatus;
    }

    /**
     * Retrieves the category of the inventory item.
     * @return A string representing the item's category.
     */
    public String getCategory(){
        return this.category;
    }

    /**
     * Retrieves the usability status of item components.
     * @return An array of booleans indicating the usability of each component.
     */
    public boolean[] getComponentStatus(){
        return this.componentStatus;
    }

    /**
     * Retrieves the unique identifier of the inventory item.
     * @return The item's ID as a string.
     */
    public String getItemId(){
        return this.itemId;
    }

    /**
     * Retrieves the cost of the inventory item.
     * @return The item's cost as an integer.
     */
    public int getCost(){
        return this.cost;
    }
    // This class streamlines the handling of inventory items, aiding in simplifying calculation and tracking tasks.
}
