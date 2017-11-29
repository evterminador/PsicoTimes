package com.example.formandocodigo.psicotimes.data.repository.datasource;

import com.example.formandocodigo.psicotimes.data.cache.StateUseCache;
import com.example.formandocodigo.psicotimes.data.entity.StateUseEntity;
import com.example.formandocodigo.psicotimes.data.net.RestApi;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by FormandoCodigo on 21/11/2017.
 */

public class CloudStateUseDataStore implements StateUseDataStore {

    private final RestApi restApi;
    private final StateUseCache stateUseCache;

    /**
     * Construct a {@link StateUseDataStore} based on connections to the api (Cloud).
     *
     * @param restApi The {@link RestApi} implementation to use.
     * @param stateUseCache A {@link StateUseCache} to cache data retrieved from the api.
     */
    CloudStateUseDataStore(RestApi restApi, StateUseCache stateUseCache) {
        this.restApi = restApi;
        this.stateUseCache = stateUseCache;
    }

    @Override
    public Observable<List<StateUseEntity>> stateUseEntityList() {
        return this.restApi.stateUseList();
    }

    @Override
    public Observable<StateUseEntity> stateUseEntityDetails(Integer stateUseId) {
        return this.restApi.stateUseById(stateUseId).doOnNext(CloudStateUseDataStore.this.stateUseCache::put);
    }
}