package com.example.agenda.vista;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.agenda.R;
import com.example.agenda.controlador.BDOControlador;
import com.example.agenda.modelo.Contactos;

public class InsertarActivity extends AppCompatActivity {
    EditText nombre, telefono, email, direccion, web;
    ImageView foto;
    final int REQUEST_IMAGE_CAPTURE = 100;
    Button subir;

    Uri imageUri, postStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insertar);

        nombre = findViewById(R.id.editTextNombreContacto);
        telefono = findViewById(R.id.editTextTelefonoContacto);
        email= findViewById(R.id.editTextEmailContacto);
        direccion = findViewById(R.id.editTextDireccionContacto);
        web = findViewById(R.id.editTextWebContacto);
        foto = findViewById(R.id.imageView2);
        subir = findViewById(R.id.buttonInsert);

        foto.setOnClickListener(v -> {
            elegirFoto();
        });

        subir.setOnClickListener(v -> {
            BDOControlador bdoc = new BDOControlador(getApplicationContext());

            if (!(nombre.getText().toString().equals("") || telefono.getText().toString().equals(""))) {
                Contactos contacto = new Contactos(bdoc.cargarUltimoId(), nombre.getText().toString(), telefono.getText().toString());
                if (!direccion.getText().toString().equals("")) {
                    contacto.setDireccion(direccion.getText().toString());
                }
                if (!email.getText().toString().equals("")) {
                    contacto.setEmail(email.getText().toString());
                }
                if (!web.getText().toString().equals("")) {
                    contacto.setWeb(web.getText().toString());
                }
                if (imageUri != null) {
                    contacto.setFoto(imageUri.toString());
                } else {
                    contacto.setFoto("https://www.pinclipart.com/picdir/big/393-3932440_png-file-svg-icono-de-contacto-png-blanco.png");
                }

                long correcto = bdoc.anadirContacto(contacto);

                if (correcto != -1) {
                    Toast.makeText(getApplicationContext(), "Se ha insertado el contacto", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "No se pudo insertar el contacto", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(getApplicationContext(), "Minimo tiene que haber valores en nombre y telefono", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void elegirFoto() {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);

        try {

            startActivityForResult(intent,REQUEST_IMAGE_CAPTURE);

        } catch(ActivityNotFoundException e){
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            assert data != null;

            if (postStorage == null){
                postStorage = Uri.EMPTY;
            }

            imageUri = data.getData();
            postStorage = imageUri;
            putImage(imageUri);

        } else {

            Toast.makeText(getApplicationContext(),"Error: No seleccionaste una imagen", Toast.LENGTH_LONG).show();

        }

    }

    private void putImage(Uri imageUri) {

        Glide.with(getApplicationContext()).load(imageUri).into(foto);

    }
}