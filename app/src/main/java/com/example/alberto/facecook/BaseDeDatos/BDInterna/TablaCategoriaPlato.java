package com.example.alberto.facecook.BaseDeDatos.BDInterna;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;

import com.example.alberto.facecook.Clases.CategoriaPlato;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

public class TablaCategoriaPlato {

    private SQLiteAssetHelper openHelper;
    private SQLiteDatabase database;
    private static TablaCategoriaPlato instance;

    /**
     * Constructor de clase
     *
     * @param context :Context
     */
    public TablaCategoriaPlato(Context context){
        this.openHelper = new DatabaseOpenHelper(context);
    }

    /**
     * Abre la conexión a la base de datos en modo escritura
     */
    private void openDatabaseRead(){
        this.database = this.openHelper.getReadableDatabase();
    }

    /**
     * Abre la conexión a la base de datos en modo lectura
     */
    private void openDatabaseWrite(){
        this.database = this.openHelper.getWritableDatabase();
    }

    /**
     * Cierra la base de datos
     */
    private void closeDatabase(){
        if (this.database != null){
            this.database.close();
        }
    }

    /**
     * Extrae todas las categorias de la base de datos
     *
     * @return :ArrayList<CategoriaPlato>
     */
    public ArrayList<CategoriaPlato> verTodasCategorias(){
        ArrayList<CategoriaPlato> listaCategorias = new ArrayList<CategoriaPlato>();
        Cursor cursor;

        /* Se abre la base de datos y se extraen los datos haciendo la consulta */
        this.openDatabaseRead();
        cursor = this.database.rawQuery("SELECT * FROM categoria_plato;", null);

        /* Se recorre el cursor y se rellena el arrayList */
        if (cursor.moveToFirst()){
            do {
                listaCategorias.add(new CategoriaPlato(cursor.getInt(0),
                        cursor.getString(1),
                        BitmapFactory.decodeStream(new ByteArrayInputStream(cursor.getBlob(2)))));
            }while (cursor.moveToNext());
        }

        /* Se cierran las conexiones */
        cursor.close();
        this.closeDatabase();

        return listaCategorias;
    }

    /**
     * Extrae todos los nombres de las categorias de la base de datos
     *
     * @return :String
     */
    public String[] verTodasCategoriasNombre(){
        int posicion = 0; //Será el que recorra el array
        String listaCategorias[] = new String[this.totalCategorias()];
        Cursor cursor;

        /* Se abre la base de datos y se extraen los datos haciendo la consulta */
        this.openDatabaseRead();
        cursor = this.database.rawQuery("SELECT * FROM categoria_plato;", null);

        /* Se recorre el cursor y se rellena el array con el nombre de las categorias */
        if (cursor.moveToFirst()){
            do {
                listaCategorias[posicion] = cursor.getString(1);
                posicion++;
            }while (cursor.moveToNext());
        }

        /* Se cierran las conexiones */
        cursor.close();
        this.closeDatabase();

        return listaCategorias;
    }

    /**
     * Devuelve el total de todas las categorias existentes
     *
     * @return :int
     */
    public int totalCategorias(){
        int total = 0;
        Cursor cursor;

        /* Se abre la base de datos y se extraen los datos haciendo la consulta */
        this.openDatabaseRead();
        cursor = this.database.rawQuery("SELECT * FROM categoria_plato;", null);

        /* Se recorre el cursor y se suma 1 por cada elemento */
        if (cursor.moveToFirst()){
            do {
                total++;
            }while (cursor.moveToNext());
        }

        /* Se cierran las conexiones */
        cursor.close();
        this.closeDatabase();

        return total;
    }

    public CategoriaPlato categoriaPorId(int id){
        CategoriaPlato categoria = new CategoriaPlato();
        Cursor cursor;

        /* Se abre la base de datos y se extraen los datos haciendo la consulta */
        this.openDatabaseRead();
        cursor = this.database.rawQuery("SELECT * FROM categoria_plato " +
                "WHERE categoria_plato.id = " + id +";", null);

        /* Se recorre el cursor y se rellena el array con el nombre de las categorias */
        if (cursor.moveToFirst()){
            do {
                categoria = new CategoriaPlato(cursor.getInt(0), cursor.getString(1),
                        BitmapFactory.decodeStream(new ByteArrayInputStream(cursor.getBlob(2))));
            }while (cursor.moveToNext());
        }

        /* Se cierran las conexiones */
        cursor.close();
        this.closeDatabase();

        return categoria;
    }
}
