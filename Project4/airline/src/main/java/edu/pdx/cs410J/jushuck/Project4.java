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
    static final String README = "This is a sample readme";


    public static void main(String... args) throws BadAttributeValueExpException {
        //String hostName = null;
        //String portString = null;
        String key = "ello";
        String value = "world";
        if(checkForFlag(args, "-README")) {
            System.out.println(README);
            System.exit(0);
        }

        verifyArgs(args);
        String airlineName = getAirlineName(args);
        System.out.println(airlineName);

        //@TODO for seach
        /*if(args.length < 9){
            int port = getPortName(args);
            String hostName = getHostName(args);

            AirlineRestClient client = new AirlineRestClient(hostName, port);

            HttpRequestHelper.Response response;
            try{
                response = client.searchFlight(airlineName, src, destination);
            }catch (IOException ex) {
                error("While contacting server: " + ex);
                return;
            }

            System.out.println(response.getContent());

            System.exit(0);


        }
        else {*/
//            String hostName = null;
//            String portString = null;

//            if (argsToCreateAirLine.length < 10) {
//                System.err.println("Missing arguments to create an airline");
//                System.exit(1);
//            }
            String hostName = getHostName(args);
            int port = getPortName(args);

            //String departure = dateAndTimeFormatInString(departDay, departTime, ampm);
            //String arrival = dateAndTimeFormatInString(arriveDay, arriveTime, ampm1);

            Flight flight = parseArgs(args);

            /*if(isPrint && isSearch){
                System.err.println("Can't handle both print and search in the same time");
                System.exit(1);
            }*/
            System.out.println(flight.print());
            /*if(isPrint){
                System.out.println(flight.print());
            }*/


            AbstractAirline abstractAirline = new Airline(airlineName);
            abstractAirline.addFlight(flight);
            System.out.println(hostName +"/"+port);
            AirlineRestClient client = new AirlineRestClient(hostName, port);

            HttpRequestHelper.Response response;
            try {
                response = client.addFlight(airlineName, Integer.toString(flight.getNumber()), flight.getSource(), flight.getDepartureString(), flight.getDestination(), flight.getArrivalString());
                System.out.println("Flight added");
                checkResponseCode(HttpURLConnection.HTTP_OK, response);

            } catch (IOException ex) {
                error("While contacting server: " + ex);
                return;
            }
            System.out.println("1"+response.getContent());


            System.exit(0);
    }

    private static String getAirlineName(String[] args) {
        try {
            return args[args.length - 10];
        } catch (ArrayIndexOutOfBoundsException e) {
            usage(MISSING_COMMAND_ARGS);
        }
        return null;
    }

    private static void postResponse(String hostName, int portNum, String key, String value) {
        /*AirlineRestClient client = new AirlineRestClient(hostName, portNum);

        HttpRequestHelper.Response response;
        try {
            if (key == null) {
                // Print all key/value pairs
                response = client.getAllKeysAndValues();

            } else if (value == null) {
                // Print all values of key
                response = client.getValues(key);

            } else {
                // Post the key/value pair
                response = client.addKeyValuePair(key, value);
            }

            checkResponseCode( HttpURLConnection.HTTP_OK, response);

        } catch ( IOException ex ) {
            error("While contacting server: " + ex);
            return;
        }

        System.out.println(response.getContent());

        System.exit(0);*/
    }
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
            usage(MISSING_COMMAND_ARGS);
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
        err.println("usage: java Project4 host port [key] [value]");
        err.println("  host    Host of web server");
        err.println("  port    Port of web server");
        err.println("  key     Key to query");
        err.println("  value   Value to add to server");
        err.println();
        err.println("This simple program posts key/value pairs to the server");
        err.println("If no value is specified, then all values are printed");
        err.println("If no key is specified, all key/value pairs are printed");
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