package edu.pdx.cs410J.jushuck.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import edu.pdx.cs410J.AirportNames;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * A basic GWT class that makes sure that we can send an airline back from the server
 */
public class AirlineGwt implements EntryPoint {
  public void onModuleLoad() {

      Button b = new Button("Submit add");
      RootPanel rootPanel = RootPanel.get();

      VerticalPanel Headers = new VerticalPanel();

      // READ-ONLY Result Text Box
      Headers.add(new Label());
      final TextArea readOnlyTextBox = new TextArea();
      readOnlyTextBox.setReadOnly(true);
      readOnlyTextBox.setSize("450px","450px");
      Headers.add(readOnlyTextBox);

      VerticalPanel addItem1 = new VerticalPanel();
      VerticalPanel addItem2 = new VerticalPanel();
      HorizontalPanel addHelpSubmit = new HorizontalPanel();

      //RESET TEXT Boxs
      Button b2 = new Button("RESET");
      //Add Button

      //Print all Button
      Button b3 = new Button("Print All");
      //README Button
      Button b4 = new Button("?");

      addHelpSubmit.add(b);
      addHelpSubmit.add(b2);
      addHelpSubmit.add(b3);
      addHelpSubmit.add(b4);
      addItem1.add(new HeaderPanel());

      //Vertical Panel for Adding
      addItem1.add(new Label("Airline Name:"));
      final TextBox airlineName = new TextBox();
      addItem1.add(airlineName);

      addItem1.add(new Label("Departing Airport:"));
      final TextBox departName = new TextBox();
      addItem1.add(departName);

      addItem1.add(new Label("Departure Time:"));
      final TextBox departTime = new TextBox();
      addItem1.add(departTime);

      addItem1.add(addHelpSubmit);

      //Vertical Panel for Adding 2
      addItem2.add(new Label("Flight Number:"));
      final TextBox airlineNum = new TextBox();
      addItem2.add(airlineNum);

      addItem2.add(new Label("Arrival Airport:"));
      final TextBox arrivalName = new TextBox();
      addItem2.add(arrivalName);

      addItem2.add(new Label("Arrival Time:"));
      final TextBox arrivalTime = new TextBox();
      addItem2.add(arrivalTime);

      /**
       *  SEARCH PANEL
       */
      VerticalPanel addItem3 = new VerticalPanel();
      addItem3.add(new Label("Airline Name:"));
      final TextBox searchAirline = new TextBox();
      addItem3.add(searchAirline);

      addItem3.add(new Label("Departure Name:"));
      final TextBox searchDepart = new TextBox();
      addItem3.add(searchDepart);

      addItem3.add(new Label("Arrival Name:"));
      final TextBox searchArrival = new TextBox();
      addItem3.add(searchArrival);

      HorizontalPanel searchButtons = new HorizontalPanel();
      Button bs = new Button("Search");
      Button bs2 = new Button("RESET");
      Button bs3 = new Button("?");
      searchButtons.add(bs);
      searchButtons.add(bs2);
      searchButtons.add(bs3);

      addItem3.add(searchButtons);
      /**
       * ***********
       */


      /**
       * Add Flight/Airline Button
       */
      b.addClickHandler(new ClickHandler() {
          public void onClick(ClickEvent clickEvent) {
              //Window.alert(airlineName.getValue());
              StringBuilder sb = new StringBuilder();
              StringBuilder sb2 = new StringBuilder();

              /*
               * Check if the add Fields are empty. If they are create an
               * error message and do not add.
               */
              if(airlineName.getValue().equals("")) {
                  sb.append("Missing Airline Name. \n");
              }
              if(airlineNum.getValue().equals("")) {
                  sb.append("Missing Flight Number. \n");
              }
              if(departName.getValue().equals("")) {
                  sb.append("Missing Departure Airport. \n");
              }
              if(departTime.getValue().equals("")) {
                  sb.append("Missing Departure Time. \n");
              }
              if(arrivalName.getValue().equals("")) {
                  sb.append("Missing Arrival Airport. \n");
              }
              if(arrivalTime.getValue().equals("")) {
                  sb.append("Missing Arrival Time. \n");
              }


              //If one of the fields is empty
              if(!sb.toString().equals("")) {
                  Window.alert(sb.toString());
                  return;
              }

              //Sanatize the content before doing anything else
              sb2.append(sanatizeAirlineNum(airlineNum.getValue()));
              sb2.append(sanatizeAirport(departName.getValue(),"Departure"));
              sb2.append(sanatizeDateTime(departTime.getValue(), "Departure"));
              sb2.append(sanatizeAirport(arrivalName.getValue(), "Arrival"));
              sb2.append(sanatizeDateTime(arrivalTime.getValue(), "Arrival"));

              //If there are no errors with the data than add, otherwise error and return
              if(sb2.toString().equals("")) {

                  AirlineServiceAsync async = GWT.create( AirlineService.class );

                  //Construct the flight with the data
                  Flight f = new Flight(Integer.parseInt(airlineNum.getValue()),arrivalName.getValue(),arrivalTime.getValue(),departName.getValue(),departTime.getValue());

                  async.add( f,airlineName.getValue(), new AsyncCallback<Void>() {

                      public void onFailure( Throwable ex )
                      {
                          Window.alert(ex.toString());
                      }

                      @Override
                      public void onSuccess(Void aVoid) {
                          readOnlyTextBox.setValue("Airline '" + airlineName.getValue() + "', Flight #" + airlineNum.getValue() + " from " + AirportNames.getName(departName.getValue().toUpperCase()) + " to " + AirportNames.getName(arrivalName.getValue().toUpperCase()) + " added.");

                      }
                  });
              } else {
                  Window.alert(sb2.toString());
              }
          }
      });

      /**
       * RESET Button
       * Resets the textboxes
       */
      b2.addClickHandler(new ClickHandler() {
          @Override
          public void onClick(ClickEvent clickEvent) {
              airlineName.setValue("");
              airlineNum.setValue("");
              arrivalName.setValue("");
              arrivalTime.setValue("");
              departName.setValue("");
              departTime.setValue("");
          }
      });

      /**
       * Print All Button
       * Prints all of the airlines and flights
       */
      b3.addClickHandler(new ClickHandler() {
          public void onClick(ClickEvent clickEvent) {
                  AirlineServiceAsync async = GWT.create( AirlineService.class );

                  async.print(new AsyncCallback<Map<String, Airline>>() {

                      public void onFailure(Throwable ex) {
                          Window.alert(ex.toString());
                      }

                      /**
                       * Print out all of the airlines on the server
                       * @param stringAirlineMap    The Map of all the airlines on the server
                       */
                      @Override
                      public void onSuccess(Map<String, Airline> stringAirlineMap) {
                          if(stringAirlineMap.size() == 0) {
                              Window.alert("ERROR: No Airlines on the Server.");
                              return;
                          }
                                           StringBuilder sb2 = new StringBuilder();
                          sb2.append(" *** PRINT RESULTS FOR '" + airlineName.getValue().toUpperCase() + "' AIRLINES: *** \n\n");
                          for (String n : stringAirlineMap.keySet()){
                              //Hold the airline information
                              Airline holder = stringAirlineMap.get(n);

                              //Get the flights for the airline
                              Collection flights = holder.getFlights();

                              //Pretty print the information for the flights
                              String flightString = PrettyPrint.makePrettyFlights(flights);

                              //Add the flight string to the string builder
                              sb2.append("Airline: '" + stringAirlineMap.get(n).toString().toUpperCase() + "':" +"\n");
                              sb2.append(flightString + "\n\n");
                          }
                          //Put the results in the results box
                          readOnlyTextBox.setValue(sb2.toString());
                      }
                  });
          }
      });

      /**
       * README Button
       * Prints the help for the button
       */
      b4.addClickHandler(new ClickHandler() {
          @Override
          public void onClick(ClickEvent clickEvent) {
              String message = "Airline Service: Add Airline/Flight\n" +
                      "- To Add a new airline/Flight fill out the boxes above with the appropriate Information\n" +
                      "\n" +
                      "Specifics:\n" +
                      "Name: Airline Name\n" +
                      "Departing Airport: 3-Letter Airport Code\n" +
                      "Departure Time: In the form MM/dd/yyyy hh:mm am\n" +
                      "Flight Number: Airline Number\n" +
                      "Arrival Airport: 3-Letter Airport Code\n" +
                      "Arrival Time: In the form MM/dd/yyyy hh:mm am";
              Window.alert(message);
          }
      });

      /**
       * Search Button
       * Search for Flights based on an airline, source and destination
       */
      bs.addClickHandler(new ClickHandler() {

          /**
           * Search for the results on click
           * @param clickEvent
           */
          @Override
          public void onClick(ClickEvent clickEvent) {
              StringBuilder sb = new StringBuilder();
              StringBuilder sb2 = new StringBuilder();

              /*
               * Check if the add Fields are empty. If they are create an
               * error message and do not add.
               */
              if(searchAirline.getValue().equals("")) {
                  sb.append("Missing Airline Name. \n");
              }
              if(searchArrival.getValue().equals("")) {
                  sb.append("Missing Arrival Airport Code. \n");
              }
              if(searchDepart.getValue().equals("")) {
                  sb.append("Missing Departure Airport Code. \n");
              }


              //If one of the fields is empty
              if(!sb.toString().equals("")) {
                  Window.alert(sb.toString());
                  return;
              }

              //Sanatize the data
              sb2.append(sanatizeAirport(searchDepart.getValue(),"Departure"));
              sb2.append(sanatizeAirport(searchArrival.getValue(), "Arrival"));

              //If the data does not have errors 'sb2' should be empty, otherwise print error messages and return
              if(sb2.toString().equals("")) {
                  AirlineServiceAsync async = GWT.create( AirlineService.class );
                  async.search(searchAirline.getValue(), searchArrival.getValue().toUpperCase(), searchDepart.getValue().toUpperCase(),new AsyncCallback<List<Flight>>(){

                      /**
                       * Search Failed
                       * @param throwable
                       */
                      @Override
                      public void onFailure(Throwable throwable) {
                          Window.alert("Error: Cannot find airline on the server.");
                      }

                      @Override
                      public void onSuccess(List<Flight> flights) {
                          if(flights == null) {
                              Window.alert("Error: No flights on the server with that name");
                          }
                          StringBuilder sb = new StringBuilder();
                          for(Flight f : flights) {
                              //Get time difference
                              String flightDiff = getFlightDiff(f.getDepartureString(),f.getArrivalString());

                              sb.append(" *** SEARCH RESULTS FOR '" + searchAirline.getValue().toUpperCase() + "' AIRLINES: *** \n\n");

                              sb.append(  " Flight Num: " + f.getNumber()
                                    + "\n Departs: " + AirportNames.getName(f.getSource().toUpperCase()) + " at " + f.getDepartureString()
                                    + "\n Arrives: " + AirportNames.getName(f.getDestination().toUpperCase()) + " at " + f.getArrivalString()
                                    + "\n Flight Duration: " + flightDiff + " Minutes."  + "\n\n");
                        }
                            // Cannot find a matching flight
                            if(sb.toString().equals("")) {
                                Window.alert("Cannot find flight from " + AirportNames.getName(searchDepart.getValue().toUpperCase()) + " to " + AirportNames.getName(searchArrival.getValue().toUpperCase()) + ".");
                                return;
                            }
                          //Set the results to the textbox
                          readOnlyTextBox.setValue(sb.toString());
                      }
                  });
              } else {
                  Window.alert(sb2.toString());
              }
          }
      });

      /**
       * SEARCH RESET BUTTON
       * Reset the text fields
       */
      bs2.addClickHandler(new ClickHandler() {
          @Override
          public void onClick(ClickEvent clickEvent) {
              searchAirline.setValue("");
              searchDepart.setValue("");
              searchArrival.setValue("");
          }
      });

      /**
       * README Button for search
       * Prints the Help README for searching
       */
      bs3.addClickHandler(new ClickHandler() {
          @Override
          public void onClick(ClickEvent clickEvent) {
              String message = "Airline Service: Search for Flights\n" +
                      "- To Search for Flights fill out the boxes above with the appropriate Information\n" +
                      "Specifics:\n" +
                      "Name: Airline Name\n" +
                      "Departure Airport: 3-Letter Airport Code\n" +
                      "Arrival Airport: 3-Letter Airport Code";
              Window.alert(message);
          }
      });


      // Place the different pannels
      rootPanel.get().add(new Label("RESULTS:"),50,32);
      rootPanel.get().add(Headers,50,50);
      rootPanel.get().add(addItem1, 550, 50);
      rootPanel.get().add(addItem2,800,50);
      rootPanel.get().add(addItem3,550,300);
  }

    /**
     * Verifys that the airline number text field is an Integer
     * @param val [String] Content of the AirlineNum textBox
     * @return  [String] Error message (Empty if no error)
     */
    public String sanatizeAirlineNum(String val) {
        try {
            Integer.parseInt(val);
            return "";
        }  catch (NumberFormatException e) {
            return "Invalid Flight Number: Flight Number must be an Integer \n \n";
        }
    }

    /**
     * Verify that the Airport code is a valid 3-letter code
     * @param val       [String] Contents of the Airport TextBox
     * @param destCode  [String] Used for error messaging (Arrival, Departure)
     * @return          [String] Error message (Empty if no error)
     */
    public String sanatizeAirport(String val,String destCode) {
            if (AirportNames.getName(val.toUpperCase()) != null) {
                return "";
            } else {
                return "Invalid " + destCode + " Airport Code: " + val + " is an Invalid Airport Code \n\n";
            }
    }

    /**
     * Verify that the Date/Time is valid
     * @param val       [String] Contents of the Time box
     * @param destCode  [String] Used for error messaging (Arrival, Departure)
     * @return          [String] Error message (Emprt if no error)
     */
    public String sanatizeDateTime(String val, String destCode) {
        //Default Error Message for Error in Date/Time
        String error = "Invalid " + destCode + " Time: " + val +" is an Invalid Time \n\n";
        String[] timePieces = val.split(" ");

        //Not in the form mm/dd/yyyy hh:mm am
        if(timePieces.length != 3) {
            return error;
        }
        String date = timePieces[0];
        String hours = timePieces[1];
        String ident = timePieces[2];

        // HANDLE THE DATE STRING
        String[] datePieces = date.split("/");
        //Not in the form mm/dd/yyyy
        if(datePieces.length != 3) {
            return error;
        }
        String month = datePieces[0];
        String days = datePieces[1];
        String year = datePieces[2];

        //Converts the sub-strings into integers
        int mm = Integer.parseInt(month);
        int dd = Integer.parseInt(days);
        int yyyy = Integer.parseInt(year);

        //Validates the sub-strings
        if(mm < 1 || mm > 12 || month.length() > 2) {
            return error;
        }

        if(dd < 1 || dd > 31 || days.length() > 2) {
            return error;
        }

        if(yyyy < 0 || year.length() > 4) {
            return error;
        }


        // HANDLE TIME STRING
        String[] tPieces = hours.split(":");
        if(tPieces.length != 2) {
            return error;
        }
        int hh = Integer.parseInt(tPieces[0]);
        int m = Integer.parseInt(tPieces[1]);

        if(hh < 0 || hh > 12 || tPieces[0].length() > 2) {
            return error;
        }

        if(m < 0 || m > 59 || tPieces[1].length() > 2) {
            return error;
        }


        //HANDLE THE IDENTIFYER
        if(!(ident.toLowerCase().equals(new String("am"))) && !(ident.toLowerCase().equals(new String("pm")))) {
            return error;
        }

        return "";
    }

    /**
     * Gets the Difference between the flight dates, in minutes
     * @param departTime    [String] Sanatized Departure date string
     * @param arriveTime    [String] Sanatized Arrival date string
     * @return              [String] Error message (Empty if no error)
     */
    public static String getFlightDiff(String departTime, String arriveTime) {
        Date dTime = new Date(departTime);
        Date aTime = new Date(arriveTime);

        //Turn Milliseconds to Minutes
        long dMiliTime = dTime.getTime();
        long aMiliTime = aTime.getTime();
        long diff = dMiliTime - aMiliTime;

        return Long.toString(diff / 60000);
    }

}
