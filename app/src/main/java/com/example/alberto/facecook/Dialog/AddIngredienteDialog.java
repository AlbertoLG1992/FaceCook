package com.example.alberto.facecook.Dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.alberto.facecook.R;

public class AddIngredienteDialog extends DialogFragment {

    respuestaDialogAddIngrediente respuesta;
    EditText editText;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        /* Se crea el builder */
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        /* Se infla el View y se crea el EditText que va dentro */
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_ingrediente, null);
        this.editText = (EditText)view.findViewById(R.id.edtIngrediente);

        builder.setTitle("Añade un ingrediente");
        builder.setView(view);
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                respuesta.onRespuestaDialogAddIngrediente(editText.getText().toString());
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //No es necesario que haga nada
            }
        });

        return builder.create();
    }

    public interface respuestaDialogAddIngrediente{
        public void onRespuestaDialogAddIngrediente(String ingrediente);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        respuesta = (respuestaDialogAddIngrediente) activity;
    }
}
