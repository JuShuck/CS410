package edu.pdx.cs410J.jushuck;

import edu.pdx.cs410J.AbstractAirline;
import edu.pdx.cs410J.AirportNames;
import edu.pdx.cs410J.web.HttpRequestHelper;

import javax.management.BadAttributeValueExpException;
import java.io.IOException;
import java.io.PrintStream;
import java.net.HttpURLConnection;

/**
 * The main class that parses the command line and communicates with the
 * Airline server using REST.
 */
public class Project4 {

    static final String MISSING_COMMAND_ARGS = "ERROR: Missing command line arguments";
    static final String README = "      usage: java Project4 [options <args>\n" +
            "        args are (in this order): \n" +
            "          name          The name of the airline\n" +
            "          flightNumber  The flight number \n" +
            "          src           Three-letter code of arrival airport\n" +
            "          departTime    Departure data/time MM/dd/yyyy hh:mm a\n" +
            "          dest          Three-letter code of arrival airport\n" +
            "          arriveTime    Arrival data/time MM/dd/yyyy hh:mm a\n" +
            "        Options are:\n" +
            "          -host hostName Host computer on which the server runs\n" +
            "          -port port     Port on which the server is listening\n" +
            "          -search        Search for flights\n" +
            "          -print         Prints a description of the new flight\n" +
            "          -REDME         Prints a README for this project and exits";


    public static void main(String... args) throws BadAttributeValueExpException {
        //Check for the README flag and then exit
        if(checkForFlag(args, "-README")) {
            System.out.println(README);
            System.exit(0);
        }

        //Try getting the host name(string) and port(int)
        //will return null and -1 respectively if the flag are not specified
        String hostName = getHostName(args);
        int port = getPortName(args);

        // Then Host/Port are not specified, continue as Program 1
        if(hostName == null && port == -1) {
            verifyArgs(args);

            //Parse args and add an airline
            Flight f = parseArgs(args);
            Airline a = new Airline(getAirlineName(args));
            a.addFlight(f);

            //print if the flag is available
            if(checkForFlag(args, "-print"))  {
                System.out.println(f.print());
            }

            System.exit(0);
        }

        //
        if(checkForFlag(args, "-search")) {
            //verifys the argument since the arg order is different in search
            verifySearch(args);
            //Gets the airline name since the arg order is different in search
            String airlineName = getAirlineName(args);

            handleSearch(args, airlineName, hostName, port);
            System.exit(0);
        }

        verifyArgs(args);
        String airlineName = getAirlineName(args);

        Flight flight = parseArgs(args);

        if(checkForFlag(args, "-print")) {
            System.out.println(flight.print());
        }

        AbstractAirline airline = new Airline(airlineName);
        airline.addFlight(flight);

        AirlineRestClient client = new AirlineRestClient(hostName, port);

        HttpRequestHelper.Response response;
        //Try to add a flight to the client, error out if cannot connect to server
        try {
            response = client.addFlight(airlineName, Integer.toString(flight.getNumber()), flight.getSource(), flight.getDepartureString(), flight.getDestination(), flight.getArrivalString());
            checkResponseCode(HttpURLConnection.HTTP_OK, response);
        } catch (IOException ex) {
            error("While contacting server: " + ex);
            return;
        }
        System.exit(0);
    }

    /**
     * Verify the arguments when using the -search option
     * @param args
     */
    private static void verifySearch(String[] args) {
        //Hold the number of flag and expected optional args
        int count = 0;

        //Search must indicate a host and port, if they don't error out
        if(checkForFlag(args,"-host")) {
            count = count + 2;
        } else {
            usage("ERROR: Host not specified");
        }

        if(checkForFlag(args,"-port")) {
            count = count +2;
        } else {
            usage("ERROR: Port not specified");
        }

        //Search for other flags
        if(checkForFlag(args, "-print")) {
            count++;
        }
        if(checkForFlag(args, "-search")) {
            count++;
        }

        //Since there are only 3 mandatory args, error with a high or low if they are not equal to args passed in
        if((count+3) > args.length) {
            usage(MISSING_COMMAND_ARGS);
        }
        if((count+3) < args.length) {
            usage("ERROR: Too many command line arguments");
        }
    }

    /**
     * Handles the search of the verified arguments
     * @param args          verified arguments
     * @param airlineName   The airline name to search
     * @param hostName      the hostname to connect to
     * @param port          the listening port
     */
    private static void handleSearch(String[] args,String airlineName, String hostName, int port) {
        try {
            int size = args.length;

            //Get the source and destination and ensure they're valid locations
            String src = args[size - 2];
            String dest = args[size - 1];
            sanitizeLocation(src);
            sanitizeLocation(dest);


            AirlineRestClient client = new AirlineRestClient(hostName, port);

            HttpRequestHelper.Response response;
            //Try to search for flight based on the airline name, source and destination
            try{
                response = client.searchFlight(airlineName, src, dest);
            } catch (IOException ex) {
                error("While contacting server: " + ex);
                return;
            }

            System.out.println(response.getContent());
            System.exit(0);

        } catch (ArrayIndexOutOfBoundsException e) {
            usage(MISSING_COMMAND_ARGS);
        }
    }

    /**
     * Find the airline named based on the mandatory arguments
     * @param args      The args passed in
     * @return          The airline name || null for a not found airline name
     */
    private static String getAirlineName(String[] args) {
        try {
            //Search has a different order, thus it's at arg.length -3 instead of -10
            if(checkForFlag(args, "-search")) {
                return args[args.length - 3];
            }
            return args[args.length - 10];

        } catch (ArrayIndexOutOfBoundsException e) {
            usage(MISSING_COMMAND_ARGS);
        }
        //Default return value (Airline name not found)
        return null;
    }

    /**
     * Determines if both -port and -host flag are in the arguments. Will error out if only one or the other are present
     * @param args      The args passed in
     * @return          if the port and host flag are both present
     */
    private static boolean portAndHostFlagPresent(String[] args) {
        if((checkForFlag(args,"-port") && !checkForFlag(args,"-host"))) {
            usage("ERROR: Host not specified");
        }
        if((!checkForFlag(args,"-port") && checkForFlag(args,"-host"))) {
            usage("ERROR: Port not specified");
        }

        return (checkForFlag(args,"-port") || checkForFlag(args,"-host"));
    }

    /**
     * This goes through, accounts the flags and then determines if there are a valid number of arguments
     * @param args
     */
    private static void verifyArgs(String[] args) {
        int count = 0;
        int size =args.length;

        if(checkForFlag(args,"-host")) {
            count += 2;
        }
        if(checkForFlag(args,"-port")) {
            count +=2;
        }
        if(checkForFlag(args,"-print")) {
            count +=1;
        }

        if(portAndHostFlagPresent(args)) {
            if((count+10) != size) {
                usage(MISSING_COMMAND_ARGS);
            }
            if (args.length > 15) {
                usage("ERROR: Too many arguments");
            }
        }
        if((count+10) != size) {
            usage(MISSING_COMMAND_ARGS);
        }
    }

    /**
     * Checks to see if the '-README' option is one of the first 2 arguments
     * @param args              Command line arguments
     * @return boolean          if the flag is in the arg list
     */
    public static boolean checkForFlag(String[] args, String flagName) {
        try {
            return (args[0].equals(flagName) || args[1].equals(flagName) || args[2].equals(flagName) || args[3].equals(flagName) || args[4].equals(flagName));
        } catch (ArrayIndexOutOfBoundsException e) {
            usage("WW"+MISSING_COMMAND_ARGS);
        }
        return false;
    }

    /**
     * Takes the command args and returns the argument after -host.
     * @param args
     * @return
     */
    private static String getHostName(String[] args) {
        int index = 0;
        for (String a : args) {
            try {
                if(a.toUpperCase().equals("-HOST")){
                    if(args[index+1].equals("-port") || args[index+1].equals("-print") || args[index+1].equals("-README")) {
                        usage("ERROR: Invalid Host Name Specified");
                    }
                    return args[index + 1];
                }
                index++;
            } catch (IndexOutOfBoundsException e) {
                usage("ERROR: Cannot find hostname");
            }
        }
        return null;
    }

    /**
     * Takes the command args and returns the argument after -host.
     * @param args
     * @return
     */
    private static int getPortName(String[] args) {
        int index = 0;
        for (String a : args) {
            try {
                if (a.toUpperCase().equals("-PORT")) {
                    return Integer.parseInt(args[index + 1]);
                }
                index++;
            } catch (IndexOutOfBoundsException e) {
                usage("ERROR: Cannot find port");
            } catch (NumberFormatException e) {
                usage("ERROR: Port must be an Integer");
            }
        } return -1;
    }

    /**
     * Makes sure that the give response has the expected HTTP status code
     * @param code The expected status code
     * @param response The response from the server
     */
    private static void checkResponseCode( int code, HttpRequestHelper.Response response )
    {
        if (response.getCode() != code) {
            error(String.format("Expected HTTP code %d, got code %d.\n\n%s", code,
                                response.getCode(), response.getContent()));
        }
    }

    private static void error( String message )
    {
        PrintStream err = System.err;
        err.println("** " + message);

        System.exit(1);
    }

    /**
     * Prints usage information for this program and exits
     * @param message An error message to print
     */
    private static void usage( String message )
    {
        PrintStream err = System.err;
        err.println("** " + message);
        err.println();
        err.println("usage: java Project4 [options <args>");
        err.println("args are (in this order): ");
        err.println("  name          The name of the airline");
        err.println("  flightNumber  The flight number ");
        err.println("  src           Three-letter code of arrival airport");
        err.println("  departTime    Departure data/time MM/dd/yyyy hh:mm a");
        err.println("  dest          Three-letter code of arrival airport");
        err.println("  arriveTime    Arrival data/time MM/dd/yyyy hh:mm a");
        err.println("Options are:");
        err.println("  -host hostName Host computer on which the server runs");
        err.println("  -port port     Port on which the server is listening");
        err.println("  -search        Search for flights");
        err.println("  -print         Prints a description of the new flight");
        err.println("  -REDME         Prints a README for this project and exits");
        err.println();

        System.exit(1);
    }

    /**
     * Parses the arguments from the arg list, sanitizes the data and then returns the data in an Airline object. The program
     * can terminate early during this process if the data provided in the args list is invalid. When this happens, an error is outputed
     * and the program terminates.
     * @return              Airline object, with sanitized data
     * @throws javax.management.BadAttributeValueExpException
     */
    public static Flight parseArgs(String[] args) throws BadAttributeValueExpException {

        int length = args.length;

        //Holds the sanitized data
        // Ex of Args: AirlineName 123 PDX 04/13/2012 11:00 PM SEA 05/04/2012 11:30 AM
        int flightNum = sanitizeFlightNumber(args[length - 9]);

        String src = sanitizeLocation(args[length - 8]);
        String departureDate = sanitizeDate(args[length - 7], "Departure");
        String departureTime = sanitizeTime(args[length - 6],args[length - 5], "Departure");

        String dst = sanitizeLocation(args[length - 4]);
        String arrivalDate = sanitizeDate(args[length - 3], "Arrival");
        String arrivalTime = sanitizeTime(args[length - 2], args[length -1], "Arrival");

        //Creates the flight from the data and then creates the airline to return
        return new Flight(flightNum, src.toUpperCase(), departureDate + " " + departureTime, dst.toUpperCase(), arrivalDate + " " + arrivalTime);

    }

    /**
     * Sanitizes the time string to have the format of hh:mm. Where the hour doesn't exceed 12 and the
     * minute doesn't exceed 59.
     * @param timeString    The time argument passed in as a string
     * @param source        The location of time (Departure or Arrival), used for Error reporting
     * @return              Validated time String in the form of hh:mm
     */
    public static String sanitizeTime(String timeString, String identifer, String source) {
        try{

            String[] time = timeString.split(":");

            //Ensures that there are only 2 time sub-strings
            if(time.length != 2) {
                throw new NumberFormatException();
            }

            String hours = time[0];
            String minutes = time[1];

            //Converts the sub-strings into integers
            int h = Integer.parseInt(hours);
            int m = Integer.parseInt(minutes);

            if(h < 0 || h > 12 || hours.length() > 2) {
                throw new NumberFormatException();
            }

            if(m < 0 || m > 59 || minutes.length() > 2) {
                throw new NumberFormatException();
            }

            if(!(identifer.toLowerCase().equals(new String("am"))) && !(identifer.toLowerCase().equals(new String("pm")))) {
                throw new NumberFormatException();
            }

        } catch (NumberFormatException e) {
            System.err.println("ERROR: Invalid " + source + " time entered" );
            System.exit(1);
        }
        return timeString + " " + identifer.toUpperCase();
    }

    /**
     * Had a tricky time getting simpleDateFormat to work, so I made a crude sanatize date/time that checks each portion of the
     * date and time passed in, otherwise prints an error and exits.
     * @param dateString            The date string passed through the command line, before being verified as a valid date
     * @return                      dateString, if it is in valid form.
     */
    public static String sanitizeDate(String dateString, String source) {

        try{
            //Split the string passed in to month, day, year, sub Strings
            String[] date = dateString.split("/");

            //Ensures that the date string has the 3 sections (XX/XX/XXX)
            if(date.length != 3) {
                throw new NumberFormatException();
            }
            String month = date[0];
            String day = date[1];
            String year = date[2];

            //Converts the sub-strings into integers
            int mm = Integer.parseInt(month);
            int dd = Integer.parseInt(day);
            int yyyy = Integer.parseInt(year);

            //Validates the sub-strings
            if(mm < 1 || mm > 12 || month.length() > 2) {
                throw new NumberFormatException();
            }

            if(dd < 1 || dd > 31 || day.length() > 2) {
                throw new NumberFormatException();
            }

            if(yyyy < 0 || year.length() > 4) {
                throw new NumberFormatException();
            }

        } catch (NumberFormatException e) {
            System.err.println("ERROR: Invalid " + source + " Date entered" );
            System.exit(1);
        }
        return dateString;
    }

    /**
     * Takes the flight number string passed in from the command line and converts it into an integer. Will throw an error
     * abd terminate the program if the string passed in cannot be casted.
     * @param string                        raw flight number string passed in from the command line arguments
     * @return                              the flight number as an integer
     */
    public static int sanitizeFlightNumber(String string) {
        try {
            return Integer.parseInt(string);
        }  catch (NumberFormatException e) {
            System.err.println("ERROR: Flight number must be an integer");
            System.exit(1);
        }
        return 0;
    }

    /**
     * Takes the location string passed in from the command line and verifies that it is only 3 characters. Will throw an eror
     * and terminate the program if it is greater than 3.
     * @param string                    raw string input passed from the command line arguments
     * @return                          validated location string
     */
    public static String sanitizeLocation(String string) {
        if(string.length() == 3) {
            try {
                if (AirportNames.getName(string.toUpperCase()) == null) {
                    throw new Exception();
                }
            } catch (Exception e) {
                usage("ERROR: Airport code '" + string + "' doesn't exist");
            }
            return string;
        }
        System.err.println("ERROR: Location must be a 3 character code");
        System.exit(1);
        return null;
    }
}