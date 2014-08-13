package edu.pdx.cs410J.jushuck.client;


import edu.pdx.cs410J.AirportNames;

import java.util.Collection;

public class PrettyPrint {

    /**
     * Makes a pretty string based on a collection of flights
     * @param f     [Collection] of flights to be pretty-fied
     * @return
     */
    public static String makePrettyFlights(Collection<Flight> f) {
        StringBuilder sb = new StringBuilder();
        for(Flight x : f) {

            String flightDiff = AirlineGwt.getFlightDiff(x.getDepartureString(),x.getArrivalString());

            sb.append(  " Flight Num: " + x.getNumber() +
                        "\n Departs: " + AirportNames.getName(x.getSource().toUpperCase()) + " at " + x.getDepartureString()
                      + "\n Arrives: " + AirportNames.getName(x.getDestination().toUpperCase()) + " at " + x.getArrivalString()
                      + "\n Flight Duration: " + flightDiff + " Minutes."  + "\n\n");
        }
        return sb.toString();

    }
}
