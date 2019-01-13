package com.example.alberto.facecook.Dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.example.alberto.facecook.BaseDeDatos.CategoriaPlatos.CategoriaPlato;
import com.example.alberto.facecook.BaseDeDatos.CategoriaPlatos.TablaCategoriaPlato;

import java.util.ArrayList;

public class CategoriasPlatosDialog extends DialogFragment {

    String[] arrayCategorias;
    int numPulsado = 0;
    respuestaDialogCategoriasPlatos respuesta;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        /* Se crea el builder */
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        /* Es necesario extraer de la base de datos todas las categorias e introducirlas
         * en un array de String para poder introducirlo en el Dialog */
        TablaCategoriaPlato tablaCategoriaPlato = new TablaCategoriaPlato(getContext());
        ArrayList<CategoriaPlato> list = tablaCategoriaPlato.verTodasCategorias();
        /* El array se crea con 1 más de tamaño para poder introducir la categoria de nada,
         * en caso de no querer filtrar por nada */
        this.arrayCategorias = new String[tablaCategoriaPlato.totalCategorias() + 1];
        for (int i = 0; i < tablaCategoriaPlato.totalCategorias(); i++){
            this.arrayCategorias[i] = list.get(i).getNombre();
        }
        this.arrayCategorias[tablaCategoriaPlato.totalCategorias()] = "Nada";

        /* Se introducen todos los datos que se mostraran en el Dialog y se crean los
         * métodos para recibir la respuesta pulsada */
        builder.setTitle("Filtrar por...");
        builder.setSingleChoiceItems(this.arrayCategorias, 0, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                numPulsado = which;
            }
        });
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                respuesta.onRespuestaDialogCategoriasPlatos(arrayCategorias[numPulsado]);
            }
        });

        return builder.create();
    }

    public interface respuestaDialogCategoriasPlatos{
        public void onRespuestaDialogCategoriasPlatos(String categoria);
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        respuesta = (respuestaDialogCategoriasPlatos) activity;
    }
}
