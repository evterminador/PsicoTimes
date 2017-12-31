package com.example.formandocodigo.psicotimes.utils.permission;

import android.app.AppOpsManager;
import android.content.Context;
import android.os.Process;

/**
 * Created by FormandoCodigo on 26/12/2017.
 */

public class UsageStatsPermission {

    public static Boolean isExistsPermission(Context context) {
        AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, Process.myUid(), context.getPackageName());
        return mode == AppOpsManager.MODE_ALLOWED;
    }
}
