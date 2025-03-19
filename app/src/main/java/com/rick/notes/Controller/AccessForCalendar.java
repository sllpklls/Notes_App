package com.rick.notes.Controller;

import com.rick.notes.activites.SaveAndCheckPass;
import com.rick.notes.security.MergeSecurity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AccessForCalendar {
    public String convertNumberToStar(String str){
        if(str.equals("1.0")) {
            return  "❤";
        }
        if(str.equals("2.0")) {
            return  "❤❤";
        }
        if(str.equals("3.0")) {
            return  "❤❤❤";
        }
        if(str.equals("4.0")) {
            return  "❤❤❤❤";
        }
        if(str.equals("5.0")) {
            return  "❤❤❤❤❤";
        }
        return "";
    }
    public String informationForCalendar(String getRate, String topic, String content, String date){
        return "\t"+convertNumberToStar(getRate)+"\n●Topic: "+topic+"\n   ●Content: "+content+" - Date: "+date;
    }
    public String getFormattedDate(int year, int month, int dayOfMonth) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date(year - 1900, month, dayOfMonth);
        return sdf.format(date);
    }
    public String getFormattedDate(long millis) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date(millis);
        return sdf.format(date);
    }
    public String convertdate(String inputDate){
        try {

            Pattern pattern = Pattern.compile("(\\d{1,2})\\s+tháng\\s+(\\d{1,2})\\s+(\\d{4})");
            Matcher matcher = pattern.matcher(inputDate);

            if (matcher.find()) {
                int day = Integer.parseInt(matcher.group(1));
                int month = Integer.parseInt(matcher.group(2));
                int year = Integer.parseInt(matcher.group(3));
                String result = "Date: "+year+"-"+month+"-"+day;

                return result;
            } else {

                return null;
                //thai
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String convertDateString(String inputDateString) {
        // Define the format of the input string
        SimpleDateFormat inputFormat = new SimpleDateFormat("EEEE, dd 'tháng' MM yyyy hh:mm a", Locale.getDefault());

        // Define the format of the output string
        SimpleDateFormat outputFormat = new SimpleDateFormat("Date: yyyy-MM-dd", Locale.getDefault());

        try {
            // Parse the input string to a Date object
            Date date = inputFormat.parse(inputDateString);

            // Format the Date object to the desired output string
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null; // Handle the exception according to your requirements
        }
    }
    private int parseMonth(String monthString) {
        String[] months = {"tháng 1", "tháng 2", "tháng 3", "tháng 4", "tháng 5", "tháng 6", "tháng 7", "tháng 8", "tháng 9", "tháng 10", "tháng 11", "tháng 12"};
        for (int i = 0; i < months.length; i++) {
            if (months[i].equalsIgnoreCase(monthString)) {
                return i + 1; // Adding 1 because Calendar.MONTH is zero-based
            }
        }
        return 1; // Default to January if not matched
    }
}
