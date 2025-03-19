package com.rick.notes.security;

import com.rick.notes.activites.SaveAndCheckPass;

public class MergeSecurity {
    //String initVector = "1234567890123456";
    public MergeSecurity(){
    }
    public String encryptData(String key, String data){
        String hashKey = HashUtils.generateSHA256(key);
        String first16Characters = hashKey.substring(0, 16);
        //String initVector = "1234567890123456";
        String encryptedData = AESUtil.encrypt(first16Characters,"1234567890123456", data);
        //String decryptedData = AESUtil.decrypt(first16Characters, initVector, encryptedData);
        //Toast.makeText(MainActivity.this, decryptedData, Toast.LENGTH_SHORT).show();
        return encryptedData;
    }

    public String decryptData(String key, String dataEncrypted){
        String hashKey = HashUtils.generateSHA256(key);
        String first16Characters = hashKey.substring(0, 16);
        //String initVector = "1234567890123456";
        //String encryptedData = AESUtil.encrypt(first16Characters, initVector, data)
        String decryptedData = AESUtil.decrypt(first16Characters, "1234567890123456", dataEncrypted);
        return decryptedData;
    }
    public String abc(){
        //String hashKey = HashUtils.generateSHA256("thai");
        //String first16Characters = hashKey.substring(0, 16);
        return  AESUtil.decrypt("hoangthaihoangthaithaivc", "1234567890123456", "dataEncrypted");
    }
}
