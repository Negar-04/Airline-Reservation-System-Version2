import java.util.ArrayList;

public class Admin {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Admin(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Admin() {
    }

    public ArrayList<Admin> defaultAdmins() {
        ArrayList<Admin> admins = new ArrayList<>();
        admins.add(new Admin("negar", "negar1383"));
        return admins;
    }
}
