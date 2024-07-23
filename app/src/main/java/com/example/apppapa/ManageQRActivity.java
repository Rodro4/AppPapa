package com.example.apppapa;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import java.io.IOException;

public class ManageQRActivity extends AppCompatActivity {

    private EditText editTextDetails;
    private ImageView imageView;
    private Button btnSave, btnAddImage;
    private static final int PICK_IMAGE = 100;
    private Uri imageUri;
    private QRCodeDatabase db;
    private QRCode qrCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_qr);

        editTextDetails = findViewById(R.id.editTextDetails);
        imageView = findViewById(R.id.imageView);
        btnSave = findViewById(R.id.btnSave);
        btnAddImage = findViewById(R.id.btnAddImage);

        db = Room.databaseBuilder(getApplicationContext(), QRCodeDatabase.class, "qrcode_db").allowMainThreadQueries().build();

        String qrContent = getIntent().getStringExtra("qrContent");
        qrCode = db.qrCodeDao().findByContent(qrContent);

        if (qrCode != null) {
            editTextDetails.setText(qrCode.getDetails());
            // Aquí podrías cargar una imagen desde una ruta guardada en la base de datos si fuera necesario
        }

        btnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDetails();
            }
        });
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveDetails() {
        String details = editTextDetails.getText().toString();
        qrCode.setDetails(details);
        db.qrCodeDao().update(qrCode);
        Toast.makeText(this, "Detalles guardados", Toast.LENGTH_SHORT).show();
    }
}
