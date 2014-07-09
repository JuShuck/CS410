package edu.pdx.cs410J.jushuck;

import edu.pdx.cs410J.AbstractAirline;
import edu.pdx.cs410J.AbstractFlight;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class Airline extends AbstractAirline {

    String airlineName;

    List<Flight> flights = new ArrayList<>();

    /**
     * The default constructor for an airline object
     * @param name      the name of the airline as a string
     * @param f         a flight to add to the list of flights
     */
    public Airline(String name, Flight f) {
        this.airlineName = name;
        addFlight(f);
    }

    /**
     * Getter function that returns the name of the airline
     * @return              the airline name as a string
     */
    @Override
    public String getName() {
        return airlineName;
    }

    /**
     * Adds the specific flight to the list of flights
     * @param abstractFlight        the flight to be added
     */
    @Override
    public void addFlight(AbstractFlight abstractFlight) {
        flights.add((Flight)abstractFlight);
    }

    /**
     * Getter function that returns the arrayList of flights for this specific airline
     * @return              the collection of flights
     */
    @Override
    public Collection getFlights() {
        return flights;
    }
}