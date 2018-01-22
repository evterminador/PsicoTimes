package com.example.formandocodigo.psicotimes.domain;

import com.example.formandocodigo.psicotimes.entity.AppTop;
import com.example.formandocodigo.psicotimes.entity.HistoricState;
import com.example.formandocodigo.psicotimes.entity.StatisticsDetail;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by FormandoCodigo on 15/12/2017.
 */

public interface StateUseCase {

    List<AppTop> getAppTopAll();

    List<HistoricState> getHistoricStateAll();

    HistoricState findHistoricState(int id);

    List<StatisticsDetail> getStatisticsDetailByDate(Timestamp date);

    List<StatisticsDetail> getStatisticsDetailByDate(Timestamp t1, Timestamp t2);

    List<StatisticsDetail> getStatisticsDetailCurrentDate();
}
