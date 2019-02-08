package com.example.alberto.facecook.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.alberto.facecook.Clases.FotoUsuario;
import com.example.alberto.facecook.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RegistroActivity extends AppCompatActivity implements View.OnClickListener,
        PopupMenu.OnMenuItemClickListener {

    /* Elementos */
    private ImageView imgUser;
    private Toolbar toolbar;
    private TextInputLayout txilNick,
                            txilPass,
                            txilNombre,
                            txilApellidos,
                            txilFecha,
                            txilTlf,
                            txilComentarios;

    /* Result Codes */
    private final int RESULT_CAMERA = 1;
    private final int RESULT_GALERY = 2;

    /* Atributos */
    private String mCurrentPhotoPath;
    private FotoUsuario fotoUsuario;


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
                Toast.makeText(this, "La imagen no se ha guardado bien",
                        Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == RESULT_GALERY && resultCode == RESULT_OK){
            this.fotoUsuario.cargarDesdeGaleria(data.getData(), getApplicationContext());
            if (this.fotoUsuario.existeFoto()){
                this.imgUser.setImageURI(this.fotoUsuario.getUriFoto());
                Log.i("onActivityResult_Galery", "La imagen se ha cargado correctamente");

                //this.fotoCambiada = true;
            }else {
                Toast.makeText(this, "La imagen no se ha guardado bien",
                        Toast.LENGTH_LONG).show();
            }
        }
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
                Snackbar.make(getCurrentFocus(), "Error al cargar los permisos, vuelva a intentarlo",
                        Snackbar.LENGTH_LONG).show();
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

                Snackbar.make(getCurrentFocus(), "Error al cargar los permisos, vuelva a intentarlo",
                        Snackbar.LENGTH_LONG).show();
                return false;
            }else{
                Log.i("checkPermisionWriteRead", "Permisos de lectura y escritura " +
                        "cargados correctamente");
                return true;
            }
        }
        return true;
    }
}
