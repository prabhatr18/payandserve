package com.digital.payandserve.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;


import com.digital.payandserve.database.entity.NotificationTable;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface NotificationDAO {

    @Insert
    public void insert(NotificationTable... model);

    @Insert
    public void insertAll(List<NotificationTable> model);

    @Update
    public void update(NotificationTable... model);

    @Delete
    public void delete(NotificationTable model);

    @Query("DELETE FROM notification")
    public void cleanTable();

    @Query("SELECT * FROM notification")
    public List<NotificationTable> get();

    @Query("SELECT * FROM notification WHERE id = :id")
    public NotificationTable getNameWithID(String id);
}
