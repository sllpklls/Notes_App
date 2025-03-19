package com.rick.notes.activites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.rick.notes.API.APIService;
import com.rick.notes.Controller.AccessForLogin;
import com.rick.notes.Model.ResultChange;
import com.rick.notes.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePassAPIActicity extends AppCompatActivity {
    EditText ed_username, ed_oldpass, ed_newpass, ed_confirm;
    TextView tv_dontchange;
    Button bt_change;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassuser);
        ed_username = findViewById(R.id.usernameapi);
        ed_oldpass = findViewById(R.id.oldpassapi);
        ed_newpass = findViewById(R.id.newpassapi);
        ed_confirm = findViewById(R.id.confirmpassapi);
        tv_dontchange = findViewById(R.id.backTextapi);
        bt_change = findViewById(R.id.buttonChangeapi);
        tv_dontchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChangePassAPIActicity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        bt_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccessForLogin access = new AccessForLogin();
                if(access.checkDifferent(ed_newpass.getText().toString(),ed_oldpass.getText().toString())){
                    if(access.checkEqual(ed_confirm.getText().toString(),ed_newpass.getText().toString())){
                        callAPIChangPass(ed_username.getText().toString(),ed_oldpass.getText().toString(),ed_newpass.getText().toString());
                    }
                    else{
                        Toast.makeText(ChangePassAPIActicity.this, access.promptMessage(4), Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(ChangePassAPIActicity.this, access.promptMessage(5), Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    private void callAPIChangPass(String username, String oldpassword, String newpassword) {
        APIService.apiService.changePass(username, oldpassword,newpassword).enqueue(new Callback<ResultChange>() {
            @Override
            public void onResponse(Call<ResultChange> call, Response<ResultChange> response) {
                ResultChange result  = response.body();
                AccessForLogin access = new AccessForLogin();
                if(result.isSuccess()){
                    Toast.makeText(ChangePassAPIActicity.this,access.promptMessage(1) , Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(ChangePassAPIActicity.this, access.promptMessage(2), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResultChange> call, Throwable t) {
                AccessForLogin access = new AccessForLogin();
                Toast.makeText(ChangePassAPIActicity.this,access.promptMessage(3),Toast.LENGTH_SHORT).show();
            }
        });
    }
}
