import java.io.IOException;
import java.io.RandomAccessFile;

public class CheckFileFormat {
    /**
     * fix data to write in file
     *
     * @param str     the data that should be fixed to write
     * @param fixSize the number of characters to fix the length's of data
     * @return the appropriate format of each data to write
     */
    String fixStringToWrite(String str, int fixSize) {
        StringBuilder strBuilder = new StringBuilder(str);
        while (strBuilder.length() < fixSize)
            strBuilder.append(" ");

        str = strBuilder.toString();
        return str.substring(0, fixSize);
    }

    /**
     * fix data to read from file
     *
     * @param file    desired file that includes information
     * @param seek    the initial byte to start reading from the file
     * @param endSize the number of characters to read
     * @return the appropriate format of each data to read
     */

    public String fixStringToRead(RandomAccessFile file, long seek, long endSize) throws IOException {
        StringBuilder str = new StringBuilder();
        file.seek(seek);

        for (long i = 0; i < endSize; i++) {
            str.append(file.readChar());
        }
        return str.toString().trim();
    }

}
