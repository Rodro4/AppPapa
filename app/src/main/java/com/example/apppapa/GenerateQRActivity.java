package com.example.apppapa;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class GenerateQRActivity extends AppCompatActivity {

    private EditText editText;
    private ImageView imageView;
    private Button btnGenerate, btnSave, btnShare;
    private Bitmap qrCodeBitmap;
    private File qrCodeImageFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_qr);

        editText = findViewById(R.id.editText);
        imageView = findViewById(R.id.imageView);
        btnGenerate = findViewById(R.id.btnGenerate);
        btnSave = findViewById(R.id.btnSave);
        btnShare = findViewById(R.id.btnShare);

        btnGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editText.getText().toString();
                if (!text.isEmpty()) {
                    generateQRCode(text);
                } else {
                    Toast.makeText(GenerateQRActivity.this, "El texto no puede estar vacío", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (qrCodeBitmap != null) {
                    saveQRCodeImage(qrCodeBitmap);
                } else {
                    Toast.makeText(GenerateQRActivity.this, "Primero genera un código QR", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (qrCodeBitmap != null) {
                    shareQRCodeImage();
                } else {
                    Toast.makeText(GenerateQRActivity.this, "Primero genera un código QR", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void generateQRCode(String text) {
        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        try {
            BitMatrix bitMatrix = barcodeEncoder.encode(text, BarcodeFormat.QR_CODE, 400, 400);
            qrCodeBitmap = barcodeEncoder.createBitmap(bitMatrix);
            imageView.setImageBitmap(qrCodeBitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private void saveQRCodeImage(Bitmap bitmap) {
        String fileName = "QRCode_" + System.currentTimeMillis() + ".jpg";
        File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "QRApp");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        qrCodeImageFile = new File(directory, fileName);
        try (FileOutputStream out = new FileOutputStream(qrCodeImageFile)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            Toast.makeText(this, "Imagen guardada en " + qrCodeImageFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void shareQRCodeImage() {
        Uri uri = FileProvider.getUriForFile(this, "com.example.koalaapp.fileprovider", qrCodeImageFile);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(intent, "Compartir QR Code"));
    }
}
