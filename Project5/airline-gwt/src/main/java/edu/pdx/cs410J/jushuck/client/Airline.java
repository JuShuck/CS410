package edu.pdx.cs410J.jushuck.client;

import edu.pdx.cs410J.AbstractAirline;
import edu.pdx.cs410J.AbstractFlight;

import java.util.ArrayList;
import java.util.Collection;

public class Airline extends AbstractAirline
{
    /**
     * The collection of flights of an Airline
     */
    private Collection<AbstractFlight> flights = new ArrayList<AbstractFlight>();

    /**
     * The Airline Name
     */
    private String airlineName;

    /**
     * Constructor
     * @param n Sets the Airline Name
     */
    public Airline(String n) {
        airlineName = n;
    }

    /**
     * Constructor
     */
    public Airline() {
    }

    /**
     * Getter for airline Name
     * @return
     */
    public String getName() {
        return airlineName;
    }

    /**
     * Adds a flight to the collection of flights
     * @param flight
     */
    public void addFlight(AbstractFlight flight) {
        this.flights.add(flight);
    }

    /**
     * Getter for the Flights
     * @return
     */
    public Collection getFlights() {
        return this.flights;
    }
}
