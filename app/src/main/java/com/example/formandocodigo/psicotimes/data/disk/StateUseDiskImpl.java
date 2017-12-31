package com.example.formandocodigo.psicotimes.data.disk;

import com.example.formandocodigo.psicotimes.entity.App;
import com.example.formandocodigo.psicotimes.entity.HistoricState;
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
            ArrayList<App> data = SQLiteManager.Instance().getAppAll();

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
            ArrayList<StateUser> data = SQLiteManager.Instance().getStateUserAll();

            int c = 0;
            if (stateUserList.size() > 0) {
                if (data.size() > 0) {
                    for (StateUser s : stateUserList) {
                        if (!isExistsUpdateStateUser(data, s)) {
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
    public Integer putHistoricStateAll(ArrayList<HistoricState> historicStateList) {
        int result;

        try {
            ArrayList<HistoricState> data = SQLiteManager.Instance().getHistoricStateAll();

            int c = 0;
            if (historicStateList.size() > 0) {
                if (data.size() > 0) {
                    for (HistoricState h : historicStateList) {
                        if (!isExistsUpdateHistoricState(data, h)) {
                            SQLiteManager.Instance().insertHistoricState(h);
                            c++;
                        }
                    }
                } else {
                    for (HistoricState h : historicStateList) {
                        SQLiteManager.Instance().insertHistoricState(h);
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
            list = SQLiteManager.Instance().getStateUserAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<App> getAppAll() {
        List<App> list = new ArrayList<>();
        try {
            list = SQLiteManager.Instance().getAppAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<HistoricState> getHistoricStateAll() {
        List<HistoricState> list = new ArrayList<>();
        try {
            list = SQLiteManager.Instance().getHistoricStateAll();
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
            if (s.getAppId().equals(o2.getAppId()) && s.getCreated_at().equals(o2.getCreated_at())) {
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
            if (a.getId().equals(a2.getId()) && (a.getUpdated_at() != a2.getUpdated_at())) {
                try {
                    SQLiteManager.Instance().updateApp(a2);
                } catch (Exception e) {
                    e.printStackTrace();
                    return true;
                }
                return true;
            }
        }
        return false;
    }

    private boolean isExistsUpdateHistoricState(ArrayList<HistoricState> h1, HistoricState h2) {
        for (HistoricState h : h1) {
            if (h.getCreated_at().equals(h2.getCreated_at())) {
                try {
                    SQLiteManager.Instance().updateHistoricState(h2);
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
