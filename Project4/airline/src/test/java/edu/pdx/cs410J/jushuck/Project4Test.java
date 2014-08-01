package edu.pdx.cs410J.jushuck;

import edu.pdx.cs410J.InvokeMainTestCase;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

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
        MainMethodResult result2 = invokeMain(Project4.class, "American", "100", "PDX", "1/2/2012", "1:05", "PM", "SEA", "01/04/2014", "11:00");
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
}