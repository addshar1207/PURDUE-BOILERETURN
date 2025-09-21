package com.example.boilereport;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Report extends AppCompatActivity {

    private EditText etName, etPhone, etEmail, etItemName, etBuilding, etDescription;
    private Spinner spinnerItemType, spinnerBuilding;
    private Button btnSubmit;
    private DatabaseHelper dbHelper;
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView ivItemPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        // Enable edge-to-edge layout
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize database helper
        dbHelper = new DatabaseHelper(this);

        // Initialize views
        etName = findViewById(R.id.etFinderName);
        etPhone = findViewById(R.id.etFinderPhone);
        etEmail = findViewById(R.id.etFinderEmail);
        etItemName = findViewById(R.id.etItemName);
        etBuilding = findViewById(R.id.etBuilding);
        etDescription = findViewById(R.id.etDescription);
        spinnerItemType = findViewById(R.id.spinnerItemType);
        spinnerBuilding = findViewById(R.id.spinnerBuilding);
        btnSubmit = findViewById(R.id.submitButton);
        ivItemPhoto = findViewById(R.id.ivItemPhoto);
        Button btnUploadPhoto = findViewById(R.id.btnUploadPhoto);

        // Open gallery to choose a photo
        btnUploadPhoto.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        // Handle submit button
        btnSubmit.setOnClickListener(v -> {
            // Get all input values
            String name = etName.getText().toString();
            String phone = etPhone.getText().toString();
            String email = etEmail.getText().toString();
            String itemType = spinnerItemType.getSelectedItem().toString();
            String itemName = etItemName.getText().toString();
            String building = spinnerBuilding.getSelectedItem().toString();
            String description = etDescription.getText().toString();

            // Insert into database
            boolean inserted = dbHelper.insertReport(
                    name, phone, email, itemType, itemName, building, description
            );

            if (inserted) {
                Toast.makeText(Report.this, "Report submitted!", Toast.LENGTH_SHORT).show();
                // Navigate to Thank You page
                Intent intent = new Intent(Report.this, ThankYou.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(Report.this, "Failed to submit report", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Handle selected image from gallery
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            ivItemPhoto.setImageURI(imageUri);
        }
    }
}
