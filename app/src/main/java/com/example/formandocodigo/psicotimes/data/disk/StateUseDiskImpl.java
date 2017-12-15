package com.example.formandocodigo.psicotimes.data.disk;


import android.util.Log;

import com.example.formandocodigo.psicotimes.entity.ApplicationEntity;
import com.example.formandocodigo.psicotimes.entity.StateUser;

import java.util.ArrayList;

/**
 * Created by FormandoCodigo on 13/12/2017.
 */

public class StateUseDiskImpl implements StateUseDisk {

    public StateUseDiskImpl() {

    }

    @Override
    public void putApplication(ApplicationEntity applicationEntity) {

    }

    @Override
    public void putApplicationAll(ArrayList<ApplicationEntity> applicationEntityList) {

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
                        if (!isExistsUpdate(data, s)) {
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
            Log.e("MainActivity", e.getMessage());
            return -1;
        }
        return result;
    }

    private boolean isExistsUpdate(ArrayList<StateUser> o1, StateUser o2) {
        for (StateUser s : o1) {
            if (s.getId_app().equals(o2.getId_app()) && s.getCreated_at().equals(o2.getCreated_at())) {
                try {
                    SQLiteManager.Instance().updateStateUser(o2);
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
