package com.example.formandocodigo.psicotimes.utils;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;

import java.util.List;

/**
 * Created by FormandoCodigo on 30/12/2017.
 */

public class PackageInformation {

    List<PackageInfo> list;
    private Activity activity;

    public PackageInformation(Activity activity) {
        this.activity = activity;
        list = getPackageInstalled();
    }

    public Drawable getIconAppByName(String name) {
        Drawable find = null;
        if (list.size() > 0) {
            for (PackageInfo info : list) {
                String label = info.applicationInfo.loadLabel(activity.getPackageManager()).toString();

                if (label.equalsIgnoreCase(name))
                    find = info.applicationInfo.loadIcon(activity.getPackageManager());
            }
        }
        return find;
    }

    private List<PackageInfo> getPackageInstalled() {
        return activity.getPackageManager().getInstalledPackages(0);
    }
}
