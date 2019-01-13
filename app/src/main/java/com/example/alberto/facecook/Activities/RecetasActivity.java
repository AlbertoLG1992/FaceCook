package com.example.alberto.facecook.Activities;

import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.alberto.facecook.Dialog.CategoriasPlatosDialog;
import com.example.alberto.facecook.R;

public class RecetasActivity extends AppCompatActivity implements CategoriasPlatosDialog.respuestaDialogCategoriasPlatos {

    /* Elementos */
    Toolbar toolbar;
    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recetas);

        this.iniciarElementos();
        this.iniciarToolbar();
        //TODO QUE SE CARGUEN LOS PLATOS

        /* Listenner de FloatingActionButton */
        this.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO AGREGAR NUEVA RECETA
            }
        });
    }

    /**
     * Inicia y enlaza los elementos del XML
     */
    private void iniciarElementos(){
        /* XML */
        this.toolbar = (Toolbar)findViewById(R.id.toolbarRecetas);
        this.floatingActionButton = (FloatingActionButton)findViewById(R.id.floatBtnRecetas);
    }

    /**
     * Carga el toolbar en la actividad
     */
    private void iniciarToolbar(){
        this.toolbar.setTitle(R.string.activity_recetas_name);
        this.toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(this.toolbar);
    }

    /**
     * Método que carga en el toolbar un menú
     *
     * @param menu :Menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_recetas, menu);
        return true;
    }

    /**
     * Método que se activa al pulsar sobre un item del menú
     *
     * @param item :MenuItem
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        CategoriasPlatosDialog dialog = new CategoriasPlatosDialog();
        dialog.show(getSupportFragmentManager(), "DialogoCategoriasPlatos");

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRespuestaDialogCategoriasPlatos(String categoria) {
        Toast.makeText(this, categoria, Toast.LENGTH_LONG).show();
        //TODO QUE SE FILTREN LOS PLATOS
    }
}
