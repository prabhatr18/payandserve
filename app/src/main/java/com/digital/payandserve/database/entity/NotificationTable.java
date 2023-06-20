package com.digital.payandserve.database.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "notification")
public class NotificationTable {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id = 0;
    private String name;
    private String detail;
    private String isRead;

    public NotificationTable(String name, String detail, String isRead) {
        this.name = name;
        this.detail = detail;
        this.isRead = isRead;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    @NonNull
    public int getId() {
        return id;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    public String isRead() {
        return isRead;
    }

    public void setRead(String read) {
        isRead = read;
    }

    @Ignore
    public NotificationTable() {
    }
}