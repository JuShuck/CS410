package edu.pdx.cs410J.jushuck;
/**
 * Author: Justin Shuck
 * Due Date: 7-9-2014
 * Course: CS410J
 * Program 1
 */
import edu.pdx.cs410J.AbstractAirline;

import javax.management.BadAttributeValueExpException;
import java.io.*;
import java.lang.String;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * The main class for the CS410J airline Project
 */
public class Project1 {
    /**
     *  Default messages used througout this file.
     */
    static final String MISSING_COMMAND_ARGS = "ERROR: Missing command line arguments";

  public static void main(String[] args) throws BadAttributeValueExpException {
    Class c = AbstractAirline.class;  // Refer to one of Dave's classes so that we can be sure it is on the classpath


      //Checks to see if the '-README' option is there
      checkForREADME(args);

      int optionCount = 0;      //Number of options in the arg list

      //If the README flag is active, try to open the file and output the content
      if(checkForREADME(args)) {
          try {
              if (args[0].equals("-README") || args[1].equals("-README")) {
                  try {
                      String text = new String(Files.readAllBytes(Paths.get("README.md")));
                      System.out.println(text);
                      System.exit(0);
                  } catch (IOException e) {
                      System.err.println("ERROR: Cannot find README.md");
                      System.exit(1);
                  }
              }
          } catch (ArrayIndexOutOfBoundsException e) {
              System.err.println(MISSING_COMMAND_ARGS);
              System.exit(1);
          }
          optionCount++;
      }

      if(checkForPrint(args)) {
          optionCount++;
      }

      //If there are less than 6 required args and the options in total
      if( args.length < 6 + optionCount) {
          System.err.println(MISSING_COMMAND_ARGS);
          System.exit(1);
      }

      //if there are more than the maximum args in total
      if( args.length > 6 + optionCount) {
          System.err.println("ERROR: Too many command line arguments");
          System.exit(1);
      }

    //parse the args to an Airline object
    Airline airline = parseArgs(args);

    //If the '-print' option is there, print to the command line
    if (checkForPrint(args)) {
        System.out.println(airline.getName());
        System.out.println(airline.getFlights());
    }

    System.exit(0);

  }

    /**
     * Checks to see if the '-print' option is one of the first 2 arguments
     * @param args              Command line arguments
     * @return boolean          if the flag is in the arg list
     */
    public static boolean checkForPrint(String[] args) {
        try {
            return (args[0].equals("-print") || args[1].equals("-print"));
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println(MISSING_COMMAND_ARGS);
            System.exit(1);
        }
        return false;
    }

    /**
     * Checks to see if the '-README' option is one of the first 2 arguments
     * @param args              Command line arguments
     * @return boolean          if the flag is in the arg list
     */
    public static boolean checkForREADME(String[] args) {
        try {
            return (args[0].equals("-README") || args[1].equals("-README"));
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
     * @param args          Command line arguments
     * @return              Airline object, with sanitized data
     * @throws BadAttributeValueExpException
     */
    public static Airline parseArgs(String[] args) throws BadAttributeValueExpException {

        int length = args.length;

        //Holds the sanitized data
        int flightNum = sanatizeFlightNumber(args[length - 5]);
        String src = sanatizeLocation(args[length - 4]);
        String dst = sanatizeLocation(args[length - 2]);
        String departure = sanatizeDateTime(args[length - 3]);
        String arrival = sanatizeDateTime(args[length - 1]);

        //Creates the flight from the data and then creates the airline to return
        Flight flight = new Flight(flightNum, src, departure, dst, arrival);
        Airline airline = new Airline(args[length-6],flight);

        return airline;

    }

    /**
     * Had a tricky time getting simpleDateFormat to work, so I made a crude sanatize date/time that checks each portion of the
     * date and time passed in, otherwise prints an error and exits.
     * @param dateString            The date string passed through the command line, before being verified as a valid date
     * @return                      dateString, if it is in valid form.
     */
    public static String sanatizeDateTime(String dateString) {

        try{
            //Split the string passed in to month, day, year, hour, minute sub strings
            String[] s = dateString.split("/");
            String month = s[0];
            String day = s[1];
            String[] yearAndTime = s[2].split(" ");
            String year = yearAndTime[0];

            //Ensures that there was a time string attached to the date string
            if(yearAndTime.length != 2) {
                throw new NumberFormatException();
            }
            String[] time = yearAndTime[1].split(":");
            String hours = time[0];
            String minutes = time[1];

            //Converts the sub-strings into integers
            int mm = Integer.parseInt(month);
            int dd = Integer.parseInt(day);
            int yyyy = Integer.parseInt(year);
            int h = Integer.parseInt(hours);
            int m = Integer.parseInt(minutes);

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

            if(h < 0 || h > 24 || hours.length() > 2) {
                throw new NumberFormatException();
            }

            if(m < 0 || m > 59 || minutes.length() > 2) {
                throw new NumberFormatException();
            }

        } catch (NumberFormatException e) {
            System.err.println("ERROR: Invalid Date entered" );
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
    public static int sanatizeFlightNumber(String string) {
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
    public static String sanatizeLocation(String string) {
        if(string.length() == 3) {
            return string;
        }
        System.err.println("ERROR: Location must be a 3 character code");
        System.exit(1);
        return null;
    }

}