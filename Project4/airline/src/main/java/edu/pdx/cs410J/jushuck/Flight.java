package edu.pdx.cs410J.jushuck;

import edu.pdx.cs410J.AbstractFlight;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class Flight extends AbstractFlight implements Comparable<Flight> {

    int flightNum;

    String source;

    String departTime;

    String destination;

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
        //Try to parse the date string (Which should come in as a validated string in the form MM/dd/yyyy HH:mm) into a Date type
        Date date = null;
        try {
            date = new SimpleDateFormat("MM/dd/yyyy HH:mm", Locale.ENGLISH).parse(departTime);
        } catch (ParseException e) {
            System.err.println("ERROR: Occured during parsing Depart Date");
            System.exit(1);
        }

        // create a date/time format using DateFormat.SHORT
        DateFormat format;
        format = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.US);
        return format.format(date);
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
        //Try to parse the date string (Which should come in as a validated string in the form MM/dd/yyyy HH:mm) into a Date type
        Date date = null;
        try {
            date = new SimpleDateFormat("MM/dd/yyyy HH:mm", Locale.ENGLISH).parse(arriveTime);
        } catch (ParseException e) {
            System.err.println("ERROR: Occured during Parsing Arrival Date");
            System.exit(1);
        }

        // create a date/time format using DateFormat.SHORT
        DateFormat format;
        format = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.US);
        return format.format(date);
    }

    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     * <p/>
     * <p>The implementor must ensure <tt>sgn(x.compareTo(y)) ==
     * -sgn(y.compareTo(x))</tt> for all <tt>x</tt> and <tt>y</tt>.  (This
     * implies that <tt>x.compareTo(y)</tt> must throw an exception iff
     * <tt>y.compareTo(x)</tt> throws an exception.)
     * <p/>
     * <p>The implementor must also ensure that the relation is transitive:
     * <tt>(x.compareTo(y)&gt;0 &amp;&amp; y.compareTo(z)&gt;0)</tt> implies
     * <tt>x.compareTo(z)&gt;0</tt>.
     * <p/>
     * <p>Finally, the implementor must ensure that <tt>x.compareTo(y)==0</tt>
     * implies that <tt>sgn(x.compareTo(z)) == sgn(y.compareTo(z))</tt>, for
     * all <tt>z</tt>.
     * <p/>
     * <p>It is strongly recommended, but <i>not</i> strictly required that
     * <tt>(x.compareTo(y)==0) == (x.equals(y))</tt>.  Generally speaking, any
     * class that implements the <tt>Comparable</tt> interface and violates
     * this condition should clearly indicate this fact.  The recommended
     * language is "Note: this class has a natural ordering that is
     * inconsistent with equals."
     * <p/>
     * <p>In the foregoing description, the notation
     * <tt>sgn(</tt><i>expression</i><tt>)</tt> designates the mathematical
     * <i>signum</i> function, which is defined to return one of <tt>-1</tt>,
     * <tt>0</tt>, or <tt>1</tt> according to whether the value of
     * <i>expression</i> is negative, zero or positive.
     *
     * @param o the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified object's type prevents it
     *                              from being compared to this object.
     */
    @Override
    public int compareTo(Flight o) {
        //The idea here is to first compare the 3-Letter source code
        int matchStatus = source.compareTo(o.getSource());

        //If there is a matching Airport Code, Sort by departure date.
        if(matchStatus == 0) {
            try {
                Date date1 = new SimpleDateFormat("MM/dd/yyy HH:mm a", Locale.ENGLISH).parse(departTime);
                DateFormat parseFormat = new SimpleDateFormat("MM/dd/yy HH:mm a");
                DateFormat formattingFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm a");

                String departure = formattingFormat.format(parseFormat.parse(o.getDepartureString()));
                Date date2 = new SimpleDateFormat("MM/dd/yyy HH:mm a", Locale.ENGLISH).parse(departure);

                return date1.before(date2) ? -1 : 1;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        //Otherwise return the status found above
        return matchStatus;
    }
    public String print(){
        return this.toString() +  ". And it takes 5 in minutes.\n";
    }
}