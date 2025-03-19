package com.rick.notes.activites;

//import static android.provider.Telephony.Mms.Part.CHARSET;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.rick.notes.Controller.AccessForLogin;
import com.rick.notes.R;
import com.rick.notes.dao.NoteDao;
import com.rick.notes.database.NotesDatabase;
import com.rick.notes.entities.Note;
import com.rick.notes.security.AESUtil;
import com.rick.notes.security.HashUtils;
import com.rick.notes.security.MergeSecurity;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class ChangePasswordActivity extends AppCompatActivity {
    Button bt1;
    public static final int REQUEST_CODE_SHOW_NOTES = 3;
    private List<Note> noteList;
    TextView tv1;
    EditText ed1,ed2,ed3;
    String oldpass,newpass;
    AccessForLogin access = new AccessForLogin();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);
        noteList = new ArrayList<Note>();
        
        bt1 = findViewById(R.id.buttonChange);
        tv1 = findViewById(R.id.backText);
        ed1 = findViewById(R.id.oldpass);
        ed2 = findViewById(R.id.newpass);
        ed3 =  findViewById(R.id.confirmpass);

        //getNotes(REQUEST_CODE_SHOW_NOTES, false);
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChangePasswordActivity.this, ConfirmActivity.class);
                startActivity(intent);
                finish();
            }
        });
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!access.checkConfirm(ed2.getText().toString(), ed3.getText().toString())){
                    Toast.makeText(ChangePasswordActivity.this, "Not equal confirm", Toast.LENGTH_SHORT).show();
                }
                if(access.checkEmpty(ed1.getText().toString(),ed3.getText().toString(),ed3.getText().toString())==true){
                    Toast.makeText(ChangePasswordActivity.this, "Empty edittext", Toast.LENGTH_SHORT).show();
                }
                if(access.checkEmpty(ed1.getText().toString(),ed3.getText().toString(),ed3.getText().toString())==false && access.checkConfirm(ed2.getText().toString(), ed3.getText().toString()) == true){
                    //getPasswordSaved();
                    SaveAndCheckPass check = new SaveAndCheckPass(getApplicationContext());
                    
                    if(check.checkPassword(ed1.getText().toString())){
                        oldpass = ed1.getText().toString();
                        newpass = ed2.getText().toString();
                        Toast.makeText(ChangePasswordActivity.this, "Changed", Toast.LENGTH_SHORT).show();
                        getNotes(REQUEST_CODE_SHOW_NOTES, false);
                        Intent intent = new Intent(ChangePasswordActivity.this, ConfirmActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        Toast.makeText(ChangePasswordActivity.this, "Old password wrong", Toast.LENGTH_SHORT).show();
                    }


                }
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
                MergeSecurity decryptdata =  new MergeSecurity();
                SaveAndCheckPass save = new SaveAndCheckPass(getApplicationContext());
                if (requestCode == REQUEST_CODE_SHOW_NOTES) {
//
                    noteList.addAll(notes);

                    Toast.makeText(ChangePasswordActivity.this, "done", Toast.LENGTH_SHORT).show();
                    
                }
                save.savePassword(newpass);

            }
        }

        new GetNoteTask().execute();
    }
    public String encryptData(String key, String data){
        String hashKey = HashUtils.generateSHA256(key);
        String first16Characters = hashKey.substring(0, 16);
        String encryptedData = AESUtil.encrypt(first16Characters, "1234567890123456", data);
        return encryptedData;
    }
    public String decryptData(String key, String dataEncrypted){
        String hashKey = HashUtils.generateSHA256(key);
        String first16Characters = hashKey.substring(0, 16);
        String decryptedData = AESUtil.decrypt(first16Characters, "1234567890123456", dataEncrypted);
        return decryptedData;
    }
    public int hashDataInput(String data) {
        return Objects.hash(data);
    }


    public static String encrypt(String key, String initVector, String data) {
        try {
            String AES_ALGORITHM = "AES";
            String AES_TRANSFORMATION = "AES/CBC/PKCS5Padding";
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), AES_ALGORITHM);

            Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(data.getBytes());

            return Base64.encodeToString(encrypted, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String decrypt(String key, String initVector, String encryptedData) {
        try {//Toast.makeText(null, "vclll", Toast.LENGTH_SHORT).show();
            String AES_ALGORITHM = "AES";
            String AES_TRANSFORMATION = "AES/CBC/PKCS5Padding";

            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), AES_ALGORITHM);

            Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(Base64.decode(encryptedData, Base64.DEFAULT));

            return new String(original, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String generateSHA256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");


            byte[] inputBytes = input.getBytes(StandardCharsets.UTF_8);

            byte[] hashedBytes = digest.digest(inputBytes);

            StringBuilder hexString = new StringBuilder();
            for (byte hashedByte : hashedBytes) {
                String hex = Integer.toHexString(0xff & hashedByte);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }


    }
    public String encodeData(String key, String data){
        String hashKey = generateSHA256(key);
        String first16Characters = hashKey.substring(0, 16);
        String encryptedData = encrypt(first16Characters,"1234567890123456", data);
        return encryptedData;
    }
    public String decodeData(String key, String dataEncrypted){
        String hashKey = generateSHA256(key);
        String first16Characters = hashKey.substring(0, 16);
        String decryptedData = decrypt(first16Characters, "1234567890123456", dataEncrypted);
        return decryptedData;
    }


    private void saveNote() {


        @SuppressLint("StaticFieldLeak")
        class SaveNoteTask extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {
                for(int i = 0 ;i<noteList.size();i++){
                    NotesDatabase.getNotesDatabase(getApplicationContext()).noteDao().insertNote(noteList.get(i));
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);

                finish();
            }
        }

        new SaveNoteTask().execute();
    }

    private void ClearNote() {


        @SuppressLint("StaticFieldLeak")
        class ClearNoteTask extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {
                NotesDatabase.getNotesDatabase(getApplicationContext()).noteDao().deleteAllNotes();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);

                finish();
            }
        }

        new ClearNoteTask().execute();
    }



}