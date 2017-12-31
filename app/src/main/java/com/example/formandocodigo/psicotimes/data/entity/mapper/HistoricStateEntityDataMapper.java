package com.example.formandocodigo.psicotimes.data.entity.mapper;

import com.example.formandocodigo.psicotimes.data.entity.HistoricStateEntity;
import com.example.formandocodigo.psicotimes.entity.HistoricState;
import com.example.formandocodigo.psicotimes.utils.Converts;

import java.text.ParseException;
import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by FormandoCodigo on 26/12/2017.
 */

public class HistoricStateEntityDataMapper {

    @Inject public HistoricStateEntityDataMapper() {}

    public HistoricState transform(HistoricStateEntity historicStateEntity) {
        HistoricState historicState = null;
        if (historicStateEntity != null) {
            historicState = new HistoricState();
            historicState.setId(historicStateEntity.getId());
            historicState.setNameAppTop(historicStateEntity.getNameTop());
            historicState.setQuantity(historicStateEntity.getQuantity());
            historicState.setTimeUse(historicStateEntity.getTimeUse());
            try {
                historicState.setCreated_at(Converts.convertStringToTimestamp(historicStateEntity.getCreated_at()));
                historicState.setUpdated_at(Converts.convertStringToTimestamp(historicStateEntity.getUpdated_at()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return historicState;
    }

    public ArrayList<HistoricState> transformArrayList(ArrayList<HistoricStateEntity> historicStateEntityCollections) {
        final ArrayList<HistoricState> historicStateList = new ArrayList<>();
        for (HistoricStateEntity historicStateEntity: historicStateEntityCollections) {
            final HistoricState historicState = transform(historicStateEntity);
            if (historicState != null) {
                historicStateList.add(historicState);
            }
        }
        return historicStateList;
    }
}
