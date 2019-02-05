package com.example.alberto.facecook.Dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.example.alberto.facecook.BaseDeDatos.BDInterna.TablaCategoriaPlato;

public class CategoriasPlatosDialog extends DialogFragment {

    String[] arrayCategorias;
    int numPulsado = 0;
    respuestaDialogCategoriasPlatos respuesta;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        /* Se crea el builder */
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        /* Se realiza una consulta para extraer un array de String con todos los nombres
         * de las categorias */
        TablaCategoriaPlato tablaCategoriaPlato = new TablaCategoriaPlato(getContext());
        this.arrayCategorias = tablaCategoriaPlato.verTodasCategoriasNombre();

        /* Se introducen todos los datos que se mostraran en el Dialog y se crean los
         * métodos para recibir la respuesta pulsada */
        builder.setTitle("Filtrar por...");
        builder.setSingleChoiceItems(this.arrayCategorias, 0, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                /* Se actualiza el número pulsado */
                numPulsado = which;
            }
        });
        builder.setPositiveButton("Filtrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                /* En caso de pulsar positivo devuelve la categoria seleccionada */
                respuesta.onRespuestaDialogCategoriasPlatos(arrayCategorias[numPulsado]);
            }
        });
        builder.setNegativeButton("No filtrar", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                /* En caso de pulsar NoFiltrar, envia ese mensaje para desacer el filtrado */
                respuesta.onRespuestaDialogCategoriasPlatos("NoFiltrar");
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
