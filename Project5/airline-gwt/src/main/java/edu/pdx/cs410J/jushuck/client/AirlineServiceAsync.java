package edu.pdx.cs410J.jushuck.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.List;
import java.util.Map;

public interface AirlineServiceAsync {


    /**
     * Adds a flight to the server based on the Airline Name
     * @param toAdd     The flight to add
     * @param name      The Airline name to add the flight to
     * @param async
     */
    void add(Flight toAdd, String name, AsyncCallback<Void> async);


    /**
     * Returns the Map of Airlines on the Server
     * @param async
     */
    void print(AsyncCallback<Map<String, Airline>> async);

    /**
     * Returns a list of Flights that match the search results
     * @param name      Name of the Airline
     * @param source    3-Letter source
     * @param destination   3-Letter destination
     * @param async
     */
    void search(String name, String source, String destination, AsyncCallback<List<Flight>> async);
}
