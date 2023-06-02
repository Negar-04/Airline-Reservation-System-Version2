import java.io.IOException;
import java.io.RandomAccessFile;

public class TicketsFile {
    private final RandomAccessFile ticketsFile;
    private final int FIX_SIZE = 20;

    public TicketsFile(RandomAccessFile ticketsFile) {
        this.ticketsFile = ticketsFile;
    }

    /**
     * write the information of purchase ticket
     *
     * @param flightId     that user wants to book it
     * @param passenger    has the information of passenger with unique username
     * @param fileDatabase has the requisite information
     * @return ticketId
     */
    public String writeTicketsFile(String flightId, Passenger passenger, FileDatabase fileDatabase) throws IOException {
        String ticketId = chooseTicketId(fileDatabase);
        ticketsFile.seek(ticketsFile.length());
        ticketsFile.writeChars(fileDatabase.checkFileFormat.fixStringToWrite(passenger.getUsername(), 20));
        ticketsFile.writeChars(fileDatabase.checkFileFormat.fixStringToWrite(ticketId, FIX_SIZE));
        ticketsFile.writeChars(fileDatabase.checkFileFormat.fixStringToWrite(fileDatabase.flightsFile.searchFlight(fileDatabase.checkFileFormat, 0, flightId)[0], 140));
        return ticketId;

    }

    /**
     * checks that ticketId don't be similar
     *
     * @param fileDatabase has the requisite information
     * @return different ticketId
     */

    public String chooseTicketId(FileDatabase fileDatabase) throws IOException {
        long ticketId = 2500;
        while (searchTicket(fileDatabase.checkFileFormat, 20, String.valueOf(ticketId))[0] != null) {
            ticketId += 17;
        }
        return String.valueOf(ticketId);
    }

    /**
     * read the information of ticket from file
     *
     * @param checkFileFormat checks the format of data to read or write in file
     * @param index           the number of record in file of tickets
     * @return the information of desired ticket
     */

    public String readTicketFile(CheckFileFormat checkFileFormat, long index) throws IOException {
        return checkFileFormat.fixStringToRead(ticketsFile, index * Ticket.FIX_BYTE, 180);
    }

    /**
     * search ticket based on data
     *
     * @param checkFileFormat checks the format of data to read or write in file
     * @param startSize       the initial field of record
     * @param values          the information of ticket
     * @return the information of desired ticket
     */

    public String[] searchTicket(CheckFileFormat checkFileFormat, long startSize, String... values) throws IOException {
        String[] searchTickets = new String[100];
        int count = 0;
        int j = 0;
        long temp = startSize;

        for (long i = 0; i < ticketsFile.length() / Ticket.FIX_BYTE; i++) {
            long size = i * Ticket.FIX_BYTE;

            for (String value : values) {
                if ((checkFileFormat.fixStringToRead(ticketsFile, size + temp * 2, FIX_SIZE).equals(value))) {
                    count++;
                    if (count == values.length)
                        searchTickets[j++] = readTicketFile(checkFileFormat, i);
                }
                temp += 20;
            }
            count = 0;
            temp = startSize;
        }
        return searchTickets;
    }

    /**
     * search and return just one field of the ticket
     *
     * @param ticketId        the requisite data to search
     * @param checkFileFormat checks the format of data to read or write in file
     * @param startField      the initial field of record
     * @return the desired field of ticket
     */

    public String searchField(String ticketId, CheckFileFormat checkFileFormat, int startField) throws IOException {
        String searchTicket = searchTicket(checkFileFormat, 20, ticketId)[0];
        StringBuilder str = new StringBuilder();

        for (int i = startField; i < FIX_SIZE + startField && i < searchTicket.length(); i++) {
            str.append(searchTicket.toCharArray()[i]);
        }
        return str.substring(0).trim();
    }

    /**
     * remove and cancel the purchased ticket based on ticketId
     *
     * @param checkFileFormat checks the format of data to read or write in file
     * @param ticketId        the requisite data to search
     */

    public void removeTickets(CheckFileFormat checkFileFormat, String ticketId) throws IOException {
        String searchTickets = searchTicket(checkFileFormat, 20, ticketId)[0];
        if (searchTickets != null) {
            for (int i = 0; i < ticketsFile.length(); i += Ticket.FIX_BYTE) {
                if (readTicketFile(checkFileFormat, i / Ticket.FIX_BYTE).equals(searchTickets)) {
                    for (int j = i; j < ticketsFile.length() - Ticket.FIX_BYTE; j += Ticket.FIX_BYTE) {
                        String data = checkFileFormat.fixStringToRead(ticketsFile, j + Ticket.FIX_BYTE, 180);
                        ticketsFile.seek(j);
                        ticketsFile.writeChars(checkFileFormat.fixStringToWrite(data, 180));
                    }
                    ticketsFile.setLength(ticketsFile.length() - Ticket.FIX_BYTE);
                    break;
                }
            }
        }
    }

}
