package com.example.alberto.facecook.Activities;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.example.alberto.facecook.R;
import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

public class VisorPdfActivity extends AppCompatActivity {

    /* Elementos */
    private PDFView viewPdf;
    private Toolbar toolbar;

    /*Atributos*/
    private String tituloActividad;
    private String urlPdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visor_pdf);

        /* Se extrae del intent el titulo y la url del pdf */
        this.tituloActividad = getIntent().getStringExtra("titulo");
        this.urlPdf = getIntent().getStringExtra("url");

        this.iniciarElementos();
        this.iniciarToolbar();
        this.cargarPdf();
    }

    /**
     * Inicia y enlaza los elementos del XML
     */
    private void iniciarElementos(){
        /* XML */
        this.toolbar = (Toolbar)findViewById(R.id.toolbarVisorPdf);
        this.viewPdf = (PDFView)findViewById(R.id.visorPdf);
    }

    /**
     * Carga el toolbar en la actividad
     */
    private void iniciarToolbar(){
        this.toolbar.setTitle(this.tituloActividad);
        this.toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(this.toolbar);
    }

    /**
     * Carga el pdf en la actividad
     */
    private void cargarPdf(){
        /* A continuación es necesario diferenciar entre platos que han sido guardados por defecto
         * en la carpeta Assets o los que se han generado posteriormente en la app.
         * Es necesario esta diferenciación porque a uno se accede desde file y al otro desde
         * Assets, la forma de saber cual es el de por defecto es porque la ruta empieza por "PDF"
         * en mayuscula, y el otro no*/
        if (urlPdf.charAt(0) =='P'){
            this.viewPdf.fromAsset(this.urlPdf) //ASSETS
                    .enableSwipe(true)
                    .enableDoubletap(true)
                    .enableAntialiasing(true)
                    .load();
        }else {
            this.viewPdf.fromFile(new File(this.urlPdf)) //FROM FILE
                    .enableSwipe(true)
                    .enableDoubletap(true)
                    .enableAntialiasing(true)
                    .load();
        }

    }
}
