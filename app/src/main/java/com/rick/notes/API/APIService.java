package com.rick.notes.API;

import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rick.notes.Model.ResultAdd;
import com.rick.notes.Model.ResultChange;
import com.rick.notes.Model.ResultDiary;
import com.rick.notes.Model.ResultDiaryHaveRate;
import com.rick.notes.Model.ResultLogin;
import com.rick.notes.Model.ResultUpdate;
import com.rick.notes.activites.SaveAndCheckPass;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface APIService {
    SaveAndCheckPass check  = new SaveAndCheckPass();
    String url = "http://192.168.52.139/NhatKyCuaThai/";
    //http://dknane.click/thai
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd")
            .create();
    OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(5, TimeUnit.SECONDS)
            .build();
    APIService apiService = new Retrofit.Builder()
            .baseUrl(url)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(APIService.class);
    @GET("api_gateway/UserService/login.php")
    Call<ResultLogin> data(@Query("id") String user_name, @Query("password") String password);

    @GET("api_gateway/LogService/listDataOfUser.php")
    Call<ResultDiary> dataDiary(@Query("username")String user_name);

    @POST("api_gateway/LogService/addDataHaveRate.php")
    Call<ResultAdd> insertData(@Query("username") String user_name, @Query("date") String date, @Query("topic") String topic, @Query("content") String content, @Query("rate") String rate);

    @PUT("api_gateway/LogService/updateDataHaveRate.php")
    Call<ResultUpdate> updateData(@Query("username") String user_name, @Query("topicold") String firstTopic, @Query("content") String content, @Query("topicnew") String lastTopic, @Query("rate") String rate);

    @DELETE("api_gateway/LogService/deleteData.php")
    Call<ResultUpdate> deleteData(@Query("username") String user_name, @Query("topic") String topic);

    @GET("api_gateway/LogService/checkExis.php")
    Call<ResultUpdate> checkExist(@Query("username") String user_name, @Query("topic") String topic);
    @PUT("api_gateway/UserService/changePass.php")
    Call<ResultChange> changePass(@Query("username") String user_name,@Query("oldpassword") String olpass, @Query("newpassword") String newpass);

    @GET("getRatebyUser.php")
    Call<ResultDiaryHaveRate> dataRate(@Query("username") String user_name, @Query("topic") String topic, @Query("content") String content);

    @GET("api_gateway/LogService/getDataFull.php")
    Call<ResultDiaryHaveRate> dataFull(@Query("username")String user_name);

}
