package com.example.alberto.facecook.BaseDeDatos.CategoriaPlatos;

import android.graphics.Bitmap;

public class CategoriaPlato {

    /* Atributos */
    int id;
    String nombre;
    Bitmap foto;

    /**
     * Constructor de clase
     *
     * @param id :int
     * @param nombre :String
     * @param foto :Bitmap
     */
    public CategoriaPlato(int id, String nombre, Bitmap foto) {
        this.id = id;
        this.nombre = nombre;
        this.foto = foto;
    }

    /**
     * Constructor de clase sin el id
     *
     * @param nombre :String
     * @param foto :Bitmap
     */
    public CategoriaPlato(String nombre, Bitmap foto) {
        this.id = id;
        this.nombre = nombre;
        this.foto = foto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Bitmap getFoto() {
        return foto;
    }

    public void setFoto(Bitmap foto) {
        this.foto = foto;
    }
}
