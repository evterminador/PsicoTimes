package com.example.formandocodigo.psicotimes.data.repository;

import com.example.formandocodigo.psicotimes.data.entity.mapper.StateUseEntityDataMapper;
import com.example.formandocodigo.psicotimes.data.repository.datasource.StateUseDataStore;
import com.example.formandocodigo.psicotimes.data.repository.datasource.StateUseDataStoreFactory;
import com.example.formandocodigo.psicotimes.model.StateUse;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

/**
 * Created by FormandoCodigo on 22/11/2017.
 */

@Singleton
public class StateUseDataRepository {

    private final StateUseDataStoreFactory stateUseDataStoreFactory;
    private final StateUseEntityDataMapper stateUseEntityDataMapper;

    @Inject
    StateUseDataRepository(StateUseDataStoreFactory stateUseDataStoreFactory,
                           StateUseEntityDataMapper stateUseEntityDataMapper) {
        this.stateUseDataStoreFactory = stateUseDataStoreFactory;
        this.stateUseEntityDataMapper = stateUseEntityDataMapper;
    }


    public Observable<List<StateUse>> statesUses() {
        final StateUseDataStore stateUseDataStore = this.stateUseDataStoreFactory.createCloudUseStateStore();
        return stateUseDataStore.stateUseEntityList().map(this.stateUseEntityDataMapper::transform);
    }


    public Observable<StateUse> stateUse(int stateUseId) {
        final StateUseDataStore stateUseDataStore = this.stateUseDataStoreFactory.create(stateUseId);
        return stateUseDataStore.stateUseEntityDetails(stateUseId).map(this.stateUseEntityDataMapper::transform);
    }
}