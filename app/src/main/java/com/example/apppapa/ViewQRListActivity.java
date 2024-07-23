package com.example.apppapa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import java.util.List;

public class ViewQRListActivity extends AppCompatActivity {

    private ListView listView;
    private QRCodeDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_qr_list);

        listView = findViewById(R.id.listView);
        db = Room.databaseBuilder(getApplicationContext(), QRCodeDatabase.class, "qrcode_db").allowMainThreadQueries().build();

        List<QRCode> qrCodeList = db.qrCodeDao().getAll();
        ArrayAdapter<QRCode> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, qrCodeList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                QRCode selectedQRCode = (QRCode) parent.getItemAtPosition(position);
                Intent intent = new Intent(ViewQRListActivity.this, ManageQRActivity.class);
                intent.putExtra("qrContent", selectedQRCode.getContent());
                startActivity(intent);
            }
        });
    }
}
