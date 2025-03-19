package com.rick.notes.activites;

import static com.rick.notes.activites.MainActivity.REQUEST_CODE_ADD_NOTE;
import static com.rick.notes.activites.MainActivity.REQUEST_CODE_SHOW_NOTES;
import static com.rick.notes.activites.MainActivity.REQUEST_CODE_UPDATE_NOTE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
        import android.widget.ListView;
        import android.widget.TextView;
        import android.widget.Toast;

        import androidx.appcompat.app.AppCompatActivity;

import com.rick.notes.Controller.AccessForCalendar;
import com.rick.notes.Model.ItemHaveRate;
import com.rick.notes.R;
import com.rick.notes.database.NotesDatabase;
import com.rick.notes.entities.Note;
import com.rick.notes.security.MergeSecurity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CalenderActivity extends AppCompatActivity {
    private int noteClickedPosition = -1;
    private CalendarView calendarView;
    private ListView listView;
    private List<Note> noteList;
    public static final int REQUEST_CODE_ADD_NOTE = 1;
    public static final int REQUEST_CODE_UPDATE_NOTE = 2;
    public static final int REQUEST_CODE_SHOW_NOTES = 3;
    public static final int REQUEST_CODE_SELECT_IMAGE = 4;
    public static final int REQUEST_CODE_STORAGE_PERMISSION = 5;
    private static final int CHANNEL_ID = 1;
    private Button bt_back;
    final ArrayList<String> dataList = new ArrayList<>();
    AccessForCalendar access = new AccessForCalendar();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calenderview);
        SaveAndCheckPass check =  new SaveAndCheckPass(getApplicationContext());
        noteList = new ArrayList<>();
        getNotes(REQUEST_CODE_SHOW_NOTES, false);
        calendarView = findViewById(R.id.calendarView);
        listView = findViewById(R.id.listView);
        bt_back = findViewById(R.id.backButton);
        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalenderActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        List<ItemHaveRate> items = check.convertData();

        for (ItemHaveRate item : items ) {
            //String str = "\t"+access.convertNumberToStar(item.getRate())+"\n●Topic: "+item.getTopic()+"\n   ●Content: "+item.getContent()+" - Date: "+item.getDate();
            dataList.add(access.informationForCalendar(item.getRate(),item.getTopic(),item.getContent(),item.getDate()));
        }

//        dataList.add("Event 1 - Date: 2023-12-01");
//        dataList.add("Event 2 - Date: 2023-12-01");
//        dataList.add("Event 3 - Date: 2023-12-01");
//        dataList.add("Event 4dfghsdgdsfvsdgsdgbdfsghsdfgse - Date: 2023-12-03");

        // Set up the initial data for the ListView
        updateListView(access.getFormattedDate(calendarView.getDate()), dataList);

        // Set a listener for CalendarView date changes
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                String selectedDate = access.getFormattedDate(year, month, dayOfMonth);
                updateListView(selectedDate, dataList);
            }
        });

    }

    private void getNotes(final int requestCode, final boolean isNoteDeleted) {

        @SuppressLint("StaticFieldLeak")
        class GetNoteTask extends AsyncTask<Void, Void, List<Note>> {

            @Override
            protected List<Note> doInBackground(Void... voids) {
                return NotesDatabase.getNotesDatabase(getApplicationContext())
                        .noteDao().getAllNotes();
            }

            @Override
            protected void onPostExecute(List<Note> notes) {
                super.onPostExecute(notes);
                SaveAndCheckPass save = new SaveAndCheckPass(getApplicationContext());
                MergeSecurity decode = new MergeSecurity();
                if (requestCode == REQUEST_CODE_SHOW_NOTES) {
                    noteList.addAll(notes);
                    for(int i = 0 ; i< noteList.size();i++){
                       dataList.add(decode.decryptData(save.getPassword(), noteList.get(i).getTitle())+" - "+access.convertdate(noteList.get(i).getDateTime()));
                    }
                    updateListView(access.getFormattedDate(calendarView.getDate()), dataList);
                    //notesAdapter.notifyDataSetChanged();
                    //noteListAccess.clear();
                    //noteListAccess.addAll(noteList);
                    //Toast.makeText(MainActivity.this, noteListAccess.get(1).getNoteText(), Toast.LENGTH_SHORT).show();
                } else if (requestCode == REQUEST_CODE_ADD_NOTE) {
                    noteList.add(0, notes.get(0));
                    for(int i = 0 ; i< noteList.size();i++){
                        dataList.add(decode.decryptData(save.getPassword(), noteList.get(i).getTitle())+" - "+access.convertdate(noteList.get(i).getDateTime()));
                    }
                    updateListView(access.getFormattedDate(calendarView.getDate()), dataList);
                    //notesAdapter.notifyItemInserted(0);
                    //notesRecyclerView.smoothScrollToPosition(0);
                } else if (requestCode == REQUEST_CODE_UPDATE_NOTE) {
                    noteList.remove(noteClickedPosition);
                    if (isNoteDeleted) {
                        //notesAdapter.notifyItemRemoved(noteClickedPosition);
                    } else {
                        noteList.add(noteClickedPosition, notes.get(noteClickedPosition));
                        //notesAdapter.notifyItemChanged(noteClickedPosition);
                    }
                }
            }
        }
        new GetNoteTask().execute();
    }

    private void updateListView(String selectedDate, ArrayList<String> dataList) {
        ArrayList<String> eventsForSelectedDate = new ArrayList<>();

        for (String event : dataList) {
            if (event.contains(selectedDate)) {
                event = event.substring(0,event.length()-18);
                eventsForSelectedDate.add(event);
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                eventsForSelectedDate
        );

        listView.setAdapter(adapter);
    }





}
