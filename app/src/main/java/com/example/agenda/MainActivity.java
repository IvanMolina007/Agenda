package com.example.agenda;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agenda.adapter.ContactosAdapter;
import com.example.agenda.controlador.BDOControlador;
import com.example.agenda.modelo.Contactos;
import com.example.agenda.vista.InsertarActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    static RecyclerView recyclerContactos;
    static ArrayList<Contactos> listaContactos;
    static BDOControlador bdoControlador;
    static Context context;
    FloatingActionButton anadir_agenda;
    TextView buscar;
    Switch boton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerContactos = findViewById(R.id.recyclerContactos);
        bdoControlador = new BDOControlador(getApplicationContext());
        listaContactos = new ArrayList<>();
        anadir_agenda = findViewById(R.id.floatingActionButton);
        buscar = findViewById(R.id.editTextBuscador);
        boton = findViewById(R.id.switch1);

        boton.setOnClickListener(v -> {
            if (boton.isChecked()) {
                buscador();
            } else {
                recargarRecycler();
                buscar.setText("");
            }
        });

        onClicks();

        recargarRecycler();
    }

    public void onClicks() {
        anadir_agenda.setOnClickListener(v -> {
            startActivity(new Intent(this, InsertarActivity.class));
        });

    }

    public static void recargarRecycler() {
        listaContactos.clear();
        listaContactos = bdoControlador.cargarContactos();
        recyclerContactos.setLayoutManager(new LinearLayoutManager(context));
        recyclerContactos.setAdapter(new ContactosAdapter(listaContactos));

    }

    @Override
    protected void onResume() {
        super.onResume();
        recargarRecycler();
    }

    public void buscador() {
        listaContactos.clear();
        if (!(buscar.getText().toString().isEmpty())) {
            listaContactos = bdoControlador.buscarContactos(buscar.getText().toString());
            recyclerContactos.setAdapter(new ContactosAdapter(listaContactos));
        } else {
            recargarRecycler();
        }
    }
}