package com.rick.notes.Controller;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import java.util.Calendar;

public class AccessForCreateAndCopy {
    public String informationNote(String topic, String content){
        return "Title:"+topic+"\nContent:"+content;

    }
    public String convertDate(){
        Calendar calendar = Calendar.getInstance();
        String monthConvert, dayConvert;
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        if(month<10) {
            monthConvert = "0"+String.valueOf(month);
        }
        else{
            monthConvert = String.valueOf(month);
        }
        if(day<10){
            dayConvert = "0"+String.valueOf(day);
        }
        else{
            dayConvert = String.valueOf(day);
        }
        return String.valueOf(year)+"-"+monthConvert+"-"+dayConvert;
    }
    public boolean isSuccess(String result){
        if(result.equals("true")) return  true;
        else return false;
    }
    public String getPathFromUri(Uri contentUri, ContentResolver ct) {
        String filePath;
        Cursor cursor = ct.query(contentUri, null, null, null, null);
        if (cursor == null) {
            filePath = contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex("_data");
            filePath = cursor.getString(index);
            cursor.close();
        }
        return filePath;
    }
}
