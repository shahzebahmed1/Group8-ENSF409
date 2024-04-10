package edu.ucalgary.oop;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * This class implements the SQL database operations.
 * @version 1.2
 * @since 1.0
 * @author Dainielle Puno, Praveen DE SILVA, Salma Ineflas, Shahzeb Ahmed
 */
public class Database {

    private String dbUrl;
    private String username;
    private String password;
    private String[] manuID;
    private Connection dbConnection;
    private ResultSet results;
    private PriceOptimizer calculator;
    private String[] IDArray = new String[50];

    /**
     * Constructs a Database object with the specified URL, username, and password.
     * @param url The URL of the MySQL database.
     * @param user The username for the database connection.
     * @param password The password for the database connection.
     */
    public Database(String url, String user, String password) {
        this.dbUrl = url;
        this.username = user;
        this.password = password;
    }

    public String getDbUrl() {
        return this.dbUrl;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    /**
     * Initializes the connection to the MySQL database using the specified URL, username, and password.
     */
    public void initializeConnection() {
        try {
            dbConnection = DriverManager.getConnection(this.dbUrl, this.username, this.password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Finds inventory items from a specific table in the database based on the type and number requested.
     * @param table The category from which the inventory item is required.
     * @param type The type of inventory item that is required.
     * @param number The number of inventory items that are required.
     * @return An array of IDs or the manufacturers' names.
     */
    public String[] findInventoryItem(String table, String type, String number) {

        int numberOfItems = Integer.parseInt(number);
        IDArray[0] = "0";
        int j = 1;

        try {
            while (numberOfItems > 0) {
                this.calculator = new PriceOptimizer();
                Statement myStmt = dbConnection.createStatement();
                results = myStmt.executeQuery("SELECT * FROM " + table);

                String[] arrayTemp = calculator.priceCalc(results, type);
                if (arrayTemp.length == 1) {
                    manuID = findManuID(table);
                    return manuID;
                } else {
                    for (int i = 1; i < arrayTemp.length; i++) {
                        deleteInventoryItem(table, arrayTemp[i]);
                    }
                }
                if (numberOfItems > 0) {
                    int cost = Integer.parseInt(IDArray[0]);
                    int costFromTemp = Integer.parseInt(arrayTemp[0]);
                    IDArray[0] = String.valueOf(cost + costFromTemp);
                    for (int i = 1; i < arrayTemp.length; i++) {
                        IDArray[j] = arrayTemp[i];
                        j++;
                    }

                }
                myStmt.close();
                numberOfItems--;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return IDArray;
    }

    /**
     * Finds suggested manufacturers' names if the inventory items are not available in the inventory.
     * @param table The category from which the suggested manufacturer needs to be selected.
     * @return An array of manufacturers' names.
     */
    public String[] findManuID(String table) {
        if (table.equalsIgnoreCase("chair")) {
            manuID = new String[]{"Office Furnishings", "Chairs R Us", "Furniture Goods", "Fine Office Supplies"};
        } else if (table.equalsIgnoreCase("desk")) {
            manuID = new String[]{"Academic Desks", "Office Furnishings", "Furniture Goods", "Fine Office Supplies"};
        } else if (table.equalsIgnoreCase("lamp") || table.equalsIgnoreCase("filing")) {
            manuID = new String[]{"Office Furnishings", "Furniture Goods", "Fine Office Supplies"};
        }
        return manuID;
    }

    /**
     * Deletes the details of the specified inventory item from the specified table in the database.
     * @param table The category from which the inventory item needs to be deleted.
     * @param id The ID of the inventory item that needs to be deleted.
     */
    public void deleteInventoryItem(String table, String id) {
        try {
            String query = "DELETE FROM " + table + " WHERE ID = ?";
            PreparedStatement myStmt = dbConnection.prepareStatement(query);
            myStmt.setString(1, id);
            myStmt.executeUpdate();
            myStmt.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Closes the ResultSet and the database connection.
     */
    public void close() {
        try {
            results.close();
            dbConnection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getDbConnection() {
        return this.dbConnection;
    }
}
