package com.example.boilereport;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class FilterResult extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_filter_result);

        // Apply system insets (status bar, nav bar, etc.)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize card views
        TextView cardItem = findViewById(R.id.cardItem);
        TextView cardLocation = findViewById(R.id.cardLocation);
        TextView cardDescription = findViewById(R.id.cardDescription);

        // Get values passed from MainActivity
        String itemType = getIntent().getStringExtra("itemType");
        String building = getIntent().getStringExtra("building");

        // Query DB for description based on filters
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        Cursor cursor = dbHelper.getFilteredReports(itemType, building);

        if (cursor != null && cursor.moveToFirst()) {
            String description = cursor.getString(
                    cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESCRIPTION));

            // Fill in card
            cardItem.setText(itemType);
            cardLocation.setText(building);
            cardDescription.setText(description);

            cursor.close();
        }
    }
}
