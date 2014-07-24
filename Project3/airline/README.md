Project 3
version 1
Due: 7-16-2014 06:00 PM

Specifications for Project 2 can be found on Professor Whitlock's page. This version of the program adds the functionality
to create an Airline Object based on a textFile and then in turn write back to that text document.Below are the usage
as specified in the assignment:

args are (in this order):

    name                The name of the airline
    flightNumber        The flight number
    src                 Three-letter code of departure airport
    departTime          Departure date and time am/pm
    dest                Three-letter code of arrival airport
    arriveTime          Arrival date and time am/pm

options are (options may appear in any order):
    -pretty file     Pretty print the airline's flights to a text file or standard out (file -)
    -textFile file  Where to read/write the airline info
    -print          Prints a description of the new flight
    -README         Prints a README for this project and exits


A few Notes:
* -README has a higher precidence than print and -textFile
* The options '-textFile', '-README', '-pretty' and '-print' are case sensitive in this version
* No option flags can Appear after the 3rd Argument (The max optional flags)
* Date and time should be in the format: mm/dd/yyyy HH:mm a
* 3 Letter airport codes should be case insensitive
* Airline should be pretty printed after the new flight has been added