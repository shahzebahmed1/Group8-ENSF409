package edu.ucalgary.oop;

import org.junit.*;
import static org.junit.Assert.*;

import java.io.File;

public class UserInterfaceTest {
    /**
     * Tests if the validateCategory method correctly identifies valid and invalid categories.
     */
    @Test
    public void testValidateCategory() {
        assertTrue("Category 'chair' should be valid", UserInterface.validateCategory("chair"));
        assertTrue("Category 'desk' should be valid", UserInterface.validateCategory("desk"));
        assertFalse("Category 'sofa' should not be valid", UserInterface.validateCategory("sofa")); 
    }

    /**
     * Tests if the validateType method correctly identifies valid and invalid types for a given category.
     */
    @Test
    public void testValidateType() {
        assertTrue("Type 'Mesh' should be valid for 'chair'", UserInterface.validateType("chair", "Mesh"));
        assertFalse("Type 'stool' should not be valid for 'chair'", UserInterface.validateType("chair", "stool")); 
    }

    /**
     * Tests if the validateNumber method correctly identifies valid and invalid numerical inputs.
     */
    @Test
    public void testValidateNumber() {
        assertTrue("Valid positive number '3' should return true", UserInterface.validateNumber("3"));
        assertFalse("Negative number '-1' should not be valid", UserInterface.validateNumber("-1"));
        assertFalse("Non-numeric string 'abc' should not be valid", UserInterface.validateNumber("abc"));
    }

    /**
     * Tests if the capitalize method correctly capitalizes each word in a string.
     */
    @Test
    public void testCapitalize() {
        assertEquals("Capitalize 'hello world' should return 'Hello World'", "Hello World", UserInterface.capitalize("hello world"));
        assertEquals("Capitalize empty string should return empty string", "", UserInterface.capitalize("")); 
    }

    /**
     * Tests if the validateType method handles null and empty string inputs correctly for a given category.
     */
    @Test
    public void testValidateTypeWithNullOrEmpty() {
        assertFalse("Null type should not be valid for 'chair'", UserInterface.validateType("chair", null));
        assertFalse("Empty string type should not be valid for 'chair'", UserInterface.validateType("chair", ""));
    }

    /**
     * Tests the writeFile method to ensure it correctly generates the inventory order form with item IDs.
     * This test also checks for file creation.
     */
    @Test
    public void testWriteFile() {
        UserInterface input = new UserInterface("Chair", "Mesh", "5");
        input.setInventoryItemIDs(new String[]{"1000", "123", "456"});
        input.setRequestDetails("Chair Mesh, 5");
        String output = input.writeFile();
        assertTrue(output.contains("Inventory Order Form"));
        assertTrue(output.contains("Original Request: Chair Mesh, 5"));
        assertTrue(output.contains("ID: 123"));
        assertTrue(output.contains("ID: 456"));
        assertTrue(output.contains("Total Cost: $1000"));
        File file = new File("output.txt");
        assertTrue(file.exists());
        file.delete();
    }
    /**
     * Tests the writeFile method with suggested manufacturers to verify output when the order cannot be fulfilled.
     * Also checks for file creation.
     */
    @Test
    public void testWriteFileWithManufacturers() {
        UserInterface input = new UserInterface("Chair", "Mesh", "5");
        input.setSuggestedManufacturers(new String[]{"Manufacturer A", "Manufacturer B"});
        input.setRequestDetails("Chair Mesh, 5");
        String output = input.writeFile();
        assertTrue(output.contains("Order cannot be fulfilled based on current inventory."));
        assertTrue(output.contains("Suggested manufacturers are Manufacturer A, Manufacturer B."));
        File file = new File("output.txt");
        assertTrue(file.exists());
        file.delete();
    }

    /**
     * Tests the writeFile method with suggested manufacturers provided, ensuring correct message when order cannot be fulfilled.
     */

    @Test
    public void testManufacturers() {
        String[] suggested = {"Office Furnishings", "Chairs R Us", "Furniture Goods", "Fine Office Supplies"};
        String expected = "Order cannot be fulfilled based on current inventory. Suggested manufacturers are Office Furnishings, Chairs R Us, Furniture Goods, Fine Office Supplies.";

        UserInterface ui = new UserInterface("chair", "Kneeling", "1");
        ui.setSuggestedManufacturers(suggested);
        ui.setRequestDetails("Kneeling chair, 1");

        String result = ui.writeFile();
        assertEquals(expected, result.trim());
    }

    /**
     * Tests the writeFile method for a successful order form, verifying the correct output format with item IDs and cost.
     */
    @Test
    public void testOrderForm() {
        String[] ids = {"200", "C8138", "C9890"};
        String expected = "Inventory Order Form ->\n\n\nFaculty Name:\nContact:\nDate:\n\n\nOriginal Request: Mesh chair, 1\n\n\nItems Ordered:\nID: C8138\nID: C9890\n\n\nTotal Cost: $200";

        UserInterface ui = new UserInterface("chair", "Mesh", "1");
        ui.setInventoryItemIDs(ids);
        ui.setRequestDetails("Mesh chair, 1");

        String result = ui.writeFile();
        assertEquals(expected, result.trim());
    }

    /**
     * Tests the writeFile method when both inventory item IDs and manufacturer suggestions are provided, 
     * ensuring that item IDs are prioritized in the output.
     */
    @Test
    public void testOrderFormWithManufacturersIgnored() {
        String[] ids = {"300", "C1234", "C5678"};
        String[] suggested = {"Manufacturer A", "Manufacturer B"};
        String expected = "Inventory Order Form ->\n\n\nFaculty Name:\nContact:\nDate:\n\n\nOriginal Request: Office Desk, 1\n\n\nItems Ordered:\nID: C1234\nID: C5678\n\n\nTotal Cost: $300";

        UserInterface ui = new UserInterface("desk", "Office", "1");
        ui.setInventoryItemIDs(ids);
        ui.setSuggestedManufacturers(suggested);
        ui.setRequestDetails("Office Desk, 1");

        String result = ui.writeFile();
        assertEquals(expected, result.trim());
    }

    /**
     * Tests the writeFile method when no inventory items or manufacturer suggestions are available, 
     * ensuring the output reflects these conditions.
     */
    @Test
    public void testEmptyOrderAndManufacturers() {
        String expected = "Order cannot be fulfilled based on current inventory. Suggested manufacturers are .";
        UserInterface ui = new UserInterface("lamp", "Desk", "2");
        ui.setInventoryItemIDs(new String[]{});
        ui.setSuggestedManufacturers(new String[]{});
        ui.setRequestDetails("Desk Lamp, 2");

        String result = ui.writeFile();
        assertEquals(expected, result.trim());
    }
}