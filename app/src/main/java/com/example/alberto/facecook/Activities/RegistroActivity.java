package com.example.alberto.facecook.Activities;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;

import com.example.alberto.facecook.R;

public class RegistroActivity extends AppCompatActivity implements View.OnClickListener,
        PopupMenu.OnMenuItemClickListener {

    /* Elementos */
    ImageView imgUser;
    Toolbar toolbar;
    TextInputLayout txilNick,
                    txilPass,
                    txilNombre,
                    txilApellidos,
                    txilFecha,
                    txilTlf,
                    txilComentarios;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        this.iniciarElementos();
        this.iniciarToolbar();
    }

    private void iniciarElementos(){
        /* XML */
        this.imgUser = (ImageView)findViewById(R.id.imgUser);
        this.toolbar = (Toolbar)findViewById(R.id.toolbarRegistro);
        this.txilNick = (TextInputLayout)findViewById(R.id.txilNick);
        this.txilPass = (TextInputLayout)findViewById(R.id.txilPass);
        this.txilNombre = (TextInputLayout)findViewById(R.id.txilNombre);
        this.txilApellidos = (TextInputLayout)findViewById(R.id.txilApellidos);
        this.txilFecha = (TextInputLayout)findViewById(R.id.txilFecha);
        this.txilTlf = (TextInputLayout)findViewById(R.id.txilTlf);
        this.txilComentarios = (TextInputLayout)findViewById(R.id.txilComentarios);

        /* Clickable */
        this.imgUser.setOnClickListener(this);
    }

    private void iniciarToolbar() {
        this.toolbar.setTitle("Registro");
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
        getMenuInflater().inflate(R.menu.menu_add_receta, menu);
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
        switch (item.getItemId()){
            case R.id.itemGuardar:{
                break;
            }
            case R.id.itemDescartar:{
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgUser:{
                PopupMenu popup = new PopupMenu(getApplicationContext(), v);
                popup.setOnMenuItemClickListener(this);
                popup.inflate(R.menu.popup_menu_foto);
                popup.show();
                break;
            }
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }
}
