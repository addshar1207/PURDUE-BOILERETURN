package com.example.boilereport;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private ActionBarDrawerToggle toggle;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Find views
        drawerLayout = findViewById(R.id.drawerLayout);
        toolbar = findViewById(R.id.toolbar);

        LinearLayout popularSearchesContainer = findViewById(R.id.popularSearchesContainer);
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        Cursor cursor = dbHelper.getTopLocations(3);

        Spinner spinnerItemType = findViewById(R.id.spinnerItemType);
        Spinner spinnerBuilding = findViewById(R.id.spinnerBuilding);
        Button filterButton = findViewById(R.id.filterButton);


        if (cursor != null && cursor.moveToFirst()) {
            do {
                String buildingName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_BUILDING));

                // Create horizontal LinearLayout
                LinearLayout itemLayout = new LinearLayout(this);
                itemLayout.setOrientation(LinearLayout.HORIZONTAL);
                itemLayout.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));

                // Add image (using a placeholder for now, like science building)
                ImageView imageView = new ImageView(this);
                imageView.setLayoutParams(new LinearLayout.LayoutParams(80, 80));
                imageView.setImageResource(R.drawable.science_building); // Replace with appropriate image if you have

                // Add TextView for building name
                TextView textView = new TextView(this);
                LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1
                );
                textView.setLayoutParams(textParams);
                textView.setPadding(8, 0, 0, 0);
                textView.setText(buildingName);
                textView.setTextSize(16);

                // Add views to horizontal layout
                itemLayout.addView(imageView);
                itemLayout.addView(textView);

                // Add horizontal layout to container
                popularSearchesContainer.addView(itemLayout);

            } while (cursor.moveToNext());

            cursor.close();
        }


        filterButton.setOnClickListener(v -> {
            String itemType = spinnerItemType.getSelectedItem().toString();
            String building = spinnerBuilding.getSelectedItem().toString();

            Intent intent = new Intent(MainActivity.this, FilterResult.class);
            intent.putExtra("itemType", itemType);
            intent.putExtra("building", building);
            startActivity(intent);

            drawerLayout.closeDrawers(); // close the sidebar
        });


        // Set up toolbar
        setSupportActionBar(toolbar);

        // Setup drawer toggle (hamburger button)
        toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        Button reportButton = findViewById(R.id.reportButton); // give your button an ID in XML
        reportButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Report.class);
            startActivity(intent);
        });

        // Handle window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
