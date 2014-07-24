package edu.pdx.cs410J.jushuck;
/**
 * Author: Justin Shuck
 * Due Date: 7-23-2014
 * Course: CS410J
 * Program 3
 */

import edu.pdx.cs410J.AirportNames;
import edu.pdx.cs410J.ParserException;

import javax.management.BadAttributeValueExpException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;


/**
 * The main class for the CS410J airline Project
 */
public class Project3 {
    /**
     *  Default messages used througout this file.
     */
   static final String MISSING_COMMAND_ARGS = "ERROR: Missing command line arguments";
   static final String README = "Project 3\n" +
           "version 1\n" +
           "Due: 7-16-2014 06:00 PM\n" +
           "\n" +
           "Specifications for Project 2 can be found on Professor Whitlock's page. This version of the program adds the functionality\n" +
           "to create an Airline Object based on a textFile and then in turn write back to that text document.Below are the usage\n" +
           "as specified in the assignment:\n" +
           "\n" +
           "args are (in this order):\n" +
           "\n" +
           "    name                The name of the airline\n" +
           "    flightNumber        The flight number\n" +
           "    src                 Three-letter code of departure airport\n" +
           "    departTime          Departure date and time am/pm\n" +
           "    dest                Three-letter code of arrival airport\n" +
           "    arriveTime          Arrival date and time am/pm\n" +
           "\n" +
           "options are (options may appear in any order):\n" +
           "    -pretty file     Pretty print the airline's flights to a text file or standard out (file -)\n" +
           "    -textFile file  Where to read/write the airline info\n" +
           "    -print          Prints a description of the new flight\n" +
           "    -README         Prints a README for this project and exits\n" +
           "\n" +
           "\n" +
           "A few Notes:\n" +
           "* -README has a higher precidence than print and -textFile\n" +
           "* The options '-textFile', '-README', '-pretty' and '-print' are case sensitive in this version\n" +
           "* No option flags can Appear after the 3rd Argument (The max optional flags)\n" +
           "* Date and time should be in the format: mm/dd/yyyy HH:mm a\n" +
           "* 3 Letter airport codes should be case insensitive\n" +
           "* Airline should be pretty printed after the new flight has been added";
    private static final int MAX = 10;


    public static void main(String[] args) throws BadAttributeValueExpException, ParserException, IOException {
      int optionCount = 0;      //Number of option flags in the arg list

       verifyArgs(args);        //verify the arguments

      //Get the size of the command line args
      int size = args.length;

       //Used to store the airline information in mainF.
      Airline airline = null;

       /**
        * If the -textFile flag is there, the airline should be constructed with the contents of the text file,
        * otherwise the airline will just contain the command line arg flight (If valid)
        */

      try {
          if (checkForFlag(args, "-textFile")) {
              if (args.length < 10) {
                  System.err.println(MISSING_COMMAND_ARGS);
                  System.exit(1);
              }
              //*** Used to ensure Args get validated before textfile contents
              Flight f = parseArgs(args);

              //The file name to be used
              String fileName = findFileName(args,"-textFile");

              //The correct args should be present, Parse to an Airline, then Dump
              Parser p = new Parser(args[size - 10], fileName);
              airline = (Airline) p.parse();
              Dumper dumper = new Dumper(fileName);

              airline.addFlight(f);
              dumper.dump(airline);

          } else {
              Flight flight = parseArgs(args);
              airline = new Airline(args[size - 10]);
              airline.addFlight(flight);
          }

      } catch (ArrayIndexOutOfBoundsException e) {
          System.out.println(MISSING_COMMAND_ARGS);
          System.exit(1);
      }

    if(checkForFlag(args,"-pretty")) {
        String fileName = findFileName(args,"-pretty");

        PrettyPrinter prettyPrinter = new PrettyPrinter(fileName);
        if(fileName.equals(new String("-"))) {
            prettyPrinter.dumpToStOut(airline);
        } else {
            prettyPrinter.dump(airline);
        }
    }
    //If the '-print' option is there, print to the command line
    if (checkForFlag(args, "-print")) {
        //Prints the airline Name
        System.out.println(airline.getName());

        //Prints the last item in the list of flights
        List<Flight> list = (List<Flight>) airline.getFlights();
        System.out.println(list.get(list.size() - 1));

    }

    System.exit(0);

  }

    /**
     * Finds the next argument to the right of the -textFile flag
     * @param args          The command line arguments
     * @return              The file name
     */
    private static String findFileName(String[] args,String flag) {
        return args[Arrays.asList(args).indexOf(flag)+1];
    }

    /**
     * Form of: [-README -print -textFile file] American 555 POR 1/1/2010 12:00 SEA 2/2/2010 12:00
     * Verifys that there are the right amount of arguments, Ran before verifying the file.
     * @param args
     */
    private static void verifyArgs(String[] args) {
        int flags = 0;
        if(args.length < 1) {
            System.err.println(MISSING_COMMAND_ARGS);
            System.exit(1);
        }

        //If the README flag is active, try to open the file and output the content of the README and exit
        if(checkForFlag(args, "-README")) {
            System.out.println(README);
            System.exit(0);
        }

        //Checks for the other flags and counts the additional arguments to be added to the size
        if(checkForFlag(args, "-print")) {
            flags++;
        }

        if(checkForFlag(args, "-textFile")) {
            flags += 2;
        }
        if(checkForFlag(args,"-pretty")) {
            flags +=2;
        }

        int size = args.length;
        if(size - flags < MAX) {

            System.err.println(MISSING_COMMAND_ARGS);
            System.exit(1);
        }

        if(flags+MAX != size) {

            System.err.println("ERROR: Unknown Command line Argument");
            System.exit(1);
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
            System.err.println(MISSING_COMMAND_ARGS);
            System.exit(1);
        }
            return false;
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
        return new Flight(flightNum, src, departureDate, departureTime, dst, arrivalDate, arrivalTime);

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
                System.err.println("ERROR: Airport code '" + string + "' doesn't exist");
                System.exit(1);
            }
            return string;
        }
        System.err.println("ERROR: Location must be a 3 character code");
        System.exit(1);
        return null;
    }


}