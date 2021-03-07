package com.example.agenda.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.agenda.MainActivity;
import com.example.agenda.R;
import com.example.agenda.controlador.BDOControlador;
import com.example.agenda.modelo.Contactos;
import com.example.agenda.vista.CompletoActivity;


import java.util.ArrayList;

public class ContactosAdapter extends RecyclerView.Adapter<ContactosAdapter.ContactoViewHolder> implements View.OnClickListener {

    Context context;
    ArrayList<Contactos> listaContactos;

    public ContactosAdapter(ArrayList<Contactos> listaContactos) {
        this.listaContactos = listaContactos;
    }

    @NonNull
    @Override
    public ContactoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contacto_row, null, false);
        return new ContactoViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final ContactoViewHolder holder, int position) {

        holder.asignarDatos(listaContactos.get(position));

        holder.itemView.setOnClickListener(v -> context.startActivity(new Intent(context, CompletoActivity.class).putExtra("id", listaContactos.get(position).getId())));
        holder.itemView.setOnLongClickListener(v -> {

            holder.imagenLlamada.setVisibility(View.VISIBLE);
            holder.imagenBorrar.setVisibility(View.VISIBLE);
            holder.imagenEmail.setVisibility(View.VISIBLE);
            holder.imagenUp.setVisibility(View.VISIBLE);

            return false;
        });

        if (holder.imagenUp != null) {
            holder.imagenUp.setOnClickListener(v -> {
                holder.imagenLlamada.setVisibility(View.GONE);
                holder.imagenBorrar.setVisibility(View.GONE);
                holder.imagenEmail.setVisibility(View.GONE);
                holder.imagenUp.setVisibility(View.GONE);
            });
        }
        if (holder.imagenLlamada != null) {
            holder.imagenLlamada.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + listaContactos.get(position).getTelefono()));
                if (intent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(intent);
                }
            });
        }

        if (holder.imagenEmail != null) {
            if (listaContactos.get(position).getEmail() != null) {
                holder.imagenEmail.setOnClickListener(v -> {
                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                    intent.setData(Uri.parse("mailto:" + listaContactos.get(position).getEmail()));
                    if (intent.resolveActivity(context.getPackageManager()) != null) {
                        context.startActivity(intent);
                    }
                });
            } else {
                holder.imagenEmail.setEnabled(false);
            }
        }

        if (holder.imagenBorrar != null) {
            holder.imagenBorrar.setOnClickListener(v -> {
                long correcto = holder.bdoc.eliminarContacto(listaContactos.get(position).getId());
                if (correcto == 1) {
                    Toast.makeText(context, "Se elimino el contacto", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "No se elimino el contacto", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return listaContactos.size();
    }

    @Override
    public void onClick(View v) {

    }

    public class ContactoViewHolder extends RecyclerView.ViewHolder {

        TextView nombre, direccion, telefono;
        ImageView imagen, imagenBorrar, imagenEmail, imagenLlamada, imagenUp;
        Contactos con;
        BDOControlador bdoc;

        public ContactoViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            bdoc = new BDOControlador(context);

            imagen = itemView.findViewById(R.id.imageViewRecyclerFoto);
            nombre = itemView.findViewById(R.id.textViewNombreContacto);
            telefono = itemView.findViewById(R.id.textViewTelefonoContacto);
            direccion = itemView.findViewById(R.id.textViewDireccionContacto);
            imagenLlamada = itemView.findViewById(R.id.imageButtonLlamar);
            imagenEmail = itemView.findViewById(R.id.imageButtonCorreo);
            imagenBorrar = itemView.findViewById(R.id.imageButtonBorrar);
            imagenUp = itemView.findViewById(R.id.imageButtonUp);
        }

        public void asignarDatos(Contactos contacto) {

            con = contacto;

            Glide.with(itemView.getContext()).load(contacto.getFoto()).into(imagen);

            nombre.setText(contacto.getNombre());

            telefono.setText(contacto.getTelefono());

            direccion.setText(contacto.getDireccion());


        }
    }
}


