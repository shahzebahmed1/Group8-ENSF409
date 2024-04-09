package edu.ucalgary.oop;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import org.junit.Test;

/**
 * Unit testing for the InventoryItem class.
 * @version 1.3
 * @since 1.0
 * @author Dainielle Puno, Praveen De Silva, Salma Ineflas, Shahzeb Ahmed
 */

public class InventoryItemTest {

    
    @Test
    public void testGetterForItemId() {
        boolean[] componentStatus = {true, false, true, false};
        InventoryItem item = new InventoryItem("mesh", "D025", 25, componentStatus);
        assertEquals("D025", item.getItemId());
    }

    /**
     * Tests getter for item cost.
     */
    @Test
    public void testGetterForCost() {
        boolean[] componentStatus = {true, false, true, false};
        InventoryItem item = new InventoryItem("mesh", "D025", 25, componentStatus);
        assertEquals(25, item.getCost());
    }

    /**
     * Tests getter for component status.
     */
    @Test
    public void testGetterForComponentStatus() {
        boolean[] componentStatus = {true, false, true, false};
        InventoryItem item = new InventoryItem("mesh", "D025", 25, componentStatus);
        assertArrayEquals(componentStatus, item.getComponentStatus());
    }

    /**
     * Tests getter for category.
     */
    @Test
    public void testGetterForCategory() {
        boolean[] componentStatus = {true, false, true, false};
        InventoryItem item = new InventoryItem("mesh", "D025", 25, componentStatus);
        assertEquals("mesh", item.getCategory());
    }
}
