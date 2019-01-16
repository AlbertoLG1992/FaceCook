package com.example.alberto.facecook.Dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.util.ArrayList;

public class EliminarIngredienteDialog extends DialogFragment {

    respuestaDialogEliminarIngredienteDialog respuesta;
    String[] arrayIngredientes;
    int numPulsado = 0;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        /* Se crea el builder */
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Selecciona el ingrediente");
        builder.setSingleChoiceItems(this.arrayIngredientes, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                numPulsado = which;
            }
        });
        builder.setPositiveButton("Borrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                respuesta.onrespuestaDialogEliminarIngredienteDialog(numPulsado);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //No tiene que hacer nada
            }
        });

        return builder.create();
    }

    public interface respuestaDialogEliminarIngredienteDialog{
        public void onrespuestaDialogEliminarIngredienteDialog(int posicion);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        respuesta = (respuestaDialogEliminarIngredienteDialog) activity;
    }

    /**
     * Introduce en el Dialog todos los ingredientes
     *
     * @param list :ArrayList<String>
     */
    public void introducirIngredientes(ArrayList<String> list){
        arrayIngredientes = new String[list.size()];
        for (int i = 0; i < list.size(); i++){
            arrayIngredientes[i] = list.get(i);
        }
    }
}
