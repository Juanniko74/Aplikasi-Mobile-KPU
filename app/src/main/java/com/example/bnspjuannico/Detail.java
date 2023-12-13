package com.example.bnspjuannico;


import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class Detail extends AppCompatActivity {

    private DatabaseHelper dbHelper; // Deklarasikan objek dbHelper

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        dbHelper = new DatabaseHelper(this); // Inisialisasi dbHelper

        // Terima data dari Intent
        Intent intent = getIntent();
        if (intent != null) {
            String selectedData = intent.getStringExtra("selectedData");
            String selectedImagePath = dbHelper.getImagePath(selectedData);

            // Tampilkan data pada layout activity detail
            TextView textViewDetail = findViewById(R.id.textViewDetail);
            ImageView imageViewDetail = findViewById(R.id.imageViewDetail);

            textViewDetail.setText(selectedData);

            // Tampilkan gambar menggunakan Glide atau metode lainnya
            if (selectedImagePath != null && !selectedImagePath.isEmpty()) {
                Glide.with(this).load(selectedImagePath).into(imageViewDetail);
            }
        }
    }
}
