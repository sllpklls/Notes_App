package com.rick.notes.activites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.rick.notes.R;

public class LinkApiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_path);
        Button btnSetLink = findViewById(R.id.btSetlinkapi);
        TextView tvLinkApi = findViewById(R.id.linkapi);
        btnSetLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveAndCheckPass check = new SaveAndCheckPass(getApplicationContext());
                check.saveLinkAPI(tvLinkApi.getText().toString());
                Intent intent = new Intent(LinkApiActivity.this, ScreenActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}