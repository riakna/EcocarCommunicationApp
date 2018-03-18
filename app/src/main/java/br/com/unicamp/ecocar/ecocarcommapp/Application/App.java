package br.com.unicamp.ecocar.ecocarcommapp.Application;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Nagaishi on 17/03/2018.
 */

public class App  extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //set database to persist
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
