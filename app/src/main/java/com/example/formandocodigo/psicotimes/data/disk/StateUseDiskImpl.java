package com.example.formandocodigo.psicotimes.data.disk;

import android.util.Log;

import com.example.formandocodigo.psicotimes.entity.App;
import com.example.formandocodigo.psicotimes.entity.StateUse;
import com.example.formandocodigo.psicotimes.entity.StateUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FormandoCodigo on 13/12/2017.
 */

public class StateUseDiskImpl implements StateUseDisk {

    public StateUseDiskImpl() {

    }

    @Override
    public Integer putApplicationAll(ArrayList<App> appList) {
        int result;

        try {
            ArrayList<App> data = SQLiteManager.Instance().getAllApp();

            int c = 0;
            if (appList.size() > 0) {
                if (data.size() > 0) {
                    for (App a : appList) {
                        if (!isExistsUpdateApp(data, a)) {
                            SQLiteManager.Instance().insertApp(a);
                            c++;
                        }
                    }
                } else {
                    for (App a : appList) {
                        SQLiteManager.Instance().insertApp(a);
                        c++;
                    }
                }
            }
            result = c;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        return result;
    }

    @Override
    public Integer putStateUserAll(ArrayList<StateUser> stateUserList) {
        int result;

        try {
            ArrayList<StateUser> data = SQLiteManager.Instance().getAllStateUser();

            int c = 0;
            if (stateUserList.size() > 0) {
                if (data.size() > 0) {
                    for (StateUser s : stateUserList) {
                        if (!isExistsUpdateStateUser(data, s)) {
                            Log.i("MainActivity", "agrega");
                            SQLiteManager.Instance().insertStateUser(s);
                            c++;
                        }
                    }
                } else {
                    for (StateUser s : stateUserList) {
                        SQLiteManager.Instance().insertStateUser(s);
                        c++;
                    }
                }
            }
            result = c;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        return result;
    }

    @Override
    public List<StateUser> getStateUserAll() {
        List<StateUser> list = null;
        try {
            list = SQLiteManager.Instance().getAllStateUser();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<App> getAppAll() {
        List<App> list = new ArrayList<>();
        try {
            list = SQLiteManager.Instance().getAllApp();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<StateUse> findStateUseByIdAll(Integer id) {
        List<StateUse> list = new ArrayList<>();

        try {
            list = SQLiteManager.Instance().findStateUseById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<StateUse> getStateUseAll() {
        List<StateUse> list = new ArrayList<>();
        try {
            list = SQLiteManager.Instance().getStateUses();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<StateUse> getStateUseByDate() {
        List<StateUse> list = new ArrayList<>();
        try {
            list = SQLiteManager.Instance().getStateUsesByDate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private boolean isExistsUpdateStateUser(ArrayList<StateUser> o1, StateUser o2) {
        for (StateUser s : o1) {
            if (s.getId_app().equals(o2.getId_app()) && s.getCreated_at().equals(o2.getCreated_at())) {
                try {
                    SQLiteManager.Instance().updateStateUser(o2);
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    private boolean isExistsUpdateApp(ArrayList<App> a1, App a2) {
        for (App a : a1) {
            if (a.getId().equals(a2.getId())) {
                try {
                    SQLiteManager.Instance().updateApp(a);
                } catch (Exception e) {
                    e.printStackTrace();
                    return true;
                }
                return true;
            }
        }
        return false;
    }
}
