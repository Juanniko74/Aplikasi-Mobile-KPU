package com.example.bnspjuannico;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.ByteArrayOutputStream;


public class formentry extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    private static final int REQUEST_IMAGE_GALLERY = 2;

    private DatabaseHelper dbHelper;
    private EditText editTextNik;
    private EditText editTextNama;
    private EditText editTextNoHp;
    private RadioGroup radioGroupJenisKelamin;
    private RadioButton radioButtonLaki;
    private RadioButton radioButtonPerempuan;
    private EditText editTextTanggal;
    private EditText editTextAlamat;
    private ImageView imageViewGambar;
    private FusedLocationProviderClient fusedLocationClient;
    private String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formentry);

        dbHelper = new DatabaseHelper(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        editTextNik = findViewById(R.id.nik);
        editTextNama = findViewById(R.id.nama);
        editTextNoHp = findViewById(R.id.nohp);
        radioGroupJenisKelamin = findViewById(R.id.radioGroupJenisKelamin);
        radioButtonLaki = findViewById(R.id.jkpria);
        radioButtonPerempuan = findViewById(R.id.jkwanita);
        editTextTanggal = findViewById(R.id.tanggal);
        editTextAlamat = findViewById(R.id.alamat);
        imageViewGambar = findViewById(R.id.gambar);

        Button btnSubmit = findViewById(R.id.btnsubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveFormData();
            }
        });

        Button btnCekLokasi = findViewById(R.id.btnceklok);
        btnCekLokasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cekLokasi();
            }
        });

        imageViewGambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
    }

    private void selectImage() {
        final CharSequence[] options = {"Pilih dari Galeri", "Batal"};
        AlertDialog.Builder builder = new AlertDialog.Builder(formentry.this);
        builder.setTitle("Pilih Aksi");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Pilih dari Galeri")) {
                    if (ContextCompat.checkSelfPermission(formentry.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(formentry.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    } else {
                        choosePhotoFromGallery();
                    }
                } else if (options[item].equals("Batal")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void choosePhotoFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, REQUEST_IMAGE_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_IMAGE_GALLERY:
                    Uri selectedImageUri = data.getData();
                    String[] projection = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(selectedImageUri, projection, null, null, null);
                    if (cursor != null) {
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(projection[0]);
                        String picturePath = cursor.getString(columnIndex);
                        cursor.close();
                        Glide.with(this).load(picturePath).into(imageViewGambar);
                    }
                    break;
            }
        }
    }

    private void saveFormData() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String nik = editTextNik.getText().toString();
        String nama = editTextNama.getText().toString();
        String noHp = editTextNoHp.getText().toString();
        String jenisKelamin = (radioButtonLaki.isChecked()) ? "Laki-laki" : "Perempuan";
        String tanggal = editTextTanggal.getText().toString();
        String alamat = editTextAlamat.getText().toString();

        ContentValues values = new ContentValues();
        values.put("nik", nik);
        values.put("nama", nama);
        values.put("no_hp", noHp);
        values.put("jenis_kelamin", jenisKelamin);
        values.put("tanggal", tanggal);
        values.put("alamat", alamat);

        BitmapDrawable drawable = (BitmapDrawable) imageViewGambar.getDrawable();
        if (drawable != null) {
            Bitmap bitmap = drawable.getBitmap();
            // Konversi gambar ke dalam bentuk byte array
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
            byte[] imageData = stream.toByteArray();
            // Tambahkan data gambar ke dalam ContentValues
            values.put("gambar", imageData);
        }


        long newRowId = db.insert("FormData", null, values);

        if (newRowId == -1) {
            Toast.makeText(this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Data berhasil disimpan dengan ID: " + newRowId, Toast.LENGTH_SHORT).show();
        }

        db.close();
    }

    private void cekLokasi() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                double latitude = location.getLatitude();
                                double longitude = location.getLongitude();
                                Toast.makeText(formentry.this,
                                        "Latitude: " + latitude + ", Longitude: " + longitude,
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(formentry.this,
                                        "Lokasi tidak tersedia",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    cekLokasi();
                } else {
                    Toast.makeText(this, "Izin lokasi dibutuhkan untuk cek lokasi", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    choosePhotoFromGallery();
                } else {
                    Toast.makeText(this, "Izin akses galeri dibutuhkan untuk memilih gambar", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
}
