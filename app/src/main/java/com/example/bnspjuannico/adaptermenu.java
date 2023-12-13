package com.example.bnspjuannico;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Toast;

public class adaptermenu extends BaseAdapter {

    private Context context;
    private String[] menuItems;

    public adaptermenu(Context context, String[] menuItems) {
        this.context = context;
        this.menuItems = menuItems;
    }

    @Override
    public int getCount() {
        return menuItems.length;
    }

    @Override
    public Object getItem(int position) {
        return menuItems[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.customitem, null);
        }

        Button button = view.findViewById(R.id.menuButton);
        button.setText(menuItems[position]);

        button.setOnClickListener(v -> {
            String selectedItem = menuItems[position];
            if (selectedItem.equals("Informasi Pemilihan Umum")) {
                Intent intent = new Intent(context, informasi.class);
                context.startActivity(intent);
            } else if (selectedItem.equals("Form Entri Data Calon Pemilih")) {
                Intent intent = new Intent(context, formentry.class);
                context.startActivity(intent);
            } else if (selectedItem.equals("Melihat Informasi yang Sudah Dimasukkan")) {
                Intent intent = new Intent(context, datalist.class);
                context.startActivity(intent);
            }
        });

        return view;
    }
}
