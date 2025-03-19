package com.rick.notes.activites;

import static com.rick.notes.API.APIService.gson;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.rick.notes.Model.ItemHaveRate;
import com.rick.notes.Model.ResultDiaryHaveRate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class SaveAndCheckPass {
    private Context mContext;
    public SaveAndCheckPass(){

    }
    public SaveAndCheckPass(Context context) {
        mContext = context;
    }
    public int hashDataInput(String data) {
        // Custom hash code implementation based on the meaningful fields of the object
        return Objects.hash(data);
    }
    public void saveUsernameAndPassword(String usernameLogin, String passwordLogin){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("save", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("usernameLogin", usernameLogin);
        editor.putString("passwordLogin", passwordLogin);
        editor.apply();
    }
    public void saveLinkAPI(String str){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("save", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("linkApi", str);
        editor.apply();
    }
    public void saveDataForCalendar(String str){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("save", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("calendar", str);
        editor.apply();
    }
    public String getDataForCalendar(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("save", Context.MODE_PRIVATE);
        return sharedPreferences.getString("calendar", "trong");
    }
    public String getLinkApi(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("save", Context.MODE_PRIVATE);
        return sharedPreferences.getString("linkApi", "192.168.1.238");

    }
    public String getUsernameLogin(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("save", Context.MODE_PRIVATE);
        return sharedPreferences.getString("usernameLogin", "trong");
    }
    public String getPasswordLogin(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("save", Context.MODE_PRIVATE);
        return sharedPreferences.getString("passwordLogin", "trong");
    }
    public void savePassword(String password) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("YourSharedPreferencesName", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("password", String.valueOf(hashDataInput(password)));
        editor.apply();
    }
    public void saveEditIndex(String topic){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("YourSharedPreferencesName", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("topicEdit", topic);
        editor.apply();
    }
    public String getEditIndex(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("YourSharedPreferencesName", Context.MODE_PRIVATE);
        return sharedPreferences.getString("topicEdit", "trong");

    }
    public void setSaveColor(String index){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("YourSharedPreferencesName", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        List<String> check = getSaveColor();
        for( String list : check){
            if(!index.equals(list)){
                check.add(index);
            }
        }
        StringBuilder builder = new StringBuilder();
        for (String str : check) {
            builder.append(str).append("-");
        }

        // Remove the trailing hyphen
        if (builder.length() > 0) {
            builder.deleteCharAt(builder.length() - 1);
        }

        //String total = getSaveColor()+","+index;
        editor.clear();
        editor.putString("saveColor", builder.toString());
        //editor.apply();
    }
    public List<String> getSaveColor(){
//        SharedPreferences sharedPreferences = mContext.getSharedPreferences("YourSharedPreferencesName", Context.MODE_PRIVATE);
//        //return sharedPreferences.getString("saveColor", null);
//        if()
//        String[] parts = sharedPreferences.getString("saveColor", null).split("-");
//        return Arrays.asList(parts);
    return null;

    }
    public void setStatus(String str){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("YourSharedPreferencesName", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("status", str);
        editor.apply();
    }
    public String getStatus(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("YourSharedPreferencesName", Context.MODE_PRIVATE);
        return sharedPreferences.getString("status", "trong");
    }
    public Boolean isOnline(){
        if(getStatus().equals("online")) return true;
        else return false;
    }

    public void saveDataRate(String str){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("YourSharedPreferencesName", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("rate", str);
        editor.apply();
    }
    public String getDataRate(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("YourSharedPreferencesName", Context.MODE_PRIVATE);
        return sharedPreferences.getString("rate", "trong");
    }
    public List<ItemHaveRate> convertData(){
        String str = getDataRate();
        Gson gson = new Gson();
        ResultDiaryHaveRate response = gson.fromJson(str, ResultDiaryHaveRate.class);
        List<ItemHaveRate> resultList = response.getResult();
        return resultList;
    }
    public void unconvertData(ResultDiaryHaveRate list){
        String jsonString = gson.toJson(list);
        //return jsonString;
        saveDataRate(jsonString);
    }
    public void setTopicAndContent(String topic, String content){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("YourSharedPreferencesName", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("topicRAM", topic);
        editor.putString("contentRAM", content);
        editor.apply();
    }
    public String getContentRAM(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("YourSharedPreferencesName", Context.MODE_PRIVATE);
        return sharedPreferences.getString("contentRAM", "trong");
    }
    public String getTopicRAM(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("YourSharedPreferencesName", Context.MODE_PRIVATE);
        return sharedPreferences.getString("topicRAM", "trong");
    }
    public void setDataOffline(String str){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("YourSharedPreferencesName", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("dataOffline", str);
        editor.apply();
    }
    public String getDataOffline(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("YourSharedPreferencesName", Context.MODE_PRIVATE);
        return sharedPreferences.getString("dataOffline", "trong");

    }
    public void setDataOfflineList(ResultDiaryHaveRate list){
        String jsonString = gson.toJson(list);
        setDataOffline(jsonString);
    }
    public List<ItemHaveRate> getDataOfflineList(){
        String str = getDataOffline();
        Gson gson = new Gson();
        ResultDiaryHaveRate response = gson.fromJson(str, ResultDiaryHaveRate.class);
        List<ItemHaveRate> resultList = response.getResult();
        return resultList;
    }
    public void clearFirstPassword(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("YourSharedPreferencesName", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("firstpassword",getPassword());
        editor.apply();
    }


    public String getPassword() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("YourSharedPreferencesName", Context.MODE_PRIVATE);
        return sharedPreferences.getString("password", "trong");
    }
    public boolean checkPassword(String input){
        if(String.valueOf(hashDataInput(input)).equals(getPassword())){
            return true;
        }
        return false;
    }
}

