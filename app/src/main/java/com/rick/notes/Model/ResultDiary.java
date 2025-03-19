package com.rick.notes.Model;

import java.util.List;

public class ResultDiary {
    private String success;
    private String message;
    private List<Item> result;

    public ResultDiary(String success, String message, List<Item> result) {
        this.success = success;
        this.message = message;
        this.result = result;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Item> getResult() {
        return result;
    }

    public void setResult(List<Item> result) {
        this.result = result;
    }
}
