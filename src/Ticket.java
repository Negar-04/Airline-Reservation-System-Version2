public class Ticket {

    private final String ticketId;
    private final Flight flight;
    private final Passenger passenger;
    static final int FIX_BYTE = 360;

    public Ticket(String ticketId, Flight flight, Passenger passenger) {
        this.ticketId = ticketId;
        this.flight = flight;
        this.passenger = passenger;
    }

}
