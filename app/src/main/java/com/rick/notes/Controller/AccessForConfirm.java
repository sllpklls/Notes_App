package com.rick.notes.Controller;

import java.util.Objects;

public class AccessForConfirm {
    public int hashDataInput(String data) {
        return Objects.hash(data);
    }
    public boolean checkPasswordConfirm(String getInForm, String password){
        if (String.valueOf(hashDataInput(getInForm)).equals(password)){
            return true;
        }
        return false;
    }
}
