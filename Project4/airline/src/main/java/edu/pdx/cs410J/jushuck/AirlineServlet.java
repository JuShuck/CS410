package edu.pdx.cs410J.jushuck;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class AirlineServlet extends HttpServlet
{
    // Hash map where all of the Airlines will be stored
    private final Map<String, Airline> data = new HashMap<String, Airline>();

    //Airline variable
    private Airline airline = null;

    /**
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
    {

        response.setContentType( "text/plain" );

        //Gets the parameters from the request.
        String name = getParameter( "name", request );
        String src = getParameter("src", request);
        String dest = getParameter("dest", request);

        //If the request is malformeed, i.e. localhost:8080/airline/flights?name=
        if(name == null) {
            PrintWriter pw = response.getWriter();
            pw.println("ERROR: Cannot find the airline name.");
            System.err.println("ERROR: Cannot find the airline name.");
            return;
        }

        //If there is a name and it's not found in the map, error out
        if(data.get(name) == null){
            PrintWriter pw = response.getWriter();
            pw.println("ERROR: The airline '" + name + "' doesn't exist");
            System.err.println("ERROR: The airline '" + name + "' doesn't exist");
            return;
        }

        //Write the value for a normal request (not a search request)
        if(name != null && src == null && dest == null) {
            writeValue(name, response);
        }

        //Write the value for a search request
        if(name != null && src != null && dest != null){
            writeSearch(name, src.toUpperCase(), dest.toUpperCase(), response);
        }
    }

    /**
     * Write the values for a GET
     * @param name      Airline Name
     * @param response
     * @throws IOException
     */
    private void writeValue(String name, HttpServletResponse response) throws IOException {
        PrintWriter pw = response.getWriter();

        //Prints the number of airlines in the server
        pw.println(Messages.getMappingCount(data.size()));

        Airline airline1 = this.data.get(name);

        //Add the airline to the server
        pw.println(airline1.constructAirlineAndFlightsString());
        pw.flush();

        response.setStatus(HttpServletResponse.SC_OK);
    }

    /**
     * The GET for a search request.
     * @param airName       Airline name
     * @param src           3-letter source
     * @param dest          3-letter destination
     * @param response
     * @throws IOException
     */
    private void writeSearch( String airName, String src, String dest, HttpServletResponse response ) throws IOException
    {
        PrintWriter pw = response.getWriter();
        //Prints the number of airlines
        pw.println(Messages.getMappingCount(data.size()));

        //construct an with the name and get the collection of flights
        Airline airline = this.data.get(airName);
        Collection flights = airline.getFlights();
        boolean airLinefound = false;

        //Writes all of the flights in the collection
        for(Object f :flights){
            if(src.equals(((Flight)f).getSource()) && dest.equals(((Flight)f).getDestination())) {
                pw.println(((Flight) f).print());
                airLinefound = true;
            }
        }

        //Sanatize the locations
        Project4.sanitizeLocation(src);
        Project4.sanitizeLocation(dest);

        //If nothing is written, there were no flights from SRC to DEST, error out
        if(airLinefound == false){
            pw.println("ERROR: There is no flight from " + src.toUpperCase() + " to " + dest.toUpperCase());
        }
    }

    /**
     * Post the request
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
    {
        response.setContentType( "text/plain" );

        //Check for the number paramater
        String num = getParameter("num", request);
        if(num == null) {
            missingRequiredParameter(response, num);
            return;
        }

        //Check for the source parameter
        String source = getParameter("src", request);
        if(source == null) {
            missingRequiredParameter(response, source);
            return;
        }

        //Check for the Depart Time parameter
        String departTime = getParameter("departTime", request);
        if(departTime == null) {
            missingRequiredParameter(response, departTime);
            return;
        }

        //Check for the destination parameter
        String destination = getParameter("dest", request);
        if(destination == null) {
            missingRequiredParameter(response, destination);
        }

        //Check for the Arrival Time parameter
        String arrivalTime = getParameter("arriveTime", request);
        if(arrivalTime == null) {
            missingRequiredParameter(response, arrivalTime);
        }

        //Check for the airline Name parameter
        String airName = getParameter("name",request);
        if(airName == null) {
            missingRequiredParameter(response, airName);
            return;
        }

        //Construct a new flight
        Flight f = new Flight(Integer.parseInt(num),source,departTime,destination,arrivalTime);

        //If the airline is not in the Server,create a new airline
        if (this.data.get(airName) == null) {
            Airline newAirline = new Airline(airName);
            this.airline = newAirline;
            this.airline.addFlight(f);
            this.data.put(airName,this.airline);
            System.out.println("New Airline:" + airName +" Created");
        //Otherwise add the flight to exsisting airline
        } else {
            this.airline.addFlight(f);
            this.data.put(airName,this.airline);
            System.out.println("Flight added to "+ airName);
        }
        response.setStatus( HttpServletResponse.SC_OK);
    }

    /**
     * Errors out with a missing parameter
     * @param response  The response
     * @param key       The key value
     * @throws IOException
     */
    private void missingRequiredParameter( HttpServletResponse response, String key )
        throws IOException
    {
        PrintWriter pw = response.getWriter();
        pw.println( Messages.missingRequiredParameter(key));
        pw.flush();
        
        response.setStatus( HttpServletResponse.SC_PRECONDITION_FAILED );
    }

    /**
     * Gets the parameter from the request
     * @param name          Name of the parameter
     * @param request       the request
     * @return
     */
    private String getParameter(String name, HttpServletRequest request) {
      String value = request.getParameter(name);
      if (value == null || "".equals(value)) {
        return null;

      } else {
        return value;
      }
    }
}
