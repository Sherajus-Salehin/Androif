package com.example.locationscanner;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
@Database(entities = LocationEntity.class,version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract LocationDao locationDao();


    private static AppDatabase instance;
    public static AppDatabase getDbInstance(Context context){
        if(instance==null){
            instance= Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class,"location_db")
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
}
