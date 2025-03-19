package com.rick.notes.activites;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.rick.notes.API.APIService;
import com.rick.notes.Controller.AccessForCreateAndCopy;
import com.rick.notes.Model.ItemHaveRate;
import com.rick.notes.Model.ResultAdd;
import com.rick.notes.Model.ResultDiaryHaveRate;
import com.rick.notes.Model.ResultUpdate;
import com.rick.notes.R;
import com.rick.notes.database.NotesDatabase;
import com.rick.notes.entities.Note;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.xml.transform.Result;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateNoteActivity extends AppCompatActivity {
    private static final String TAG = CreateNoteActivity.class.getSimpleName();

    private EditText inputNoteTitle, inputNoteSubtitle, inputNoteText;
    private TextView textDateTime;
    private View viewSubtitleIndicator;
    private ImageView imageNote;
    private TextView textWebURL;
    private LinearLayout layoutWebURL;

    private String selectedNoteColor;
    private String selectedImagePath;
    private RatingBar rateNote;
    private static final int REQUEST_CODE_STORAGE_PERMISSION = 1;
    private static final int REQUEST_CODE_SELECT_IMAGE = 2;
    private TextView textShareQR;
    private AlertDialog dialogAddURL;
    private AlertDialog dialogDeleteNote;
    private TextView TextShare;
    private Note alreadyAvailableNote;
    private String rateString;
    private TextView CreateImage;
    private List<ItemHaveRate> itemss = new ArrayList<>();
    AccessForCreateAndCopy access = new AccessForCreateAndCopy();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        //insertData(null,null,null);
        getDataFull();
        ImageView imageBack = findViewById(R.id.imageBack);
        imageBack.setOnClickListener(v -> onBackPressed());
        SaveAndCheckPass check =  new SaveAndCheckPass(getApplicationContext());
        //titleQRcode= findViewById(R.id.tv_qrcode);
        inputNoteTitle = findViewById(R.id.inputNoteTitle);
        inputNoteSubtitle = findViewById(R.id.inputNoteSubtitle);
        inputNoteSubtitle.setText(check.getUsernameLogin().toString());
        inputNoteText = findViewById(R.id.inputNoteText);
        viewSubtitleIndicator = findViewById(R.id.viewSubtitleIndicator);
        imageNote = findViewById(R.id.imageNote);
        textWebURL = findViewById(R.id.textWebURL);
        layoutWebURL = findViewById(R.id.layoutWebURL);
        rateNote = findViewById(R.id.ratingBar);
        TextShare = findViewById(R.id.textShare);
        CreateImage = findViewById(R.id.textCreateimage);
        CreateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveAndCheckPass check = new SaveAndCheckPass(getApplicationContext());

                check.setTopicAndContent(inputNoteTitle.getText().toString(), inputNoteText.getText().toString());
                Intent intent = new Intent(CreateNoteActivity.this, ConvertImageActivity.class);
                startActivity(intent);
                finish();
            }
        });
        TextShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccessForCreateAndCopy access = new AccessForCreateAndCopy();
                //String s = "Title:"+inputNoteTitle.getText().toString()+"\nContent:"+inputNoteText.getText().toString();
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Copied Text", access.informationNote(inputNoteTitle.getText().toString(),inputNoteText.getText().toString()));
                clipboard.setPrimaryClip(clip);

                Toast.makeText(CreateNoteActivity.this, "Text copied to clipboard", Toast.LENGTH_SHORT).show();

            }
        });

        textDateTime = findViewById(R.id.textDateTime);
        textDateTime.setText(new SimpleDateFormat(
                "EEEE, dd MMMM yyyy HH:mm a", Locale.getDefault()).format(new Date().getTime())
        );

        ImageView imageSave = findViewById(R.id.imageSave);
        //imageSave.setOnClickListener(v -> saveNote());
        imageSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //for(Note item : noteList)
                //checkExist(inputNoteTitle.getText().toString());
                ///================
                //SaveAndCheckPass check = new SaveAndCheckPass(getApplicationContext());
                if(check.getEditIndex().equals("trong")){
                    insertData(check.getUsernameLogin(),inputNoteTitle.getText().toString(), inputNoteText.getText().toString(),String.valueOf(rateNote.getRating()));

                }
                else{
                    //Toast.makeText(CreateNoteActivity.this,check.getEditIndex(),Toast.LENGTH_SHORT).show();
                    updateData(check.getEditIndex(),inputNoteTitle.getText().toString(),inputNoteText.getText().toString(),String.valueOf(rateNote.getRating()));
                }
            }
        });

        selectedNoteColor = "#E1A5A5";
        selectedImagePath = "";

        if (getIntent().getBooleanExtra("isViewOrUpdate", false)) {
            alreadyAvailableNote = (Note) getIntent().getSerializableExtra("note");
            setViewOrUpdateNote();
        }

        findViewById(R.id.imageRemoveWebURL).setOnClickListener(v -> {
            textWebURL.setText(null);
            layoutWebURL.setVisibility(View.GONE);
        });

        findViewById(R.id.imageRemoveImage).setOnClickListener(v -> {
            imageNote.setImageBitmap(null);
            imageNote.setVisibility(View.GONE);
            findViewById(R.id.imageRemoveImage).setVisibility(View.GONE);
            selectedImagePath = "";
        });

        if (getIntent().getBooleanExtra("isFromQuickActions", false)) {
            String type = getIntent().getStringExtra("quickActionType");
            if (type != null) {
                if (type.equals("image")) {
                    selectedImagePath = getIntent().getStringExtra("imagePath");
                    imageNote.setImageBitmap(BitmapFactory.decodeFile(selectedImagePath));
                    imageNote.setVisibility(View.VISIBLE);
                    findViewById(R.id.imageRemoveImage).setVisibility(View.VISIBLE);
                } else if (type.equals("URL")) {
                    textWebURL.setText(getIntent().getStringExtra("URL"));
                    layoutWebURL.setVisibility(View.VISIBLE);
                }
            }
        }

        initMiscellaneous();
        setSubtitleIndicatorColor();
    }
    private void getDataFull(){
        SaveAndCheckPass check = new SaveAndCheckPass(getApplicationContext());
        APIService.apiService.dataFull(check.getUsernameLogin()).enqueue(new Callback<ResultDiaryHaveRate>() {
            @Override
            public void onResponse(Call<ResultDiaryHaveRate> call, Response<ResultDiaryHaveRate> response) {
                ResultDiaryHaveRate result  =  response.body();
                if (result != null) {
                    itemss = result.getResult();
                }
            }

            @Override
            public void onFailure(Call<ResultDiaryHaveRate> call, Throwable throwable) {

            }
        });

    }
    private void insertData(String user_name, String topic, String content, String rate){

        SaveAndCheckPass check = new SaveAndCheckPass(getApplicationContext());
        AccessForCreateAndCopy access = new AccessForCreateAndCopy();
        if(!check.isOnline()){
            ItemHaveRate obj = new ItemHaveRate(access.convertDate(),topic,content,rate);
            List<ItemHaveRate> listObj = new ArrayList<ItemHaveRate>();
            if(!check.getDataOffline().equals("trong")){
                listObj = check.getDataOfflineList();
            }
            listObj.add(obj);
            ResultDiaryHaveRate obj2 = new ResultDiaryHaveRate("success",check.getUsernameLogin(),listObj);
            check.setDataOfflineList(obj2);
            Toast.makeText(CreateNoteActivity.this, "Them Thanh cong", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(CreateNoteActivity.this, MainActivity.class);
            startActivity(intent);
            finish();

        }
        else{
        APIService.apiService.insertData(user_name,access.convertDate(),topic, content, rate).enqueue(new Callback<ResultAdd>() {
            @Override
            public void onResponse(Call<ResultAdd> call, Response<ResultAdd> response) {
                Toast.makeText(CreateNoteActivity.this, "Them Thanh cong", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CreateNoteActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Call<ResultAdd> call, Throwable t) {
                SaveAndCheckPass check =new SaveAndCheckPass(getApplicationContext());
                Log.d("vcl--------",check.getDataRate());
                Toast.makeText(CreateNoteActivity.this, "them that bai", Toast.LENGTH_SHORT).show();
            }
        });}
        //APIService.apiService.insertData()
    }
    private void updateData(String oldtopic, String newtopic, String content, String rate){
        SaveAndCheckPass check =  new SaveAndCheckPass(getApplicationContext());
        if(!check.isOnline()){
            List<ItemHaveRate> listObj = new ArrayList<ItemHaveRate>();
            if(!check.getDataOffline().equals("trong")){
                listObj = check.getDataOfflineList();
            }
            for(int i=0;i<listObj.size();i++){
                if(oldtopic.equals(listObj.get(i).getTopic())) {
                    listObj.get(i).setContent(content);
                    listObj.get(i).setRate(rate);
                    listObj.get(i).setTopic(newtopic);
                    //Log.d("inforhere",content+"-"+newtopic);
                    Log.d("inforhere2",listObj.get(i).getContent()+"-"+listObj.get(i).getTopic());
                }
            }
            ResultDiaryHaveRate obj2 = new ResultDiaryHaveRate("success",check.getUsernameLogin(),listObj);
            check.setDataOfflineList(obj2);
            Intent intent = new Intent(CreateNoteActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        else{
        APIService.apiService.updateData(check.getUsernameLogin(),oldtopic,content,newtopic, rate).enqueue(new Callback<ResultUpdate>() {
            @Override
            public void onResponse(Call<ResultUpdate> call, Response<ResultUpdate> response) {
                //AccessForCreateAndCopy access= new AccessForCreateAndCopy();
                ResultUpdate result = response.body();
                if(access.isSuccess(result.getSuccess())){
                    Toast.makeText(CreateNoteActivity.this,"Sửa thành công",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CreateNoteActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(CreateNoteActivity.this,"Có vẻ bạn chưa chỉnh sửa gì",Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<ResultUpdate> call, Throwable t) {
                Toast.makeText(CreateNoteActivity.this,"Không thể sửa",Toast.LENGTH_SHORT).show();

            }
        });}

        //APIService.apiService.updateData()
    }
    private void deleteData(String topic){
        SaveAndCheckPass check = new SaveAndCheckPass(getApplicationContext());
        Toast.makeText(this, topic, Toast.LENGTH_SHORT).show();
        if(!check.isOnline()){
            List<ItemHaveRate> listObj = new ArrayList<ItemHaveRate>();
            if(!check.getDataOffline().equals("trong")){
                listObj = check.getDataOfflineList();
            }
            for(int i=0;i<=listObj.size();i++){
                if(topic.equals(listObj.get(i).getTopic())) listObj.remove(i);
            }
            ResultDiaryHaveRate obj2 = new ResultDiaryHaveRate("success",check.getUsernameLogin(),listObj);
            check.setDataOfflineList(obj2);

        }
        else{
        APIService.apiService.deleteData(check.getUsernameLogin(),topic).enqueue(new Callback<ResultUpdate>() {
            @Override
            public void onResponse(Call<ResultUpdate> call, Response<ResultUpdate> response) {
                ResultUpdate result = response.body();
                if(access.isSuccess(result.getSuccess())){
                    Toast.makeText(CreateNoteActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(CreateNoteActivity.this,result.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResultUpdate> call, Throwable t) {
                    Toast.makeText(CreateNoteActivity.this,"Xoa khong thanh cong",Toast.LENGTH_SHORT).show();
            }
        });}
    }
    private void setViewOrUpdateNote() {

        inputNoteTitle.setText(alreadyAvailableNote.getTitle());
        inputNoteSubtitle.setText(alreadyAvailableNote.getSubtitle());
        inputNoteText.setText(alreadyAvailableNote.getNoteText());
        textDateTime.setText(alreadyAvailableNote.getDateTime());
        SaveAndCheckPass check = new SaveAndCheckPass(getApplicationContext());
        itemss = check.convertData();

        for ( ItemHaveRate item : itemss){
            if(!item.getRate().equals("")&&item.getTopic().toString().equals(alreadyAvailableNote.getTitle())){
                if(!item.getRate().equals("")){
                    rateNote.setRating(Float.valueOf(item.getRate()));
                    Toast.makeText(this, String.valueOf(rateNote.getRating()), Toast.LENGTH_SHORT).show();
                }

            }
        }
        //rateNote.setRating(4.0f);
        //Toast.makeText(CreateNoteActivity.this,inputNoteText.getText().toString(),Toast.LENGTH_SHORT).show();

        final String imagePathStr = alreadyAvailableNote.getImagePath();
        if (imagePathStr != null && !imagePathStr.trim().isEmpty()) {
            imageNote.setImageBitmap(BitmapFactory.decodeFile(imagePathStr));
            imageNote.setVisibility(View.VISIBLE);
            findViewById(R.id.imageRemoveImage).setVisibility(View.VISIBLE);
            selectedImagePath = imagePathStr;
        }

        final String webLinkStr = alreadyAvailableNote.getWebLink();
        if (webLinkStr != null && !webLinkStr.trim().isEmpty()) {
            textWebURL.setText(alreadyAvailableNote.getWebLink());
            layoutWebURL.setVisibility(View.VISIBLE);
        }
    }

//    private void saveNote() {
//        final String noteTitle = inputNoteTitle.getText().toString().trim();
//        final String noteSubtitle = inputNoteSubtitle.getText().toString().trim();
//        final String noteText = inputNoteText.getText().toString().trim();
//        final String dateTimeStr = textDateTime.getText().toString().trim();
//
//        if (noteTitle.isEmpty()) {
//            Toast.makeText(this, "Note title can't be empty!", Toast.LENGTH_SHORT).show();
//            return;
//        } else if (noteSubtitle.isEmpty() && noteText.isEmpty()) {
//            Toast.makeText(this, "Note can't be empty!", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        //MergeSecurity encryptdata = new MergeSecurity();
//        //SaveAndCheckPass save = new SaveAndCheckPass(getApplicationContext());
//        //encryptdata.encryptData(save.getPassword(),dateTimeStr)
//        final Note note = new Note();
//        note.setTitle(noteTitle);
//        note.setSubtitle(noteSubtitle);
//        note.setNoteText(noteText);
//        note.setDateTime(dateTimeStr);
//        note.setColor(selectedNoteColor);
//        note.setImagePath(selectedImagePath);
//
//        if (layoutWebURL.getVisibility() == View.VISIBLE) {
//            note.setWebLink(textWebURL.getText().toString());
//        }
//
//        if (alreadyAvailableNote != null) {
//            note.setId(alreadyAvailableNote.getId());
//        }
//
//        @SuppressLint("StaticFieldLeak")
//        class SaveNoteTask extends AsyncTask<Void, Void, Void> {
//            @Override
//            protected Void doInBackground(Void... voids) {
//                NotesDatabase.getNotesDatabase(getApplicationContext()).noteDao().insertNote(note);
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(Void aVoid) {
//                super.onPostExecute(aVoid);
//                Intent intent = new Intent();
//                setResult(RESULT_OK, intent);
//
//                finish();
//            }
//        }
//
//        new SaveNoteTask().execute();
//    }

    private void initMiscellaneous() {
        final LinearLayout layoutMiscellaneous = findViewById(R.id.layoutMiscellaneous);
        final BottomSheetBehavior<LinearLayout> bottomSheetBehavior = BottomSheetBehavior.from(layoutMiscellaneous);
        layoutMiscellaneous.findViewById(R.id.textMiscellaneous).setOnClickListener(v -> {
            if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            } else {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });



        if (alreadyAvailableNote != null) {
            final String noteColorCode = alreadyAvailableNote.getColor();
            if (noteColorCode != null && !noteColorCode.trim().isEmpty()) {
                switch (noteColorCode) {
                    case "#FDBE3B":
                        //layoutMiscellaneous.findViewById(R.id.viewColor2).performClick();
                        break;
                    case "#8BC34A":
                        //layoutMiscellaneous.findViewById(R.id.viewColor3).performClick();
                        break;
                    case "#F44336":
                        //layoutMiscellaneous.findViewById(R.id.viewColor4).performClick();
                        break;
                    case "#FF2196F3":
                        //layoutMiscellaneous.findViewById(R.id.viewColor5).performClick();
                        break;
                }
            }
        }

        layoutMiscellaneous.findViewById(R.id.layoutAddImage).setOnClickListener(v -> {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            if (ContextCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                SaveAndCheckPass check =  new SaveAndCheckPass(getApplicationContext());
                check.setSaveColor(alreadyAvailableNote.getTitle());
            } else {
                //selectImage();
                SaveAndCheckPass check =  new SaveAndCheckPass(getApplicationContext());
                check.setSaveColor(alreadyAvailableNote.getTitle());
            }
        });

        layoutMiscellaneous.findViewById(R.id.layoutAddUrl).setOnClickListener(v -> {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            showAddURLDialog();
        });

        if (alreadyAvailableNote != null) {
            layoutMiscellaneous.findViewById(R.id.layoutDeleteNote).setVisibility(View.VISIBLE);
            layoutMiscellaneous.findViewById(R.id.layoutDeleteNote).setOnClickListener(v -> {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                showDeleteNoteDialog();
            });
        }
    }

    private void showDeleteNoteDialog() {
        if (dialogDeleteNote == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(CreateNoteActivity.this);
            View view = LayoutInflater.from(this).inflate(
                    R.layout.layout_delete_note,
                    (ViewGroup) findViewById(R.id.layoutDeleteNoteContainer)
            );
            builder.setView(view);
            dialogDeleteNote = builder.create();
            if (dialogDeleteNote.getWindow() != null) {
                dialogDeleteNote.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }
            view.findViewById(R.id.textDeleteNote).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView topicDelete = findViewById(R.id.inputNoteTitle);
                    deleteData(topicDelete.getText().toString());
                    Intent intent = new Intent(CreateNoteActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();


                }
            });


            view.findViewById(R.id.textCancel).setOnClickListener(v -> dialogDeleteNote.dismiss());
        }

        dialogDeleteNote.show();
    }

    private void setSubtitleIndicatorColor() {
        GradientDrawable gradientDrawable = (GradientDrawable) viewSubtitleIndicator.getBackground();
        gradientDrawable.setColor(Color.parseColor(selectedNoteColor));
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri selectedImageUri = data.getData();
                if (selectedImageUri != null) {
                    try {


                        Glide.with(CreateNoteActivity.this)
                                .load(selectedImageUri)
                                .into(imageNote);

                        imageNote.setVisibility(View.VISIBLE);
                        findViewById(R.id.imageRemoveImage).setVisibility(View.VISIBLE);

                        selectedImagePath = access.getPathFromUri(selectedImageUri,getContentResolver());
                    } catch (Exception e) {
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }
    private void showAddURLDialog() {
        if (dialogAddURL == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(CreateNoteActivity.this);
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
                    Toast.makeText(CreateNoteActivity.this, "Enter URL", Toast.LENGTH_SHORT).show();
                } else if (!Patterns.WEB_URL.matcher(inputURLStr).matches()) {
                    Toast.makeText(CreateNoteActivity.this, "Enter valid URL", Toast.LENGTH_SHORT).show();
                } else {
                    textWebURL.setText(inputURL.getText().toString());
                    layoutWebURL.setVisibility(View.VISIBLE);
                    dialogAddURL.dismiss();
                }
            });

            view.findViewById(R.id.textCancel).setOnClickListener(v -> dialogAddURL.dismiss());
        }
        dialogAddURL.show();
    }
}