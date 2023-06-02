import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FlightsFile {
    private RandomAccessFile flightsFile;

    public RandomAccessFile getFlightsFile() {
        return flightsFile;
    }

    private final int FIX_SIZE = 20;
    File file;

    public FlightsFile(CheckFileFormat checkFileFormat) throws IOException {

        this.file = new File("FlightsFile.dat");
        if (!file.exists()) {
            this.flightsFile = new RandomAccessFile("FlightsFile.dat", "rw");
            writeDefaultFlights(checkFileFormat);
        }
        if (flightsFile == null) {
            this.flightsFile = new RandomAccessFile("FlightsFile.dat", "rw");
        }
    }

    /**
     * write the information of each flight in file
     *
     * @param flight          has the information of flight
     * @param checkFileFormat checks the format of data to read or write in file
     */

    public void writeFlightsFile(Flight flight, CheckFileFormat checkFileFormat) throws IOException {
        flightsFile.seek(flightsFile.length());
        flightsFile.writeChars(checkFileFormat.fixStringToWrite(flight.getFlightId(), FIX_SIZE));
        flightsFile.writeChars(checkFileFormat.fixStringToWrite(flight.getOrigin(), FIX_SIZE));
        flightsFile.writeChars(checkFileFormat.fixStringToWrite(flight.getDestination(), FIX_SIZE));
        flightsFile.writeChars(checkFileFormat.fixStringToWrite(flight.getDate(), FIX_SIZE));
        flightsFile.writeChars(checkFileFormat.fixStringToWrite(flight.getTime(), FIX_SIZE));
        flightsFile.writeChars(checkFileFormat.fixStringToWrite(flight.getPrice(), FIX_SIZE));
        flightsFile.writeChars(checkFileFormat.fixStringToWrite(flight.getSeat(), FIX_SIZE));
    }

    /**
     * read the information of flight from file
     *
     * @param checkFileFormat checks the format of data to read or write in file
     * @param index           the number of record in file of flights
     * @return the information of desired flight
     */

    public String readFlightsFile(CheckFileFormat checkFileFormat, long index) throws IOException {
        return checkFileFormat.fixStringToRead(flightsFile, index * Flight.FIX_BYTE, 140);
    }

    /**
     * search flight based on data
     *
     * @param checkFileFormat checks the format of data to read or write in file
     * @param startSize       the initial field of record
     * @param values          the information of flight
     * @return the information of desired flight
     */
    public String[] searchFlight(CheckFileFormat checkFileFormat, long startSize, String... values) throws IOException {
        String[] searchFlights = new String[100];
        int count = 0;
        int j = 0;
        long temp = startSize;

        for (long i = 0; i < flightsFile.length() / Flight.FIX_BYTE; i++) {
            long size = i * Flight.FIX_BYTE;

            for (String value : values) {
                if ((checkFileFormat.fixStringToRead(flightsFile, size + temp * 2, FIX_SIZE).equals(value))) {
                    count++;
                    if (count == values.length)
                        searchFlights[j++] = readFlightsFile(checkFileFormat, i);
                }
                temp += 20;
            }
            count = 0;
            temp = startSize;
        }
        return searchFlights;
    }

    /**
     * search and return just one field of the flight
     *
     * @param flightId        the requisite data to search
     * @param checkFileFormat checks the format of data to read or write in file
     * @param startField      the initial field of record
     * @return the desired field of flight
     */
    public String searchField(String flightId, CheckFileFormat checkFileFormat, int startField) throws IOException {
        String searchFlight = searchFlight(checkFileFormat, 0, flightId)[0];
        StringBuilder str = new StringBuilder();

        for (int i = startField; i < FIX_SIZE + startField && i < searchFlight.length(); i++) {
            str.append(searchFlight.toCharArray()[i]);
        }
        return str.substring(0).trim();
    }

    /**
     * remove the flight based on removeId that has gotten in admin menu
     *
     * @param checkFileFormat checks the format of data to read or write in file
     * @param removeId        hat has gotten from admin in admin menu
     */
    public void removeFlights(CheckFileFormat checkFileFormat, String removeId) throws IOException {
        String searchFlights = searchFlight(checkFileFormat, 0, removeId)[0];
        if (searchFlights != null) {
            for (int i = 0; i < flightsFile.length(); i += Flight.FIX_BYTE) {
                if (readFlightsFile(checkFileFormat, i / Flight.FIX_BYTE).equals(searchFlights)) {
                    for (int j = i; j < flightsFile.length() - Flight.FIX_BYTE; j += Flight.FIX_BYTE) {
                        String data = checkFileFormat.fixStringToRead(flightsFile, j + Flight.FIX_BYTE, 140);
                        flightsFile.seek(j);
                        flightsFile.writeChars(checkFileFormat.fixStringToWrite(data, 140));
                    }
                    flightsFile.setLength(flightsFile.length() - Flight.FIX_BYTE);
                    break;
                }
            }
        }
    }

    /**
     * update the branches of flight based on flightId in file
     *
     * @param checkFileFormat checks the format of data to read or write in file
     * @param startSize       the initial field of record
     * @param startField      the related field of flight that admin wants to update
     * @param flightId        the requisite data to search
     * @param newValue        the replacement quantity
     */
    public void updateFlights(CheckFileFormat checkFileFormat, long startSize, long startField, String flightId, String newValue) throws IOException {
        String searchFlights = searchFlight(checkFileFormat, startSize, flightId)[0];

        if (searchFlights != null) {
            for (long i = 0; i < flightsFile.length() / Flight.FIX_BYTE; i++) {
                if ((readFlightsFile(checkFileFormat, i).equals(searchFlights))) {
                    flightsFile.seek(i * Flight.FIX_BYTE + startField * 2);
                    flightsFile.writeChars(checkFileFormat.fixStringToWrite(newValue, FIX_SIZE));
                }
            }
        }
    }

    /**
     * write default flights in file
     *
     * @param checkFileFormat checks the format of data to read or write in file
     */
    public void writeDefaultFlights(CheckFileFormat checkFileFormat) throws IOException {
        writeFlightsFile(new Flight("WX-12", "Yazd", "Tehran", "1401/12/10", "12:30", "700000", "51"), checkFileFormat);
        writeFlightsFile(new Flight("WZ-15", "Mashhad", "Ahvaz", "1401/12/11", "8:00", "900000", "245"), checkFileFormat);
        writeFlightsFile(new Flight("BG-22", "Shiraz", "Tabriz", "1401/12/12", "22:30", "1100000", "12"), checkFileFormat);
        writeFlightsFile(new Flight("bg-20", "Tehran", "Kish", "1402/01/20", "22:00", "1359000", "90"), checkFileFormat);
        writeFlightsFile(new Flight("LK-11", "Isfahan", "Tabriz", "1402/01/26", "08:10", "1009000", "121"), checkFileFormat);
        writeFlightsFile(new Flight("wx-12", "Mashhad", "Yazd", "1402/02/04", "00:30", "645000", "14"), checkFileFormat);
        writeFlightsFile(new Flight("BG-28", "Shiraz", "Kermanshah", "1402/01/28", "10:45", "1770000", "64"), checkFileFormat);
        writeFlightsFile(new Flight("AB-12", "Ahvaz", "Tehran", "1402/02/06", "16:30", "985000", "29"), checkFileFormat);
        writeFlightsFile(new Flight("wz-10", "Kish", "Yazd", "1402/01/31", "17:00", "1200000", "62"), checkFileFormat);
        writeFlightsFile(new Flight("lk-14", "Mashhad", "Yazd", "1402/02/11", "21:00", "2100000", "31"), checkFileFormat);

    }

}
