package com.rick.notes.activites;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.rick.notes.Controller.AccessForConfirm;
import com.rick.notes.R;
import com.rick.notes.security.MergeSecurity;

import java.util.Objects;

public class ConfirmActivity extends AppCompatActivity {
        Button login;
        EditText ed1;
        TextView tv1,tv2;
        SaveAndCheckPass check;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_confirm);
            //initSharedPreferences();
            check = new SaveAndCheckPass(getApplicationContext());
            ed1 = findViewById(R.id.password);
            login = findViewById(R.id.btlogin);
            tv1 =  findViewById(R.id.loginText);
            tv2 = findViewById(R.id.loginMain);
            tv2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ConfirmActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            tv1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ConfirmActivity.this, ChangePasswordActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            String chuoiDaLuu = check.getPassword();


            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AccessForConfirm access = new AccessForConfirm();
                    if(access.checkPasswordConfirm(ed1.getText().toString(),chuoiDaLuu)){
                        Intent intent = new Intent(ConfirmActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        Toast.makeText(ConfirmActivity.this, "Saiiiii", Toast.LENGTH_SHORT).show();
                    }

                }
            });




        }
}

