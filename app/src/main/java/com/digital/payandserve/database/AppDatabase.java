package com.digital.payandserve.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.digital.payandserve.database.dao.NotificationDAO;
import com.digital.payandserve.database.entity.NotificationTable;
import com.digital.payandserve.database.typeconverters.DateTypeConverter;


@Database(entities = {NotificationTable.class}, version = 2, exportSchema = false)
@TypeConverters({DateTypeConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    private static final String DB_NAME = "db-notification";
    private static AppDatabase appDatabase = null;

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {

        }
    };

    public static AppDatabase getInstance(Context context) {
        if (appDatabase == null) {
            appDatabase = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, DB_NAME).allowMainThreadQueries().fallbackToDestructiveMigration().build();
        }
        return appDatabase;
    }


    public abstract NotificationDAO getNotificationDao();
}

