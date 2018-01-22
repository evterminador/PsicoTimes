package com.example.formandocodigo.psicotimes.data.disk;

import com.example.formandocodigo.psicotimes.entity.AppTop;
import com.example.formandocodigo.psicotimes.entity.HistoricState;
import com.example.formandocodigo.psicotimes.entity.StatisticsDetail;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by FormandoCodigo on 13/12/2017.
 */

public interface StateUseDisk {
    Integer putAppTopAll(ArrayList<AppTop> applicationList);

    Integer putHistoricStateAll(ArrayList<HistoricState> historicStateList);

    Integer putStatisticsDetailAll(ArrayList<StatisticsDetail> statisticsDetails);

    List<AppTop> getAppTopAll();

    List<StatisticsDetail> getStatisticsDetailByDate(Timestamp date);

    List<StatisticsDetail> getStatisticsDetailByDate(Timestamp t1, Timestamp t2);

    List<HistoricState> getHistoricStateAll();

    List<StatisticsDetail> getStatisticsDetailCurrentDate();

    HistoricState findHistoricState(int id);
}
