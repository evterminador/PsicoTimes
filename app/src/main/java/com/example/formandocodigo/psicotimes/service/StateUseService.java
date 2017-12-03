package com.example.formandocodigo.psicotimes.service;

import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.example.formandocodigo.psicotimes.data.entity.StateUseEntity;
import com.example.formandocodigo.psicotimes.data.cache.FileManager;
import com.example.formandocodigo.psicotimes.data.cache.StateUseCacheImpl;
import com.example.formandocodigo.psicotimes.data.cache.serializer.Serializer;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by FormandoCodigo on 27/11/2017.
 */

public class StateUseService extends Service implements StateUseServiceView {

    private ArrayList<StateUseEntity> stateUses = new ArrayList<>();

    StateUseCacheImpl save;

    public StateUseService() {}

    @Override
    public void onCreate() {
        super.onCreate();
        save = new StateUseCacheImpl(this, new Serializer(), new FileManager());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //return super.onStartCommand(intent, flags, startId);
        Toast.makeText(this, "Service starting", Toast.LENGTH_SHORT).show();
        //save.setLastCacheUpdateTimeMillis();
        initializeApp();
        return  START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        saveApplication();
        Toast.makeText(this, "Service done", Toast.LENGTH_LONG).show();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void initializeApp() {
        getApp();
    }

    @Override
    public void getApp() {
        UsageStatsManager manager = (UsageStatsManager) getSystemService(this.USAGE_STATS_SERVICE);
        long beginTime = System.currentTimeMillis();
        long endTime = getAppLastUse();

        List<UsageStats> stats = manager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, beginTime - endTime, beginTime);

        Iterator i = stats.iterator();

        PackageManager pm = this.getPackageManager();

        UsageStats sta;
        Timestamp timeUse;
        while (i.hasNext()) {
            sta = (UsageStats) i.next();

            CharSequence c;
            try {
                timeUse = new Timestamp(sta.getLastTimeStamp());
                c = pm.getApplicationLabel(pm.getApplicationInfo(sta.getPackageName(), PackageManager.GET_META_DATA));
                String nPk = String.valueOf(c);

                if (sta.getLastTimeUsed() > endTime) {
                    StateUseEntity  stateUse;
                    if (!repeatApp(sta, nPk)) {
                        stateUse = new StateUseEntity();

                        stateUse.setNameApplication(c.toString());
                        stateUse.setUseTime(sta.getTotalTimeInForeground());
                        stateUse.setLastUseTime(timeUse);
                        stateUse.setQuantity(1);

                        stateUses.add(stateUse);
                    }
                }
                //runningApplications.add(c.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private long getAppLastUse() {
        long lastTime = save.getLastCacheUpdateTimeMillis();
        if (lastTime > 0) {
            return  lastTime;
        }
        return (System.currentTimeMillis() - 1000*60*60);
    }

    private void saveApplication() {
        save.putAll(stateUses);
    }

    private boolean repeatApp(UsageStats stats, String nPackage) {
        try {
            if (stateUses.size() > 0) {
                for (int x = 0; x < stateUses.size(); x++) {
                    if (stateUses.get(x).getNameApplication().equalsIgnoreCase(nPackage)) {
                        stateUses.get(x).setUseTime(stats.getLastTimeUsed());
                        stateUses.get(x).setQuantity(stateUses.get(x).getQuantity() + 1);
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    void lastAppUseTime() {
        
    }
}
