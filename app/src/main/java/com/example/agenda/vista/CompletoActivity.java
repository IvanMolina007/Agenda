package com.example.agenda.vista;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.example.agenda.R;
import com.example.agenda.controlador.BDOControlador;
import com.example.agenda.modelo.Contactos;

public class CompletoActivity extends AppCompatActivity {
    Toolbar toolbar;
    Bundle bundle;
    int id;
    BDOControlador bdoc;
    TextView nombre, telefono, direccion, email, emailIcono, llamarIcono, SMSIcono, web;
    ImageView imagen;
    Contactos contacto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completo);

        bdoc = new BDOControlador(getApplicationContext());
        bundle = getIntent().getExtras();
        id = bundle.getInt("id");

        nombre = findViewById(R.id.textViewCompletoNombre);
        telefono = findViewById(R.id.textViewTelefonoCompletoTexto);
        direccion = findViewById(R.id.textViewDireccionCompleto);
        email = findViewById(R.id.textViewEMAILCompletoTexto);
        web = findViewById(R.id.textViewWebCompleto);
        imagen  = findViewById(R.id.imageViewCompleto);
        emailIcono = findViewById(R.id.textViewEMAILCompleto);
        llamarIcono = findViewById(R.id.textViewLLamarCompleto);
        SMSIcono = findViewById(R.id.textViewSMSCompleto);

        toolbar = findViewById(R.id.toolbar);

        rellenarDatos();

        onClicks();

    }

    private void rellenarDatos() {
        contacto = bdoc.getContacto(id);

        if (contacto.getDireccion() != null) {
            direccion.setText(contacto.getDireccion());
        } else {
            direccion.setText("No hay dirección disponible");
        }

        nombre.setText(contacto.getNombre());
        telefono.setText(contacto.getTelefono());

        if (contacto.getEmail() != null) {
            email.setText(contacto.getEmail());
        } else {
            email.setText("No hay email disponible");
        }

        if (contacto.getWeb() != null) {
            web.setText(contacto.getWeb());
        } else {
            web.setText("No hay web disponible");
        }

        Glide.with(getApplicationContext()).load(contacto.getFoto()).into(imagen);

    }

    @Override
    protected void onResume() {
        super.onResume();
        rellenarDatos();
    }

    public void onClicks() {
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.eliminar_agenda) {
                long correcto = bdoc.eliminarContacto(id);
                if (correcto == 1) {
                    Toast.makeText(getApplicationContext(), "Se elimino el contacto", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "No se elimino el contacto", Toast.LENGTH_SHORT).show();
                }
            }
            if (item.getItemId() == R.id.modificar_agenda) {
                startActivity(new Intent(getApplicationContext(), EditarActivity.class).putExtra("id", id));
            }
            return true;
        });
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });

        llamarIcono.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:"+contacto.getTelefono()));
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        });

        telefono.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:"+contacto.getTelefono()));
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        });


        SMSIcono.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms",contacto.getTelefono(),null));
            startActivity(intent);
        });

        if (contacto.getWeb() != null) {
            web.setOnClickListener(v -> {
                Uri uri = Uri.parse("https://"+contacto.getWeb());
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
            });
        }

        if (contacto.getEmail()!= null) {
            email.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"+contacto.getEmail()));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            });

            emailIcono.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"+contacto.getEmail()));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            });
        }
    }
}