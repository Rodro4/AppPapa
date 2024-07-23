package com.example.apppapa;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {QRCode.class}, version = 1)
public abstract class QRCodeDatabase extends RoomDatabase {
    public abstract QRCodeDao qrCodeDao();

    private static volatile QRCodeDatabase INSTANCE;

    static QRCodeDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (QRCodeDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    QRCodeDatabase.class, "qr_code_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
