package com.example.locationrecord;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.net.URL;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private Button button1;
    private EditText Clocation;
    private ListView listView;
    private AppDatabase db;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button1 = findViewById(R.id.button1);
        Clocation = findViewById(R.id.Clocation);
        listView = findViewById(R.id.listView3);

        db = AppDatabase.getInstance(this);
        updateListView();

        button1.setOnClickListener(v -> fetchLocationFromAPI());
        startBackgroundLoop();
    }

    private void fetchLocationFromAPI() {
        new Thread(() -> {
            try {
                final String displayString = fetchLocationData(false);
                runOnUiThread(() -> {
                    Clocation.setText(displayString);
                    updateListView();
                });

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Clocation.setText("Error: " + e.getMessage()));
            }
        }).start();
    }

    private void startBackgroundLoop() {
        new Thread(() -> {
            while (true) {
                try {
                    String data = fetchLocationData(true);
                    if (data != null) {
                        saveToDatabase(data);
                        runOnUiThread(() -> refreshUI(data));
                    }

                    //5 minutes (300,000 milliseconds)
                    Thread.sleep(300000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }).start();
    }

    private String fetchLocationData(boolean backgroundCall) {
        try {
            URL url = new URL("http://ip-api.com/json/");
            Scanner sc = new Scanner(url.openStream());
            StringBuilder sb = new StringBuilder();
            while (sc.hasNext()) sb.append(sc.next());

            JSONObject loc = new JSONObject(sb.toString());
            //Random r= new Random();
            //lat= r.nextInt(100);
            if(backgroundCall){

                Instant stamp= Instant.now();
                String time=String.valueOf(stamp);
                return "Lat: " + loc.getDouble("lat") + " | Lon: " + loc.getDouble("lon")+"\nTime: "+time;
            }else {
                return "Lat: " + loc.getDouble("lat") + " | Lon: " + loc.getDouble("lon")+ " (" + loc.getString("city") + ")";
            }
        } catch (Exception e) {
            return null;
        }
    }
    private void saveToDatabase(String data) {
        AppDatabase.getInstance(this).locationDao().insert(new LocationRecord(data));
    }
    private void refreshUI(String a) {
        updateListView();
    }

    private void updateListView() {
        List<String> history = db.locationDao().getAllLocationStrings();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                history
        );
        listView.setAdapter(adapter);
    }
}