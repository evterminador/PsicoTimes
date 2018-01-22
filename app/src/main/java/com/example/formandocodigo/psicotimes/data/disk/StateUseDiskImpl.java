package com.example.formandocodigo.psicotimes.data.disk;

import com.example.formandocodigo.psicotimes.entity.AppTop;
import com.example.formandocodigo.psicotimes.entity.HistoricState;
import com.example.formandocodigo.psicotimes.entity.StatisticsDetail;
import com.example.formandocodigo.psicotimes.utils.Converts;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by FormandoCodigo on 13/12/2017.
 */

public class StateUseDiskImpl implements StateUseDisk {

    public StateUseDiskImpl() {

    }

    @Override
    public Integer putAppTopAll(ArrayList<AppTop> appTopList) {
        int result;

        try {
            ArrayList<AppTop> data = SQLiteManager.Instance().getAppTopAll();

            int c = 0;
            if (appTopList.size() > 0) {
                if (data.size() > 0) {
                    for (AppTop a : appTopList) {
                        if (!isExistsUpdateAppTop(data, a)) {
                            SQLiteManager.Instance().insertAppTop(a);
                            c++;
                        }
                    }
                } else {
                    for (AppTop a : appTopList) {
                        SQLiteManager.Instance().insertAppTop(a);
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
    public Integer putStatisticsDetailAll(ArrayList<StatisticsDetail> statisticsDetails) {
        int result;

        try {
            ArrayList<StatisticsDetail> data = SQLiteManager.Instance().getStatisticsDetailAll();

            int c = 0;
            if (statisticsDetails.size() > 0) {
                if (data.size() > 0) {
                    for (StatisticsDetail s : statisticsDetails) {
                        if (!isExistsUpdateStatisticsDetail(data, s)) {
                            SQLiteManager.Instance().insertStatisticsDetail(s);
                            c++;
                        }
                    }
                } else {
                    for (StatisticsDetail s : statisticsDetails) {
                        SQLiteManager.Instance().insertStatisticsDetail(s);
                        c++;
                    }
                }
            }
            result = c;
        } catch (Exception e) {
            e.printStackTrace();
            return  -1;
        }
        return result;
    }


    @Override
    public List<AppTop> getAppTopAll() {
        List<AppTop> list = new ArrayList<>();
        try {
            list = SQLiteManager.Instance().getAppTopAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<StatisticsDetail> getStatisticsDetailByDate(Timestamp date) {
        List<StatisticsDetail> list = new ArrayList<>();
        try {
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(date.getTime());

            list = SQLiteManager.Instance().findStatisticsDetailByDate(getBeginDay(c), getEndDay(c));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<StatisticsDetail> getStatisticsDetailByDate(Timestamp t1, Timestamp t2) {
        List<StatisticsDetail> list = new ArrayList<>();

        try {
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(t1.getTime());

            Calendar c2 = Calendar.getInstance();
            c2.setTimeInMillis(t2.getTime());

            list = SQLiteManager.Instance().findStatisticsDetailByDateSort(getBeginDay(c), getEndDay(c2));
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
    public List<StatisticsDetail> getStatisticsDetailCurrentDate() {
        List<StatisticsDetail> list = new ArrayList<>();

        try {
            Calendar c = Calendar.getInstance();

            list = SQLiteManager.Instance().findStatisticsDetailByDate(getBeginDay(c), getEndDay(c));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public HistoricState findHistoricState(int id) {
        HistoricState historicState = null;

        try {
            historicState = SQLiteManager.Instance().findHistoricState(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return historicState;
    }


    private boolean isExistsUpdateAppTop(ArrayList<AppTop> a1, AppTop a2) {
        for (AppTop a : a1) {
            if (a.getId().equals(a2.getId()) && (a.getUpdated_at() != a2.getUpdated_at())) {
                try {
                    SQLiteManager.Instance().updateAppTop(a2);
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

    private boolean isExistsUpdateStatisticsDetail(ArrayList<StatisticsDetail> s1, StatisticsDetail s2) {
        Calendar c = Calendar.getInstance();
        Timestamp bTime;
        Timestamp eTime;
        for (StatisticsDetail s : s1) {
            c.setTimeInMillis(s.getCreated_at().getTime());

            bTime = getBeginDay(c);
            eTime = getEndDay(c);

            if (s.getNameApp().equalsIgnoreCase(s2.getNameApp()) && s.getCreated_at().after(bTime) && s.getCreated_at().before(eTime)) {
                try {
                    SQLiteManager.Instance().updateStatisticsDetail(s2, s);
                } catch (Exception e) {
                    e.printStackTrace();
                    return true;
                }
                return true;
            }
        }
        return false;
    }

    private Timestamp getBeginDay(Calendar c) {
        Converts.setTimeToBeginningOfDay(c);

        return new Timestamp(c.getTimeInMillis());
    }

    private Timestamp getEndDay(Calendar c) {
        Converts.setTimeToEndOfDay(c);

        return new Timestamp(c.getTimeInMillis());
    }
}
