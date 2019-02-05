package com.example.alberto.facecook.Activities;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.alberto.facecook.BaseDeDatos.BDInterna.TablaCategoriaPlato;
import com.example.alberto.facecook.BaseDeDatos.BDInterna.TablaPlato;
import com.example.alberto.facecook.Dialog.AddIngredienteDialog;
import com.example.alberto.facecook.Dialog.EliminarIngredienteDialog;
import com.example.alberto.facecook.PDF.GenerarPdfReceta;
import com.example.alberto.facecook.R;

import java.util.ArrayList;

public class AgregarRecetaActivity extends AppCompatActivity implements View.OnClickListener,
    AddIngredienteDialog.respuestaDialogAddIngrediente, EliminarIngredienteDialog.respuestaDialogEliminarIngredienteDialog{

    /* Elementos */
    private Toolbar toolbar;
    private EditText edtNombre;
    private Spinner spinnerCategorias;
    private ImageButton imgBtnAdd,
                        imgBtnDelete;
    private ListView listViewIngredientes;
    private EditText edtDescripReceta;

    /* Atributos */
    private ArrayList<String> ingredientes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_receta);

        this.iniciarElementos();
        this.iniciarToolbar();
        this.cargarCategoriasSpinner();

        this.ingredientes = new ArrayList<String>();
    }

    /**
     * Inicia y enlaza los elementos del XML
     */
    private void iniciarElementos(){
        /* XML */
        this.toolbar = (Toolbar)findViewById(R.id.toolbarAgregarRecetas);
        this.edtNombre = (EditText)findViewById(R.id.edtTituloReceta);
        this.spinnerCategorias = (Spinner)findViewById(R.id.spinnerReceta);
        this.imgBtnAdd = (ImageButton)findViewById(R.id.imgBtnAdd);
        this.imgBtnDelete = (ImageButton)findViewById(R.id.imgBtnDelete);
        this.edtDescripReceta = (EditText)findViewById(R.id.edtDescripReceta);
        this.listViewIngredientes = (ListView)findViewById(R.id.listViewAddIngredientes);

        /* Clickables */
        this.imgBtnAdd.setOnClickListener(this);
        this.imgBtnDelete.setOnClickListener(this);
    }

    /**
     * Carga el toolbar en la actividad
     */
    private void iniciarToolbar(){
        this.toolbar.setTitle(R.string.activity_agregar_receta_name);
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
                if (comprobarDatosRellenos()){
                    this.guardarReceta();
                    Toast.makeText(this, "La receta ha sido guardada correctamente",
                            Toast.LENGTH_LONG).show();
                    setResult(RESULT_OK);
                    finish();
                }
                break;
            }
            case R.id.itemDescartar:{
                setResult(RESULT_CANCELED);
                finish();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Guarda la receta en la base de datos
     */
    private void guardarReceta(){
        TablaPlato tablaPlato = new TablaPlato(this);

        tablaPlato.addPlato(this.edtNombre.getText().toString(), this.generarPdf(),
                spinnerCategorias.getSelectedItemPosition() + 1);
    }

    /**
     * Genera el pdf de la receta de cocina
     *
     * @return :String con el paht del pdf
     */
    private String generarPdf(){
        GenerarPdfReceta pdf = new GenerarPdfReceta(getApplicationContext());
        pdf.openDocument();
        pdf.addTitulo(this.edtNombre.getText().toString());
        pdf.addIngredientes(this.ingredientes);
        pdf.addDescripcion(this.edtDescripReceta.getText().toString());
        pdf.closeDocument();
        return pdf.verPathPdf();
    }

    /**
     * Carga en el Spinner todas las categorias
     */
    private void cargarCategoriasSpinner(){
        TablaCategoriaPlato tablaCategoriaPlato = new TablaCategoriaPlato(getApplicationContext());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.spinner_item_add_receta, tablaCategoriaPlato.verTodasCategoriasNombre());
        this.spinnerCategorias.setAdapter(adapter);
        this.spinnerCategorias.setSelection(-1);
    }

    /**
     * Comprueba que todos los campos necesarios estan rellenos
     *
     * @return true en caso afirmativo y false en caso contrario
     */
    private boolean comprobarDatosRellenos(){
        if (!this.edtNombre.getText().toString().isEmpty()){
            if (!this.ingredientes.isEmpty()){
                if (!this.edtDescripReceta.getText().toString().isEmpty()){
                    return true;
                }else {
                    Snackbar.make(getCurrentFocus(), "No hay Descripción", Snackbar.LENGTH_LONG)
                            .show();
                }
            }else {
                Snackbar.make(getCurrentFocus(), "No hay Ingredientes", Snackbar.LENGTH_LONG)
                        .show();
            }
        }else {
            Snackbar.make(getCurrentFocus(), "No has puesto el nombre", Snackbar.LENGTH_LONG)
                    .show();
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgBtnAdd:{
                AddIngredienteDialog dialog = new AddIngredienteDialog();
                dialog.show(getSupportFragmentManager(), "Dialog Add Ingrediente");
                break;
            }
            case R.id.imgBtnDelete:{
                /* Antes de abrir el dialog comprueba que la lista no este vacía, dado que si
                 * estuviese vacia no se podría eliminar nada */
                if (!this.ingredientes.isEmpty()) {
                    EliminarIngredienteDialog dialog = new EliminarIngredienteDialog();
                    dialog.introducirIngredientes(this.ingredientes);
                    dialog.show(getSupportFragmentManager(), "Dialog Eliminar Ingrediente");
                }else {
                    Snackbar.make(v, "La lista no se puede borrar" +
                            " porque esta vacía", Snackbar.LENGTH_LONG).show();
                }
                break;
            }
        }
    }

    /**
     * Método que se ejecuta al volver del Dialog AddIngrediente
     *
     * @param ingrediente :String
     */
    @Override
    public void onRespuestaDialogAddIngrediente(String ingrediente) {
        this.ingredientes.add(ingrediente);
        cargarAdaptadorIngredientes();
    }

    /**
     * Carga el adaptador en el listView de Ingredientes
     */
    private void cargarAdaptadorIngredientes(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, this.ingredientes);
        this.listViewIngredientes.setAdapter(adapter);
    }

    /**
     * Método que se ejecuta al volver del dialog EliminarIngrediente
     *
     * @param posicion :int
     */
    @Override
    public void onrespuestaDialogEliminarIngredienteDialog(int posicion) {
        this.ingredientes.remove(posicion);
        this.cargarAdaptadorIngredientes();
    }
}
