package com.example.alberto.facecook.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.alberto.facecook.R;

public class RegistroActivity extends AppCompatActivity implements View.OnClickListener,
        PopupMenu.OnMenuItemClickListener {

    /* Elementos */
    ImageView imgUser;
    TextView txvInfoReg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        this.iniciarElementos();
    }

    private void iniciarElementos(){
        /* XML */
        this.imgUser = (ImageView)findViewById(R.id.imgUser);
        this.txvInfoReg = (TextView)findViewById(R.id.txvInfoReg);

        /* Clickable */
        this.imgUser.setOnClickListener(this);
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
        this.txvInfoReg.setVisibility(View.GONE);
        return false;
    }
}
