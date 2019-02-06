package com.example.alberto.facecook.Dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.widget.Toast;

public class LoginProgressDialog extends AsyncTask<String, Integer, String> {

    private ProgressDialog progressDialog;

    /**
     * Constructor de clase
     *
     * @param context :Context
     */
    public LoginProgressDialog(Context context, String titulo, String mensaje){
        this.progressDialog = new ProgressDialog(context);
        this.progressDialog.setTitle(titulo);
        this.progressDialog.setMessage(mensaje);
        this.progressDialog.setIndeterminate(false);
        this.progressDialog.setMax(10);
        this.progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }

    @Override
    protected void onPreExecute(){
        super.onPreExecute();
        this.progressDialog.show();
    }

    @Override
    protected String doInBackground(String... strings) {
        return null;
    }

    public void parar(){
        this.progressDialog.dismiss();
    }
}
