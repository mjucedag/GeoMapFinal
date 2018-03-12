package com.example.uceda.geomapfinal;

/**
 * Created by uceda on 21/2/18.
 */

import java.io.Serializable;
import java.util.Date;
import java.util.GregorianCalendar;

public class Localizacion implements Serializable{

    public static final long serialVersionUID = 42L;

    private double latitud;
    private double longitud;
    private Date fecha;


    public Localizacion(double latitud, double longitud) {
        this(latitud, longitud, new GregorianCalendar().getTime());
    }

    public Localizacion(double latitud, double longitud, Date fecha) {
        this.latitud = latitud;
        this.longitud = longitud;
        this.fecha = fecha;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return "Localizacion{" +
                "latitud=" + latitud +
                ", longitud=" + longitud +
                ", fecha=" + fecha +
                '}';
    }
}