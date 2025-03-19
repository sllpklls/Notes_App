package com.rick.notes.activites;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.rick.notes.R;

public class FirstPassActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enterfirstpass);
        Button bt1 = findViewById(R.id.btSet);
        EditText ed1 = findViewById(R.id.firstpass);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveAndCheckPass check = new SaveAndCheckPass(getApplicationContext());
                check.savePassword(ed1.getText().toString());
                Toast.makeText(FirstPassActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(FirstPassActivity.this, ConfirmActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
