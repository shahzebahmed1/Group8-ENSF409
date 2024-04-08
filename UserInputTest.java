package edu.ucalgary.oop;

/**
 * @author Dainielle Puno <a href = "mailto:dainielle.puno@ucalgary.ca">
 * 		dainielle.puno@ucalgary.ca</a>
 * @author Praveen De Silva <a href = "mailto:weeraddana.desilva@ucalgary.ca">
 * 		weeraddana.desilva@ucalgary.ca
 * @author Salma Ineflas <a href = "mailto:salma.ineflas1@ucalgary.ca">
 * 		salma.ineflas1@ucalgary.ca</a>
 * @author Shahzeb Ahmed <a href = "mailto:shahzeb.ahmed1@ucalgary.ca">
 * 		shahzeb.ahmed1@ucalgary.ca</a>
 * This class manages user interacions for ordering furni
 * @version 1.0
 * @since 1.0
 */

 
/*  This class is for testing the UserInput class.
 *  It ensures that the validation methods work correctly for various inputs.
 */

import org.junit.Test;
import static org.junit.Assert.*;

public class UserInputTest {

    /**
     * Tests if the validateCategory method correctly identifies valid and invalid categories.
     */
    @Test
    public void testValidateCategory() {
        assertTrue("Category 'chair' should be valid", UserInput.validateCategory("chair"));
        assertTrue("Category 'desk' should be valid", UserInput.validateCategory("desk"));
        assertFalse("Category 'sofa' should not be valid", UserInput.validateCategory("sofa")); 
    }

    /**
     * Tests if the validateType method correctly identifies valid and invalid types for a given category.
     */
    @Test
    public void testValidateType() {
        assertTrue("Type 'Mesh' should be valid for 'chair'", UserInput.validateType("chair", "Mesh"));
        assertFalse("Type 'stool' should not be valid for 'chair'", UserInput.validateType("chair", "stool")); 
    }

    /**
     * Tests if the validateNumber method correctly identifies valid and invalid numerical inputs.
     */
    @Test
    public void testValidateNumber() {
        assertTrue("Valid positive number '3' should return true", UserInput.validateNumber("3"));
        assertFalse("Negative number '-1' should not be valid", UserInput.validateNumber("-1"));
        assertFalse("Non-numeric string 'abc' should not be valid", UserInput.validateNumber("abc"));
    }

    /**
     * Tests if the capitalize method correctly capitalizes each word in a string.
     */
    @Test
    public void testCapitalize() {
        assertEquals("Capitalize 'hello world' should return 'Hello World'", "Hello World", UserInput.capitalize("hello world"));
        assertEquals("Capitalize empty string should return empty string", "", UserInput.capitalize("")); 
    }

    /**
     * Tests if the validateType method handles null and empty string inputs correctly for a given category.
     */
    @Test
    public void testValidateTypeWithNullOrEmpty() {
        assertFalse("Null type should not be valid for 'chair'", UserInput.validateType("chair", null));
        assertFalse("Empty string type should not be valid for 'chair'", UserInput.validateType("chair", ""));
    }
}
