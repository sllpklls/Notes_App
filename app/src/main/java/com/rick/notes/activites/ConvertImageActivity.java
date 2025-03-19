package com.rick.notes.activites;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.rick.notes.Controller.AccessForConvertImage;
import com.rick.notes.R;




import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.TextPaint;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ConvertImageActivity extends AppCompatActivity {

    private static final int REQUEST_WRITE_STORAGE = 112;
    private static final int REQUEST_MANAGE_STORAGE = 113;

    private ImageView imageView;
    private Bitmap textImage;
    AccessForConvertImage access = new AccessForConvertImage();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convertimage);

        imageView = findViewById(R.id.image_view);
        //Button buttonConvert = findViewById(R.id.button_convert);
        Button buttonSave = findViewById(R.id.button_save);
        SaveAndCheckPass check = new SaveAndCheckPass(getApplicationContext());


                String textToConvert = "-Content: "+check.getContentRAM();
                int textColor = Color.RED; // Change text color here
                String textTitle= "-Topic: "+check.getTopicRAM();
                textImage = access.textToImage(textTitle,textToConvert, textColor);
                imageView.setImageBitmap(textImage);


        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textImage != null) {
                    if (access.isExternalStorageWritable()) {
                        checkPermissionAndSaveImage();
                    } else {
                        Toast.makeText(ConvertImageActivity.this, "External storage is not writable", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ConvertImageActivity.this, "No image to save", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void checkPermissionAndSaveImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                try {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.setData(Uri.parse(String.format("package:%s", getApplicationContext().getPackageName())));
                    startActivityForResult(intent, REQUEST_MANAGE_STORAGE);
                } catch (Exception e) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                    startActivityForResult(intent, REQUEST_MANAGE_STORAGE);
                }
            } else {
                if(access.saveImageToGallery(textImage)){
                    Toast.makeText(ConvertImageActivity.this, "Image saved", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(ConvertImageActivity.this, "Error saving image", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_WRITE_STORAGE);
            } else {
                if(access.saveImageToGallery(textImage)){
                    Toast.makeText(ConvertImageActivity.this, "Image saved", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(ConvertImageActivity.this, "Error saving image", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_WRITE_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if(access.saveImageToGallery(textImage)){
                    Toast.makeText(ConvertImageActivity.this, "Image saved", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(ConvertImageActivity.this, "Error saving image", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_MANAGE_STORAGE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    if(access.saveImageToGallery(textImage)){
                        Toast.makeText(ConvertImageActivity.this, "Image saved", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(ConvertImageActivity.this, "Error saving image", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


//    private boolean isExternalStorageWritable() {
//        String state = Environment.getExternalStorageState();
//        return Environment.MEDIA_MOUNTED.equals(state);
//    }
}
