package edu.pdx.cs410J.jushuck;


import org.junit.Test;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertTrue;
import edu.pdx.cs410J.InvokeMainTestCase;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * Tests the functionality in the {@link Project1} main class.
 */
public class Project1Test extends InvokeMainTestCase {

    /**
     * Invokes the main method of {@link Project1} with the given arguments.
     */
    private MainMethodResult invokeMain(String... args) {
        return invokeMain( Project1.class, args );
    }

  /**
   * Tests that invoking the main method with no arguments issues an error
   */
  @Test
  public void testNoCommandLineArguments() {
    MainMethodResult result = invokeMain();
    assertEquals(new Integer(1), result.getExitCode());
    assertTrue(result.getErr().contains( "ERROR: Missing command line arguments" ));
  }

    /**
     * Test what happens when we don't enter enough arguments
     */
    @Test
    public void testInvalidNumberOfArguments() {
        String[] args = new String[]{"testName", "100", "POR", "1/2/2012 1:05", "SEA"};
        MainMethodResult result = invokeMain(args);
        assertEquals(new Integer(1), result.getExitCode());
        assertTrue(result.getErr().contains("ERROR: Missing command line arguments"));
    }

    /**
     * Test to make sure that we exit cleanly with valid input
     */
    @Test
    public void testValidCommandLineArguments() {
        String[] args = new String[]{"american", "787", "POR", "1/2/2012 1:05", "SEA", "01/04/2014 11:00"};
        MainMethodResult result = invokeMain(args);
        assertEquals(new Integer(0), result.getExitCode());
    }
    /**
     * Test what happenes when we have more than the maximum arguments (with both options the max would be 8)
     */
    @Test
    public void testMaximumArgs() {
        String[] args = new String[]{"-print", "american", "abc", "POR", "1/2/2012 1:05", "SEA", "01/04/2014 11:00", "8", "9"};
        MainMethodResult result = invokeMain(args);
        assertEquals(new Integer(1), result.getExitCode());
        assertTrue(result.getErr().contains("ERROR: Too many command line arguments"));
    }

    /**
     * Test for error with invalid airline number
     */
    @Test
    public void testInvalidAirlineNumber() {
        String[] args = new String[]{"american", "abc", "POR", "1/2/2012 1:05", "SEA", "01/04/2014 11:00"};
        MainMethodResult result = invokeMain(args);
        assertEquals(new Integer(1), result.getExitCode());
        assertTrue(result.getErr().contains("ERROR: Flight number must be an integer"));
    }

    /**
     * Test for error with invalid source and destination
     */
    @Test
    public void testInvalidSourceAndDestination() {
        String[] args = new String[]{"american", "787", "PORT", "1/2/2012 1:05", "SEAT", "01/04/2014 11:00"};
        MainMethodResult result = invokeMain(args);
        assertEquals(new Integer(1), result.getExitCode());
        assertTrue(result.getErr().contains("ERROR: Location must be a 3 character code"));
    }

    /**
     * Test for -README with no other parameters
     */
    @Test
    public void testReadmeOption() {
        String[] args = new String[]{"-README"};
        MainMethodResult result = invokeMain(args);
        assertEquals(new Integer(0), result.getExitCode());
        assertNotNull(result.getOut());
    }

    /**
     * Test for -README with other parameters(should have same behavior as if it was by itself)
     */
    @Test
    public void testReadmeOptionWithMultipleParameters() {
        String[] args = new String[]{"-README", "-print", "american", "787", "PORT", "1/2/2012 1:05", "SEAT", "01/04/2014 11:00"};
        MainMethodResult result = invokeMain(args);
        assertEquals(new Integer(0), result.getExitCode());
        assertNotNull(result.getOut());
    }

    /**
     * Test for invalid month in the time/date field for arrival and departure (High)
     */
    @Test
    public void testInvalidMonthHigh() {
        String[] args = new String[]{"-print", "american", "787", "POR", "13/2/2012 1:05", "SEA", "15/04/2014 11:00"};
        MainMethodResult result = invokeMain(args);
        assertEquals(new Integer(1), result.getExitCode());
        assertTrue(result.getErr().contains("ERROR: Invalid Date entered"));
    }

    /**
     * Test for invalid month in the time/date field for arrival and departure (Low)
     */
    @Test
    public void testInvalidMonthLow() {
        String[] args = new String[]{"-print", "american", "787", "POR", "0/2/2012 1:05", "SEA", "0/04/2014 11:00"};
        MainMethodResult result = invokeMain(args);
        assertEquals(new Integer(1), result.getExitCode());
        assertTrue(result.getErr().contains("ERROR: Invalid Date entered"));
    }

    /**
     * Test for invalid day in the time/date field for arrival and departure
     */
    @Test
    public void testInvalidDay() {
        String[] args = new String[]{"-print", "american", "787", "POR", "13/32/2012 1:05", "SEA", "15/044/2014 11:00"};
        MainMethodResult result = invokeMain(args);
        assertEquals(new Integer(1), result.getExitCode());
        assertTrue(result.getErr().contains("ERROR: Invalid Date entered"));
    }

    /**
     * Test for invalid year in the time/date field for arrival and departure
     */
    @Test
    public void testInvalidYear() {
        String[] args = new String[]{"-print", "american", "787", "POR", "13/2/20124 1:05", "SEA", "15/04/2014 11:00"};
        MainMethodResult result = invokeMain(args);
        assertEquals(new Integer(1), result.getExitCode());
        assertTrue(result.getErr().contains("ERROR: Invalid Date entered"));
    }

    /**
     * Test for invalid Hour in the time/date field for arrival and departure
     */
    @Test
    public void testInvalidHour() {
        String[] args = new String[]{"-print", "american", "787", "POR", "33/2/2012 13:05", "SEA", "35/04/2014 22:00"};
        MainMethodResult result = invokeMain(args);
        assertEquals(new Integer(1), result.getExitCode());
        assertTrue(result.getErr().contains("ERROR: Invalid Date entered"));
    }

    /**
     * Test for invalid Minute in the time/date field for arrival and departure (High)
     */
    @Test
    public void testInvalidMinute() {
        String[] args = new String[]{"-print", "american", "787", "POR", "13/2/2012 1:60", "SEA", "15/04/2014 11:70"};
        MainMethodResult result = invokeMain(args);
        assertEquals(new Integer(1), result.getExitCode());
        assertTrue(result.getErr().contains("ERROR: Invalid Date entered"));
    }

}