import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class AdminsFile {
    private RandomAccessFile adminsFile;

    public RandomAccessFile getAdminsFile() {
        return adminsFile;
    }

    Admin admin = new Admin();

    File file;

    public AdminsFile(CheckFileFormat checkFileFormat) throws IOException {
        this.file = new File("AdminsFile.dat");
        if (!file.exists()) {
            this.adminsFile = new RandomAccessFile("AdminsFile.dat", "rw");
            writeAdminsFile(admin.defaultAdmins().get(0), checkFileFormat);
        }
        if (adminsFile == null)
            this.adminsFile = new RandomAccessFile("AdminsFile.dat", "rw");
    }

    /**
     * write the information of admin in file
     *
     * @param admin           has the information of admin
     * @param checkFileFormat checks the format of data to read or write in file
     */

    public void writeAdminsFile(Admin admin, CheckFileFormat checkFileFormat) throws IOException {
        int FIX_SIZE = 20;
        adminsFile.writeChars(checkFileFormat.fixStringToWrite(admin.getUsername(), FIX_SIZE));
        adminsFile.writeChars(checkFileFormat.fixStringToWrite(admin.getPassword(), FIX_SIZE));
    }


}
