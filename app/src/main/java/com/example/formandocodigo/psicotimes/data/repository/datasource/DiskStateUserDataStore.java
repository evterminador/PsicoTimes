package com.example.formandocodigo.psicotimes.data.repository.datasource;

import com.example.formandocodigo.psicotimes.data.cache.StateUseCache;
import com.example.formandocodigo.psicotimes.data.entity.StateUseEntity;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by FormandoCodigo on 21/11/2017.
 */

public class DiskStateUserDataStore implements StateUseDataStore {
    private final StateUseCache stateUseCache;

    public DiskStateUserDataStore(StateUseCache stateUseCache) {
        this.stateUseCache = stateUseCache;
    }


    @Override
    public Observable<List<StateUseEntity>> stateUseEntityList() {
        // Implementar
        throw new UnsupportedOperationException("Operation is not available!!!");
    }

    @Override
    public Observable<StateUseEntity> stateUseEntityDetails(Integer stateUseId) {
        return this.stateUseCache.get(stateUseId);
    }
}