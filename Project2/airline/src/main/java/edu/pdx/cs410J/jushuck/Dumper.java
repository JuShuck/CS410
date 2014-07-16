package edu.pdx.cs410J.jushuck;

import edu.pdx.cs410J.AbstractAirline;
import edu.pdx.cs410J.AirlineDumper;

import java.io.*;
import java.util.List;

/**
 * Created by justin on 7/13/2014.
 */
public class Dumper implements AirlineDumper {
    /**
     * The file name to be written to
     */
    String fileName;

    /**
     * Passes the file name for the dump to use
     * @param s
     */
    public Dumper(String s) {
        fileName = s;
    }

    /**
     * Writes the data from the Airline to the file specified
     * @param a         The airline that contains the data to be written
     * @throws IOException          If file cannot be written to
     */
    @Override
    public void dump(AbstractAirline a) throws IOException {

        clearDocument();

        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fileName, true)));

            //Holds the list of Flights the Airline has
            List<Flight> list = (List<Flight>) a.getFlights();

            //Go through each flight and write to the document
            for(Flight f : list) {
                out.println(a.getName() + " "+ f.getNumber() + " " + f.getSource() + " " + f.getDepartureString() + " " + f.getDestination() + " " + f.getArrivalString());
            }

            out.close();
        } catch (IOException e) {
            System.err.println("ERROR: Cannot write to the text file");
            System.exit(1);
        }
    }

    /**
     * Clears the documents previous content
     */
    public void clearDocument() {

        try {
            PrintWriter writer = new PrintWriter(fileName);
            writer.print("");
            writer.close();
        } catch (FileNotFoundException e) {
            System.err.println("ERROR: Cannot write to the text file");
            System.exit(1);
        }

    }
}
