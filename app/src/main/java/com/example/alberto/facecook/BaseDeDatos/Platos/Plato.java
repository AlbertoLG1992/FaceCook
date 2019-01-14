package com.example.alberto.facecook.BaseDeDatos.Platos;

import android.graphics.Bitmap;

import com.example.alberto.facecook.BaseDeDatos.CategoriaPlatos.CategoriaPlato;

public class Plato {

    /* Atributo */
    int id;
    String nombre,
            urlPdf;
    CategoriaPlato categoriaPlato;

    /**
     * Constructor de clase
     *
     * @param id :int
     * @param nombre :String
     * @param urlPdf :String
     * @param nombreCategoria :String
     * @param fotoCategoria :Bitmap
     */
    public Plato(int id, String nombre, String urlPdf, String nombreCategoria, Bitmap fotoCategoria) {
        this.id = id;
        this.nombre = nombre;
        this.urlPdf = urlPdf;
        this.categoriaPlato = new CategoriaPlato(nombreCategoria, fotoCategoria);
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

    public String getUrlPdf() {
        return urlPdf;
    }

    public void setUrlPdf(String urlPdf) {
        this.urlPdf = urlPdf;
    }

    public CategoriaPlato getCategoriaPlato() {
        return categoriaPlato;
    }

    public void setCategoriaPlato(CategoriaPlato categoriaPlato) {
        this.categoriaPlato = categoriaPlato;
    }
}
