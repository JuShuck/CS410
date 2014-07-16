package edu.pdx.cs410J.jushuck;
/**
 * Author: Justin Shuck
 * Due Date: 7-9-2014
 * Course: CS410J
 * Program 1
 */

import edu.pdx.cs410J.ParserException;

import javax.management.BadAttributeValueExpException;
import java.io.IOException;
import java.util.List;


/**
 * The main class for the CS410J airline Project
 */
public class Project2 {
    /**
     *  Default messages used througout this file.
     */
   static final String MISSING_COMMAND_ARGS = "ERROR: Missing command line arguments";
   static final String README = "Project 2\n" +
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
           "    departTime          Departure date and time (24-hour time)\n" +
           "    dest                Three-letter code of arrival airport\n" +
           "    arriveTime          Arrival date and time (24-hour time)\n" +
           "\n" +
           "options are (options may appear in any order):\n" +
           "    -textFile file  Where to read/write the airline info\n" +
           "    -print          Prints a description of the new flight\n" +
           "    -README         Prints a README for this project and exits\n" +
           "\n" +
           "Date and time should be in the format: mm/dd/yyyy hh:mm\n" +
           "\n" +
           "A few Notes:\n" +
           "* -README has a higher precidence than print and -textFile\n" +
           "* The options '-textFile', '-README' and 'print' are case sensitive in this version\n" +
           "* No option flags can Appear after the 3rd Argument (The max optional flags)";


   public static void main(String[] args) throws BadAttributeValueExpException, ParserException, IOException {
      int optionCount = 0;      //Number of option flags in the arg list

      //If the README flag is active, try to open the file and output the content of the README and exit
      if(checkForFlag(args, "-README")) {
          try {
              if (args[0].equals("-README") || args[1].equals("-README")) {
                System.out.println(README);
                System.exit(0);
              }
          } catch (ArrayIndexOutOfBoundsException e) {
              System.err.println(MISSING_COMMAND_ARGS);
              System.exit(1);
          }
          optionCount++;
      }

      //Checks for the other flags and counts the additional arguments to be added to the size
      if(checkForFlag(args, "-print")) {
          optionCount++;
      }
      if(checkForFlag(args, "-textFile")) {
          optionCount += 2;
      }

      //Get the size of the command line args
      int size = args.length;

       //Used to store the airline information in main.
      Airline airline = null;

       /**
        * If the -textFile flag is there, the airline should be constructed with the contents of the text file,
        * otherwise the airline will just contain the command line arg flight (If valid)
        */

      try {
          if (checkForFlag(args, "-textFile")) {
              if (args.length < 8 + optionCount) {
                  System.err.println(MISSING_COMMAND_ARGS);
                  System.exit(1);
              }
              //The file name to be used
              String fileName = args[size - 9];

              //The correct args should be present, Parse to an Airline, then Dump
              Parser p = new Parser(args[size - 8], fileName);
              airline = (Airline) p.parse();
              airline.addFlight(parseArgs(args));
              Dumper dumper = new Dumper(fileName);
              dumper.dump(airline);

          } else {
              Flight flight = parseArgs(args);
              airline = new Airline(args[size - 7]);
              airline.addFlight(flight);
          }
      } catch (ArrayIndexOutOfBoundsException e) {
          System.out.println(MISSING_COMMAND_ARGS);
          System.exit(1);
      }

      //If there are less than 6 required args and the options in total
      if( args.length < 6 + optionCount) {
          System.err.println(MISSING_COMMAND_ARGS);
          System.exit(1);
      }

      //if there are more than the maximum args in total
      if( args.length > 8 + optionCount) {
          System.err.println("ERROR: Too many command line arguments");
          System.exit(1);
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
     * Checks to see if the '-README' option is one of the first 2 arguments
     * @param args              Command line arguments
     * @return boolean          if the flag is in the arg list
     */
    public static boolean checkForFlag(String[] args, String flagName) {
        try {
            return (args[0].equals(flagName) || args[1].equals(flagName) || args[2].equals(flagName));
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
        // Ex of Args: AirlineName 123 PDX 04/13/2012 11:00 SEA 05/04/2012 11:30
        int flightNum = sanitizeFlightNumber(args[length - 7]);

        String src = sanitizeLocation(args[length - 6]);
        String departureDate = sanitizeDate(args[length - 5], "Departure");
        String departureTime = sanitizeTime(args[length - 4], "Departure");

        String dst = sanitizeLocation(args[length - 3]);
        String arrivalDate = sanitizeDate(args[length - 2], "Arrival");
        String arrivalTime = sanitizeTime(args[length - 1], "Arrival");

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
    public static String sanitizeTime(String timeString, String source) {
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

        } catch (NumberFormatException e) {
            System.err.println("ERROR: Invalid " + source + " time entered" );
            System.exit(1);
        }
        return timeString;
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
            return string;
        }
        System.err.println("ERROR: Location must be a 3 character code");
        System.exit(1);
        return null;
    }


}