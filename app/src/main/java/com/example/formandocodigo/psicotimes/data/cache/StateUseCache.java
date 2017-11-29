package com.example.formandocodigo.psicotimes.data.cache;

import android.app.usage.UsageStats;

import com.example.formandocodigo.psicotimes.data.entity.StateUseEntity;

import java.util.ArrayList;

import io.reactivex.Observable;

/**
 * Created by FormandoCodigo on 29/11/2017.
 */

public interface StateUseCache {
    /**
     * Gets an {@link Observable} which will emit a {@link StateUseEntity}.
     *
     * @param stateUseId The user id to retrieve data.
     */
    Observable<StateUseEntity> get(final int stateUseId);

    /**
     * Puts and element into the cache.
     *
     * @param stateUseEntity Element to insert in the cache.
     */
    void put(StateUseEntity stateUseEntity);

    void putAll(ArrayList<StateUseEntity> stateUseEntities);

    /**
     * Checks if an element (User) exists in the cache.
     *
     * @param stateUseId The id used to look for inside the cache.
     * @return true if the element is cached, otherwise false.
     */
    boolean isCached(final int stateUseId);

    /**
     * Checks if the cache is expired.
     *
     * @return true, the cache is expired, otherwise false.
     */
    boolean isExpired();

    /**
     * Evict all elements of the cache.
     */
    void evictAll();
}
