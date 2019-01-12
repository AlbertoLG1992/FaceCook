package com.example.alberto.facecook;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    /* Elementos */
    Toolbar toolbar;
    Button btnEntrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.iniciarElementos();
        this.iniciarToolbar();
    }

    /**
     * Inicia y enlaza los elementos del XML
     */
    private void iniciarElementos(){
        /* XML */
        this.toolbar = (Toolbar)findViewById(R.id.toolbar);
        this.btnEntrar = (Button)findViewById(R.id.btnEntrar);

        /* Clicklable */
        this.btnEntrar.setOnClickListener(this);
    }

    /**
     * Carga el toolbar en la actividad
     */
    private void iniciarToolbar(){
        this.toolbar.setTitle(R.string.app_name);
        this.toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(this.toolbar);
    }

    @Override
    public void onClick(View v) {
        //TODO ACTIVITY PARA SELECCIONAR SI QUEREMOS VER LOS COCINEROS O VER LAS RECETAS
    }
}
