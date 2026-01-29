package com.example.locationrecord;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;
import com.example.locationrecord.LocationRecord;

import java.util.List;

@Dao
public interface LocationDao {
    @Insert
    void insert(LocationRecord record);

    @Query("SELECT data FROM location_history ORDER BY id DESC")
    List<String> getAllLocationStrings();
}