import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckFormat {
    /**
     * check and accept just numbers
     *
     * @param str it's string that entered from admin to add into flight's information
     * @return -1 if the format is invalid or Integer number if it's valid
     */
    public Integer isNumeric(String str) {
        if (str == null || str.length() == 0)
            return -1;
        try {
            return Integer.parseInt(str);

        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * check the correct format of origin and destination that should be capitalized in first letter
     *
     * @param str it's origin or destination that entered from admin
     * @return false if the format is invalid or true if it's valid
     */
    public boolean checkLetters(String str) {
        boolean test;
        char[] str2 = str.toCharArray();

        if (!(str2[0] >= 'A' && str2[0] <= 'Z'))
            return false;

        for (int i = 1; i < str.length(); i++) {
            test = str2[i] >= 'a' && str2[i] <= 'z';
            if (!test)
                return false;
        }
        return true;
    }

    /**
     * check the correct format of time
     *
     * @param str it's time that entered from admin
     * @return false if the format is invalid or true if it's valid
     */
    public boolean checkTime(String str) {

        Matcher matcher = Pattern.compile("\\d{2}:\\d{2}").matcher(str);

        if (matcher.find()) {
            String[] digits = str.split(":");
            int[] convertedDigits = new int[digits.length];

            for (int i = 0; i < digits.length; i++)
                convertedDigits[i] = Integer.parseInt(digits[i]);

            boolean hourCheck = convertedDigits[0] >= 0 && convertedDigits[0] <= 23;
            boolean minuteCheck = convertedDigits[1] >= 0 && convertedDigits[1] <= 59;

            return hourCheck && minuteCheck;
        }
        return false;
    }

    /**
     * check the correct format of date
     *
     * @param str it's date that entered from admin
     * @return false if the format is invalid or true if it's valid
     */
    public boolean checkDate(String str) {

        Matcher matcher = Pattern.compile("\\d{4}/\\d{2}/\\d{2}").matcher(str);

        if (matcher.find()) {
            String[] digits = str.split("/");
            int[] convertedDigits = new int[digits.length];

            for (int i = 0; i < digits.length; i++)
                convertedDigits[i] = Integer.parseInt(digits[i]);

            boolean yearCheck = convertedDigits[0] > 0 && convertedDigits[0] < 10000;
            boolean monthCheck = convertedDigits[1] > 0 && convertedDigits[1] < 13;
            boolean dayCheck;

            if (convertedDigits[1] > 0 && convertedDigits[1] <= 6)
                dayCheck = convertedDigits[2] > 0 && convertedDigits[2] <= 32;
            else if (convertedDigits[1] == 12)
                dayCheck = convertedDigits[2] > 0 && convertedDigits[2] <= 29;
            else
                dayCheck = convertedDigits[2] > 0 && convertedDigits[2] <= 30;

            return monthCheck && dayCheck && yearCheck;

        } else
            return false;
    }
}
