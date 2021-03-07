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
import com.example.agenda.MainActivity;
import com.example.agenda.R;
import com.example.agenda.controlador.BDOControlador;
import com.example.agenda.modelo.Contactos;

public class EditarActivity extends AppCompatActivity {
    EditText nombreE, telefonoE, emailE, direccionE, webE;
    ImageView fotoE;
    final int REQUEST_IMAGE_CAPTUREE = 100;
    Button subirE;
    Bundle bundle;
    Contactos contactoE;
    Uri imageUriE, postStorageE;
    int id;
    String imageUriEAnt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar);

        bundle = getIntent().getExtras();
        id = bundle.getInt("id");

        nombreE = findViewById(R.id.editTextNombreContactoEditar);
        telefonoE = findViewById(R.id.editTextTelefonoContactoEditar);
        emailE = findViewById(R.id.editTextEmailContactoEditar);
        direccionE = findViewById(R.id.editTextDireccionContactoEditar);
        webE = findViewById(R.id.editTextWebContactoEditar);
        fotoE = findViewById(R.id.imageViewEditar);
        subirE = findViewById(R.id.buttonInsertEditar);

        BDOControlador bdoc = new BDOControlador(getApplicationContext());
        contactoE = bdoc.getContacto(id);
        imageUriEAnt = contactoE.getFoto();

        nombreE.setText(contactoE.getNombre());
        telefonoE.setText(contactoE.getTelefono());
        emailE.setText(contactoE.getEmail());
        direccionE.setText(contactoE.getDireccion());
        webE.setText(contactoE.getWeb());

        Glide.with(getApplicationContext()).load(contactoE.getId()).into(fotoE);


        fotoE.setOnClickListener(v -> {
            elegirFoto();
        });

        subirE.setOnClickListener(v -> {

            if(nombreE.getText().toString().equals("") || (telefonoE.getText().toString().equals(""))) {
                Toast.makeText(getApplicationContext(), "No pueden estar vacios ni telefono ni nombre", Toast.LENGTH_SHORT).show();
            }else {
                Contactos contacto = new Contactos();
                contacto.setId(id);
                contacto.setNombre(nombreE.getText().toString());
                if(!telefonoE.getText().toString().equals("")){
                    contacto.setTelefono(telefonoE.getText().toString());
                }
                if (!direccionE.getText().toString().equals("")) {
                    contacto.setDireccion(direccionE.getText().toString());
                }
                if (!emailE.getText().toString().equals("")){
                    contacto.setEmail(emailE.getText().toString());
                }
                if(!webE.getText().toString().equals("")){
                    contacto.setWeb(webE.getText().toString());
                }
                if(imageUriE!=null){
                    contacto.setFoto(imageUriE.toString());
                }else{
                    contacto.setFoto(imageUriEAnt);
                }
                long correcto;
                BDOControlador bco = new BDOControlador(getApplicationContext());
                MainActivity.recargarRecycler();
                correcto=bco.editarContactos(contacto);
                if (correcto!=0){
                    Toast.makeText(getApplicationContext(), "Contacto editado", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "No se pudo editar", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    private void elegirFoto() {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);

        try {

            startActivityForResult(intent, REQUEST_IMAGE_CAPTUREE);

        } catch (ActivityNotFoundException e) {
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTUREE && resultCode == RESULT_OK) {

            assert data != null;

            if (postStorageE == null) {
                postStorageE = Uri.EMPTY;
            }

            imageUriE = data.getData();
            postStorageE = imageUriE;
            putImage(imageUriE);

        } else {

            Toast.makeText(getApplicationContext(), "Error: No seleccionaste una imagen", Toast.LENGTH_LONG).show();

        }

    }

    private void putImage(Uri imageUri) {

        Glide.with(getApplicationContext()).load(imageUri).into(fotoE);

    }
}