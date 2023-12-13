package com.example.bnspjuannico;

import android.os.Bundle;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ambil referensi ke ListView dari XML
        ListView listView = findViewById(R.id.listViewMenu);

        if (listView != null) {
            // Buat data untuk ditampilkan di ListView
            String[] menuItems = {"Informasi Pemilihan Umum", "Form Entri Data Calon Pemilih", "Melihat Informasi yang Sudah Dimasukkan", "Keluar"};

            // Buat adapter untuk ListView
            adaptermenu adapter = new adaptermenu(this, menuItems);

            // Tetapkan adapter ke ListView
            listView.setAdapter(adapter);

        }
    }
}