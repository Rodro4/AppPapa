package com.example.apppapa;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface QRCodeDao {

    @Insert
    void insert(QRCode qrCode);

    @Update
    void update(QRCode qrCode);

    @Query("SELECT * FROM QRCode WHERE content = :content")
    QRCode findByContent(String content);

    @Query("SELECT * FROM QRCode")
    List<QRCode> getAll();
}
