package utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.joda.time.DateTime;

public class DateUtils {

    final static DateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
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
    public static String reformateDate(String stringDate, String patternFrom, String patternTo) {
        try {
            Date date = new SimpleDateFormat(patternFrom).parse(stringDate);
            return new SimpleDateFormat(patternTo).format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return stringDate;
        }
    }
    public static String reformatMinusWeeks(String stringDate, int weeks) {
        try {
            Date date = inputFormat.parse(stringDate);
            DateTime dateTime = new DateTime(date);
            dateTime = dateTime.minusWeeks(weeks);
            return outputFormat.format(dateTime.toDate());
        } catch (Exception e) {
            e.printStackTrace();
            return stringDate;
        }
    }
    
}
