package com.example.locationscanner;

import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

public interface LocationDao {
    @Insert
    void insert(LocationEntitity le);
    @Query("SELECT * FROM locations ORDER BY id DESC")
    List<LocationEntitity> getAllLocations();
}
