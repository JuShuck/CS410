package edu.pdx.cs410J.jushuck;

/**
 * Class for formatting messages on the server side.  This is mainly to enable
 * test methods that validate that the server returned expected strings.
 */
public class Messages
{
    /**
     * Gets the mapping count of the airlines on the server
     * @param count     number of airlines
     * @return          pretty string of airline count
     */
    public static String getMappingCount( int count )
    {
        //the single and plural airline correction
        String airline = (count == 1) ? "Airline" : "Airlines";

        return String.format( "Server contains %d %s", count, airline );
    }

    /**
     * Errors out when a parameter is missing
     * @param key       The parameter that's missing (as a string)
     * @return          pretty string of the error message
     */
    public static String missingRequiredParameter( String key )
    {
        return String.format("ERROR: The required parameter \"%s\" is missing", key);
    }

}
