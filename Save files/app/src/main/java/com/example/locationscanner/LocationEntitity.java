package com.example.locationscanner;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity(tableName="locations")
public class LocationEntitity {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public double latitude;
    public double longitude;
    public String timestamp;

    public LocationEntitity(double latitude, double longitude, String timestamp) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
    }
}
