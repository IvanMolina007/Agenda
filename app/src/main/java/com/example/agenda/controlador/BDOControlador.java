package com.example.agenda.controlador;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.agenda.modelo.Contactos;

import java.util.ArrayList;

public class BDOControlador extends SQLiteOpenHelper {

    public static String name = "BDOAgenda.db";
    public static final String NOMBRE_TABLA = "Contactos";
    public static int version_baseDatos = 9;

    private static final String INS = "CREATE TABLE Contactos (id INT PRIMARY KEY, nombre VARCHAR(100), " +
            "telefono VARCHAR(20), direccion VARCHAR(100), email VARCHAR(100), web VARCHAR(100), " +
            "foto VARCHAR(100))";

    public BDOControlador( Context context) {
        super(context, name, null, version_baseDatos);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(INS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + NOMBRE_TABLA);
        onCreate(db);
    }

    public long anadirContacto(Contactos contactos) {
        SQLiteDatabase db = getWritableDatabase();
        long id = -1;

        if (db != null) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("id", contactos.getId());
            contentValues.put("nombre", contactos.getNombre());
            contentValues.put("telefono", contactos.getTelefono());
            contentValues.put("direccion", contactos.getDireccion());
            contentValues.put("email", contactos.getEmail());
            contentValues.put("web", contactos.getWeb());
            contentValues.put("foto", contactos.getFoto());
            id = db.insert("Contactos", null, contentValues);
        }
        db.close();
        return id;
    }

    public long eliminarContacto(int id) {
        long nLineas = 0;

        SQLiteDatabase db = getWritableDatabase();

        if(db != null){
            nLineas = db.delete("Contactos","id="+ id,null);
        }
        db.close();
        return nLineas;
    }

    public long editarContactos(Contactos contactos) {
        SQLiteDatabase db = getWritableDatabase();
        long nLineas = 0;

        if (db != null) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("id", contactos.getId());
            contentValues.put("nombre", contactos.getNombre());
            contentValues.put("telefono", contactos.getTelefono());
            contentValues.put("direccion", contactos.getDireccion());
            contentValues.put("email", contactos.getEmail());
            contentValues.put("web", contactos.getWeb());
            contentValues.put("foto", contactos.getFoto());
            nLineas = db.update("Contactos", contentValues, "id="+contactos.getId(), null);
        }
        db.close();
        return nLineas;
    }

    public ArrayList<Contactos> cargarContactos() {
        SQLiteDatabase db = getWritableDatabase();
        ArrayList<Contactos> listaContactos = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM Contactos ORDER BY nombre ASC, id ASC", null);
        while (cursor.moveToNext()) {
            Contactos contacto = new Contactos();
            contacto.setId(cursor.getInt(0));
            contacto.setNombre(cursor.getString(1));
            contacto.setTelefono(cursor.getString(2));
            contacto.setDireccion(cursor.getString(3));
            contacto.setEmail(cursor.getString(4));
            contacto.setWeb(cursor.getString(5));
            contacto.setFoto(cursor.getString(6));

            listaContactos.add(contacto);
        }

        return listaContactos;
    }

    public Contactos getContacto(int id) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Contactos WHERE id="+ id, null);
        cursor.moveToFirst();
        Contactos contacto = new Contactos();
        contacto.setId(cursor.getInt(0));
        contacto.setNombre(cursor.getString(1));
        contacto.setTelefono(cursor.getString(2));
        contacto.setDireccion(cursor.getString(3));
        contacto.setEmail(cursor.getString(4));
        contacto.setWeb(cursor.getString(5));
        contacto.setFoto(cursor.getString(6));

        return contacto;
    }

    public int cargarUltimoId(){
        int newid;
        SQLiteDatabase db = getReadableDatabase();
        Cursor newcursor = db.rawQuery("SELECT MAX(id) FROM Contactos ORDER BY id ASC",null);
        newcursor.moveToFirst();
        newid = newcursor.getInt(0);

        return newid+1;
    }

    public ArrayList<Contactos> buscarContactos(String nombre) {
        SQLiteDatabase db = getWritableDatabase();
        nombre = nombre.toUpperCase();
        ArrayList<Contactos> listaContactos = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM Contactos WHERE UPPER(nombre) like '%"+ nombre + "%' ORDER BY nombre ASC, id ASC", null);
        while (cursor.moveToNext()) {
            Contactos contacto = new Contactos();
            contacto.setId(cursor.getInt(0));
            contacto.setNombre(cursor.getString(1));
            contacto.setTelefono(cursor.getString(2));
            contacto.setDireccion(cursor.getString(3));
            contacto.setEmail(cursor.getString(4));
            contacto.setWeb(cursor.getString(5));
            contacto.setFoto(cursor.getString(6));

            listaContactos.add(contacto);
        }

        return listaContactos;
    }
}
