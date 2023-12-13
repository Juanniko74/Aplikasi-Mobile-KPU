package com.example.bnspjuannico;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class datalist extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DataAdapter dataAdapter;
    private DatabaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datalist);
        recyclerView = findViewById(R.id.recyclerViewData);
        dbHelper = new DatabaseHelper(this);

        // Mendapatkan data dari database
        List<String> formDataList = dbHelper.getAllFormData();

        // Inisialisasi dan mengatur RecyclerView
        dataAdapter = new DataAdapter(this, formDataList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(dataAdapter);

        // Set up listener untuk RecyclerView di InformasiActivity
        dataAdapter.setOnItemClickListener(new DataAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // Dapatkan data yang dipilih
                String selectedData = formDataList.get(position);

                // Buat Intent untuk membuka DetailActivity
                Intent intent = new Intent(datalist.this, Detail.class);

                // Sertakan data yang diperlukan sebagai ekstra
                intent.putExtra("selectedData", selectedData);

                // Mulai DetailActivity
                startActivity(intent);
            }
        });
    }
}