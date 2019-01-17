package com.example.alberto.facecook.BaseDeDatos.Platos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;

import com.example.alberto.facecook.BaseDeDatos.DatabaseOpenHelper;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

public class TablaPlato {

    private SQLiteAssetHelper openHelper;
    private SQLiteDatabase database;
    private static TablaPlato instance;

    /**
     * Constructor de clase
     *
     * @param context :Context
     */
    public TablaPlato(Context context){
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
     * Extrae todos los platos de la base de datos
     *
     * @return :ArrayList<Plato>
     */
    public ArrayList<Plato> verTodosPlatos(){
        ArrayList<Plato> listaPlatos = new ArrayList<Plato>();
        Cursor cursor;

        /* Se abre la base de datos y se extraen los datos haciendo la consulta */
        this.openDatabaseRead();
        cursor = this.database.rawQuery("SELECT plato.id, plato.nombre, " +
                        "plato.url_pdf, categoria_plato.nombre, categoria_plato.foto " +
                        "FROM plato " +
                        "INNER JOIN categoria_plato on categoria_plato.id = plato.id_categoria_plato " +
                        "ORDER BY plato.nombre ASC;",
                        null);

        /* Se recorre el cursor y se rellena el arrayList */
        if (cursor.moveToFirst()){
            do {
                listaPlatos.add(new Plato(cursor.getInt(0), cursor.getString(1),
                        cursor.getString(2), cursor.getString(3),
                        BitmapFactory.decodeStream(new ByteArrayInputStream(cursor.getBlob(4)))));
            }while (cursor.moveToNext());
        }

        /* Se cierran las conexiones */
        cursor.close();
        this.closeDatabase();

        return listaPlatos;
    }

    /**
     * Extrae todos los platos de una categoria especifica de la base de datos
     *
     * @return :ArrayList<Plato>
     */
    public ArrayList<Plato> verTodosPlatosFiltrados(String categoria){
        ArrayList<Plato> listaPlatos = new ArrayList<Plato>();
        Cursor cursor;

        /* Se abre la base de datos y se extraen los datos haciendo la consulta */
        this.openDatabaseRead();
        cursor = this.database.rawQuery("SELECT plato.id, plato.nombre, " +
                        "plato.url_pdf, categoria_plato.nombre, categoria_plato.foto " +
                        "FROM plato " +
                        "INNER JOIN categoria_plato on categoria_plato.id = plato.id_categoria_plato " +
                        "WHERE categoria_plato.nombre = '" + categoria + "'" +
                        "ORDER BY plato.nombre ASC;",
                null);

        /* Se recorre el cursor y se rellena el arrayList */
        if (cursor.moveToFirst()){
            do {
                listaPlatos.add(new Plato(cursor.getInt(0), cursor.getString(1),
                        cursor.getString(2), cursor.getString(3),
                        BitmapFactory.decodeStream(new ByteArrayInputStream(cursor.getBlob(4)))));
            }while (cursor.moveToNext());
        }

        /* Se cierran las conexiones */
        cursor.close();
        this.closeDatabase();

        return listaPlatos;
    }

    /**
     * Añade en la base de datos un plato nuevo
     *
     * @param nombre :String
     * @param url :String
     * @param categoria :int
     */
    public void addPlato(String nombre, String url, int categoria){
        /* Se abre la base de datos en modo escritura */
        this.openDatabaseWrite();

        /* Se insertan los valores que se grabarán */
        if (this.database != null){
            ContentValues values = new ContentValues();
            values.put("nombre", nombre);
            values.put("url_pdf", url);
            values.put("id_categoria_plato", categoria);
            this.database.insert("plato",null,  values);
        }

        /* Se cierra la base de datos */
        this.closeDatabase();
    }
}
