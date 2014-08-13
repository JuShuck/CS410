package edu.pdx.cs410J.jushuck.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.List;
import java.util.Map;

@RemoteServiceRelativePath("add")
public interface AirlineService extends RemoteService {

    /**
     * Adds a flight to the Server (Based on the Airline Name)
     */
    public void add(Flight toAdd, String name);

    /**
     * Returns the Airlines to print
     */
    public Map<String, Airline> print();

    /**
     * Returns a List of all the flights in the search
     */
    public List<Flight> search(String name, String source, String destination);

}
