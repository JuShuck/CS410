package edu.pdx.cs410J.jushuck.client;

import edu.pdx.cs410J.AbstractFlight;


public class Flight extends AbstractFlight {

    /**
     * The Flight number
     */
    int flightNum;

    /**
     * The Source (Departure Airport)
     */
    String source;

    /**
     * The Departure time in the form MM/dd/yyyy hh:mm a
     */
    String departTime;

    /**
     * The Destination (Arrival Airport)
     */
    String destination;

    /**
     * The Arrival time in the form MM/dd/yyyy hh:mm a
     */
    String arriveTime;

    /**
     * Default constructor for a flight
     * @param num           Flight number
     * @param src           the 3-Character Source code
     * @param depart    the departure date in the format of MM/dd/yyyy hh:mm a
     * @param dest          the 3-Character Destination code
     * @param arrival   the arrival date in the format of MM/dd/yyyy hh:mm a
     */
    public Flight(int num, String src, String depart, String dest, String arrival) {
        this.flightNum = num;
        this.source = src.toUpperCase();
        this.departTime = depart;
        this.destination = dest.toUpperCase();
        this.arriveTime = arrival;
    }

    /**
     * Constructor
     */
    public Flight() {
    }

    /**
     * Getter function that returns the flight number
     * @return          the flight number as an int
     */
    @Override
    public int getNumber() {
        return flightNum;
    }

    /**
     * Getter function that returns the 3-character source code
     * @return          3-character source code
     */
    @Override
    public String getSource() {
        return source;
    }

    /**
     * Getter function that returns the departure time in the form of MM/dd/yyyy hh:mm
     * @return          departure time as a string
     */
    @Override
    public String getDepartureString() {
        return departTime;
    }

    /**
     * Getter function that returns the 3-character destination code
     * @return          3-character destination code
     */
    @Override
    public String getDestination() {
        return destination;
    }

    /**
     * Getter function that returns the arrival time in the form of MM/dd/yyyy hh:mm
     * @return          arrival time as a string
     */
    @Override
    public String getArrivalString() {
        return arriveTime;
    }
}