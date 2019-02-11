package com.example.alberto.facecook.Dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.alberto.facecook.R;

import java.util.Calendar;

public class FechaDatePickerDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);
        return dialog;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        TextInputEditText txieFecha = (TextInputEditText)getActivity().findViewById(R.id.txieFecha);
        txieFecha.setText(transDatos(year) + "-" + transDatos(month+1) + "-" + transDatos(dayOfMonth));
    }

    /**
     * Añade a los meses y los días menos a 10 un cero delante, además de transformarlos
     * a String
     * @param num :int
     * @return String
     */
    private String transDatos(int num){
        return (num <= 9) ? ("0" + num) : String.valueOf(num);
    }
}
