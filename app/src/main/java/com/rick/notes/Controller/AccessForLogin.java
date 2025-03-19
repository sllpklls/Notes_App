package com.rick.notes.Controller;


import java.util.Objects;

public class AccessForLogin {
    public Boolean checkEmpty(String str){
        if(str.equals("")){
            return true;
        }
        return false;
    }
    public String promptMessage(int result){
        if( result == 1) {
            return "Đổi mật khẩu thành công!";
        }
        if(result == 2){
            return "Sai tài khoản hoặc mật khẩu!";
        }
        if(result == 3){
            return "Không thể đổi mật khẩu!";
        }
        if(result == 4){
            return "Mật khẩu mới phải trùng với xác nhận!";
        }
        if(result == 5){
            return "Mật khẩu mới không thể trùng mật khẩu cũ!";
        }
        return "";
    }
    public boolean checkEqual(String str1, String str2){
        if(str1.equals(str2)) return true;
        else return false;
    }
    public boolean checkDifferent(String str1, String str2){
        if(str1.equals(str2)) return false;
        else return true;
    }
    public boolean CheckExist(String str1, String str2){
        String rs = "trong";
        if(str1.equals(rs)||str2.equals(rs)) return true;
        return false;
    }
    public boolean checkConfirm(String str1, String str2){
        if(Objects.equals(str1,str2)){

            return true;
        }
        else return false;
    }
    public boolean checkEmpty(String str1, String str2, String str3){
        if(str1.equals(null) || str2.equals(null) || str3.equals(null)){
            return true;
        }
        else return false;
    }

}
