package edu.pdx.cs410J.jushuck;


import edu.pdx.cs410J.InvokeMainTestCase;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests the functionality in the {@link Project3} main class.
 */
public class Project3Test extends InvokeMainTestCase {

    /**
     * Invokes the main method of {@link Project3} with the given arguments.
     */
    private MainMethodResult invokeMain(String... args) {
        return invokeMain( Project3.class, args );
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
        String[] args = new String[]{"testName", "100", "PDX", "1/2/2012", "1:05", "SEA"};
        MainMethodResult result = invokeMain(args);
        assertEquals(new Integer(1), result.getExitCode());
        assertTrue(result.getErr().contains("ERROR: Missing command line arguments"));
    }

    /**
     * Test to make sure that we exit cleanly with valid input
     */
    @Test
    public void testValidCommandLineArguments() {
        String[] args = new String[]{"american", "787", "PDX", "1/2/2012", "1:05", "AM", "SEA", "01/04/2014", "11:00", "PM"};
        MainMethodResult result = invokeMain(args);
        assertEquals(new Integer(0), result.getExitCode());
    }
    /**
     * Test what happenes when we have more than the maximum arguments
     */
    @Test
    public void testMaximumArgs() {
        String[] args = new String[]{"-print", "-textFile", "hi", "american", "abc", "PDX", "1/2/2012", "1:05", "AM", "SEA", "01/04/2014", "11:00", "PM", "8", "9"};

        MainMethodResult result = invokeMain(args);
        assertEquals(new Integer(1), result.getExitCode());
        assertTrue(result.getErr().contains("ERROR: Unknown Command line Argument"));
    }

    /**
     * Test 7: Tests the max limit of arguments (CREATE NEW FILE)
     */
    @Test
    public void testCreateNewFile() {
        String[] args = new String[]{"-print", "-textFile", "tmp", "american", "123", "PDX", "1/2/2012", "1:05", "AM", "SEA", "01/04/2014", "11:00" ,"PM"};
        String[] args2 = new String[]{"-textFile", "tmp2", "american", "123", "PDX", "1/2/2012", "1:05", "AM", "SEA", "01/04/2014", "11:00","PM"};

        MainMethodResult result = invokeMain(args);
        assertEquals(new Integer(0), result.getExitCode());

        MainMethodResult result2= invokeMain(args2);
        assertEquals(new Integer(0), result2.getExitCode());
    }

    /**
     * Test 8: Tests the max limit of arguments (ADD TO EXISTING FILE)
     */
    @Test
    public void testAddToFile() {
        String[] args = new String[]{"-print", "-textFile", "tmp", "american", "1", "PDX", "1/2/2012", "1:05","PM", "SEA", "01/04/2014", "11:00", "AM"};
        String[] args2 = new String[]{"-textFile", "tmp2", "american", "1", "PDX", "1/2/2012", "1:05", "PM", "SEA", "01/04/2014", "11:00", "AM"};

        MainMethodResult result = invokeMain(args);
        assertEquals(new Integer(0), result.getExitCode());

        MainMethodResult result2= invokeMain(args2);
        assertEquals(new Integer(0), result2.getExitCode());
    }

    /**
     * Test 10: Tests Malformed text Doc
     */
    @Test
    public void testInvalidText() {

        String[] args = new String[]{"-print", "-textFile", "Error-Text", "american", "1", "PDX", "1/2/2012", "1:05","PM", "SEA", "01/04/2014", "11:00", "AM"};
        String[] args2 = new String[]{"-textFile", "Error-Text", "american", "1", "PDX", "1/2/2012", "1:05","PM", "SEA", "01/04/2014", "11:00","AM"};

        MainMethodResult result = invokeMain(args);
        assertEquals(new Integer(1), result.getExitCode());
        assertTrue(result.getErr().contains("ERROR: Malformed text Document"));

        MainMethodResult result2= invokeMain(args2);
        assertEquals(new Integer(1), result2.getExitCode());
        assertTrue(result.getErr().contains("ERROR: Malformed text Document"));
    }
    /**
     * Test for error with invalid airline number
     */
    @Test
    public void testInvalidAirlineNumber() {
        String[] args = new String[]{"american", "abc", "PDX", "1/2/2012", "1:05", "PM", "SEA", "01/04/2014","11:00", "AM"};
        String[] args2 = new String[] {"-print", "american", "abc", "PDX", "1/2/2012", "PM", "1:05", "SEA", "01/04/2014","11:00", "AM"};
        String[] args3 = new String[] {"-print", "-textFile", "tmp", "american", "abc", "PDX", "1/2/2012", "1:05", "AM", "SEA", "01/04/2014","11:00", "PM"};

        MainMethodResult result = invokeMain(args);
        MainMethodResult result2 = invokeMain(args2);
        MainMethodResult result3 = invokeMain(args3);

        assertEquals(new Integer(1), result.getExitCode());
        assertTrue(result.getErr().contains("ERROR: Flight number must be an integer"));

        assertEquals(new Integer(1), result2.getExitCode());
        assertTrue(result2.getErr().contains("ERROR: Flight number must be an integer"));

        assertEquals(new Integer(1), result3.getExitCode());
        assertTrue(result3.getErr().contains("ERROR: Flight number must be an integer"));
    }

    /**
     * Test for error with invalid source and destination
     */
    @Test
    public void testInvalidSourceAndDestination() {
        String[] args = new String[]{"american", "787", "PORT", "1/2/2012", "1:05", "AM", "SEAT", "01/04/2014", "11:00", "PM"};
        String[] args2 = new String[] {"-print", "american", "111", "PORT", "1/2/2012", "1:05", "AM", "SEAT", "01/04/2014","11:00", "PM"};
        String[] args3 = new String[] {"-print", "-textFile", "tmp", "american", "111", "PORT", "1/2/2012", "1:05", "AM", "SEAT", "01/04/2014","11:00", "PM"};

        MainMethodResult result = invokeMain(args);
        assertEquals(new Integer(1), result.getExitCode());
        assertTrue(result.getErr().contains("ERROR: Location must be a 3 character code"));

        MainMethodResult result2 = invokeMain(args2);
        assertEquals(new Integer(1), result2.getExitCode());
        assertTrue(result2.getErr().contains("ERROR: Location must be a 3 character code"));

        MainMethodResult result3 = invokeMain(args3);
        assertEquals(new Integer(1), result3.getExitCode());
        assertTrue(result3.getErr().contains("ERROR: Location must be a 3 character code"));
    }

    /**
     * Test for -README with no other parameters
     */
    @Test
    public void testReadmeOption() {
        String[] args = new String[]{"-README"};
        String[] args2 = new String[]{"-README", "1", "2", "3", "4", "5", "6", "7", "8"};

        MainMethodResult result = invokeMain(args);
        assertEquals(new Integer(0), result.getExitCode());
        assertNotNull(result.getOut());

        MainMethodResult result2 = invokeMain(args2);
        assertEquals(new Integer(0), result2.getExitCode());
        assertNotNull(result2.getOut());
    }

    /**
     * Test for -README with other parameters(should have same behavior as if it was by itself)
     */
    @Test
    public void testReadmeOptionWithMultipleParameters() {
        String[] args = new String[]{"-README", "-print", "american", "787", "PORT", "1/2/2012", "1:05", "PM","SEAT", "01/04/2014", "11:00", "AM"};
        MainMethodResult result = invokeMain(args);
        assertEquals(new Integer(0), result.getExitCode());
        assertNotNull(result.getOut());
    }

    /**
     * Test for invalid month in the time/date field for arrival and departure (High)
     */
    @Test
    public void testInvalidDeparture() {

        String[] args = new String[]{"-print", "american", "787", "PDX", "13/2/2012", "1:05", "PM", "SEA", "12/04/2014", "11:00", "AM"};
        String[] args2 = new String[]{"american", "787", "PDX", "13/2/2012", "1:05", "AM", "SEA", "12/04/2014", "11:00", "PM"};
        String[] args3 = new String[]{"-print", "-textFile", "tmp", "american", "787", "PDX", "13/2/2012", "1:05", "AM", "SEA", "12/04/2014", "11:00", "PM"};

        MainMethodResult result = invokeMain(args);
        assertEquals(new Integer(1), result.getExitCode());
        assertTrue(result.getErr().contains("ERROR: Invalid Departure Date entered"));

        MainMethodResult result2 = invokeMain(args2);
        assertEquals(new Integer(1), result2.getExitCode());
        assertTrue(result2.getErr().contains("ERROR: Invalid Departure Date entered"));

        MainMethodResult result3 = invokeMain(args3);
        assertEquals(new Integer(1), result3.getExitCode());
        assertTrue(result3.getErr().contains("ERROR: Invalid Departure Date entered"));
    }

    /**
     * Testing the arrival date
     */
    @Test
    public void testInvalidArrival() {

        String[] args = new String[]{"-print", "american", "787", "PDX", "12/2/2012", "1:05", "PM", "SEA", "13/04/2014", "11:00", "PM"};
        String[] args2 = new String[]{"american", "787", "PDX", "12/2/2012", "1:05", "PM", "SEA", "13/04/2014", "11:00", "AM"};
        String[] args3 = new String[]{"-print", "-textFile", "tmp", "american", "787", "PDX", "12/2/2012", "1:05", "AM", "SEA", "13/04/2014", "11:00", "PM"};

        MainMethodResult result = invokeMain(args);
        assertEquals(new Integer(1), result.getExitCode());
        assertTrue(result.getErr().contains("ERROR: Invalid Arrival Date entered"));

        MainMethodResult result2 = invokeMain(args2);
        assertEquals(new Integer(1), result2.getExitCode());
        assertTrue(result2.getErr().contains("ERROR: Invalid Arrival Date entered"));

        MainMethodResult result3 = invokeMain(args3);
        assertEquals(new Integer(1), result3.getExitCode());
        assertTrue(result3.getErr().contains("ERROR: Invalid Arrival Date entered"));
    }

    /**
     * Test AM and PM
     */
    @Test
    public void testNewAMPMParams() {
        String[] args = new String[]{"-print", "american", "787", "PDX", "12/2/2012", "1:05", "pM", "SEA", "12/04/2014", "11:00", "Am"};
        String[] args2 = new String[]{"american", "787", "PDX", "12/2/2012", "1:05", "Pm", "SEA", "12/04/2014", "11:00", "aM"};

        String[] args3 = new String[]{"-print", "-textFile", "tmp", "american", "787", "PDX", "12/2/2012", "1:05", "AM", "SEA", "12/04/2014", "11:00", "PMP"};
        String[] args4 = new String[]{"-print", "-textFile", "tmp", "american", "787", "PDX", "12/2/2012", "1:05", "AMS", "SEA", "12/04/2014", "11:00", "PM"};

        MainMethodResult result = invokeMain(args2);
        assertEquals(new Integer(0), result.getExitCode());

        MainMethodResult result2 = invokeMain(args2);
        assertEquals(new Integer(0), result2.getExitCode());

        MainMethodResult result3 = invokeMain(args3);
        assertEquals(new Integer(1), result3.getExitCode());
        assertTrue(result3.getErr().contains("ERROR: Invalid Arrival time entered"));

        MainMethodResult result4 = invokeMain(args4);
        assertEquals(new Integer(1), result4.getExitCode());
        assertTrue(result4.getErr().contains("ERROR: Invalid Departure time entered"));
    }

    /**
     * Unknown command line
     */
    @Test
    public void testUnknownCommand() {
        String[] args = new String[]{"-textFile", "jushuck-x.txt", "Test6", "123", "PDX", "03/03/2014", "12:00", "AM", "ORD", "01/04/2014", "12:00", "AM", "fred"};
        String[] args2 = new String[]{"-print", "-textFile", "jushuck-x.txt", "Test6", "123", "PDX", "03/03/2014", "12:00", "PM", "ORD", "01/04/2014", "12:00", "AM", "fred"};
        String[] args3 = new String[]{"Test6", "123", "PDX", "03/03/2014", "12:00", "PM", "ORD", "01/04/2014", "16:00", "AM","fred"};

        MainMethodResult result = invokeMain(args);
        assertEquals(new Integer(1), result.getExitCode());
        assertTrue(result.getErr().contains("ERROR: Unknown Command line Argument"));

        MainMethodResult result2 = invokeMain(args2);
        assertEquals(new Integer(1), result2.getExitCode());
        assertTrue(result2.getErr().contains("ERROR: Unknown Command line Argument"));

        MainMethodResult result3 = invokeMain(args3);
        assertEquals(new Integer(1), result3.getExitCode());
        assertTrue(result3.getErr().contains("ERROR: Unknown Command line Argument"));

    }

    /**
     * Prints to text File
     */
    @Test
    public void  validInputWithPretty() {

        String[] args = new String[]{"-pretty", "t", "american", "787", "PDX", "1/2/2012", "1:05", "AM", "SEA", "01/04/2014", "11:00", "PM"};
        String[] args2 = new String[]{"-print", "-pretty", "t", "american", "787", "PDX", "1/2/2012", "1:05", "AM", "SEA", "01/04/2014", "11:00", "PM" };
        String[] args3 = new String[]{"-print", "-pretty", "-", "american", "787", "PDX", "1/2/2012", "1:05", "AM", "SEA", "01/04/2014", "11:00", "PM" };
        String[] args4 = new String[]{"-textFile","t2", "-pretty", "-", "american", "787", "PDX", "1/2/2012", "1:05", "AM", "SEA", "01/04/2014", "11:00", "PM" };
        String[] args5 = new String[]{"-textFile", "t2", "-print", "-pretty", "helloWorld", "american", "787", "PDX", "1/2/2012", "1:05", "AM", "SEA", "01/04/2014", "11:00", "PM" };

        MainMethodResult result = invokeMain(args);
        assertEquals(new Integer(0), result.getExitCode());

        MainMethodResult result2 = invokeMain(args2);
        assertEquals(new Integer(0), result2.getExitCode());

        MainMethodResult result3 = invokeMain(args3);
        assertEquals(new Integer(0), result3.getExitCode());

        MainMethodResult result4 = invokeMain(args4);
        assertEquals(new Integer(0), result4.getExitCode());

        MainMethodResult result5 = invokeMain(args5);
        assertEquals(new Integer(0), result5.getExitCode());
    }

    /**
     * Test Pretty with Invalid Args
     */
    @Test
     public void testPrettyWithInvalid() {
         String[] args = new String[]{"-pretty", "t", "american", "787", "PORT", "1/2/2012", "1:05", "AM", "SEA", "01/04/2014", "11:00", "PM"};
         String[] args2 = new String[]{"-print", "-pretty", "t", "american", "787", "POR", "1/2/2012", "1:05", "AM", "SEA", "01/04/2014", "11:00", "PM" };
         String[] args3 = new String[]{"-print", "-pretty", "-", "american", "787", "PDX", "1/2/2012", "1:05", "AM", "SEA", "01/04/2014", "11:00", "PM", "Extra", "Args"};

         MainMethodResult result = invokeMain(args);
         assertEquals(new Integer(1), result.getExitCode());
         assertTrue(result.getErr().contains("ERROR: Location must be a 3 character code"));

         MainMethodResult result2 = invokeMain(args2);
         assertEquals(new Integer(1), result2.getExitCode());
         assertTrue(result2.getErr().contains("ERROR: Airport code 'POR' doesn't exist"));


         MainMethodResult result3 = invokeMain(args3);
         assertEquals(new Integer(1), result3.getExitCode());
         assertTrue(result3.getErr().contains("ERROR: Unknown Command line Argument"));
     }

}