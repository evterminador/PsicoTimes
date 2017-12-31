package com.example.formandocodigo.psicotimes.service;

import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.example.formandocodigo.psicotimes.data.entity.StateUseEntity;
import com.example.formandocodigo.psicotimes.data.cache.FileManager;
import com.example.formandocodigo.psicotimes.data.cache.StateUseCacheImpl;
import com.example.formandocodigo.psicotimes.data.cache.serializer.Serializer;
import com.example.formandocodigo.psicotimes.utils.Continual;
import com.example.formandocodigo.psicotimes.utils.Converts;
import com.example.formandocodigo.psicotimes.utils.permission.UsageStatsPermission;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

/**
 * Created by FormandoCodigo on 27/11/2017.
 */

public class StateUseService extends Service implements StateUseServiceView {

    private ArrayList<StateUseEntity> stateUses = new ArrayList<>();
    private long initialTime;
    StateUseCacheImpl save;

    private Handler serviceLoop = null;
    private Runnable run = null;
    private Runnable runLock = null;

    private StateUseServiceReceiver lockScreenReceiver;
    private int countLookScreen = 0;

    public StateUseService() {}

    @Override
    public void onCreate() {
        super.onCreate();

        save = new StateUseCacheImpl(this, new Serializer(), new FileManager());

        initialTime = getAppLastUse();
        save.setLastCacheUpdateTimeMillis();

        serviceLoop = new Handler();
        run = new Runnable() {
            @Override
            public void run() {
                //Toast.makeText(StateUseService.this, "Guardando", Toast.LENGTH_SHORT).show();

                Toast.makeText(StateUseService.this, String.valueOf(countLookScreen), Toast.LENGTH_LONG).show();

                if (checkPermission()) {
                    initializeApp();
                    saveApplication();

                    initialTime = getAppLastUse();
                    save.setLastCacheUpdateTimeMillis();
                } else {
                    Toast.makeText(StateUseService.this, "Permiso revocado", Toast.LENGTH_LONG).show();
                    onDestroy();
                }

                serviceLoop.postDelayed(run, 10000);
            }
        };

        serviceLoop.postDelayed(run, 15000);

        lockScreenReceiver = new StateUseServiceReceiver();
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);

        registerReceiver(lockScreenReceiver, filter);
    }

    public class StateUseServiceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                // screen is turn off
                //("Screen locked");
            } else {
                //Handle resuming events if user is present/screen is unlocked
                countLookScreen++;
                //("Screen unlocked");
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //return super.onStartCommand(intent, flags, startId);
        //Toast.makeText(this, "Service starting", Toast.LENGTH_SHORT).show();
        //initialTime = getAppLastUse();
        //save.setLastCacheUpdateTimeMillis();
        //initializeApp();

        return  START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        saveApplication();
        //saveLookScreen();
        serviceLoop.removeCallbacks(run);
        //serviceLoop.removeCallbacks(runLock);
        unregisterReceiver(lockScreenReceiver);
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
        long endTime = initialTime;

        List<UsageStats> stats = manager.queryUsageStats(UsageStatsManager.INTERVAL_BEST,  endTime, beginTime);

        Iterator i = stats.iterator();

        PackageManager pm = this.getPackageManager();

        UsageStats sta;
        Timestamp timeUse;
        while (i.hasNext()) {
            sta = (UsageStats) i.next();

            CharSequence c;
            try {
                timeUse = new Timestamp(sta.getLastTimeUsed());
                c = pm.getApplicationLabel(pm.getApplicationInfo(sta.getPackageName(), PackageManager.GET_META_DATA));
                String nPk = String.valueOf(c);

                if (sta.getLastTimeUsed() > endTime) {
                    StateUseEntity  stateUse;
                    if (!repeatApp(sta, nPk)) {
                        stateUse = new StateUseEntity();

                        stateUse.setNameApplication(nPk);
                        stateUse.setImage(sta.getPackageName());
                        stateUse.setUseTime(sta.getTotalTimeInForeground());
                        stateUse.setLastUseTime(timeUse);
                        stateUse.setQuantity(1);
                        stateUse.setCreated_at(new Timestamp(beginTime));
                        stateUse.setUpdated_at(new Timestamp(beginTime));

                        stateUses.add(stateUse);
                    }
                }
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
        stateUses.clear();
    }

    private void saveLookScreen() {
        SharedPreferences preferences = getSharedPreferences(Continual.Shared.LockScreen.FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();

        String date = preferences.getString(Continual.Shared.LockScreen.KEY_CREATED_AT, null);

        try {
            Timestamp getDate = Converts.convertStringToTimestamp(date);

            if (getDate.before(getCurrentDay())) {
                countLookScreen = 0;
                edit.putString(Continual.Shared.LockScreen.KEY_CREATED_AT, Converts.convertTimestampToString(new Timestamp(System.currentTimeMillis())));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int lock = preferences.getInt(Continual.Shared.LockScreen.KEY_SCREEN, -1);

        if (lock > 0) {
            countLookScreen += lock;
        }

        edit.putInt(Continual.Shared.LockScreen.KEY_SCREEN, countLookScreen);

        if (date == null) {
            edit.putString(Continual.Shared.LockScreen.KEY_CREATED_AT, Converts.convertTimestampToString(new Timestamp(System.currentTimeMillis())));
        }

        if (edit.commit()) {
            countLookScreen = 0;
        }
    }

    private boolean repeatApp(UsageStats stats, String nPackage) {
        try {
            if (stateUses.size() > 0) {
                for (int x = 0; x < stateUses.size(); x++) {
                    if (stateUses.get(x).getNameApplication().equalsIgnoreCase(nPackage)
                            && stateUses.get(x).getCreated_at().after(getCurrentDay())) {
                        stateUses.get(x).setUseTime(stats.getTotalTimeInForeground());
                        stateUses.get(x).setQuantity(stateUses.get(x).getQuantity() + 1);
                        stateUses.get(x).setUpdated_at(new Timestamp(System.currentTimeMillis()));
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
        return false;
    }

    protected Boolean checkPermission() {
        return UsageStatsPermission.isExistsPermission(this);
    }

    private Timestamp getCurrentDay() {
        Calendar c = Calendar.getInstance();

        Converts.setTimeToBeginningOfDay(c);

        return new Timestamp(c.getTimeInMillis());
    }

}
