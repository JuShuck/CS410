package edu.pdx.cs410J.jushuck;

/**
 * Class for formatting messages on the server side.  This is mainly to enable
 * test methods that validate that the server returned expected strings.
 */
public class Messages
{
    public static String getMappingCount( int count )
    {
        String airline = (count == 1) ? "Airline" : "Airlines";

        return String.format( "Server contains %d %s", count, airline );
    }

    public static String missingRequiredParameter( String key )
    {
        return String.format("ERROR: The required parameter \"%s\" is missing", key);
    }

}
