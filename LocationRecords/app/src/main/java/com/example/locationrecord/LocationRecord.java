package com.example.locationrecord;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "location_history")
public class LocationRecord {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String data;

    public LocationRecord(String data) {
        this.data = data;
    }
}