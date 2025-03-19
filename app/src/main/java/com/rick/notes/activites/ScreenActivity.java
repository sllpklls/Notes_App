package com.rick.notes.activites;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.rick.notes.API.APIService;
import com.rick.notes.Controller.AccessForCreateAndCopy;
import com.rick.notes.Controller.AccessForLogin;
import com.rick.notes.Model.ItemHaveRate;
import com.rick.notes.Model.ResultAdd;
import com.rick.notes.Model.ResultDiaryHaveRate;
import com.rick.notes.Model.ResultLogin;
import com.rick.notes.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScreenActivity extends AppCompatActivity {
    TextView username, password;
    Button btLogin;
    AccessForLogin access = new AccessForLogin();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SaveAndCheckPass check = new SaveAndCheckPass(getApplicationContext());
                if(access.CheckExist(check.getUsernameLogin(),check.getPasswordLogin())){
                    Intent intent = new Intent(ScreenActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    CallAPI(check.getUsernameLogin(),check.getPasswordLogin());
                }



            }
        },3000);
    }
    private void CallAPI(String username, String password) {
        APIService.apiService.data(username, password).enqueue(new Callback<ResultLogin>() {
            @Override
            public void onResponse(Call<ResultLogin> call, Response<ResultLogin> response) {
                ResultLogin result  = response.body();
                //SaveAndCheckPass check = new SaveAndCheckPass(getApplicationContext());

                if(result != null){
                    if(result.getSuccess().equals("true")){
                        SaveAndCheckPass check = new SaveAndCheckPass(getApplicationContext());
                        check.setStatus("online");

                        insertDataOnline();
                        //up api here
                        if(check.getPassword().equals("trong")){
                            Intent intent = new Intent(ScreenActivity.this, FirstPassActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            Intent intent = new Intent(ScreenActivity.this, ConfirmActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                    else{
                        Toast.makeText(ScreenActivity.this, "Co ve mat khau bi thay doi", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ScreenActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResultLogin> call, Throwable t) {
                Toast.makeText(ScreenActivity.this, "fail",Toast.LENGTH_SHORT).show();
                Toast.makeText(ScreenActivity.this, "hello----", Toast.LENGTH_SHORT).show();
                SaveAndCheckPass check = new SaveAndCheckPass(getApplicationContext());
                check.setStatus("offline");
                Intent intent = new Intent(ScreenActivity.this, ConfirmActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void insertDataOnline(){
        SaveAndCheckPass check = new SaveAndCheckPass(getApplicationContext());
        List<ItemHaveRate> listObj = new ArrayList<ItemHaveRate>();
        if(!check.getDataOffline().equals("trong")){
            listObj = check.getDataOfflineList();
            for(int i=0;i<listObj.size();i++){
                insertData(check.getUsernameLogin(),listObj.get(i).getTopic(),listObj.get(i).getContent(),"",listObj.get(i).getDate());
            }
            check.setDataOffline("trong");
        }

    }
    private void insertData(String user_name, String topic, String content, String rate,String date){
        SaveAndCheckPass check = new SaveAndCheckPass(getApplicationContext());
         APIService.apiService.insertData(user_name,date,topic, content, rate).enqueue(new Callback<ResultAdd>() {
                @Override
                public void onResponse(Call<ResultAdd> call, Response<ResultAdd> response) {
                    Toast.makeText(ScreenActivity.this, "Đã đồng bộ", Toast.LENGTH_SHORT).show();
                    //Intent intent = new Intent(CreateNoteActivity.this, MainActivity.class);
                    //startActivity(intent);
                    //finish();
                }

                @Override
                public void onFailure(Call<ResultAdd> call, Throwable t) {
                    SaveAndCheckPass check =new SaveAndCheckPass(getApplicationContext());
                    Log.d("vcl--------",check.getDataRate());
                    Toast.makeText(ScreenActivity.this, "Đồng bộ không thành công", Toast.LENGTH_SHORT).show();
                }
      });
    }


}
