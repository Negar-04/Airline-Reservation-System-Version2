import java.io.IOException;
import java.util.Scanner;

public class PassengerMenu {

    /**
     * print the primary menu of passengers
     */
    public void printMenu() {
        System.out.println("\n\t\t\t\t\u001b[33m<<PASSENGER MENU OPTIONS>>\u001b[0m\t\t");
        System.out.println("<1> Change password");
        System.out.println("<2> Search flight tickets");
        System.out.println("<3> Booking ticket");
        System.out.println("<4> Ticket cancellation");
        System.out.println("<5> Booked tickets");
        System.out.println("<6> Add charge");
        System.out.println("<0> Sign out");
    }

    /**
     * get the command from passenger to perform his / her required activities
     *
     * @param passenger    has the information of passenger with unique username
     * @param checkFormat  checks the format of inputs
     * @param input        the instance of Scanner class to get input
     * @param fileDatabase has the requisite information
     */

    public void passengersMenu(Passenger passenger, CheckFormat checkFormat, Scanner input, FileDatabase fileDatabase) throws IOException {

        printMenu();
        String command = input.next();

        while (!command.equals("0")) {
            switch (command) {
                case "1" -> changePassword(fileDatabase, input, passenger);
                case "2" -> searchFlight(input, fileDatabase);
                case "3" -> bookTicket(input, fileDatabase, passenger);
                case "4" -> cancellation(fileDatabase, input, passenger);
                case "5" -> bookedTickets(fileDatabase, passenger);
                case "6" -> increaseCharge(passenger, fileDatabase, input, checkFormat);
                default -> System.out.println("\u001b[31mNot valid !\u001b[0m\nPlease try again.");
            }
            printMenu();
            command = input.next();
        }
    }

    /**
     * First , get previous password from user and then ask the user to enter the new password to change it
     *
     * @param fileDatabase has the requisite information
     * @param input        the instance of Scanner class to get input
     * @param passenger    has the information of passenger with unique username
     */

    public void changePassword(FileDatabase fileDatabase, Scanner input, Passenger passenger) throws IOException {

        int count = 0;
        System.out.println("Please enter your previous password: ");
        String previousPassword = input.next();

        if (fileDatabase.passengersFile.searchPassenger(fileDatabase.checkFileFormat, 0, passenger.getUsername(), previousPassword)[0] != null) {
            System.out.println("Please enter the new password:");
            String newPassword = input.next();
            fileDatabase.passengersFile.updatePassengers(fileDatabase.checkFileFormat, 20, 20, previousPassword, newPassword);
            System.out.println("This action was done successfully.\n");
            count = 1;
        }

        if (count == 0)
            System.out.println("Your previous password is wrong.\n");
    }

    /**
     * increase the charge of the passengers' account for booking ticket
     *
     * @param passenger    has the information of passenger with unique username
     * @param fileDatabase fileDatabase has the requisite information
     * @param input        the instance of Scanner class to get input
     * @param checkFormat  checks the format of inputs
     */

    public void increaseCharge(Passenger passenger, FileDatabase fileDatabase, Scanner input, CheckFormat checkFormat) throws IOException {

        System.out.println("Please enter the amount to increase your charge :");
        String charge = input.next();

        String previousCharge = fileDatabase.passengersFile.searchField(passenger, fileDatabase.checkFileFormat, 40);

        while (checkFormat.isNumeric(charge) < 0 || charge.length() > 10) {
            System.out.println("\u001b[31mNot valid !\u001b[0m");
            System.out.println("Please enter the amount to increase your charge :");
            charge = input.next();
        }

        try {
            String sum = String.valueOf(Long.parseLong(charge) + Long.parseLong(previousCharge));
        } catch (Exception e) {
            System.out.println("\u001b[31mNot valid !\u001b[0m");
            System.out.println("Please enter the amount to increase your charge :");
            charge = input.next();
        }

        fileDatabase.passengersFile.updatePassengers(fileDatabase.checkFileFormat, 0, 40, passenger.getUsername(), String.valueOf(Long.parseLong(charge) + Long.parseLong(previousCharge)));
        System.out.print("Your charge is >>> ");
        System.out.println(fileDatabase.passengersFile.searchField(passenger, fileDatabase.checkFileFormat, 40));

    }

    /**
     * reserve flight and book ticket based on flightId
     *
     * @param input        the instance of Scanner class to get input
     * @param fileDatabase fileDatabase has the requisite information
     * @param passenger    has the information of passenger with unique username
     */

    public void bookTicket(Scanner input, FileDatabase fileDatabase, Passenger passenger) throws IOException {
        System.out.println("Please enter the flightId that you want to book:");
        String flightId = input.next();

        if (fileDatabase.flightsFile.searchFlight(fileDatabase.checkFileFormat, 0, flightId)[0] != null) {
            long price = Long.parseLong(fileDatabase.flightsFile.searchField(flightId, fileDatabase.checkFileFormat, 100));
            long charge = Long.parseLong(fileDatabase.passengersFile.searchField(passenger, fileDatabase.checkFileFormat, 40));
            long seat = Long.parseLong(fileDatabase.flightsFile.searchField(flightId, fileDatabase.checkFileFormat, 120));
            if (seat <= 0 || price > charge)
                System.out.println("\nYou can't book the ticket.");

            else {
                fileDatabase.flightsFile.updateFlights(fileDatabase.checkFileFormat, 0, 120, flightId, String.valueOf(--seat));
                fileDatabase.passengersFile.updatePassengers(fileDatabase.checkFileFormat, 0, 40, passenger.getUsername(), String.valueOf(charge - price));
                System.out.print("Your ticketId is >> ");
                System.out.print(fileDatabase.ticketsFile.writeTicketsFile(flightId, passenger, fileDatabase));
                System.out.println();
            }

        } else
            System.out.println("This Id wasn't found.");
    }

    /**
     * search flights based on two filters : origin and destination
     *
     * @param input        the instance of Scanner class to get input
     * @param fileDatabase fileDatabase has the requisite information
     */

    public void searchFlight(Scanner input, FileDatabase fileDatabase) throws IOException {
        System.out.println("Please enter your origin:    <enter the first letter in upper case>");
        String origin = input.next();
        System.out.println("Please enter your destination:    <enter the first letter in upper case>");
        String destination = input.next();

        System.out.println();
        int i = 0;

        if (fileDatabase.flightsFile.searchFlight(fileDatabase.checkFileFormat, 20, origin, destination)[0] == null) {
            System.out.println("There isn't any flights with these specifications.");
        } else {
            System.out.println("\t\tFlightId             Origin           Destination              Date              Time                Price              Seats             ");
            System.out.print("\t" + "\u001b[35m-\u001b[0m".repeat(130));

            while (fileDatabase.flightsFile.searchFlight(fileDatabase.checkFileFormat, 20, origin, destination)[i] != null) {
                System.out.print("\n\t\t " + fileDatabase.flightsFile.searchFlight(fileDatabase.checkFileFormat, 20, origin, destination)[i]);
                System.out.print("\n\t" + "\u001b[35m-\u001b[0m".repeat(130));
                i++;
            }
            System.out.println();
        }
    }

    /**
     * print and show all the booked tickets of each passenger
     *
     * @param fileDatabase fileDatabase has the requisite information
     * @param passenger    has the information of passenger with unique username
     */

    public void bookedTickets(FileDatabase fileDatabase, Passenger passenger) throws IOException {
        int count = 0;
        String[] reserveTickets = fileDatabase.ticketsFile.searchTicket(fileDatabase.checkFileFormat, 0, passenger.getUsername());

        System.out.println("\t\tUsername             TicketId             FlightId             Origin           Destination              Date              Time                Price              Seats             ");
        System.out.print("\t" + "\u001b[35m-\u001b[0m".repeat(180));

        for (long i = 0; i < reserveTickets.length; i++) {
            if (reserveTickets[(int) i] != null) {
                System.out.print("\n\t\t   " + reserveTickets[(int) i] + "\t\t");
                System.out.print("\n\t" + "\u001b[35m-\u001b[0m".repeat(180));
                count++;
            }
        }

        if (count == 0) {
            System.out.println("\n\n<<  You don't reserve any flights.  >>");
        }
    }

    /**
     * cancel and remove the purchased ticket based on ticketId
     *
     * @param fileDatabase fileDatabase has the requisite information
     * @param input        the instance of Scanner class to get input
     * @param passenger    has the information of passenger with unique username
     */

    public void cancellation(FileDatabase fileDatabase, Scanner input, Passenger passenger) throws IOException {
        System.out.println("Please enter the ticketId to cancel your reservation :");
        String ticketId = input.next();

        if (fileDatabase.ticketsFile.searchTicket(fileDatabase.checkFileFormat, 20, ticketId)[0] != null) {
            String flightId = fileDatabase.ticketsFile.searchField(ticketId, fileDatabase.checkFileFormat, 40);
            long seat = Long.parseLong(fileDatabase.ticketsFile.searchField(ticketId, fileDatabase.checkFileFormat, 160));
            long price = Long.parseLong(fileDatabase.ticketsFile.searchField(ticketId, fileDatabase.checkFileFormat, 140));
            long charge = Long.parseLong(fileDatabase.passengersFile.searchField(passenger, fileDatabase.checkFileFormat, 40));

            fileDatabase.flightsFile.updateFlights(fileDatabase.checkFileFormat, 0, 120, flightId, String.valueOf(++seat));
            fileDatabase.passengersFile.updatePassengers(fileDatabase.checkFileFormat, 0, 40, passenger.getUsername(), String.valueOf(price + charge));
            fileDatabase.ticketsFile.removeTickets(fileDatabase.checkFileFormat, ticketId);
            System.out.println("This action was done successfully.\n");

        } else
            System.out.println("This ticketId is invalid.");
    }
}
