package com.rick.notes.Controller;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Environment;
import android.text.TextPaint;
import android.widget.Toast;

import com.rick.notes.activites.ConvertImageActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class AccessForConvertImage {
    public Bitmap textToImage(String title, String text, int textColor) {
        String[] lines = text.split("\n");
        int maxWidth = 800; // Width of the bitmap
        int padding = 50; // Padding from the sides
        int lineHeight = 60; // Height of each line of text
        int textSize = 50; // Text size

        TextPaint textPaint = new TextPaint();
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
        textPaint.setTextAlign(Paint.Align.LEFT);

        // Calculate total height based on number of lines
        int totalHeight = 200 + (lines.length * lineHeight); // 200 for title

        // Create a mutable bitmap with calculated dimensions
        Bitmap bitmap = Bitmap.createBitmap(maxWidth, totalHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE); // Background color

        // Draw title
        canvas.drawText(title, padding, 100, textPaint);
        textPaint.setColor(Color.BLACK);
        // Draw each line of text
        int y = 220; // Starting y position for text
        for (String line : lines) {
            canvas.drawText(line, padding, y, textPaint);
            y += lineHeight; // Move to next line
        }

        return bitmap;
    }
    public boolean saveImageToGallery(Bitmap bitmap) {
        FileOutputStream fos = null;
        try {
            File directory = new File(Environment.getExternalStorageDirectory().toString() + "/MyImages");
            if (!directory.exists()) {
                directory.mkdirs();
            }
            File file = new File(directory, System.currentTimeMillis() + ".png");
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            return true;
            //Toast.makeText(ConvertImageActivity.this, "Image saved", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
            //Toast.makeText(ConvertImageActivity.this, "Error saving image", Toast.LENGTH_SHORT).show();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }
}
