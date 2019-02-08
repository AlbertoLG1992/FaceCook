package com.example.alberto.facecook.Clases;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FotoUsuario {

    /* Atributos */
    private File foto;
    private boolean fotoCargada;

    /**
     * Constructor
     */
    public FotoUsuario(){
        this.fotoCargada = false;
    }

    /**
     * Método para saber si la foto esta cargada en la clase
     *
     * @return boolean
     */
    public boolean estaCargada(){
        return this.fotoCargada;
    }

    /**
     * Carga una foto que se ha optenido desde la cámara
     *
     * @param path :String
     */
    public void cargarDesdeCamara(String path){
        this.borrarFotoAnterior();
        this.foto = new File(path);
        this.fotoCargada = true;
    }

    /**
     * Carga una foto que se ha optenido desde la galería
     *
     * @param uri :Uri
     * @param context :Contex
     */
    public void cargarDesdeGaleria(Uri uri, Context context){
        this.borrarFotoAnterior();
        /* Se cre el fichero contenedor */
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        this.foto = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), imageFileName + ".jpg");

        /* Se copia en el fichero contenedor los datos del fichero de origen */
        try {
            Bitmap bitmap = bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);

            OutputStream os = new FileOutputStream(this.foto);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.fotoCargada = true;
    }

    /**
     * Comprueba que la foto no este cargada anteriormente, en caso de que exista la borra
     * para crear otra y que no se acumulen
     */
    private void borrarFotoAnterior(){
        if (this.estaCargada()){
            this.foto.delete();
        }
    }

    /**
     * Comprueba que la foto existe en memoria
     *
     * @return boolean
     */
    public boolean existeFoto(){
        return this.foto.exists();
    }

    /**
     * Devuelve la uri de la foto
     *
     * @return Uri
     */
    public Uri getUriFoto(){
        return Uri.fromFile(this.foto);
    }
}
