package com.example.formandocodigo.psicotimes.utils;

import android.app.Activity;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.pm.PackageManager;

import com.example.formandocodigo.psicotimes.data.entity.StateUseEntity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

/**
 * Created by FormandoCodigo on 22/12/2017.
 */

public class StateUseAll {

    private Activity activity;

    public StateUseAll (Activity activity) {
        super();
        this.activity = activity;
    }

    public ArrayList<StateUseEntity> getStateUseEntityAll() {
        ArrayList<StateUseEntity> stateUseEntities = new ArrayList<>();

        UsageStatsManager manager = (UsageStatsManager) activity.getSystemService(Context.USAGE_STATS_SERVICE);
        long beginTime = System.currentTimeMillis();
        long endTime = initialTime();

        List<UsageStats> stats = manager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, endTime, beginTime);

        Iterator i = stats.iterator();

        PackageManager pm = activity.getPackageManager();

        UsageStats sta;
        Timestamp timeUse;
        while (i.hasNext()) {
            sta = (UsageStats) i.next();

            CharSequence c;
            try {
                timeUse = new Timestamp(sta.getLastTimeUsed());
                c = pm.getApplicationLabel(pm.getApplicationInfo(sta.getPackageName(), PackageManager.GET_META_DATA));

                String npk = String.valueOf(c);

                if (sta.getLastTimeUsed() > endTime) {
                    StateUseEntity  stateUseEntity;
                    if (!repeatApp(stateUseEntities, sta, npk)) {
                        stateUseEntity = new StateUseEntity();

                        stateUseEntity.setNameApplication(npk);
                        stateUseEntity.setImage(sta.getPackageName());
                        stateUseEntity.setUseTime(sta.getTotalTimeInForeground());
                        stateUseEntity.setLastUseTime(timeUse);

                        stateUseEntities.add(stateUseEntity);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return stateUseEntities;
    }

    private boolean repeatApp(ArrayList<StateUseEntity> stateUseEntities, UsageStats stats, String nPackage) {
        try {
            if (stateUseEntities.size() > 0) {
                for (int x = 0; x < stateUseEntities.size(); x++) {
                    if (stateUseEntities.get(x).getNameApplication().equalsIgnoreCase(nPackage)) {
                        stateUseEntities.get(x).setUseTime(stats.getTotalTimeInForeground());
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

    private long initialTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        return calendar.getTimeInMillis();
    }
}
