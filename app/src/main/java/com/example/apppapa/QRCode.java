package com.example.apppapa;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class QRCode {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String content;
    private String details;

    public QRCode(String content) {
        this.content = content;
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

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return content;
    }
}
