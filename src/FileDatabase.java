import java.io.IOException;
import java.io.RandomAccessFile;

public class FileDatabase {
    public FileDatabase() throws IOException {
    }

    CheckFileFormat checkFileFormat = new CheckFileFormat();
    AdminsFile adminsFile = new AdminsFile(checkFileFormat);
    RandomAccessFile passengerFile = new RandomAccessFile("PassengersFile.dat", "rw");
    PassengersFile passengersFile = new PassengersFile(passengerFile);
    FlightsFile flightsFile = new FlightsFile(checkFileFormat);
    RandomAccessFile ticketFile = new RandomAccessFile("TicketsFile.dat", "rw");
    TicketsFile ticketsFile = new TicketsFile(ticketFile);

}
