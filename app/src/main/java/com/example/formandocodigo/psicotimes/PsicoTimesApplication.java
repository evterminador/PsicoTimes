package com.example.formandocodigo.psicotimes;

import android.app.Application;


import com.example.formandocodigo.psicotimes.data.disk.SQLiteManager;
import com.example.formandocodigo.psicotimes.utils.Continual;
import com.facebook.stetho.Stetho;
import com.squareup.leakcanary.LeakCanary;

/**
 * Created by FormandoCodigo on 01/12/2017.
 */

public class PsicoTimesApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        SQLiteManager.Initialize(this, Continual.SQLite.DEFAULT_FILE_NAME, null, 1);

        Stetho.initializeWithDefaults(this);

        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
    }
}
