package com.example.alberto.facecook.Localizacion;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.IBinder;

public class LocalizacionListener extends Service implements LocationListener {

    /* Atributos */
    Context context;

    /**
     * Constructor de clase
     *
     * @param context :Contex
     */
    public LocalizacionListener(Context context){
        this.context = context;
        this.activarLocalizacion();
    }

    private void activarLocalizacion(){
        //TODO ACTIVAR LA LOCALIZACION Y LOS MÉTODOS PARA DEVOLVERLOS
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
