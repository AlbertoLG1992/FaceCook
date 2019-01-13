package com.example.alberto.facecook.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.example.alberto.facecook.R;

public class ChoiceActivity extends AppCompatActivity implements View.OnClickListener {

    /* Elementos */
    Toolbar toolbar;
    Button btnVerCocineros, btnVerRecetas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);

        this.iniciarElementos();
        this.iniciarToolbar();
    }

    /**
     * Inicia y enlaza los elementos del XML
     */
    private void iniciarElementos(){
        /* XML */
        this.toolbar = (Toolbar)findViewById(R.id.toolbarChoice);
        this.btnVerCocineros = (Button)findViewById(R.id.btnVerCocineros);
        this.btnVerRecetas = (Button)findViewById(R.id.btnVerRecetas);

        /* Clicklable */
        this.btnVerCocineros.setOnClickListener(this);
        this.btnVerRecetas.setOnClickListener(this);
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
        switch (v.getId()){
            case R.id.btnVerRecetas:{
                /* Abre la actividad de RecetasActivity */
                Intent intent = new Intent(getApplicationContext(), RecetasActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.btnVerCocineros:{
                //TODO ACTIVIDAD PARA VER LOS COCINEROS CERCA
                break;
            }
        }
    }
}
