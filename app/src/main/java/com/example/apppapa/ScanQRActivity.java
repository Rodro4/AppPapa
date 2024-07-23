package com.example.apppapa;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class ScanQRActivity extends AppCompatActivity {

    private QRCodeDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr);

        db = Room.databaseBuilder(getApplicationContext(), QRCodeDatabase.class, "qrcode_db").allowMainThreadQueries().build();

        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        integrator.setPrompt("Escanear código QR");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(true);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Escaneo cancelado", Toast.LENGTH_LONG).show();
            } else {
                String scannedText = result.getContents();
                QRCode qrCode = db.qrCodeDao().findByContent(scannedText);
                if (qrCode == null) {
                    qrCode = new QRCode(scannedText);
                    db.qrCodeDao().insert(qrCode);
                    Toast.makeText(this, "QR Code guardado en la base de datos", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "QR Code ya existente en la base de datos", Toast.LENGTH_LONG).show();
                }
                Intent intent = new Intent(this, ManageQRActivity.class);
                intent.putExtra("qrContent", scannedText);
                startActivity(intent);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
