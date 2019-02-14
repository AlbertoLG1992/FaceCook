package com.example.alberto.facecook.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.alberto.facecook.BaseDeDatos.BDExterna.ControlUsuariosRequest;
import com.example.alberto.facecook.Clases.FotoUsuario;
import com.example.alberto.facecook.Clases.Usuario;
import com.example.alberto.facecook.Dialog.FechaDatePickerDialog;
import com.example.alberto.facecook.Dialog.LoginProgressDialog;
import com.example.alberto.facecook.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegistroActivity extends AppCompatActivity implements View.OnClickListener,
        PopupMenu.OnMenuItemClickListener {

    /* Elementos */
    private CircleImageView imgUser;
    private Toolbar toolbar;
    private TextInputLayout txilNick,
                            txilPass,
                            txilNombre,
                            txilApellidos,
                            txilFecha,
                            txilTlf,
                            txilComentarios,
                            txilCorreo;
    private TextInputEditText txieFecha;

    /* Result Codes */
    private final int RESULT_CAMERA = 1;
    private final int RESULT_GALERY = 2;

    /* Atributos */
    private String mCurrentPhotoPath;
    private FotoUsuario fotoUsuario;
    private LoginProgressDialog progress;
    private double locatX, locatY;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        this.iniciarElementos();
        this.iniciarToolbar();

        this.fotoUsuario = new FotoUsuario();
        Toast.makeText(this, "Para introducir la foto de usuario, " +
                "toque el icono de usuario", Toast.LENGTH_LONG).show();
    }

    private void iniciarElementos(){
        /* XML */
        this.imgUser = (CircleImageView)findViewById(R.id.imgUser);
        this.toolbar = (Toolbar)findViewById(R.id.toolbarRegistro);
        this.txilNick = (TextInputLayout)findViewById(R.id.txilNick);
        this.txilPass = (TextInputLayout)findViewById(R.id.txilPass);
        this.txilNombre = (TextInputLayout)findViewById(R.id.txilNombre);
        this.txilApellidos = (TextInputLayout)findViewById(R.id.txilApellidos);
        this.txilFecha = (TextInputLayout)findViewById(R.id.txilFecha);
        this.txilTlf = (TextInputLayout)findViewById(R.id.txilTlf);
        this.txilComentarios = (TextInputLayout)findViewById(R.id.txilComentarios);
        this.txilCorreo = (TextInputLayout)findViewById(R.id.txilCorreo);
        this.txieFecha = (TextInputEditText)findViewById(R.id.txieFecha);

        /* Clickable */
        this.imgUser.setOnClickListener(this);
        this.txieFecha.setOnClickListener(this);
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
                if (this.comprobarCamposRellenos()){
                    if (comprobarCamposCorrectos()) {
                        if (getCoordenadas()) {
                            this.subirUsuario();
                        }
                    }
                }
                break;
            }
            case R.id.itemDescartar:{
                this.vaciarCampos();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Método para obtener latitud y longitud
     * @return
     */
    private boolean getCoordenadas(){
        if (checkPermisionLocation()){
            LocationManager locManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
            if (locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                //TODO EXTRAER COORDENADAS
                return true;
            }else {
                this.mostrarSnackbar("Es necesario activar el GPS para poder registrarse");
                return false;
            }
        }else {
            return false;
        }
    }

    /**
     * Sube un usuario a la base de datos externa
     */
    private void subirUsuario(){
        progress = new LoginProgressDialog(this, "Creando usuario",
                "Conectando con el servidor...");
        progress.execute();

        Usuario usuario = new Usuario(
                this.fotoUsuario,
                this.txilNick.getEditText().getText().toString(),
                this.txilPass.getEditText().getText().toString(),
                this.txilNombre.getEditText().getText().toString(),
                this.txilApellidos.getEditText().getText().toString(),
                this.txieFecha.getText().toString(),
                this.txilCorreo.getEditText().getText().toString(),
                this.txilTlf.getEditText().getText().toString(),
                this.txilComentarios.getEditText().getText().toString());

        Response.Listener<String> respuesta = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonRespuesta = new JSONObject(response);
                    String mensaje = jsonRespuesta.getString("mensaje");
                    switch (mensaje){
                        case "El usuario se ha guardado correctamente":{
                            Toast.makeText(getApplicationContext(), "El usuario se ha creado " +
                                    "correctamente", Toast.LENGTH_LONG).show();
                            fotoUsuario.borrarFotoAnterior(); //Para que no se acumulen en memoria
                            finish(); //Se cierra la Actividad
                            break;
                        }
                        case "El usuario ya existe en la Base de Datos":{
                            mostrarSnackbar("Error, el Nick ya está en uso");
                            txilNick.setError("Intenta con otro nick");
                            break;
                        }
                        default:{
                            mostrarSnackbar("Algo raro le pasa al servidor con el usuario");
                            break;
                        }
                    }
                }catch (JSONException e){
                    e.getMessage();
                }
                progress.parar(); //Para parar el progress dialog
            }
        };
        Response.ErrorListener errorRespuesta = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Si existe un error
                mostrarSnackbar("Error al conectar con el servidor");
                progress.parar(); //Para parar el progress dialog
            }
        };
        ControlUsuariosRequest request = new ControlUsuariosRequest(usuario, respuesta, errorRespuesta);
        RequestQueue cola = Volley.newRequestQueue(this);
        cola.add(request);
    }

    private boolean comprobarCamposCorrectos(){
        //TODO COMPROBAR CAMPOS CORRECTOS
        return true;
    }

    /**
     * Comprueba que todos los campos para la creación de usuario estan rellenos
     *
     * @return True en caso de que todos los campos esten rellenos y false en caso contrario
     */
    private boolean comprobarCamposRellenos(){
        String mensajeError = "ERROR, compruebe que todos los campos estan rellenos";
        boolean camposRellenos = true;

        if (!this.fotoUsuario.estaCargada()){
            camposRellenos = false;
            mensajeError = "Es necesario insertar una foto de usuario.\n\t" +
                    "Pulsa en el icono principal para ello.";
        }

        if (this.txilNick.getEditText().getText().toString().isEmpty()){
            camposRellenos = false;
            this.txilNick.setError("El nick no puede estar en blanco");
        }else {
            this.txilNick.setErrorEnabled(false);
        }

        if (this.txilPass.getEditText().getText().toString().isEmpty()){
            camposRellenos = false;
            this.txilPass.setError("La contraseña no puede estar en blanco");
        }else {
            this.txilPass.setErrorEnabled(false);
        }

        if (this.txilNombre.getEditText().getText().toString().isEmpty()){
            camposRellenos = false;
            this.txilNombre.setError("El nombre no puede estar en blanco");
        }else {
            this.txilNombre.setErrorEnabled(false);
        }

        if (this.txilApellidos.getEditText().getText().toString().isEmpty()){
            camposRellenos = false;
            this.txilApellidos.setError("Los apellidos no pueden estar en blanco");
        }else {
            this.txilApellidos.setErrorEnabled(false);
        }

        if (this.txieFecha.getText().toString().isEmpty()){
            camposRellenos = false;
            this.txilFecha.setError("La fecha no puede estar en blanco");
        }else {
            this.txilFecha.setErrorEnabled(false);
        }

        if (this.txilCorreo.getEditText().getText().toString().isEmpty()){
            camposRellenos = false;
            this.txilCorreo.setError("El correo no puede estar en blanco");
        }else {
            this.txilCorreo.setErrorEnabled(false);
        }

        if (this.txilTlf.getEditText().getText().toString().isEmpty()){
            camposRellenos = false;
            this.txilTlf.setError("El teléfono no puede estar en blanco");
        }else {
            this.txilTlf.setErrorEnabled(false);
        }

        if (this.txilComentarios.getEditText().getText().toString().isEmpty()){
            camposRellenos = false;
            this.txilComentarios.setError("Los comentarios no pueden estar en blanco");
        }else {
            this.txilComentarios.setErrorEnabled(false);
        }

        if (!camposRellenos){
            mostrarSnackbar(mensajeError);
        }
        return camposRellenos;
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
            case R.id.txieFecha:{
                FechaDatePickerDialog dialog = new FechaDatePickerDialog();
                dialog.show(getSupportFragmentManager(), "datePiker");
                break;
            }
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.itemCamara:{
                if (this.checkPermisionCamera()) {
                    this.cargarCamara();
                }
                return true;
            }
            case R.id.itemGaleria:{
                if (this.checkPermisionWriteRead()){
                    this.CargarGaleria();
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Inicia la cámara
     */
    private void cargarCamara() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {
            // Crea el File
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error cuando se creo el archivo
            }
            if (photoFile != null) {
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, RESULT_CAMERA);
            }
        }
    }

    /**
     * Crea un File donde volcaremos la foto desde la cámara
     *
     * @return :File
     */
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        /* Se guarda la ruta donde está el archivo, que es desde donde se recogerá en
         * el onResult de la cámara */
        mCurrentPhotoPath = image.getAbsolutePath();
        Log.d("tag","el path de la imagen es = " + mCurrentPhotoPath);
        return image;
    }

    /**
     * Carga la galeria
     */
    private void CargarGaleria(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, RESULT_GALERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_CAMERA && resultCode == RESULT_OK) {
            this.fotoUsuario.cargarDesdeCamara(mCurrentPhotoPath);
            if (this.fotoUsuario.existeFoto()) {
                this.imgUser.setImageURI(this.fotoUsuario.getUriFoto());
                Log.i("onActivityResult_CAMERA", "La imagen se ha cargado correctamente");
            }else {
                mostrarSnackbar("La imagen no se ha guardado bien");
            }
        }
        if (requestCode == RESULT_GALERY && resultCode == RESULT_OK){
            this.fotoUsuario.cargarDesdeGaleria(data.getData(), getApplicationContext());
            if (this.fotoUsuario.existeFoto()){
                this.imgUser.setImageURI(this.fotoUsuario.getUriFoto());
                Log.i("onActivityResult_Galery", "La imagen se ha cargado correctamente");
            }else {
                mostrarSnackbar("La imagen no se ha guardado bien");
            }
        }
    }

    /**
     * Vacía todos los elementos de la actividad y elimina los errores
     */
    private void vaciarCampos(){
        this.txilNick.getEditText().setText("");
        this.txilPass.getEditText().setText("");
        this.txilNombre.getEditText().setText("");
        this.txilApellidos.getEditText().setText("");
        this.txilFecha.getEditText().setText("");
        this.txilCorreo.getEditText().setText("");
        this.txilTlf.getEditText().setText("");
        this.txilComentarios.getEditText().setText("");

        this.imgUser.setImageResource(R.drawable.ic_person_black_24dp);
        this.fotoUsuario.borrarFotoAnterior();

        this.txilNick.setErrorEnabled(false);
        this.txilPass.setErrorEnabled(false);
        this.txilNombre.setErrorEnabled(false);
        this.txilApellidos.setErrorEnabled(false);
        this.txilFecha.setErrorEnabled(false);
        this.txilCorreo.setErrorEnabled(false);
        this.txilTlf.setErrorEnabled(false);
        this.txilComentarios.setErrorEnabled(false);
    }

    /**
     * Valida los permisos para abrir la cámara
     *
     * @return :True si los permisos estan cargados, en caso negativo los pide
     */
    private boolean checkPermisionCamera(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
                mostrarSnackbar("Error al cargar los permisos, vuelva a intentarlo");
                return false;
            }else{
                Log.i("CheckPermisionCamera", "Permisos de cámara cargados correctamente");
                return true;
            }
        }
        return true;
    }

    /**
     * Valida los permisos para leer y escribir de la memoria externa
     *
     * @return :True si los permisos estan cargados, en caso negativo los pide
     */
    private boolean checkPermisionWriteRead(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED) &&
                    (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) !=
                            PackageManager.PERMISSION_GRANTED)) {

                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE}, 100);

                mostrarSnackbar("Error al cargar los permisos, vuelva a intentarlo");
                return false;
            }else{
                Log.i("checkPermisionWriteRead", "Permisos de lectura y escritura " +
                        "cargados correctamente");
                return true;
            }
        }
        return true;
    }

    /**
     * Valida los permisos para acceder a la localización
     *
     * @return :True si los permisos estan cargados, en caso negativo los pide
     */
    private boolean checkPermisionLocation(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
                mostrarSnackbar("Error al cargar los permisos de localización, " +
                        "vuelva a intentarlo");
                return false;
            }else{
                Log.i("checkPermisionLocation", "Permisos de localización cargados " +
                        "correctamente");
                return true;
            }
        }
        return true;
    }

    /**
     * Muestra un mensaje en Snackbar
     *
     * @param mensaje :String
     */
    private void mostrarSnackbar(String mensaje){
        Snackbar.make(getCurrentFocus(), mensaje, Snackbar.LENGTH_LONG).show();
    }
}
