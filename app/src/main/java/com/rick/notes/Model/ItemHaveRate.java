package com.rick.notes.Model;

public class ItemHaveRate {
    private String date;
    private String topic;
    private String content;

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    private String rate;
    public ItemHaveRate(String date, String topic, String content, String rate) {
        this.date = date;
        this.topic = topic;
        this.content = content;
        this.rate = rate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
