package edu.ucalgary.oop;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.*;
import java.sql.*;

/**
 * @author Dainielle Puno <a href="mailto:dainielle.puno@ucalgary.ca">
 * dainielle.puno@ucalgary.ca</a>
 * @author Praveen De Silva <a href="mailto:weeraddana.desilva@ucalgary.ca">
 * weeraddana.desilva@ucalgary.ca
 * @author Salma Ineflas <a href="mailto:salma.ineflas1@ucalgary.ca">
 * salma.ineflas1@ucalgary.ca</a>
 * @author Shahzeb Ahmed <a href="mailto:shahzeb.ahmed1@ucalgary.ca">
 * shahzeb.ahmed1@ucalgary.ca</a>
 * Unit testing for the PriceOptimizer class to see if the best available options are being chosen.
 * @version 1.5
 * @since 1.0
 */
// NOTE: This test assumes that no modifications has been made to the inventory.sql provided

public class PriceOptimizerTest {
  private final String DBURL = "jdbc:postgresql://localhost/inventory";
  private final String USERNAME = "oop";
  private final String PASSWORD = "ucalgary";
  private Connection dbConnect;
  private ResultSet results;

  /**
   * Initializes the database connection.
   */
  @Before
  public void setup(){
    try{
      dbConnect = DriverManager.getConnection(DBURL, USERNAME, PASSWORD);
    } catch(SQLException e){
      e.printStackTrace();
    }
  }

  // Testing Desks
  /**
   * Tests the correct cost calculation for a mesh chair.
   */
  @Test
  public void testMeshChairPriceCalculation(){
    String expected = "200";
    PriceOptimizer calculator = new PriceOptimizer();
    try{
      Statement myStmt = dbConnect.createStatement();
      results = myStmt.executeQuery("SELECT * FROM chair WHERE Type = 'Mesh'");
      String[] result = calculator.priceCalc(results, "Mesh");
      myStmt.close();
      assertEquals(expected,result[0]);
    } catch(SQLException e){
      e.printStackTrace();
    }
  }

  /**
   * Tests the correct cost calculation for a task chair.
   */
  @Test
  public void testTaskChairPriceCalculation(){
    String expected = "150";
    PriceOptimizer calculator = new PriceOptimizer();
    try{
      Statement myStmt = dbConnect.createStatement();
      results = myStmt.executeQuery("SELECT * FROM chair WHERE Type = 'Task'");
      String[] result = calculator.priceCalc(results, "Task");
      myStmt.close();
      assertEquals(expected,result[0]);
    } catch(SQLException e){
      e.printStackTrace();
    }
  }

  /**
   * Tests the correct cost calculation for a kneeling chair, which should be 0.
   */
  @Test
  public void testKneelingChairPriceCalculation(){
    String expected = "0";
    PriceOptimizer calculator = new PriceOptimizer();
    try{
      Statement myStmt = dbConnect.createStatement();
      results = myStmt.executeQuery("SELECT * FROM chair WHERE Type = 'Kneeling'");
      String[] result = calculator.priceCalc(results, "Kneeling");
      myStmt.close();
      assertEquals(expected,result[0]);
    } catch(SQLException e){
      e.printStackTrace();
    }
  }

  /**
   * Tests the correct cost calculation for an executive chair.
   */
  @Test
  public void testExecutiveChairPriceCalculation(){
    String expected = "400";
    PriceOptimizer calculator = new PriceOptimizer();
    try{
      Statement myStmt = dbConnect.createStatement();
      results = myStmt.executeQuery("SELECT * FROM chair WHERE Type = 'Executive'");
      String[] result = calculator.priceCalc(results, "Executive");
      myStmt.close();
      assertEquals(expected,result[0]);
    } catch(SQLException e){
      e.printStackTrace();
    }
  }

  /**
   * Tests the correct cost calculation for an ergonomic desk.
   */
  @Test
  public void testErgonomicDeskPriceCalculation(){
    String expected = "250";
    PriceOptimizer calculator = new PriceOptimizer();
    try{
      Statement myStmt = dbConnect.createStatement();
      results = myStmt.executeQuery("SELECT * FROM chair WHERE Type = 'Ergonomic'");
      String[] result = calculator.priceCalc(results, "Ergonomic");
      myStmt.close();
      assertEquals(expected,result[0]);
    } catch(SQLException e){
      e.printStackTrace();
    }
  }
  // Testing Desks
  /**
   * Tests the correct cost calculation for a traditional desk.
   */
  @Test
  public void testTraditionalDeskPriceCalculation(){
    String expected = "100";
    PriceOptimizer calculator = new PriceOptimizer();
    try{
      Statement myStmt = dbConnect.createStatement();
      results = myStmt.executeQuery("SELECT * FROM desk WHERE Type = 'Traditional'");
      String[] result = calculator.priceCalc(results, "Traditional");
      myStmt.close();
      assertEquals(expected,result[0]);
    } catch(SQLException e){
      e.printStackTrace();
    }
  }

  /**
   * Tests the correct cost calculation for an adjustable desk.
   */
  @Test
  public void testAdjustableDeskPriceCalculation(){
    String expected = "400";
    PriceOptimizer calculator = new PriceOptimizer();
    try{
      Statement myStmt = dbConnect.createStatement();
      results = myStmt.executeQuery("SELECT * FROM desk WHERE Type = 'Adjustable'");
      String[] result = calculator.priceCalc(results, "Adjustable");
      myStmt.close();
      assertEquals(expected,result[0]);
    } catch(SQLException e){
      e.printStackTrace();
    }
  }

  /**
   * Tests the correct cost calculation for an ergonomic desk.
   */
  @Test
  public void testStandingDeskPriceCalculation(){
    String expected = "300";
    PriceOptimizer calculator = new PriceOptimizer();
    try{
      Statement myStmt = dbConnect.createStatement();
      results = myStmt.executeQuery("SELECT * FROM desk WHERE Type = 'Standing'");
      String[] result = calculator.priceCalc(results, "Standing");
      myStmt.close();
      assertEquals(expected,result[0]);
    } catch(SQLException e){
      e.printStackTrace();
    }
  }

  // Testing filing category
  /**
   * Tests the correct cost calculation for a small filing cabinet.
   */
  @Test
  public void testSmallFilingCabinetPriceCalculation(){
    String expected = "100";
    PriceOptimizer calculator = new PriceOptimizer();
    try{
      Statement myStmt = dbConnect.createStatement();
      results = myStmt.executeQuery("SELECT * FROM filing WHERE Type = 'Small'");
      String[] result = calculator.priceCalc(results, "Small");
      myStmt.close();
      assertEquals(expected,result[0]);
    } catch(SQLException e){
      e.printStackTrace();
    }
  }

  /**
   * Tests the correct cost calculation for a medium filing cabinet.
   */
  @Test
  public void testMediumFilingCabinetPriceCalculation(){
    String expected = "200";
    PriceOptimizer calculator = new PriceOptimizer();
    try{
      Statement myStmt = dbConnect.createStatement();
      results = myStmt.executeQuery("SELECT * FROM filing WHERE Type = 'Medium'");
      String[] result = calculator.priceCalc(results, "Medium");
      myStmt.close();
      assertEquals(expected,result[0]);
    } catch(SQLException e){
      e.printStackTrace();
    }
  }

  /**
   * Tests the correct cost calculation for a large filing cabinet.
   */
  @Test
  public void testLargeFilingCabinetPriceCalculation(){
    String expected = "300";
    PriceOptimizer calculator = new PriceOptimizer();
    try{
      Statement myStmt = dbConnect.createStatement();
      results = myStmt.executeQuery("SELECT * FROM filing WHERE Type = 'Large'");
      String[] result = calculator.priceCalc(results, "Large");
      myStmt.close();
      assertEquals(expected,result[0]);
    } catch(SQLException e){
      e.printStackTrace();
    }
  }

  // Testing Lamp category
  /**
   * Tests the correct cost calculation for a desk lamp.
   */
  @Test
  public void testDeskLampPriceCalculation(){
    String expected = "20";
    PriceOptimizer calculator = new PriceOptimizer();
    try{
      Statement myStmt = dbConnect.createStatement();
      results = myStmt.executeQuery("SELECT * FROM lamp WHERE Type = 'Desk'");
      String[] result = calculator.priceCalc(results, "Desk");
      myStmt.close();
      assertEquals(expected,result[0]);
    } catch(SQLException e){
      e.printStackTrace();
    }
  }

  /**
   * Tests the correct cost calculation for a swing arm lamp.
   */
  @Test
  public void testSwingArmLampPriceCalculation(){
    String expected = "30";
    PriceOptimizer calculator = new PriceOptimizer();
    try{
      Statement myStmt = dbConnect.createStatement();
      results = myStmt.executeQuery("SELECT * FROM lamp WHERE Type = 'Swing Arm'");
      String[] result = calculator.priceCalc(results, "Swing Arm");
      myStmt.close();
      assertEquals(expected,result[0]);
    } catch(SQLException e){
      e.printStackTrace();
    }
  }

  /**
   * Tests the correct cost calculation for a study lamp.
   */
  @Test
  public void testStudyLampPriceCalculation(){
    String expected = "10";
    PriceOptimizer calculator = new PriceOptimizer();
    try{
      Statement myStmt = dbConnect.createStatement();
      results = myStmt.executeQuery("SELECT * FROM lamp WHERE Type = 'Study'");
      String[] result = calculator.priceCalc(results, "Study");
      myStmt.close();
      assertEquals(expected,result[0]);
    } catch(SQLException e){
      e.printStackTrace();
    }
  }
}
