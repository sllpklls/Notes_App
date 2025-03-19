package com.rick.notes.Model;

public class ResultAdd {
    private String success;
    private String message;

    public ResultAdd(String success, String message) {
        this.success = success;
        this.message = message;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }
}
