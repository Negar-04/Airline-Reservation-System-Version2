import java.io.IOException;
import java.util.Scanner;

public class Menu {
    private final AdminMenu adminmenu = new AdminMenu();
    private final PassengerMenu passengermenu = new PassengerMenu();

    /**
     * print and show the start menu
     * press (1) to sign in
     * press (2) to sign up
     * press (3) to exit from the program
     *
     * @param checkFormat  checks the format of inputs
     * @param input        the instance of Scanner class to get input
     * @param fileDatabase has the requisite information
     */
    public void startMenu(CheckFormat checkFormat, Scanner input, FileDatabase fileDatabase) throws IOException {

        System.out.println("\n\033[94m----------------\u001b[0mWELCOME TO AIRLINE RESERVATION SYSTEM\033[94m----------------\u001b[0m\n");
        System.out.println("\t\t\t\t\t\t\u001b[33m<<MENU OPTIONS>>\u001b[0m\t\t\t\n");
        System.out.println("Please choose one of these :");
        System.out.println("<1> Sign in");
        System.out.println("<2> Sign up");
        System.out.println("<3> Exit");
        String command = input.next();

        while (!command.equals("3")) {
            switch (command) {
                case "1" -> signIn(checkFormat, input, fileDatabase);
                case "2" -> signUp(input, fileDatabase);
                default -> System.out.println("\u001b[31mNot valid !\u001b[0m\nPlease try again.");
            }

            System.out.println("\t\t\t\t\t\t <<MENU OPTIONS>>\t\t\t\n");
            System.out.println("Please choose one of these :");
            System.out.println("<1> Sign in");
            System.out.println("<2> Sign up");
            System.out.println("<3> Exit");
            command = input.next();
        }
    }

    /**
     * get the username and password ---> create new account for users
     * don't accept existed username
     *
     * @param input        the instance of Scanner class to get input
     * @param fileDatabase has the requisite information
     */

    public void signUp(Scanner input, FileDatabase fileDatabase) throws IOException {
        System.out.println("Please enter username:");
        String username = input.next();

        while (fileDatabase.passengersFile.searchPassenger(fileDatabase.checkFileFormat, 0, username)[0] != null || username.equals("negar")) {
            System.out.println("This username exists !\nPlease enter another username :");
            username = input.next();
        }

        System.out.println("Please enter password:");
        String password = input.next();

        Passenger passenger = new Passenger(username, password, "0");
        fileDatabase.passengersFile.writePassengersFile(passenger, fileDatabase.checkFileFormat);
    }

    /**
     * get username and password from admin and passengers to log into their accounts
     *
     * @param checkFormat  checks the format of inputs
     * @param input        the instance of Scanner class to get input
     * @param fileDatabase has the requisite information
     */

    public void signIn(CheckFormat checkFormat, Scanner input, FileDatabase fileDatabase) throws IOException {

        System.out.println("Please enter your username:");
        String username = input.next();
        System.out.println("Please enter your password:");
        String password = input.next();
        Passenger passenger = new Passenger(username, password, "0");
        int count = 0;

        if (passenger.getUsername().equals(fileDatabase.checkFileFormat.fixStringToRead(fileDatabase.adminsFile.getAdminsFile(), 0, 20)) && passenger.getPassword().equals(fileDatabase.checkFileFormat.fixStringToRead(fileDatabase.adminsFile.getAdminsFile(), 20, 20))) {
            adminmenu.adminMenu(checkFormat, input, fileDatabase);
            count = 7;
        }

        if (fileDatabase.passengersFile.searchPassenger(fileDatabase.checkFileFormat, 0, passenger.getUsername(), passenger.getPassword())[0] != null) {
            passengermenu.passengersMenu(passenger, checkFormat, input, fileDatabase);
            count = 1;
        }

        if (count == 0)
            System.out.println("This account hasn't been created yet !\nPlease sign up first.");
    }

}
