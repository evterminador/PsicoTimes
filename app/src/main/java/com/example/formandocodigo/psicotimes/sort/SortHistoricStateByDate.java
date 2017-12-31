package com.example.formandocodigo.psicotimes.sort;

import com.example.formandocodigo.psicotimes.entity.HistoricState;

import java.util.Comparator;

/**
 * Created by FormandoCodigo on 26/12/2017.
 */

public class SortHistoricStateByDate implements Comparator<HistoricState> {

    @Override
    public int compare(HistoricState h0, HistoricState h1) {
        return h0.getCreated_at().compareTo(h1.getCreated_at());
    }

}
