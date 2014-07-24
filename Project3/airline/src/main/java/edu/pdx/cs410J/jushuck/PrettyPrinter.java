package edu.pdx.cs410J.jushuck;

import edu.pdx.cs410J.AbstractAirline;
import edu.pdx.cs410J.AirlineDumper;
import edu.pdx.cs410J.AirportNames;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by justin on 7/22/2014.
 */
public class PrettyPrinter implements AirlineDumper {

    /**
     * The file name to be written to
     */
    String fileName;

    /**
     * Passes the file name for the dump to use
     * @param s         The name of the string
     */
    public PrettyPrinter(String s) {
        fileName = s;
    }

    /**
     * Dumps the pretty text into a new text file
     * @param a
     * @throws IOException
     */
    @Override
    public void dump(AbstractAirline a) throws IOException {
        clearDocument();

        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fileName, true)));

            //Holds the list of Flights the Airline has
            List<Flight> list = (List<Flight>) a.getFlights();
            //Go through each flight and write to the document
            for(Flight f : list) {
                out.println(formPrettySentence(f.getNumber(),f.getSource(),f.getDepartureString(),f.getDestination(),f.getArrivalString()));
            }
            out.close();

        } catch (IOException e) {
            System.err.println("ERROR: Cannot write to the text file");
            System.exit(1);
        }
    }

    /**
     * Clears the documents previous content
     */
    public void clearDocument() {

        try {
            PrintWriter writer = new PrintWriter(fileName);
            writer.print("");
            writer.close();
        } catch (FileNotFoundException e) {
            System.err.println("ERROR: Cannot write to the text file");
            System.exit(1);
        }

    }

    /**
     * Creates a 'pretty' sentence based on the parameters passed in.
     * @param flightNum     The flight number
     * @param Source        3-Letter Airport Source code
     * @param dTime         Departure Time
     * @param Dest          3-Letter Destination code
     * @param aTime         Arrival Time
     * @return              'Pretty'fied string
     */
    public String formPrettySentence(int flightNum, String Source, String dTime, String Dest, String aTime) {

        // Find the duration of the Flight
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm a", Locale.ENGLISH);

        //Time difference (in milliseconds)
        long diff = 0;

        try {
            Date date1 = sdf.parse(dTime);
            Date date2 = sdf.parse(aTime);
            diff = date2.getTime() - date1.getTime();

        } catch(ParseException e) {
            System.err.println("ERROR: Error while calculating time difference");
            System.exit(1);
        }

        //Returns the pretty string
        return "Flight " + flightNum + ": \n" + "Departs: " + AirportNames.getName(Source) + " at " + dTime +
                                       "  \n" + "Arrives: " + AirportNames.getName(Dest) + " at " + aTime +
                                       "  \n" + "Flight Duration: " +   String.format("%02d hours, %02d minutes",
                                                                        TimeUnit.MILLISECONDS.toHours(diff),
                                                                        TimeUnit.MILLISECONDS.toMinutes(diff) -
                                                                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(diff)),
                                                                        TimeUnit.MILLISECONDS.toSeconds(diff) -
                                                                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(diff)))
                                        + "\n\n";
    }

    /**
     * Prints the flights to standard out (Opposed to writing it to a text file)
     * @param airline       The airline inquiring about
     */
    public void dumpToStOut(Airline airline) {
        List<Flight> list = (List<Flight>) airline.getFlights();
        //Go through each flight and write to Standard out
        for(Flight f : list) {
            System.out.println(formPrettySentence(f.getNumber(),f.getSource(),f.getDepartureString(),f.getDestination(),f.getArrivalString()));
        }
    }
}

