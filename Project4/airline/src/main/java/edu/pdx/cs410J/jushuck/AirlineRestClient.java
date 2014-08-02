package edu.pdx.cs410J.jushuck;

import edu.pdx.cs410J.web.HttpRequestHelper;

import java.io.IOException;

/**
 * A helper class for accessing the rest client.  Note that this class provides
 * an example of how to make gets and posts to a URL.  You'll need to change it
 * to do something other than just send key/value pairs.
 */
public class AirlineRestClient extends HttpRequestHelper
{
    private static final String WEB_APP = "airline";
    private static final String SERVLET = "flights";

    private final String url;


    /**
     * Creates a client to the airline REST service running on the given host and port
     * @param hostName The name of the host
     * @param port The port
     */
    public AirlineRestClient( String hostName, int port )
    {
        this.url = String.format( "http://%s:%d/%s/%s", hostName, port, WEB_APP, SERVLET );
    }

    /**
     * Searchs for the flight based on the name, source and destination
     * @param airline       The airline name
     * @param src           3-Letter source code
     * @param dest          3-Letter Arrival code
     * @return              The response from the given parameters
     * @throws IOException
     */
    public Response searchFlight(String airline, String src, String dest) throws IOException{
        return get(this.url, "name", airline, "src", src, "dest", dest);
    }

    /**
     * Adds a flight to the server
     * @param airlineName       Airline name
     * @param flightNumber      Flight number
     * @param source            3-Letter source code
     * @param departTime        Departure time MM/dd/yyyy hh:mm a
     * @param dest              3-Letter destination code
     * @param arriveTime        Arrival time MM/dd/yyyy hh:mm a
     * @return                  The post of the information
     * @throws IOException
     */
    public Response addFlight(String airlineName, String flightNumber, String source, String departTime, String dest, String arriveTime) throws IOException{
        return post(this.url, "name", airlineName, "num", flightNumber, "src", source, "departTime", departTime,
                "dest", dest, "arriveTime", arriveTime);
    }
}
