package edu.pdx.cs410J.jushuck;

import edu.pdx.cs410J.InvokeMainTestCase;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests the {@link Project4} class by invoking its main method with various arguments 
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Project4Test extends InvokeMainTestCase {
    private static final String HOSTNAME = "localhost";
    private static final String PORT = String.valueOf(8080);

    @Test
    public void testNoCommandLineArgs() {
        MainMethodResult result = invokeMain( Project4.class );
        assertThat(result.getExitCode(), equalTo(1));
        assertThat(result.getErr(), containsString(Project4.MISSING_COMMAND_ARGS));
    }

    @Test
    public void nonIntPortNum() {
        MainMethodResult result = invokeMain(Project4.class, "-host","localhost", "-port","eight","American", "100", "PDX", "1/2/2012", "1:05", "PM", "SEA", "01/04/2014", "11:00", "pm");
        assertThat(result.getExitCode(), equalTo(1));
        assertThat(result.getErr(), containsString("ERROR: Port must be an Integer"));
    }

    @Test
    public void testHostButNoPort() {
        MainMethodResult result = invokeMain(Project4.class, "-host", HOSTNAME,"American", "100", "PDX", "1/2/2012", "1:05", "PM", "SEA", "01/04/2014", "11:00", "AM");
        assertThat(result.getExitCode(), equalTo(1));
        assertThat(result.getErr(), containsString("ERROR: Port not specified"));
    }

    @Test
    public void testPortButNoHost() {
        MainMethodResult result = invokeMain(Project4.class,"-port","8080","American", "100", "PDX", "1/2/2012", "1:05", "PM", "SEA", "01/04/2014", "11:00", "AM", "x");
        assertThat(result.getExitCode(), equalTo(1));
        assertThat(result.getErr(), containsString("ERROR: Host not specified"));
    }

    @Test
    public void testAirlineName() {
        //with valid host/port thinks that "American" is flight number
        MainMethodResult result = invokeMain(Project4.class, "-host","localhost", "-port","8080","American", "100", "PDX", "1/2/2012", "1:05", "PM", "SEA", "01/04/2014", "11:00");
        assertThat(result.getExitCode(), equalTo(1));
        assertThat(result.getErr(), containsString(Project4.MISSING_COMMAND_ARGS));

        //with no host/port
        MainMethodResult result2 = invokeMain(Project4.class, "American", "100", "PDX", "1/2/2012", "1:05", "PM", "SEA", "01/04/2014", "11:00", "AM");
        assertThat(result2.getExitCode(), equalTo(0));

    }

    @Test
    public void noPortHostTests() {
        MainMethodResult result1 = invokeMain(Project4.class, "American", "100", "PDX", "1/2/2012", "1:05", "PM", "SEA", "01/04/2014", "11:00");
        assertThat(result1.getExitCode(), equalTo(1));
        assertThat(result1.getErr(), containsString(Project4.MISSING_COMMAND_ARGS));

        MainMethodResult result2 = invokeMain(Project4.class,"-print", "American", "100", "PDX", "1/2/2012", "1:05", "PM", "SEA", "01/04/2014", "11:00");
        assertThat(result2.getExitCode(), equalTo(1));
        assertThat(result2.getErr(), containsString(Project4.MISSING_COMMAND_ARGS));
    }

    @Test
    public void cantFindHost() {
        MainMethodResult result = invokeMain(Project4.class, "-host","1", "-port","8080","American", "100", "PDX", "1/2/2012", "1:05", "PM", "SEA", "01/04/2014", "11:00", "pm");
        assertThat(result.getExitCode(), equalTo(1));
        assertThat(result.getErr(), containsString("unreachable"));
    }

    @Test
    public void cantFindPort() {
        MainMethodResult result = invokeMain(Project4.class, "-host","localhost", "-port","1","American", "100", "PDX", "1/2/2012", "1:05", "PM", "SEA", "01/04/2014", "11:00", "pm");
        assertThat(result.getExitCode(), equalTo(1));
        assertThat(result.getErr(), containsString("Connection refused"));
    }
    @Test
    public void validRequestsNoSearch() {
        MainMethodResult result = invokeMain(Project4.class, "-host","localhost", "-port","8080","American", "100", "PDX", "1/2/2012", "1:05", "PM", "SEA", "01/04/2014", "11:00", "pm");
        assertThat(result.getExitCode(), equalTo(0));

        MainMethodResult result2 = invokeMain(Project4.class, "-host","localhost", "-port","8080","-print", "American", "100", "PDX", "1/2/2012", "1:05", "PM", "SEA", "01/04/2014", "11:00", "pm");
        assertThat(result2.getExitCode(), equalTo(0));

        MainMethodResult result3 = invokeMain(Project4.class, "-print", "-host","localhost", "-port","8080","American", "100", "PDX", "1/2/2012", "1:05", "PM", "SEA", "01/04/2014", "11:00", "pm");
        assertThat(result3.getExitCode(), equalTo(0));

        MainMethodResult result4 = invokeMain(Project4.class, "-port", "8080", "-host","localhost", "American", "100", "PDX", "1/2/2012", "1:05", "PM", "SEA", "01/04/2014", "11:00", "pm");
        assertThat(result4.getExitCode(), equalTo(0));

        MainMethodResult result5 = invokeMain(Project4.class, "American", "100", "PDX", "1/2/2012", "1:05", "PM", "SEA", "01/04/2014", "11:00", "pm");
        assertThat(result5.getExitCode(), equalTo(0));

        MainMethodResult result6 = invokeMain(Project4.class,"-print", "American", "100", "PDX", "1/2/2012", "1:05", "PM", "SEA", "01/04/2014", "11:00", "pm");
        assertThat(result6.getExitCode(), equalTo(0));
    }

    @Test
    public void validRequestsSearch() {
        MainMethodResult result = invokeMain(Project4.class, "-host","localhost", "-port","8080","American", "100", "PDX", "1/2/2012", "1:05", "PM", "SEA", "01/04/2014", "11:00", "pm");
        assertThat(result.getExitCode(), equalTo(0));

        MainMethodResult result2 = invokeMain(Project4.class, "-host","localhost", "-port","8080","-print", "American", "100", "PDX", "1/2/2012", "1:05", "PM", "SEA", "01/04/2014", "11:00", "pm");
        assertThat(result2.getExitCode(), equalTo(0));

        MainMethodResult result3 = invokeMain(Project4.class, "-print", "-host","localhost", "-port","8080","American", "100", "PDX", "1/2/2012", "1:05", "PM", "SEA", "01/04/2014", "11:00", "pm");
        assertThat(result3.getExitCode(), equalTo(0));

        MainMethodResult result4 = invokeMain(Project4.class, "-port", "8080", "-host","localhost", "American", "100", "PDX", "1/2/2012", "1:05", "PM", "SEA", "01/04/2014", "11:00", "pm");
        assertThat(result4.getExitCode(), equalTo(0));
    }

    @Test
    public void testSearchFailureTest() {
        MainMethodResult result = invokeMain(Project4.class, "-host","localhost", "-port","8080","-search", "American", "PDX", "SEA", "MISSING COmmand");
        assertThat(result.getExitCode(), equalTo(1));
        assertThat(result.getErr(), containsString("ERROR: Too many command line arguments"));

        MainMethodResult result2 = invokeMain(Project4.class, "-host","localhost", "-port","8080","-search", "-print", "American", "PDX", "SEA", "MISSING COmmand");
        assertThat(result2.getExitCode(), equalTo(1));
        assertThat(result2.getErr(), containsString("ERROR: Too many command line arguments"));

        MainMethodResult result3 = invokeMain(Project4.class, "-host","localhost", "-port","8080","-search", "American", "PDX");
        assertThat(result3.getExitCode(), equalTo(1));
        assertThat(result3.getErr(), containsString(Project4.MISSING_COMMAND_ARGS));

        MainMethodResult result4 = invokeMain(Project4.class,"-print", "-host","localhost", "-port","8080","-search", "American", "PDX");
        assertThat(result4.getExitCode(), equalTo(1));
        assertThat(result4.getErr(), containsString(Project4.MISSING_COMMAND_ARGS));
    }

    @Test
    public void testNewAMPMParamsNoLocalHostPort() {
        String[] args = new String[]{"-print", "american", "787", "PDX", "12/2/2012", "1:05", "pM", "SEA", "12/04/2014", "11:00", "Am"};
        String[] args2 = new String[]{"american", "787", "PDX", "12/2/2012", "1:05", "Pm", "SEA", "12/04/2014", "11:00", "aM"};

        String[] args3 = new String[]{"-print", "american", "787", "PDX", "12/2/2012", "1:05", "AM", "SEA", "12/04/2014", "11:00", "PMP"};
        String[] args4 = new String[]{"-print", "american", "787", "PDX", "12/2/2012", "1:05", "AMS", "SEA", "12/04/2014", "11:00", "PM"};

        MainMethodResult result = invokeMain(Project4.class,args2);
        assertEquals(new Integer(0), result.getExitCode());

        MainMethodResult result2 = invokeMain(Project4.class, args2);
        assertEquals(new Integer(0), result2.getExitCode());

        MainMethodResult result3 = invokeMain(Project4.class, args3);
        assertEquals(new Integer(1), result3.getExitCode());
        assertTrue(result3.getErr().contains("ERROR: Invalid Arrival time entered"));

        MainMethodResult result4 = invokeMain(Project4.class, args4);
        assertEquals(new Integer(1), result4.getExitCode());
        assertTrue(result4.getErr().contains("ERROR: Invalid Departure time entered"));
    }

    @Test
    public void testNewAMPMParamsWithLocalHostPort() {
        String[] args = new String[]{"-host","localhost","-port","8080","-print", "american", "787", "PDX", "12/2/2012", "1:05", "pM", "SEA", "12/04/2014", "11:00", "Am"};
        String[] args2 = new String[]{"-port", "8080", "-host", "localhost","american", "787", "PDX", "12/2/2012", "1:05", "Pm", "SEA", "12/04/2014", "11:00", "aM"};

        String[] args3 = new String[]{"-host", "localhost", "-port", "8080","-print", "american", "787", "PDX", "12/2/2012", "1:05", "AM", "SEA", "12/04/2014", "11:00", "PMP"};
        String[] args4 = new String[]{"-host", "localhost", "-port", "8080","-print", "american", "787", "PDX", "12/2/2012", "1:05", "AMS", "SEA", "12/04/2014", "11:00", "PM"};

        MainMethodResult result = invokeMain(Project4.class,args2);
        assertEquals(new Integer(0), result.getExitCode());

        MainMethodResult result2 = invokeMain(Project4.class, args2);
        assertEquals(new Integer(0), result2.getExitCode());

        MainMethodResult result3 = invokeMain(Project4.class, args3);
        assertEquals(new Integer(1), result3.getExitCode());
        assertTrue(result3.getErr().contains("ERROR: Invalid Arrival time entered"));

        MainMethodResult result4 = invokeMain(Project4.class, args4);
        assertEquals(new Integer(1), result4.getExitCode());
        assertTrue(result4.getErr().contains("ERROR: Invalid Departure time entered"));
    }
}