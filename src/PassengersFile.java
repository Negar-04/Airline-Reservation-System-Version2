import java.io.IOException;
import java.io.RandomAccessFile;

public class PassengersFile {
    private final RandomAccessFile passengersFile;
    private final int FIX_SIZE = 20;

    public PassengersFile(RandomAccessFile passengersFile) {
        this.passengersFile = passengersFile;
    }

    /**
     * write the information of each passenger in file
     *
     * @param passenger       has the information of passenger with unique username
     * @param checkFileFormat checks the format of data to read or write in file
     */
    public void writePassengersFile(Passenger passenger, CheckFileFormat checkFileFormat) throws IOException {
        passengersFile.seek(passengersFile.length());
        passengersFile.writeChars(checkFileFormat.fixStringToWrite(passenger.getUsername(), FIX_SIZE));
        passengersFile.writeChars(checkFileFormat.fixStringToWrite(passenger.getPassword(), FIX_SIZE));
        passengersFile.writeChars(checkFileFormat.fixStringToWrite(passenger.getCredit(), FIX_SIZE));
    }

    /**
     * read the information of passengers from file
     *
     * @param checkFileFormat checks the format of data to read or write in file
     * @param index           the number of record in file of passengers
     * @return the information of desired user
     */
    public String readPassengersFile(CheckFileFormat checkFileFormat, long index) throws IOException {
        return checkFileFormat.fixStringToRead(passengersFile, index * Passenger.FIX_BYTE, 60);
    }

    /**
     * search passenger based on data
     *
     * @param checkFileFormat checks the format of data to read or write in file
     * @param startSize       the initial field of record
     * @param values          the information of flight
     * @return the information of desired user
     */
    public String[] searchPassenger(CheckFileFormat checkFileFormat, long startSize, String... values) throws IOException {
        String[] searchPassenger = new String[100];
        int count = 0;
        int j = 0;

        for (long i = 0; i < passengersFile.length() / Passenger.FIX_BYTE; i++) {
            long size = i * Passenger.FIX_BYTE;

            for (String value : values) {
                if ((checkFileFormat.fixStringToRead(passengersFile, size + startSize * 2, FIX_SIZE).equals(value))) {
                    count++;
                    if (count == values.length)
                        searchPassenger[j++] = readPassengersFile(checkFileFormat, i);
                }
                size += 20;
            }
            count = 0;
        }
        return searchPassenger;
    }

    /**
     * search the desired field of the passenger's information
     *
     * @param passenger       has the information of passenger with unique username
     * @param checkFileFormat checks the format of data to read or write in file
     * @param startField      the initial field of record
     * @return the desired field of user's information
     */
    public String searchField(Passenger passenger, CheckFileFormat checkFileFormat, int startField) throws IOException {
        String searchPassenger = searchPassenger(checkFileFormat, 0, passenger.getUsername())[0];
        StringBuilder str = new StringBuilder();
        for (int i = startField; i < FIX_SIZE + startField && i < searchPassenger.length(); i++) {
            str.append(searchPassenger.toCharArray()[i]);
        }
        return str.substring(0).trim();
    }

    /**
     * update the branches of user's information based on username in file
     *
     * @param checkFileFormat checks the format of data to read or write in file
     * @param startSize       the initial field of record
     * @param startField      the related field of flight that admin wants to update
     * @param value           the requisite data to search
     * @param newValue        the replacement quantity
     */
    public void updatePassengers(CheckFileFormat checkFileFormat, long startSize, long startField, String value, String newValue) throws IOException {
        if (searchPassenger(checkFileFormat, startSize, value)[0] != null) {
            for (int i = 0; i < passengersFile.length() / Passenger.FIX_BYTE; i++) {
                if ((readPassengersFile(checkFileFormat, i).equals(searchPassenger(checkFileFormat, startSize, value)[0]))) {
                    passengersFile.seek((long) i * Passenger.FIX_BYTE + startField * 2);
                    passengersFile.writeChars(checkFileFormat.fixStringToWrite(newValue, FIX_SIZE));
                }
            }
        }
    }
}
