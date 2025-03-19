package com.rick.notes.activites;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.rick.notes.API.APIService;
import com.rick.notes.Controller.AccessForMainAct;
import com.rick.notes.Model.Item;
import com.rick.notes.Model.ItemHaveRate;
import com.rick.notes.Model.ResultDiary;
import com.rick.notes.Model.ResultDiaryHaveRate;
import com.rick.notes.R;
import com.rick.notes.adapters.NotesAdapter;
import com.rick.notes.database.NotesDatabase;
import com.rick.notes.entities.Note;
import com.rick.notes.listeners.NotesListener;
import com.rick.notes.security.MergeSecurity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends Activity implements NotesListener {
    public static final int REQUEST_CODE_ADD_NOTE = 1;
    public static final int REQUEST_CODE_UPDATE_NOTE = 2;
    public static final int REQUEST_CODE_SHOW_NOTES = 3;
    public static final int REQUEST_CODE_SELECT_IMAGE = 4;
    public static final int REQUEST_CODE_STORAGE_PERMISSION = 5;
    private static final int CHANNEL_ID = 1;
    AccessForMainAct access = new AccessForMainAct();
    private RecyclerView notesRecyclerView;
    private List<Note> noteList;
    private List<Note> noteListAccess = new ArrayList<>();
    private NotesAdapter notesAdapter;
    private List<Item> items = new ArrayList<>();
    private List<ItemHaveRate> itemss = new ArrayList<>();
    private int noteClickedPosition = -1;
    private String rateNoteString;
    private AlertDialog dialogAddURL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getDataFull();
        getDataAPI();

        ImageView imageAddNoteMain = findViewById(R.id.imageAddNoteMain);

        imageAddNoteMain.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  SaveAndCheckPass check = new SaveAndCheckPass(getApplicationContext());
                  check.saveEditIndex("trong");
                   startActivityForResult(new Intent(getApplicationContext(), CreateNoteActivity.class), REQUEST_CODE_ADD_NOTE);
                   }
              }
        );

        notesRecyclerView = findViewById(R.id.notesRecyclerView);
        notesRecyclerView.setLayoutManager(
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        noteList = new ArrayList<>();

        notesAdapter = new NotesAdapter(noteList, this, getApplicationContext());
        notesRecyclerView.setAdapter(notesAdapter);
        getDataOffline();
        EditText inputSearch = findViewById(R.id.inputSearch);
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                notesAdapter.cancelTimer();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (noteList.size() != 0) {
                    
                    CheckBox a = findViewById(R.id.checkbox1);
                    CheckBox b = findViewById(R.id.checkbox2);
                    b.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                           EditText ed1 =  findViewById(R.id.inputSearch);
                           ed1.setText("");
                        }
                    });
                    a.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            EditText ed1 =  findViewById(R.id.inputSearch);
                            ed1.setText("");
                        }
                    });
                    notesAdapter.searchNotes(s.toString(),a.isChecked(),b.isChecked());
                }
            }
        });

        findViewById(R.id.imageAddNote).setOnClickListener(v -> startActivityForResult(
                new Intent(getApplicationContext(), CreateNoteActivity.class), REQUEST_CODE_ADD_NOTE));

        findViewById(R.id.imageAddImage).setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_STORAGE_PERMISSION);
            } else {
                selectImage();
            }
        });
        findViewById(R.id.imageCalender).setOnClickListener( v-> {
            Intent intent = new Intent(getApplicationContext(), CalenderActivity.class);
            startActivity(intent);
            startActivityForResult(
                    intent, REQUEST_CODE_ADD_NOTE);
        });

    }




    @SuppressLint("QueryPermissionsNeeded")
    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_STORAGE_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                selectImage();
            } else {
                Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void getDataFull(){
        SaveAndCheckPass check = new SaveAndCheckPass(getApplicationContext());
        APIService.apiService.dataFull(check.getUsernameLogin()).enqueue(new Callback<ResultDiaryHaveRate>() {
            @Override
            public void onResponse(Call<ResultDiaryHaveRate> call, Response<ResultDiaryHaveRate> response) {
                ResultDiaryHaveRate result  =  response.body();
                if (result != null) {
                    itemss = result.getResult();
                    check.unconvertData(result);
                }
            }

            @Override
            public void onFailure(Call<ResultDiaryHaveRate> call, Throwable throwable) {

            }
        });

    }
    private void getDataAPI(){
        SaveAndCheckPass check = new SaveAndCheckPass(getApplicationContext());
        APIService.apiService.dataDiary(check.getUsernameLogin()).enqueue(new Callback<ResultDiary>() {
            @Override
            public void onResponse(Call<ResultDiary> call, Response<ResultDiary> response) {
                ResultDiary result  =  response.body();
                if (result != null) {
                    items = result.getResult();
                    getNotes(REQUEST_CODE_SHOW_NOTES, false);
                }
                noteList.addAll(access.transportInfor(items,check.getUsernameLogin()));
                notesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ResultDiary> call, Throwable t) {

            }
        });
    }
    private void getDataOffline(){
        SaveAndCheckPass check =new SaveAndCheckPass(getApplicationContext());
        List<ItemHaveRate> listOff = new ArrayList<ItemHaveRate>();
        if(!check.getDataOffline().equals("trong")){
            listOff = check.getDataOfflineList();
            //Log.d("vc-----------",listOff.get(0).getContent());
            Note obj = new Note();
            //getNotes(REQUEST_CODE_SHOW_NOTES, false);
//            Toast.makeText(this, listOff.get(0).getTopic(), Toast.LENGTH_SHORT).show();
//            obj.setNoteText(listOff.get(0).getContent());
//            obj.setSubtitle(check.getUsernameLogin());
//            obj.setTitle(listOff.get(0).getTopic());
//            obj.setDateTime(listOff.get(0).getDate());
//            noteList.add(obj);
//            notesAdapter.notifyDataSetChanged();
            noteList.addAll(access.transportInfor2(listOff,check.getUsernameLogin()));
            notesAdapter.notifyDataSetChanged();
        }
        //noteList.addAll(access.transportInfor2(listOff,check.getUsernameLogin()));
    }

    @Override
    public void onNoteClicked(Note note, int position) {
        noteClickedPosition = position;
        Intent intent = new Intent(getApplicationContext(), CreateNoteActivity.class);
        intent.putExtra("isViewOrUpdate", true);
        intent.putExtra("note", note);
        startActivityForResult(intent, REQUEST_CODE_UPDATE_NOTE);
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
                MergeSecurity decryptdata =  new MergeSecurity();
                SaveAndCheckPass save = new SaveAndCheckPass(getApplicationContext());
                if (requestCode == REQUEST_CODE_SHOW_NOTES) {
                    notesAdapter.notifyDataSetChanged();
                    noteListAccess.clear();
                    //noteListAccess.addAll(noteList);
                    //Toast.makeText(MainActivity.this, noteListAccess.get(1).getNoteText(), Toast.LENGTH_SHORT).show();
                } else if (requestCode == REQUEST_CODE_ADD_NOTE) {
                    noteList.add(0, notes.get(0));
                    notesAdapter.notifyItemInserted(0);
                    notesRecyclerView.smoothScrollToPosition(0);
                } else if (requestCode == REQUEST_CODE_UPDATE_NOTE) {
                    noteList.remove(noteClickedPosition);
                    if (isNoteDeleted) {
                        notesAdapter.notifyItemRemoved(noteClickedPosition);
                    } else {
                        noteList.add(noteClickedPosition, notes.get(noteClickedPosition));
                        notesAdapter.notifyItemChanged(noteClickedPosition);
                    }
                }
            }
        }

        new GetNoteTask().execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD_NOTE && resultCode == RESULT_OK) {
            //noteListAccess = noteList;
            getNotes(REQUEST_CODE_ADD_NOTE, false);

        } else if (requestCode == REQUEST_CODE_UPDATE_NOTE && resultCode == RESULT_OK) {
            if (data != null) {
                getNotes(REQUEST_CODE_UPDATE_NOTE, data.getBooleanExtra("isNoteDeleted", false));
            }
        } else if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri selectedImageUri = data.getData();
                if (selectedImageUri != null) {
                    try {
                        String selectedImagePath = access.getPathFromUri(selectedImageUri,getContentResolver());
                        Intent intent = new Intent(getApplicationContext(), CreateNoteActivity.class);
                        intent.putExtra("isFromQuickActions", true);
                        intent.putExtra("quickActionType", "image");
                        intent.putExtra("imagePath", selectedImagePath);
                        startActivityForResult(intent, REQUEST_CODE_ADD_NOTE);
                    } catch (Exception e) {
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    private void showAddURLDialog() {
        if (dialogAddURL == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            View view = LayoutInflater.from(this)
                    .inflate(R.layout.layout_add_url, findViewById(R.id.layoutAddUrlContainer));
            builder.setView(view);

            dialogAddURL = builder.create();
            if (dialogAddURL.getWindow() != null) {
                dialogAddURL.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }

            final EditText inputURL = view.findViewById(R.id.inputURL);
            inputURL.requestFocus();

            view.findViewById(R.id.textAdd).setOnClickListener(v -> {
                final String inputURLStr = inputURL.getText().toString().trim();

                if (inputURLStr.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Enter URL", Toast.LENGTH_SHORT).show();
                } else if (!Patterns.WEB_URL.matcher(inputURLStr).matches()) {
                    Toast.makeText(MainActivity.this, "Enter valid URL", Toast.LENGTH_SHORT).show();
                } else {
                    dialogAddURL.dismiss();
                    Intent intent = new Intent(getApplicationContext(), CreateNoteActivity.class);
                    intent.putExtra("isFromQuickActions", true);
                    intent.putExtra("quickActionType", "URL");
                    intent.putExtra("URL", inputURLStr);
                    startActivityForResult(intent, REQUEST_CODE_ADD_NOTE);

                }
            });

            view.findViewById(R.id.textCancel).setOnClickListener(v -> dialogAddURL.dismiss());
        }
        dialogAddURL.show();
    }


}