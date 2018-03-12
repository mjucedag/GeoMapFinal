package com.example.uceda.geomapfinal;
//https://github.com/izvproyectodam/Maps1718/blob/master/app/src/main/java/com/izv/dam/maps1718/MapsActivity.java
//https://github.com/izvproyectodam/Maps1718 APUNTES DE CARMELO

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.Date;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Date gc;
    ObjectSet<Localizacion> locations;
    private ObjectContainer objectContainer;

    private static final String TAG = MapsActivity.class.getSimpleName();

    private void startThreadGetLocations() {
        objectContainer = openDataBase(getApplicationContext());
        Thread t = new Thread() {
            @Override
            public void run() {
                super.run();
                try {

                    Bundle extras = getIntent().getExtras();
                    if (extras == null) {
                        Log.d(TAG, "Intent null");
                        return;
                    }

                    gc = (Date) extras.get("fecha");

                    locations = Db4oUtils.getLocations(objectContainer, gc);
                } catch (Exception e) {

                }
            }
        };
        t.start();
        try {
            t.join();
        } catch (Exception e) {

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startThreadGetLocations();
        setContentView(R.layout.activity_maps);

        // Check if the system supports OpenGL ES 2.0.
        final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;

        System.out.println("Soporta OpenGL ES 2.0 -> " + supportsEs2);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used. Tratan de cargar de forma asincrona
        // el mapa y le decimos que el mapa una vez que esté cargado, me llame a mi mismo para acceder al mapa y empezar a interactuar
        //con él
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {


            this.mMap = googleMap;

            //Llamar a la bbdd, con la fecha obtenida
            // ObjectSet<Localizacion> locations = Db4oUtils.getLocations(objectContainer, gc);

            PolylineOptions polylineOptions = new PolylineOptions();
            LatLng startLatLng = null;
            LatLng endLatLng = null;

            for (int i = 0; i < locations.size(); i++) { //saca todas las localiz
                Localizacion localizacion = locations.get(i);
                System.out.println("Localizacion " + i+1 + ": " + localizacion);
                LatLng newLatLng = new LatLng(localizacion.getLatitud(), localizacion.getLongitud());
                polylineOptions.add(newLatLng);

                if (i == 0) {
                    startLatLng = newLatLng;
                } else if (i == locations.size() - 1) {
                    endLatLng = newLatLng;
                }
            }

            if (startLatLng == null || endLatLng == null) {
                Log.v(TAG, "No latlng");
                return;
            }

            // LatLng granada = new LatLng(37.1608,-3.5911);
            this.mMap.addMarker(new MarkerOptions().position(startLatLng).title("IZV"));
            this.mMap.moveCamera(CameraUpdateFactory.newLatLng(startLatLng));
            this.mMap.moveCamera(CameraUpdateFactory.zoomTo(17));
            this.mMap.addPolyline(polylineOptions);

            mMap.addMarker(new MarkerOptions().position(endLatLng).title("Marker in Granada"));//añade la marca final
            mMap.moveCamera(CameraUpdateFactory.newLatLng(endLatLng));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Db4oUtils.closeDb4oDataBase(objectContainer);
        }
    }

    public ObjectContainer openDataBase(Context context) { //abre la conexion con tu BBDD
        ObjectContainer objectContainer = null;
        try {
            String name = Db4oUtils.db4oDBFullPath(context);
            objectContainer = Db4oEmbedded.openFile(Db4oUtils.getDb4oConfig(), name);
        } catch (IOException e) {
            Log.v(TAG, e.toString());
        }
        return objectContainer;
    }
}
