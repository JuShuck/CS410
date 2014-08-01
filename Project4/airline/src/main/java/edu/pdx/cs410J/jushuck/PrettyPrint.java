package edu.pdx.cs410J.jushuck;

import edu.pdx.cs410J.AirportNames;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by justin on 7/22/2014.
 */
class PrettyPrinter  {


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
        return "Flight " + flightNum +
                ": \n\t" + "Departs:         " + AirportNames.getName(Source) + " at " + dTime +
                "  \n\t" + "Arrives:         " + AirportNames.getName(Dest) + " at " + aTime +
                "  \n\t" + "Flight Duration: " +   String.format("%02d hours, %02d minutes",
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
