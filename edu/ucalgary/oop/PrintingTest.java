package edu.ucalgary.oop;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit testing for the Printing class.
 * @version 1.5
 * @since 1.0
 * @author Dainielle Puno, Praveen De Silva, Salma Ineflas, Shahzeb Ahmed
 */

public class PrintingTest {

    /**
     * Tests the output when the order cannot be fulfilled and manufacturers are suggested.
     */
    @Test
    public void testManufacturers() {
        String[] suggested = {"Office Furnishings", "Chairs R Us", "Furniture Goods", "Fine Office Supplies"};
        String expected = "Order cannot be fulfilled based on current inventory. Suggested manufacturers are Office Furnishings, Chairs R Us, Furniture Goods, Fine Office Supplies.";
        Printing p = new Printing("Kneeling chair, 1", null, suggested);
        String result = p.writeFile();
        assertEquals("The suggested manufacturers string does not match the expected output.", expected, result.trim());
    }

    /**
     * Tests the output for a successful order form printout.
     */
    @Test
    public void testOrderForm() {
        String[] ids = {"200", "C8138", "C9890"};
        String expected = "Inventory Order Form ->\n\n\nFaculty Name:\nContact:\nDate:\n\n\nOriginal Request: Mesh chair, 1\n\n\nItems Ordered:\nID: C8138\nID: C9890\n\n\nTotal Cost: $200";
        Printing p = new Printing("Mesh chair, 1", ids, null);
        String result = p.writeFile();
        assertEquals("The order form string does not match the expected output.", expected, result.trim());
    }

    /**
     * Tests the output when both itemIDs and suggestedManufacturers are provided but itemIDs is prioritized.
     */
    @Test
    public void testOrderFormWithManufacturersIgnored() {
        String[] ids = {"300", "C1234", "C5678"};
        String[] suggested = {"Manufacturer A", "Manufacturer B"};
        String expected = "Inventory Order Form ->\n\n\nFaculty Name:\nContact:\nDate:\n\n\nOriginal Request: Office Desk, 1\n\n\nItems Ordered:\nID: C1234\nID: C5678\n\n\nTotal Cost: $300";
        Printing p = new Printing("Office Desk, 1", ids, suggested);
        String result = p.writeFile();
        assertEquals("Items should be prioritized over manufacturers in output.", expected, result.trim());
    }

    /**
     * Tests the output when no items are available, and no manufacturers are suggested.
     */
    @Test
    public void testEmptyOrderAndManufacturers() {
        String expected = "Order cannot be fulfilled based on current inventory. Suggested manufacturers are .";
        Printing p = new Printing("Desk Lamp, 2", new String[]{}, new String[]{});
        String result = p.writeFile();
        assertEquals("Empty order and no manufacturers should result in a specific output.", expected, result.trim());
    }
}
