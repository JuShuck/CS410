package edu.pdx.cs410J.jushuck;

import edu.pdx.cs410J.AbstractFlight;


public class Flight extends AbstractFlight {

    int flightNum;

    String source;

    String departTime;

    String destination;

    String arriveTime;

    /**
     * Default constructor for a flight
     * @param num           Flight number
     * @param src           the 3-Character Source code
     * @param departDate    the departure date in the format of MM/dd/yyyy
     * @param departTime    the departure time in the format hh:mm
     * @param dest          the 3-Character Destination code
     * @param arrivalDate   the arrival date in the format of MM/dd/yyyy
     * @param arrivalTime   the arrival time in the format of hh:mm
     */
    public Flight(int num, String src, String departDate,String departTime, String dest, String arrivalDate, String arrivalTime) {
        this.flightNum = num;
        this.source = src;
        this.departTime = departDate + " " + departTime;
        this.destination = dest;
        this.arriveTime = arrivalDate + " " + arrivalTime;
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
