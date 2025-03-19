package com.rick.notes.Controller;

//import static com.rick.notes.API.APIService.check;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import com.rick.notes.Model.Item;
import com.rick.notes.Model.ItemHaveRate;
import com.rick.notes.entities.Note;

import java.util.ArrayList;
import java.util.List;

public class AccessForMainAct {

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

    public List<Note> transportInfor(List<Item> items, String usernameLogin) {
        List<Note> noteList = new ArrayList<>();
        for (Item item : items) {
            Note item1 = new Note();
            item1.setTitle(item.getTopic());
            item1.setDateTime(item.getDate());
            item1.setSubtitle(usernameLogin);//check.getUsernameLogin()
            item1.setNoteText(item.getContent());
            noteList.add(item1);
        }
        return noteList;
    }

    public List<Note> transportInfor2(List<ItemHaveRate> items, String usernameLogin) {
        List<Note> noteList = new ArrayList<>();
        for (ItemHaveRate item : items) {
            Note item1 = new Note();
            item1.setTitle(item.getTopic());
            item1.setDateTime(item.getDate());
            item1.setSubtitle(usernameLogin);//check.getUsernameLogin()
            item1.setNoteText(item.getContent());
            noteList.add(item1);
        }
        return noteList;
    }
}