package com.room.arcadelive.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public static Matcher getRegexMatcher(String regexPattern, String textToMatch) {
        return Pattern.compile(regexPattern).matcher(textToMatch);
    }

    public static String getTodayDate(String format) {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(c);
    }

    public static String getDateString(String format, Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    public static int getSeconds(String time) {
        String[] units = time.split(":"); //will break the string up into an array
        int minutes = Integer.parseInt(units[0]); //first element
        int seconds = Integer.parseInt(units[1]); //second element
        int duration = 60 * minutes + seconds;
        return duration;
    }

    public static Date convertToDate(String dateString,String format) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            return simpleDateFormat.parse(dateString);
        } catch (Exception e) {
            String msg = e.getMessage();
            try {
                return new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
            } catch (ParseException e1) {
                e1.printStackTrace();
                return null;
            }
        }
    }

    public static String getRandomId() {
        String randomId = "";
        try{
            randomId = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
        }catch (Exception e){

        }
        return randomId;
    }
}
