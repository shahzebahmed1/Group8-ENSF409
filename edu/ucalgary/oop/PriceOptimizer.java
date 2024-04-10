package edu.ucalgary.oop;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import edu.ucalgary.oop.InventoryItem;

/**
 * Manages calculations to determine the most cost-effective way
 * to assemble a specified piece of furniture.
 * @version 1.1
 * @since 1.0
 * @author Dainielle Puno, Praveen De Silva, Salma Ineflas, Shahzeb Ahmed
 */

public class PriceOptimizer {
    private ArrayList<Integer> priceChoices;
    private ArrayList<InventoryItem> furniture;
    private ArrayList<String[]> idForPriceChoices;
    private ArrayList<ArrayList<boolean[]>> piecesForChoices;

    /**
     * Constructor initializes instance variables.
     */
    public PriceOptimizer(){
        this.priceChoices = new ArrayList<Integer>();
        this.furniture = new ArrayList<InventoryItem>();
        this.idForPriceChoices = new ArrayList<String[]>();
        this.piecesForChoices = new ArrayList<ArrayList<boolean[]>>();
    }


    public int optimizedPrice(){
        if(priceChoices.size() == 0){
            return 0;
        }
        int bestPrice = this.priceChoices.get(0);
        for(int i = 0; i < this.priceChoices.size();i++){
            if(this.priceChoices.get(i)<bestPrice){
                bestPrice=this.priceChoices.get(i);
            }
        }
        return bestPrice;
    }

    /**
     * Extracts necessary information from the ResultSet and uses it to compute the optimal cost.
     * @param results the ResultSet from MySQL
     * @param furnitureType specific category of furniture
     * @return String array containing the total cost and IDs of the required furniture.
     */
    public String[] priceCalc(ResultSet results, String furnitureType) throws SQLException{
        String[] ids = {"0"};
        try{
            while(results.next()){
                if(matchesFurnitureCategory(furnitureType, results.getString("Type"))){
                    ArrayList<String> s = new ArrayList<String>();
                    ResultSetMetaData rsmd = results.getMetaData();
                    for(int i = 1; i < rsmd.getColumnCount()+1; i++){
                        if(results.getString(i).equals("Y") || results.getString(i).equals("N")){
                            s.add(results.getString(i));
                        }
                    }
                    boolean[] availability = convertStringsToBooleans(convertListToStringArr(s));
                    furniture.add(new InventoryItem(results.getString("Type"),results.getString("ID"),results.getInt("Price"), availability));
                }
            }
            
            String[] temp = findFurnitureCombo();
            if(temp[0].equals("0")){
                return ids;
            }
            else{
                ids = temp;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return ids;
    }

   /**
    * Compares desired and actual furniture categories to check for a match.
    * @param wantedInventoryItem the desired furniture category
    * @param furnitureToCheck the category to validate
    * @return true if categories match, otherwise false.
    */
    private boolean matchesFurnitureCategory(String wantedInventoryItem, String furnitureToCheck){
        if(wantedInventoryItem.equals(furnitureToCheck)){
            return true;
        }
        else{
            return false;
        }
    }

    private boolean convertToBool(String available){
        if(available.equals("Y")){
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Converts an array of 'Y' and 'N' strings into an array of booleans.
     * @param booleans array of strings to convert
     * @return array of booleans reflecting the input strings
     */
    public boolean[] convertStringsToBooleans(String[] booleans){
        boolean[] b = new boolean[booleans.length];
        for(int i = 0; i < b.length; i++){
            b[i] = convertToBool(booleans[i]);
        }
        return b;
    }

    /**
     * Iterates through possible furniture combinations to find viable assembly options.
     * @return String array with the lowest cost and associated IDs.
     */
    public String[] findFurnitureCombo(){
        //need to add print statements to figure out whats gone wrong
        if(furniture.size()==0){
            String[] error = {"0"};
           return error;
        }
        boolean[] b = generateAllArray(furniture.get(0).getComponentStatus().length);
       for(int i = 0; i <furniture.size();i++){
           String[] idCombo = {furniture.get(i).getItemId()};
           boolean[] parts = furniture.get(i).getComponentStatus();
           ArrayList<boolean[]> partsUsed = new ArrayList<boolean[]>();
           partsUsed.add(parts);
           for(int j=0; j<furniture.size();j++){
                if(!areArraysEquivalent(parts, furniture.get(j).getComponentStatus()) && 
                !areArraysEquivalent(parts, b)&& !containsId(idCombo, furniture.get(j).getItemId())){
                    parts = mergeAvailabilityStatus(parts, furniture.get(j).getComponentStatus());
                    idCombo = appendArray(idCombo, furniture.get(j).getItemId());
                    partsUsed.add(furniture.get(j).getComponentStatus());
                }
           }
           if(areArraysEquivalent(parts, b)){
                priceChoices.add(suitableCost(idCombo));
                idForPriceChoices.add(idCombo);
                piecesForChoices.add(partsUsed);
           }
       }
       for(int i = 0; i < furniture.size(); i++){
         refineCostCalculations(i);
       }
       
       int cost = optimizedPrice();
       if(cost == 0){
           String[] error = {"0"};
           return error;
       }
       String[] idForSelected = new String[idForPriceChoices.get(priceChoices.indexOf(cost)).length+1];
       idForSelected[0] = String.valueOf(cost);
       for(int i = 1; i <idForPriceChoices.get(priceChoices.indexOf(cost)).length+1;i++){
        idForSelected[i] = idForPriceChoices.get(priceChoices.indexOf(cost))[i-1];
       }
       return idForSelected;
       }


    /**
     * adds a String to a String array
     * @param array array to be added to
     * @param toAdd String to add to array
     * @return String array with the added String
     */
    private String[] appendArray(String[] array, String toAdd){
        String[] results = new String[array.length+1];
        for(int i = 0; i < array.length; i++){
            results[i] = array[i];
        }
        results[array.length] = toAdd;
        return results;
    }

    /**
     * Adds a new boolean to an existing boolean array based on specified conditions.
     * @param array original boolean array
     * @param toAdd boolean array to merge
     * @return merged boolean array reflecting the combination of both inputs.
     */
    private boolean[] mergeAvailabilityStatus(boolean[] array, boolean[] toAdd){
        boolean[] results = new boolean[array.length];
        for(int i = 0; i < array.length; i++){
            if(array[i]==false && toAdd[i] == true){
                results[i] = toAdd[i];
            }else{
                results[i] = array[i];
            }
        }
        return results;
    }

    /**
     * Checks if two boolean arrays are equivalent.
     * @param one first array for comparison
     * @param two second array for comparison
     * @return true if both arrays are identical, otherwise false.
     */
    private boolean areArraysEquivalent(boolean[] one, boolean[] two){
        for(int i =0; i <one.length; i++){
            if(one[i] != two[i]){
                return false;
            }
        }
        return true;
    }

    /**
     * Generates a boolean array of specified size, filled with true values.
     * @param size length of the array
     * @return boolean array where all values are true.
     */
    private boolean[] generateAllArray(int size){
      boolean[] a = new boolean[size];
      for(int i = 0; i <size; i++){
          a[i] = true;
      }
      return a;
    }

    /**
     * Calculates the total cost for a set of furniture pieces identified by their IDs.
     * @param ids array of furniture IDs
     * @return total cost of the specified furniture items.
     */
    private int suitableCost(String[] ids){
        int cost = 0;
        for(int i = 0; i < furniture.size(); i++){
            for(int j = 0; j < ids.length; j++){
                if(furniture.get(i).getItemId().equals(ids[j])){
                    cost += furniture.get(i).getCost();
                }
            }
        }
        return cost;
    }

    /**
     * Converts an ArrayList of strings to a standard string array.
     * @param s ArrayList of strings to convert
     * @return corresponding string array.
     */
    private String[] convertListToStringArr(ArrayList<String> s){
        String[] array = new String[s.size()];
        for(int i = 0; i < s.size();i++){
            array[i] = s.get(i);
        }
        return array;
    }

    /**
     * Retrieves the current list of InventoryItem objects.
     * @return ArrayList of InventoryItem instances.
     */
    public ArrayList<InventoryItem> getItemsArr(){
        return this.furniture;
    }

    /**
     * Determines if a specific string is contained within a string array.
     * @param array array to search
     * @param toCheck string to find
     * @return true if the string is present, otherwise false.
     */
    private boolean containsId(String[] array, String toCheck){
        for(int i =0; i < array.length; i++){
            if(array[i].equals(toCheck)){
                return true;
            }
        }
        return false;
    }

    /**
     * Additional verification step to refine cost calculations based on an offset.
     * @param increase offset to adjust index calculations.
     */
    private void refineCostCalculations(int increase){
        boolean[] b = generateAllArray(furniture.get(0).getComponentStatus().length);
        for(int i = 0; i <furniture.size();i++){
            String[] idCombo = {furniture.get(i).getItemId()};
            boolean[] parts = furniture.get(i).getComponentStatus();
            ArrayList<boolean[]> partsUsed = new ArrayList<boolean[]>();
            partsUsed.add(parts);
            for(int j=i+increase; j<furniture.size();j++){
                if(i+increase < furniture.size()){
                    if(!areArraysEquivalent(parts, furniture.get(j).getComponentStatus()) && 
                    !areArraysEquivalent(parts, b)&& !containsId(idCombo, furniture.get(j).getItemId())){
                        parts = mergeAvailabilityStatus(parts, furniture.get(j).getComponentStatus());
                        idCombo = appendArray(idCombo, furniture.get(j).getItemId());
                        partsUsed.add(furniture.get(j).getComponentStatus());
                    }
                }
            }
            if(areArraysEquivalent(parts, b)){
                 priceChoices.add(suitableCost(idCombo));
                 idForPriceChoices.add(idCombo);
                 piecesForChoices.add(partsUsed);
            }
        }
    }
}
