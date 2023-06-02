import java.io.IOException;
import java.util.Scanner;

public class AdminMenu {

    /**
     * print the primary menu of admin
     */
    public void printAdminMenu() {
        System.out.println("\n\t\t\t\t\t\t\u001b[33m<<Admin Menu OPTIONS>>\u001b[0m\t\t");
        System.out.println("<1> Add");
        System.out.println("<2> update");
        System.out.println("<3> Remove");
        System.out.println("<4> Flight Schedules");
        System.out.println("<0> Sign out");
    }

    /**
     * get command from admin to perform his / her tasks :
     * command 1 : add flight
     * command 2 : update flight
     * command 3 : remove flight
     * command 4 : show flight schedule
     *
     * @param checkFormat  checks the format of inputs
     * @param input        the instance of Scanner class to get input
     * @param fileDatabase has the requisite information
     */
    public void adminMenu(CheckFormat checkFormat, Scanner input, FileDatabase fileDatabase) throws IOException {

        printAdminMenu();
        String command = input.next();

        while (!command.equals("0")) {
            switch (command) {
                case "1" -> addFlight(checkFormat, input, fileDatabase);
                case "2" -> updateFile(input, fileDatabase, checkFormat);
                case "3" -> removeFile(fileDatabase, input);
                case "4" -> showChart(fileDatabase);
                default -> System.out.println("\u001b[31mNot valid !\u001b[0m\nPlease try again.");
            }
            printAdminMenu();
            command = input.next();
        }
    }

    /**
     * admin enters the information of  the flight and adds them in File
     *
     * @param checkFormat  checks the format of inputs
     * @param input        the instance of Scanner class to get input
     * @param fileDatabase has the requisite information
     */
    public void addFlight(CheckFormat checkFormat, Scanner input, FileDatabase fileDatabase) throws IOException {

        System.out.println("please enter flightId :");
        String flightId = input.next();
        while (fileDatabase.flightsFile.searchFlight(fileDatabase.checkFileFormat, 0, flightId)[0] != null) {
            System.out.println("This Id exists.");
            System.out.println("Please enter flightId :");
            flightId = input.next();
        }

        System.out.println("please enter origin :  <enter the first letter in upper case>");
        String origin = input.next();
        while (!checkFormat.checkLetters(origin)) {
            System.out.println("\u001b[31mNot valid !\u001b[0m");
            System.out.println("please enter origin :  <enter the first letter in upper case>");
            origin = input.next();
        }

        System.out.println("please enter destination :   <enter the first letter in upper case>");
        String destination = input.next();
        while (origin.equals(destination) || !checkFormat.checkLetters(destination)) {
            System.out.println("\u001b[31mNot valid !\u001b[0m");
            System.out.println("please enter destination :   <enter the first letter in upper case>");
            destination = input.next();
        }

        System.out.println("please enter date :    <enter zero wherever required and use \u001b[31m/\u001b[0m >");
        String date = input.next();
        while (!checkFormat.checkDate(date)) {
            System.out.println("\u001b[31mNot valid !\u001b[0m");
            System.out.println("please enter date :    <enter zero wherever required and use \u001b[31m/\u001b[0m >");
            date = input.next();
        }

        System.out.println("please enter time :    <enter zero wherever required and use \u001b[31m:\u001b[0m >");
        String time = input.next();
        while (!checkFormat.checkTime(time)) {
            System.out.println("\u001b[31mNot valid !\u001b[0m");
            System.out.println("please enter time :    <enter zero wherever required and use \u001b[31m:\u001b[0m >");
            time = input.next();
        }

        System.out.println("please enter price :");
        String price = input.next();
        while (checkFormat.isNumeric(price) <= 0 || price.length() > 10) {
            System.out.println("\u001b[31mNot valid !\u001b[0m");
            System.out.println("please enter price :");
            price = input.next();
        }

        System.out.println("please enter seats :");
        String seat = input.next();
        while (checkFormat.isNumeric(seat) <= 0) {
            System.out.println("\u001b[31mNot valid !\u001b[0m");
            System.out.println("please enter seats :");
            seat = input.next();
        }

        Flight addedInfo = new Flight(flightId, origin, destination, date, time, String.valueOf(price), String.valueOf(seat));
        fileDatabase.flightsFile.writeFlightsFile(addedInfo, fileDatabase.checkFileFormat);
        System.out.println("This action was done successfully.\n");
    }

    /**
     * remove the flight based on flightId
     * just remove the flights that aren't reserved
     *
     * @param fileDatabase has the requisite information
     * @param input        the instance of Scanner class to get input
     */

    public void removeFile(FileDatabase fileDatabase, Scanner input) throws IOException {

        int count = 0;
        System.out.println("Please enter the flightId that you want to remove :");
        String removeId = input.next();

        if (fileDatabase.ticketsFile.searchTicket(fileDatabase.checkFileFormat, 40, removeId)[0] != null) {
            System.out.println("You can't remove this flight ; it has already booked.\n");
            count = 1;
        }

        if (count == 0) {
            if (fileDatabase.flightsFile.searchFlight(fileDatabase.checkFileFormat, 0, removeId)[0] != null) {
                fileDatabase.flightsFile.removeFlights(fileDatabase.checkFileFormat, removeId);
                System.out.println("This action was done successfully.\n");
            } else
                System.out.println("This Id wasn't found !\n");
        }
    }

    /**
     * update the branches of flight based on flightId
     * just update the flights that aren't reserved
     *
     * @param input        the instance of Scanner class to get input
     * @param fileDatabase has the requisite information
     * @param checkFormat  checks the format of inputs
     */

    public void updateFile(Scanner input, FileDatabase fileDatabase, CheckFormat checkFormat) throws IOException {

        int temp = 0;
        int count = 0;

        System.out.println("Please enter the flightId that you want to update :");
        String updateId = input.next();

        if (fileDatabase.ticketsFile.searchTicket(fileDatabase.checkFileFormat, 40, updateId)[0] != null) {
            System.out.println("You can't update this flight ; it has already booked.\n");
            count = 1;
        }

        if (count == 0) {
            if (fileDatabase.flightsFile.searchFlight(fileDatabase.checkFileFormat, 0, updateId)[0] != null) {
                String number = showSubMenu(input);
                switch (number) {
                    case "1" -> {
                        System.out.println("Please enter new Id :");
                        String newId = input.next();
                        while (fileDatabase.flightsFile.searchFlight(fileDatabase.checkFileFormat, 0, newId)[0] != null) {
                            System.out.println("This Id exists.");
                            System.out.println("Please enter flightId :");
                            newId = input.next();
                        }
                        fileDatabase.flightsFile.updateFlights(fileDatabase.checkFileFormat, 0, 0, updateId, newId);
                    }
                    case "2" -> {
                        System.out.println("Please enter new origin :  <enter the first letter in upper case>");
                        String newOrigin = input.next();
                        while (fileDatabase.flightsFile.searchField(updateId, fileDatabase.checkFileFormat, 40).equals(newOrigin) || !checkFormat.checkLetters(newOrigin)) {
                            System.out.println("\u001b[31mNot valid !\u001b[0m");
                            System.out.println("Please enter new origin :  <enter the first letter in upper case>");
                            newOrigin = input.next();
                        }
                        fileDatabase.flightsFile.updateFlights(fileDatabase.checkFileFormat, 0, 20, updateId, newOrigin);
                    }
                    case "3" -> {
                        System.out.println("Please enter new Destination :   <enter the first letter in upper case>");
                        String newDestination = input.next();
                        while (fileDatabase.flightsFile.searchField(updateId, fileDatabase.checkFileFormat, 20).equals(newDestination) || !checkFormat.checkLetters(newDestination)) {
                            System.out.println("\u001b[31mNot valid !\u001b[0m");
                            System.out.println("Please enter new Destination :   <enter the first letter in upper case>");
                            newDestination = input.next();
                        }
                        fileDatabase.flightsFile.updateFlights(fileDatabase.checkFileFormat, 0, 40, updateId, newDestination);
                    }
                    case "4" -> {
                        System.out.println("Please enter new Date :     <enter zero wherever required and use \u001b[31m/\u001b[0m >");
                        String newDate = input.next();
                        while (!checkFormat.checkDate(newDate)) {
                            System.out.println("\u001b[31mNot valid !\u001b[0m");
                            System.out.println("Please enter new Date :     <enter zero wherever required and use \u001b[31m/\u001b[0m >");
                            newDate = input.next();
                        }
                        fileDatabase.flightsFile.updateFlights(fileDatabase.checkFileFormat, 0, 60, updateId, newDate);
                    }
                    case "5" -> {
                        System.out.println("Please enter new Time :     <enter zero wherever required and use \u001b[31:\u001b[0m >");
                        String newTime = input.next();
                        while (!checkFormat.checkTime(newTime)) {
                            System.out.println("\u001b[31mNot valid !\u001b[0m");
                            System.out.println("Please enter new Time :     <enter zero wherever required and use \u001b[31:\u001b[0m >");
                            newTime = input.next();
                        }
                        fileDatabase.flightsFile.updateFlights(fileDatabase.checkFileFormat, 0, 80, updateId, newTime);
                    }
                    case "6" -> {
                        System.out.println("Please enter new Price :");
                        String newPrice = input.next();
                        while (checkFormat.isNumeric(newPrice) <= 0 || newPrice.length() > 10) {
                            System.out.println("\u001b[31mNot valid !\u001b[0m");
                            System.out.println("Please enter new Price :");
                            newPrice = input.next();
                        }
                        fileDatabase.flightsFile.updateFlights(fileDatabase.checkFileFormat, 0, 100, updateId, String.valueOf(newPrice));
                    }
                    case "7" -> {
                        System.out.println("Please enter new capacity for seats :");
                        String newSeats = input.next();
                        while (checkFormat.isNumeric(newSeats) < 0) {
                            System.out.println("\u001b[31mNot valid !\u001b[0m");
                            System.out.println("Please enter new capacity for seats :");
                            newSeats = input.next();
                        }
                        fileDatabase.flightsFile.updateFlights(fileDatabase.checkFileFormat, 0, 120, updateId, String.valueOf(newSeats));
                    }
                    default -> {
                        System.out.println("This branch wasn't found !\n");
                        temp = 1;
                    }
                }
                if (temp == 0)
                    System.out.println("This action was done successfully.\n");
            } else
                System.out.println("This Id wasn't found !\n");
        }
    }

    /**
     * show the menu of fields
     *
     * @param input the instance of Scanner class to get input
     * @return the field that admin wants to update
     */
    public String showSubMenu(Scanner input) {
        System.out.println("\nWhich branch of flight do you want to update ?\n");
        System.out.println("1-FlightId\t 2-origin\t3-Destination\t 4-Date\t  5-Time\t 6-Price\t7-seats\n");
        System.out.println("Please enter the number :");
        return input.next();
    }

    /**
     * print and show flight schedule
     *
     * @param fileDatabase has the requisite information
     */

    public void showChart(FileDatabase fileDatabase) throws IOException {
        System.out.println("\t\tFlightId             Origin           Destination              Date              Time                Price              Seats             ");
        System.out.print("\t" + "\u001b[35m-\u001b[0m".repeat(130));

        for (long i = 0; i < fileDatabase.flightsFile.getFlightsFile().length() / Flight.FIX_BYTE; i++) {
            System.out.print("\n\t\t " + fileDatabase.flightsFile.readFlightsFile(fileDatabase.checkFileFormat, i));
            System.out.print("\n\t" + "\u001b[35m-\u001b[0m".repeat(130));
        }
    }
}
