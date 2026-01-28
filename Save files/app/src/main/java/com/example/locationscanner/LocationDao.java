package com.example.locationscanner;

import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

public interface LocationDao {
    @Insert
    void insert(LocationEntity le);
    @Query("SELECT * FROM LocationEntity ORDER BY id DESC")
    List<LocationEntity> getAllLocations();
}
