package edu.pdx.cs410J.jushuck;


import edu.pdx.cs410J.AbstractAirline;
import edu.pdx.cs410J.AirlineParser;

import java.io.*;


public class Parser implements AirlineParser{

    /**
     * The Airline Name, as passed in from the command line
     */
    String airlineName;

    /**
     * The file Name, as passed in from the command line
     */
    String fileName;

    /**
     * Gets the Airline name and file name for this particular parser
     * @param n     The name of the airline
     * @param f     The name of the file
     */
    public Parser(String n, String f) {
        airlineName = n;
        fileName = f;
    }

    /**
     * Parses through the textfile (if one exsists and is well-formed) and creates flights from a single line in the file.
     * The data from the file is sanitized the same way the data from the command line is sanitized.
     * @return holder           An Airline that is populated with the flights from the text document
     */
    @Override
    public AbstractAirline parse() {

        //Used to hold the created airline object that will eventually be passed back
        Airline holder = new Airline(airlineName);

        try {
            //Used to go through the file line by line
            BufferedReader reader = new BufferedReader(new FileReader(fileName));

            //Holds the text for a particular line in the file
            String line;

            //Counter that counts the number of lines parsed through
            int num = 0;

            //Go through the text file and validate the data and add it to the airline holder
            while ((line = reader.readLine()) != null) {

                String[] lineArgs = line.split(" ");
                //Checks if the file is malformed (by not having enough args on that specific line)
                if(lineArgs.length != 10) {
                    throw new NumberFormatException();
                }

                //If the command line airline doesn't equal the airline in the textfile, exit gracefully
                if(!lineArgs[0].equals(airlineName)) {
                    throw new Exception();
                }

                //Validate the line and add the flight to the list of flights
                Flight flight = Project3.parseArgs(lineArgs);
                holder.addFlight(flight);

                //Adds to the number of lines parsed through
                num++;
            }

            //If the file is empty, exit with a malformed file
            if(num == 0) {
                throw new NumberFormatException();
            }
        //If the file isn't there, create a new file
        } catch (FileNotFoundException e0) {
            File file = new File(fileName);

            //try/catch incase the file cannot be created for some reason
            try {
                file.createNewFile();
            } catch (IOException e1) {
                System.err.println("ERROR: Cannot create new file");
                System.exit(1);
            }
        //Malformed document
        } catch (NumberFormatException e1) {
            System.err.println("ERROR: Malformed text Document");
            System.exit(1);
        //In case, for some reason, a line cannot be processed
        } catch (IOException e2) {
            System.err.println("ERROR: Cannot read lines");
            System.exit(1);
        //If the airline name passed in doesnot match the airline in the text file.
        } catch (Exception e3) {
            System.err.println("ERROR: Airline Name in file and the command-line do not match");
            System.exit(1);
        }

        return holder;
    }
}
