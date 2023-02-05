package com.example.WhatsappAPI;

import java.util.Date;

public class Message {
    private int id;
    private String content;
    private Date timeStamp ;

    public Message(int id, String content) {
        this.id = id;
        this.content = content;
        this.timeStamp = new Date();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }
}
