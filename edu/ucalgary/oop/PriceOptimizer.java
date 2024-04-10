package edu.ucalgary.oop;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import edu.ucalgary.oop.InventoryItem;

public class PriceOptimizer {
    private ArrayList<Integer> costOptions;
    private ArrayList<InventoryItem> furniture;
    private ArrayList<String[]> idsForEachCostOption;
    private ArrayList<ArrayList<boolean[]>> piecesPerOption;

    public PriceOptimizer(){
        this.costOptions = new ArrayList<Integer>();
        this.furniture = new ArrayList<InventoryItem>();
        this.idsForEachCostOption = new ArrayList<String[]>();
        this.piecesPerOption = new ArrayList<ArrayList<boolean[]>>();
    }


    public int choseBestPrice(){
        //add option that if # of costOptions is 0 it'll return 0
        if(costOptions.size() == 0){
            return 0;
        }
        int bestPrice = this.costOptions.get(0);
        for(int i = 0; i < this.costOptions.size();i++){
            if(this.costOptions.get(i)<bestPrice){
                bestPrice=this.costOptions.get(i);
            }
        }
        return bestPrice;
    }

    public String[] calculatePrices(ResultSet results, String furnitureType) throws SQLException{
        String[] ids = {"0"};
        try{
            while(results.next()){
                if(checkInventoryItemType(furnitureType, results.getString("Type"))){
                    ArrayList<String> s = new ArrayList<String>();
                    ResultSetMetaData rsmd = results.getMetaData();
                    for(int i = 1; i < rsmd.getColumnCount()+1; i++){
                        if(results.getString(i).equals("Y") || results.getString(i).equals("N")){
                            s.add(results.getString(i));
                        }
                    }
                    boolean[] availability = makeBooleanArray(toStringArray(s));
                    furniture.add(new InventoryItem(results.getString("Type"),results.getString("itemId"),results.getInt("Price"), availability));
                }
            }
            
            String[] temp = createOptions();
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

    private boolean checkInventoryItemType(String wantedInventoryItem, String furnitureToCheck){
        if(wantedInventoryItem.equals(furnitureToCheck)){
            return true;
        }
        else{
            return false;
        }
    }

    private boolean convertPieceAvalabilityToBoolean(String available){
        if(available.equals("Y")){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean[] makeBooleanArray(String[] booleans){
        boolean[] b = new boolean[booleans.length];
        for(int i = 0; i < b.length; i++){
            b[i] = convertPieceAvalabilityToBoolean(booleans[i]);
        }
        return b;
    }


    public String[] createOptions(){
        //need to add print statements to figure out whats gone wrong
        if(furniture.size()==0){
            String[] error = {"0"};
           return error;
        }
        boolean[] b = createAllTrueArray(furniture.get(0).getComponentStatus().length);
       for(int i = 0; i <furniture.size();i++){
           String[] idCombo = {furniture.get(i).getItemId()};
           boolean[] parts = furniture.get(i).getComponentStatus();
           ArrayList<boolean[]> partsUsed = new ArrayList<boolean[]>();
           partsUsed.add(parts);
           for(int j=0; j<furniture.size();j++){
                if(!checkArrayEquivalency(parts, furniture.get(j).getComponentStatus()) && 
                !checkArrayEquivalency(parts, b)&& !isInArray(idCombo, furniture.get(j).getItemId())){
                    parts = addToBooleanArray(parts, furniture.get(j).getComponentStatus());
                    idCombo = addToStringArray(idCombo, furniture.get(j).getItemId());
                    partsUsed.add(furniture.get(j).getComponentStatus());
                }
           }
           if(checkArrayEquivalency(parts, b)){
                costOptions.add(calculateCostFromIds(idCombo));
                idsForEachCostOption.add(idCombo);
                piecesPerOption.add(partsUsed);
           }
       }
       for(int i = 0; i < furniture.size(); i++){
         secondCheck(i);
       }
       
       int cost = choseBestPrice();
       if(cost == 0){
           String[] error = {"0"};
           return error;
       }
       String[] idForSelected = new String[idsForEachCostOption.get(costOptions.indexOf(cost)).length+1];
       idForSelected[0] = String.valueOf(cost);
       for(int i = 1; i <idsForEachCostOption.get(costOptions.indexOf(cost)).length+1;i++){
        idForSelected[i] = idsForEachCostOption.get(costOptions.indexOf(cost))[i-1];
       }
       return idForSelected;
       }


    /**
     * adds a String to a String array
     * @param array array to be added to
     * @param toAdd String to add to array
     * @return String array with the added String
     */
    private String[] addToStringArray(String[] array, String toAdd){
        String[] results = new String[array.length+1];
        for(int i = 0; i < array.length; i++){
            results[i] = array[i];
        }
        results[array.length] = toAdd;
        return results;
    }

    private boolean[] addToBooleanArray(boolean[] array, boolean[] toAdd){
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

    private boolean checkArrayEquivalency(boolean[] one, boolean[] two){
        for(int i =0; i <one.length; i++){
            if(one[i] != two[i]){
                return false;
            }
        }
        return true;
    }

    private boolean[] createAllTrueArray(int size){
      boolean[] a = new boolean[size];
      for(int i = 0; i <size; i++){
          a[i] = true;
      }
      return a;
    }

    private int calculateCostFromIds(String[] ids){
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

    private String[] toStringArray(ArrayList<String> s){
        String[] array = new String[s.size()];
        for(int i = 0; i < s.size();i++){
            array[i] = s.get(i);
        }
        return array;
    }

    public ArrayList<InventoryItem> getInventoryItemArray(){
        return this.furniture;
    }

    private boolean isInArray(String[] array, String toCheck){
        for(int i =0; i < array.length; i++){
            if(array[i].equals(toCheck)){
                return true;
            }
        }
        return false;
    }

    private void secondCheck(int increase){
        boolean[] b = createAllTrueArray(furniture.get(0).getComponentStatus().length);
        for(int i = 0; i <furniture.size();i++){
            String[] idCombo = {furniture.get(i).getItemId()};
            boolean[] parts = furniture.get(i).getComponentStatus();
            ArrayList<boolean[]> partsUsed = new ArrayList<boolean[]>();
            partsUsed.add(parts);
            for(int j=i+increase; j<furniture.size();j++){
                if(i+increase < furniture.size()){
                    if(!checkArrayEquivalency(parts, furniture.get(j).getComponentStatus()) && 
                    !checkArrayEquivalency(parts, b)&& !isInArray(idCombo, furniture.get(j).getItemId())){
                        parts = addToBooleanArray(parts, furniture.get(j).getComponentStatus());
                        idCombo = addToStringArray(idCombo, furniture.get(j).getItemId());
                        partsUsed.add(furniture.get(j).getComponentStatus());
                    }
                }
            }
            if(checkArrayEquivalency(parts, b)){
                 costOptions.add(calculateCostFromIds(idCombo));
                 idsForEachCostOption.add(idCombo);
                 piecesPerOption.add(partsUsed);
            }
        }
    }
}