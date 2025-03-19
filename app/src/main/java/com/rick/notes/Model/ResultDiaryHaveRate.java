package com.rick.notes.Model;

import java.util.List;

public class ResultDiaryHaveRate {
    private String success;
    private String message;
    private List<ItemHaveRate> result;

    public ResultDiaryHaveRate(String success, String message, List<ItemHaveRate> result) {
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

    public List<ItemHaveRate> getResult() {
        return result;
    }

    public void setResult(List<ItemHaveRate> result) {
        this.result = result;
    }
}
