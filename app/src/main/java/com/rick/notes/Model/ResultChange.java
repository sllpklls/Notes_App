package com.rick.notes.Model;

public class ResultChange {
    private boolean success;
    private String message;
    private int rows_affected;

    public ResultChange(boolean success, String message, int rows_affected) {
        this.success = success;
        this.message = message;
        this.rows_affected = rows_affected;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getRows_affected() {
        return rows_affected;
    }

    public void setRows_affected(int rows_affected) {
        this.rows_affected = rows_affected;
    }
}
