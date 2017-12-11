package com.example.formandocodigo.psicotimes.data.entity.mapper;

import com.example.formandocodigo.psicotimes.entity.StateUse;
import com.example.formandocodigo.psicotimes.data.entity.StateUseEntity;

import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Inject;

/**
 * Created by FormandoCodigo on 29/11/2017.
 */

public class StateUseEntityDataMapper {

    @Inject
    public StateUseEntityDataMapper () {}

    public StateUse transform(StateUseEntity stateUseEntity) {
        StateUse stateUse = null;
        if (stateUseEntity != null) {
            stateUse = new StateUse();
            stateUse.setId(stateUseEntity.getId());
            stateUse.setNameApplication(stateUseEntity.getNameApplication());
            stateUse.setUseTime(stateUseEntity.getUseTime());
            stateUse.setLastUseTime(stateUseEntity.getLastUseTime());
            stateUse.setQuantity(stateUseEntity.getQuantity());
            stateUse.setCreated_at(stateUseEntity.getCreated_at());
            stateUse.setUpdated_at(stateUseEntity.getUpdated_at());
        }

        return stateUse;
    }

    /**
     * Transform a List of {@link StateUseEntity} into a Collection of {@link StateUse}.
     *
     * @param stateUseEntityCollection Object Collection to be transformed.
     * @return {@link StateUse} if valid {@link StateUseEntity} otherwise null.
     */
    public ArrayList<StateUse> transform(Collection<StateUseEntity> stateUseEntityCollection) {
        final ArrayList<StateUse> stateUseList = new ArrayList<>();
        for (StateUseEntity stateUseEntity : stateUseEntityCollection) {
            final StateUse stateUse = transform(stateUseEntity);
            if (stateUse != null) {
                stateUseList.add(stateUse);
            }
        }
        return stateUseList;
    }

    public ArrayList<StateUse> transformArrayList(ArrayList<StateUseEntity> stateUseEntityCollection) {
        final ArrayList<StateUse> stateUseList = new ArrayList<>();
        for (StateUseEntity stateUseEntity : stateUseEntityCollection) {
            final StateUse stateUse = transform(stateUseEntity);
            if (stateUse != null) {
                stateUseList.add(stateUse);
            }
        }
        return stateUseList;
    }
}
