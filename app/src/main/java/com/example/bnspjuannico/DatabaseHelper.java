package com.example.bnspjuannico;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "FormData.db";
    private static final int DATABASE_VERSION = 2;

    // Nama tabel dan kolom
    public static final String TABLE_NAME = "FormData";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NIK = "nik";
    public static final String COLUMN_NAMA = "nama";
    public static final String COLUMN_NO_HP = "no_hp";
    public static final String COLUMN_JENIS_KELAMIN = "jenis_kelamin";
    public static final String COLUMN_TANGGAL = "tanggal";
    public static final String COLUMN_ALAMAT = "alamat";
    public static final String COLUMN_GAMBAR = "gambar";

    // Konstruktor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Buat tabel di sini
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_NIK + " TEXT," +
                COLUMN_NAMA + " TEXT," +
                COLUMN_NO_HP + " TEXT," +
                COLUMN_JENIS_KELAMIN + " TEXT," +
                COLUMN_TANGGAL + " TEXT," +
                COLUMN_ALAMAT + " TEXT," +
                COLUMN_GAMBAR + " BLOB)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Kode untuk meng-upgrade database jika diperlukan
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // EKSEKUSI SQL LITE BUAT BISA NAMPULIN DATANYA DI LIST VIEW
    public List<String> getAllFormData() {
        List<String> formDataList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            do {
                String dataText = "NIK: " + cursor.getString(cursor.getColumnIndex(COLUMN_NIK)) +
                        "\nNama: " + cursor.getString(cursor.getColumnIndex(COLUMN_NAMA)) +
                        "\nNo HP: " + cursor.getString(cursor.getColumnIndex(COLUMN_NO_HP)) +
                        "\nJenis Kelamin: " + cursor.getString(cursor.getColumnIndex(COLUMN_JENIS_KELAMIN)) +
                        "\nTanggal: " + cursor.getString(cursor.getColumnIndex(COLUMN_TANGGAL)) +
                        "\nAlamat: " + cursor.getString(cursor.getColumnIndex(COLUMN_ALAMAT));
                formDataList.add(dataText);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return formDataList;
    }

    // Metode untuk mendapatkan path gambar berdasarkan data tertentu
    public String getImagePath(String data) {
        SQLiteDatabase db = this.getReadableDatabase();
        String imagePath = null;

        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_GAMBAR}, COLUMN_NAMA + "=?",
                new String[]{data}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            imagePath = cursor.getString(cursor.getColumnIndex(COLUMN_GAMBAR));
            cursor.close();
        }

        return imagePath;
    }
}
