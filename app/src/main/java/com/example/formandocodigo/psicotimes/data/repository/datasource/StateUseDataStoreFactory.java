package com.example.formandocodigo.psicotimes.data.repository.datasource;

import android.content.Context;

import com.example.formandocodigo.psicotimes.data.entity.mapper.StateUseEntityJsonMapper;
import com.example.formandocodigo.psicotimes.data.cache.StateUseCache;
import com.example.formandocodigo.psicotimes.data.net.RestApi;
import com.example.formandocodigo.psicotimes.data.net.RestApiImpl;

/**
 * Created by FormandoCodigo on 21/11/2017.
 */

public class StateUseDataStoreFactory {

    private final Context context;
    private final StateUseCache stateUseCache;

    public StateUseDataStoreFactory(Context context, StateUseCache stateUseCache) {
        this.context = context;
        this.stateUseCache = stateUseCache;
    }

    /*Create (@link StateUseDataStoreFactory) from */
    public StateUseDataStore create(final int stateUseId) {
        StateUseDataStore useStore;

        if (this.stateUseCache.isExpired() && this.stateUseCache.isCached(stateUseId)){
            useStore = new DiskStateUserDataStore(this.stateUseCache);
        } else {
            useStore = createCloudUseStateStore();
        }

        return useStore;
    }

    public StateUseDataStore createCloudUseStateStore() {
        final StateUseEntityJsonMapper userEntityJsonMapper = new StateUseEntityJsonMapper();
        final RestApi restApi = new RestApiImpl(this.context, userEntityJsonMapper);

        return new CloudStateUseDataStore(restApi, this.stateUseCache);
    }
}
