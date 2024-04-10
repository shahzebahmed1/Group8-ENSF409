package edu.ucalgary.oop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for the Database class.
 * 
 * @version 1.6
 * @since 1.0
 * @author Dainielle Puno, Praveen De Silva, Salma Ineflas, Shahzeb Ahmed
 */
public class DatabaseTest {

    private Connection dbConnection;

    /**
     * Setup method to initialize database connection before each test.
     */
    @Before
    public void setUp() {
        try {
            dbConnection = DriverManager.getConnection("jdbc:mysql://localhost/inventory", "username", "password");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Tear down method to close database connection after each test.
     */
    @After
    public void tearDown() {
        try {
            if (dbConnection != null) {
                dbConnection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test the initialization of the database connection.
     * 
     * This test ensures that the initializeConnection method correctly establishes a
     * connection to the database.
     */
    @Test
    public void testInitializeConnection() {
        // Create a new Database object
        Database db = new Database("jdbc:mysql://localhost/inventory", "username", "password");

        // Initialize the connection
        db.initializeConnection();

        // Check if the connection is not null
        assertNotNull("Connection is null", db.getDbConnection());
    }

    /**
     * Test the closing of the database connection.
     * 
     * This test ensures that the close method correctly closes the database connection.
     */
    @Test
    public void testCloseConnection() {
        // Create a new Database object
        Database db = new Database("jdbc:mysql://localhost/inventory", "username", "password");

        // Initialize the connection
        db.initializeConnection();

        // Close the connection
        db.close();

        // Check if the connection is closed
        try {
            assertEquals(true, db.getDbConnection().isClosed());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
