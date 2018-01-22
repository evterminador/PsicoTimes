package com.example.formandocodigo.psicotimes.domain;

import com.example.formandocodigo.psicotimes.data.disk.StateUseDisk;
import com.example.formandocodigo.psicotimes.data.disk.StateUseDiskImpl;
import com.example.formandocodigo.psicotimes.entity.AppTop;
import com.example.formandocodigo.psicotimes.entity.HistoricState;
import com.example.formandocodigo.psicotimes.entity.StateUse;
import com.example.formandocodigo.psicotimes.entity.StatisticsDetail;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by FormandoCodigo on 15/12/2017.
 */

public class StateUseCaseImpl implements StateUseCase {

    private StateUseDisk stateUseDisk;

    public StateUseCaseImpl() {
        stateUseDisk = new StateUseDiskImpl();
    }

    @Override
    public List<AppTop> getAppTopAll() {
        return stateUseDisk.getAppTopAll();
    }

    @Override
    public List<HistoricState> getHistoricStateAll() {
        return stateUseDisk.getHistoricStateAll();
    }

    @Override
    public HistoricState findHistoricState(int id) {
        return stateUseDisk.findHistoricState(id);
    }

    @Override
    public List<StatisticsDetail> getStatisticsDetailByDate(Timestamp date) {
        return stateUseDisk.getStatisticsDetailByDate(date);
    }

    @Override
    public List<StatisticsDetail> getStatisticsDetailByDate(Timestamp t1, Timestamp t2) {
        return stateUseDisk.getStatisticsDetailByDate(t1, t2);
    }

    @Override
    public List<StatisticsDetail> getStatisticsDetailCurrentDate() {
        return stateUseDisk.getStatisticsDetailCurrentDate();
    }

}
