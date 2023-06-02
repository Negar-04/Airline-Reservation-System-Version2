public class Passenger {
    private final String username;
    private final String password;
    private final String credit;

    static final int FIX_BYTE = 120;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getCredit() {
        return credit;
    }

    public Passenger(String username, String password, String credit) {
        this.username = username;
        this.password = password;
        this.credit = credit;
    }
}


