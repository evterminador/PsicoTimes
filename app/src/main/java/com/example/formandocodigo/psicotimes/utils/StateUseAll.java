package com.example.formandocodigo.psicotimes.utils;

import android.app.Activity;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.pm.PackageManager;

import com.example.formandocodigo.psicotimes.entity.StateUse;

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

    public ArrayList<StateUse> getStateUseAll() {
        ArrayList<StateUse> stateUses = new ArrayList<>();

        UsageStatsManager manager = (UsageStatsManager) activity.getSystemService(Context.USAGE_STATS_SERVICE);
        long beginTime = System.currentTimeMillis();
        long endTime = initialTime();

        List<UsageStats> stats = manager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, beginTime - endTime, beginTime);

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

                StateUse  stateUse;
                if (!repeatApp(stateUses, sta, npk)) {
                    stateUse = new StateUse();

                    stateUse.setNameApplication(npk);
                    stateUse.setImageApp(sta.getPackageName());
                    stateUse.setUseTime(sta.getTotalTimeInForeground());
                    stateUse.setLastUseTime(timeUse);

                    stateUses.add(stateUse);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return stateUses;
    }

    private boolean repeatApp(ArrayList<StateUse> stateUses, UsageStats stats, String nPackage) {
        try {
            if (stateUses.size() > 0) {
                for (int x = 0; x < stateUses.size(); x++) {
                    if (stateUses.get(x).getNameApplication().equalsIgnoreCase(nPackage)) {
                        stateUses.get(x).setUseTime(stats.getTotalTimeInForeground());
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
        Converts.setTimeToBeginningOfDay(calendar);

        return calendar.getTimeInMillis();
    }
}
