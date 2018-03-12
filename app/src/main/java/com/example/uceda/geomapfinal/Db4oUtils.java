package com.example.uceda.geomapfinal;


import android.content.Context;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.AndroidSupport;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.query.Predicate;
import com.db4o.query.Query;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Db4oUtils {



    public static EmbeddedConfiguration getDb4oConfig() throws IOException {
        EmbeddedConfiguration configuration = Db4oEmbedded.newConfiguration();
        configuration.common().add(new AndroidSupport());
        configuration.common().objectClass(Localizacion.class).
                objectField("fecha").indexed(true);
        return configuration;
    }

    public static void storeInDb4oDataBase(ObjectContainer db, Localizacion localizacion){
        db.store(localizacion);
        db.commit();
    }

    public static void closeDb4oDataBase(ObjectContainer db){
        db.close();
    }

    public static ObjectSet<Localizacion> getLocations(ObjectContainer db, final Date gc){
        ObjectSet<Localizacion> locs = db.query(
                new Predicate<Localizacion>() { //predicate tiene que ser true para que me devuelva el objeto de la bbdd
                    @Override
                    public boolean match(Localizacion loc) {

                        Calendar cal = Calendar.getInstance();
                        cal.setTime(loc.getFecha());
                        int year = cal.get(Calendar.YEAR);
                        int month = cal.get(Calendar.MONTH);
                        int day = cal.get(Calendar.DAY_OF_MONTH);

                        Calendar calToCheck = Calendar.getInstance();
                        calToCheck.setTime(gc);
                        int yearToCheck = cal.get(Calendar.YEAR);
                        int monthToCheck = cal.get(Calendar.MONTH);
                        int dayToCheck = cal.get(Calendar.DAY_OF_MONTH);

                        if(year == yearToCheck && month == monthToCheck && day == dayToCheck){
                            return true;
                        }
                        return false;
                    }
                });
        return locs;
    }

    public static ObjectSet<Localizacion> getAllLocations(ObjectContainer db){
        Query consulta = db.query();
        consulta.constrain(Localizacion.class);
        ObjectSet<Localizacion> localizaciones = consulta.execute();
        return localizaciones;
    }

    /**
     * Returns the path for the database location
     */

    public static String db4oDBFullPath(Context ctx) {
        return ctx.getDir("data", 0) + "/" + "geomap11.db4o";
    }
}
