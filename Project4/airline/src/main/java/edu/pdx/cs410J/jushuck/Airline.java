package edu.pdx.cs410J.jushuck;

import edu.pdx.cs410J.AbstractAirline;
import edu.pdx.cs410J.AbstractFlight;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


public class Airline extends AbstractAirline {

    String airlineName;

    List<Flight> flights = new LinkedList<>();

    /**
     * The default constructor for an airline object
     * @param name      the name of the airline as a string
     */
    public Airline(String name) {
        this.airlineName = name;
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
        flights.add((Flight) abstractFlight);
        Collections.sort(flights);;
    }
    /**
     * Getter function that returns the arrayList of flights for this specific airline
     * @return              the collection of flights
     */
    @Override
    public Collection getFlights() {
        return flights;
    }


    public String printAirlineAndFlights(){
        String airString = "Airline name: " + airlineName + " has : \n";
        PrettyPrinter p = new PrettyPrinter();
        for(int i=0; i<flights.size(); ++i){
            airString +=  p.formPrettySentence(flights.get(i).getNumber(),flights.get(i).getSource(), flights.get(i).getDepartureString(),flights.get(i).getDestination(),flights.get(i).getArrivalString());//flights.get(i).toString()  + ". And it takes 1 in minutes.\n";
        }
        System.out.println();
        return airString;
    }
}