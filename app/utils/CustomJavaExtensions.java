package utils;

import play.templates.JavaExtensions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class CustomJavaExtensions extends JavaExtensions {
    public static long daysBetween(String dateString1, String dateString2) {

        SimpleDateFormat formatter1=new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = null;
        Date date2 = null;
        try {
            date1 = formatter1.parse(dateString1);
            date2 = formatter1.parse(dateString2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long diff = date2.getTime() - date1.getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }
}
