package utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    final static DateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
    final static DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static String reformateDate(String stringDate) {
        try {
            Date date = inputFormat.parse(stringDate);
            return outputFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return stringDate;
        }
    }
}
