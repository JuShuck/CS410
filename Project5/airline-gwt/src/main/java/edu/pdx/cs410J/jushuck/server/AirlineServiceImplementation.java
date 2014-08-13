package edu.pdx.cs410J.jushuck.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import edu.pdx.cs410J.jushuck.client.Airline;
import edu.pdx.cs410J.jushuck.client.AirlineService;
import edu.pdx.cs410J.jushuck.client.Flight;

import java.util.*;

public class AirlineServiceImplementation extends RemoteServiceServlet implements AirlineService {

    // Hash map where all of the Airlines will be stored
    private final Map<String, Airline> data = new HashMap<String, Airline>();

    //The Holder for an Airline
    private Airline airline;

    /**
     * Adds a flight to the Map
     * @param toAdd [Flight] To be added
     * @param name  [String] The name of the Airline to add to
     */
    public void add(Flight toAdd, String name) {
        //The airline doesn't exsist, create a new Airline
       if (this.data.get(name) == null) {
            Airline newAirline = new Airline(name);
            this.airline = newAirline;
            this.airline.addFlight(toAdd);
            this.data.put(name,this.airline);

            //Otherwise add the flight to exsisting airline
        } else {
            this.airline.addFlight(toAdd);
            this.data.put(name,this.airline);
        }
    }

    /**
     * Returns the Map for printing purposes
     * @return  [Map] The map of Airlines on the Server
     */
    @Override
    public Map<String, Airline> print() {
        return data;
    }

    /**
     * Returns a List of all the flights That match the service
     * @param name      [String] Name of the Airline
     * @param source    [String] Name of the source (Departure Airline)
     * @param destination   [String] Name of the Destination (Arrival Airport)
     */
    @Override
    public List<Flight> search(String name, String source, String destination) {
        List<Flight> list = new ArrayList<Flight>();

        //Returns early if the map is empty
        if(data.size() == 0) {
            return null;
        }

        //Holds the airline to search for flights
        Airline airline = this.data.get(name);

        //If the airline is not found in the map
        if(airline == null) {
            return null;
        }

        //Holds the flights for the airline
        Collection flights = airline.getFlights();

        //Writes all of the flights in the collection
        for(Object f :flights){
            if(source.equals(((Flight)f).getSource()) && destination.equals(((Flight)f).getDestination())) {
                list.add((Flight)f);
            }
        }
        return list;
    }

}
