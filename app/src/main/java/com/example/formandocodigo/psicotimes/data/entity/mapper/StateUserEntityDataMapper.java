package com.example.formandocodigo.psicotimes.data.entity.mapper;

import com.example.formandocodigo.psicotimes.data.entity.StateUserEntity;
import com.example.formandocodigo.psicotimes.entity.StateUser;
import com.example.formandocodigo.psicotimes.utils.Converts;

import java.text.ParseException;
import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by FormandoCodigo on 14/12/2017.
 */

public class StateUserEntityDataMapper {

    @Inject public StateUserEntityDataMapper() {}

    public StateUser transform(StateUserEntity stateUserEntity) {
        StateUser stateUser = null;
        if (stateUserEntity != null) {
            stateUser = new StateUser();
            stateUser.setAppId(stateUserEntity.getAppId());
            stateUser.setUserId(stateUserEntity.getUserId());
            stateUser.setTimeUse(stateUserEntity.getTimeUse());
            stateUser.setQuantity(stateUserEntity.getQuantity());
            try {
                stateUser.setLastUseTime(Converts.convertStringToTimestamp(stateUserEntity.getLastUseTime()));
                stateUser.setCreated_at(Converts.convertStringToTimestamp(stateUserEntity.getCreated_at()));
                stateUser.setUpdated_at(Converts.convertStringToTimestamp(stateUserEntity.getUpdated_at()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return stateUser;
    }

    public ArrayList<StateUser> transformArrayList(ArrayList<StateUserEntity> stateUserEntityCollection) {
        final ArrayList<StateUser> stateUserList = new ArrayList<>();
        for (StateUserEntity stateUserEntity : stateUserEntityCollection) {
            final StateUser stateUser = transform(stateUserEntity);
            if (stateUser != null) {
                stateUserList.add(stateUser);
            }
        }
        return stateUserList;
    }
}
