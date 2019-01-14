package com.example.alberto.facecook.Activities;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.alberto.facecook.R;
import com.github.barteksc.pdfviewer.PDFView;

public class VisorPdfActivity extends AppCompatActivity {

    /* Elementos */
    PDFView viewPdf;
    Toolbar toolbar;

    /*Atributos*/
    String tituloActividad;
    String urlPdf;

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
        this.viewPdf.fromAsset(this.urlPdf)
                .enableSwipe(true)
                .enableDoubletap(true)
                .enableAntialiasing(true)
                .load();
    }
}
