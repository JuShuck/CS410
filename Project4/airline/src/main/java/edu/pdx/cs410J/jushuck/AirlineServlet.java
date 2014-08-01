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
    private final Map<String, Airline> data = new HashMap<String, Airline>();
    private Airline airline = null;


    @Override
    protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
    {

        response.setContentType( "text/plain" );

        //Gets the parameters from the request.
        String name = getParameter( "name", request );
        String src = getParameter("src", request);
        String dest = getParameter("dest", request);

        if(data.get(name) == null){
            PrintWriter pw = response.getWriter();
            pw.println("ERROR: The airline '" + name + "' doesn't exist");
            System.err.println("ERROR: The airline '" + name + "' doesn't exist");
            return;
        }

        if(name != null && src == null && dest == null) {
            writeValue(name, response);
        }

        if(name != null && src != null && dest != null){
            writeSearch(name, src.toUpperCase(), dest.toUpperCase(), response);
        }

    }

    private void writeValue(String name, HttpServletResponse response) throws IOException {
        PrintWriter pw = response.getWriter();
        pw.println(Messages.getMappingCount(data.size()));

        Airline airline1 = this.data.get(name);
        pw.println(airline1.printAirlineAndFlights());

        pw.flush();
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private void writeSearch( String airName, String src, String dest, HttpServletResponse response ) throws IOException
    {
        PrintWriter pw = response.getWriter();
        pw.println(Messages.getMappingCount(data.size()));
        Airline airline1 = this.data.get(airName);
        Collection flights = airline1.getFlights();
        boolean airLinefound = false;

        for(Object f :flights){
            if(src.equals(((Flight)f).getSource()) && dest.equals(((Flight)f).getDestination())) {
                pw.println(((Flight) f).print());
                airLinefound = true;
            }
        }

        if(!airLinefound){
            pw.println("ERROR: There is no flight from " + src + " to " + dest);
        }
    }

    @Override
    protected void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
    {
        response.setContentType( "text/plain" );
        System.out.println("in post");
        String num = getParameter("num", request);
        if(num == null) {
            missingRequiredParameter(response, num);
            return;
        }
        String source = getParameter("src", request);
        System.out.println(">"+source);
        if(source == null) {
            missingRequiredParameter(response, source);
            return;
        }
        String departTime = getParameter("departTime", request);
        if(departTime == null) {
            missingRequiredParameter(response, departTime);
            return;
        }
        String destination = getParameter("dest", request);
        if(destination == null) {
            missingRequiredParameter(response, destination);
        }
        String arrivalTime = getParameter("arriveTime", request);
        if(arrivalTime == null) {
            missingRequiredParameter(response, arrivalTime);
        }

        Flight f = new Flight(Integer.parseInt(num),source,departTime,destination,arrivalTime);

        String airName = getParameter("name",request);
        if(airName == null) {
            missingRequiredParameter(response, airName);
            return;
        }

        if(this.data.get(airName) == null) {
            Airline newAirline = new Airline(airName);
            this.airline = newAirline;
            this.airline.addFlight(f);
            this.data.put(airName,this.airline);
            System.out.println("New Airline:" + airName +" Created");
        } else {
            this.airline.addFlight(f);
            this.data.put(airName,this.airline);
            System.out.println("Just adding flight");
        }

        response.setStatus( HttpServletResponse.SC_OK);
    }

    private void missingRequiredParameter( HttpServletResponse response, String key )
        throws IOException
    {
        PrintWriter pw = response.getWriter();
        pw.println( Messages.missingRequiredParameter(key));
        pw.flush();
        
        response.setStatus( HttpServletResponse.SC_PRECONDITION_FAILED );
    }

    private String getParameter(String name, HttpServletRequest request) {
      String value = request.getParameter(name);
      if (value == null || "".equals(value)) {
        return null;

      } else {
        return value;
      }
    }

}
