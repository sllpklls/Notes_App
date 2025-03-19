package com.rick.notes.activites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.rick.notes.API.APIService;
import com.rick.notes.Model.ResultLogin;
import com.rick.notes.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    TextView username, password;
    Button btLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = findViewById(R.id.usernameLogin);
        password = findViewById(R.id.passwordLogin);
        btLogin = findViewById(R.id.btloginAPI);
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveAndCheckPass check =  new SaveAndCheckPass(getApplicationContext());
                check.saveUsernameAndPassword(username.getText().toString(),password.getText().toString());
                CallAPI(username.getText().toString(),password.getText().toString());
            }
        });
        TextView tv1 = findViewById(R.id.changepassapi);
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ChangePassAPIActicity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void CallAPI(String username, String password) {
        //SaveAndCheckPass a = new SaveAndCheckPass();
        APIService.apiService.data(username, password).enqueue(new Callback<ResultLogin>() {
            @Override
            public void onResponse(Call<ResultLogin> call, Response<ResultLogin> response) {
                ResultLogin result  = response.body();
                SaveAndCheckPass check = new SaveAndCheckPass(getApplicationContext());
                if(result != null && result.getSuccess().equals("true")){
                    //a.saveUsernameAndPassword(null,null);
                    check.setStatus("online");
                    Toast.makeText(LoginActivity.this, result.getMessage(), Toast.LENGTH_SHORT).show();
                    if(check.getPassword().equals("trong")){
                        Intent intent = new Intent(LoginActivity.this, FirstPassActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        Intent intent = new Intent(LoginActivity.this, ConfirmActivity.class);
                        startActivity(intent);
                        finish();
                    }



                }
                else{
                    Toast.makeText(LoginActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResultLogin> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "fail",Toast.LENGTH_SHORT).show();
//                Toast.makeText(LoginActivity.this, "hello----", Toast.LENGTH_SHORT).show();
//                SaveAndCheckPass check = new SaveAndCheckPass(getApplicationContext());
//                check.setStatus("offline");
//                Intent intent = new Intent(LoginActivity.this, ConfirmActivity.class);
//                startActivity(intent);
//                finish();
            }
        });
    }
}