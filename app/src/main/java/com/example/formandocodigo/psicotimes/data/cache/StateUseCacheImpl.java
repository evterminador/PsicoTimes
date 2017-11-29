package com.example.formandocodigo.psicotimes.data.cache;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.formandocodigo.psicotimes.utils.CompareTwoStateArrayList;
import com.example.formandocodigo.psicotimes.utils.Utils;
import com.example.formandocodigo.psicotimes.data.entity.StateUseEntity;
import com.example.formandocodigo.psicotimes.data.cache.serializer.Serializer;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by FormandoCodigo on 29/11/2017.
 */

public class StateUseCacheImpl implements StateUseCache {

    private static final String SETTINGS_FILE_NAME = "com.example.formandocodigo.psicotimes.SETTINGS";
    private static final String DEFAULT_FILE_NAME = "state_";
    private static final String DEFAULT_PREFERENCES_NAME = "states";

    private static final String SETTINGS_KEY_LAST_CACHE_UPDATE = "last_cache_update";
    private static final long EXPIRATION_TIME = 60 * 10 * 1000;

    private final Context context;
    private final File cacheDir;
    private final Serializer serializer;
    private final FileManager fileManager;

    @Inject
    public StateUseCacheImpl(Context context, Serializer serializer,
                             FileManager fileManager) {
        if (context == null || serializer == null || fileManager == null) {
            throw new IllegalArgumentException("Invalid null parameter");
        }
        this.context = context.getApplicationContext();
        this.cacheDir = this.context.getCacheDir();
        this.serializer = serializer;
        this.fileManager = fileManager;
    }

    @Override
    public Observable<StateUseEntity> get(int stateUseId) {
        return null;
    }

    public ArrayList<StateUseEntity> getAll() {
        String stateUses = fileManager.getFromPreferencesStates(context, DEFAULT_FILE_NAME, DEFAULT_PREFERENCES_NAME);
        return this.serializer.deserializeAll(stateUses);
    }

    @Override
    public void put(StateUseEntity stateUseEntity) {

    }

    @Override
    public void putAll(ArrayList<StateUseEntity> stateUseEntity) {
        if (stateUseEntity != null) {
            //final File userEntityFile = this.buildFile(stateUseEntity);
            String value = getAppLast();
            // In process implementation
            if (value != null) {
                ArrayList<StateUseEntity> stateUseEntities = this.serializer.deserializeAll(value);

                CompareTwoStateArrayList comparar = new CompareTwoStateArrayList(stateUseEntity, stateUseEntities);

                ArrayList<StateUseEntity> newArray = comparar.fix();
            }

            final String jsonString = this.serializer.serializeAll(stateUseEntity);

            setLastCacheUpdateTimeMillis();

            fileManager.writeToPreferences(context, DEFAULT_FILE_NAME, DEFAULT_PREFERENCES_NAME, jsonString);

            /*if (!isCached(stateUseEntity.getId())) {
                new CacheWriter(this.fileManager, userEntityFile, jsonString);
            }*/
        }
    }

    private String getAppLast() {
        final SharedPreferences sharedPreferences = context.getSharedPreferences(DEFAULT_FILE_NAME,
                Context.MODE_PRIVATE);
        return sharedPreferences.getString(DEFAULT_PREFERENCES_NAME, null);
    }



    @Override
    public boolean isCached(int stateUseId) {
        return false;
    }

    @Override
    public boolean isExpired() {
        long currentTime = System.currentTimeMillis();
        long lastUpdateTime = this.getLastCacheUpdateTimeMillis();

        boolean expired = ((currentTime - lastUpdateTime) > EXPIRATION_TIME);

        if (expired) {
            // Hacer otro metodo
            this.evictAll();
        }

        return expired;
    }

    @Override
    public void evictAll() {

    }

    /*public boolean isCached(int stateUseId) {
        final File userEntityFile = this.buildFile(stateUseId);
        return this.fileManager.exists(userEntityFile);
    }*/

    /**
     * Build a file, used to be inserted in the disk cache.
     *
     * @param stateUses The id user to build the file.
     * @return A valid file.
     */
    private File buildFile(List<StateUseEntity> stateUses) {
        final StringBuilder fileNameBuilder = new StringBuilder();
        fileNameBuilder.append(this.cacheDir.getPath());
        fileNameBuilder.append(File.separator);
        fileNameBuilder.append(DEFAULT_FILE_NAME);
        fileNameBuilder.append(stateUses);

        return new File(fileNameBuilder.toString());
    }

    /**
     * Set in millis, the last time the cache was accessed.
     */
    private void setLastCacheUpdateTimeMillis() {
        final long currentMillis = System.currentTimeMillis();
        this.fileManager.writeToPreferences(this.context, SETTINGS_FILE_NAME,
                SETTINGS_KEY_LAST_CACHE_UPDATE, currentMillis);
    }

    /**
     * Get in millis, the last time the cache was accessed.
     */
    public long getLastCacheUpdateTimeMillis() {
        return this.fileManager.getFromPreferences(this.context, SETTINGS_FILE_NAME,
                SETTINGS_KEY_LAST_CACHE_UPDATE);
    }

    private static class CacheWriter implements Runnable {
        private final FileManager fileManager;
        private final File fileToWrite;
        private final String fileContent;

        CacheWriter(FileManager fileManager, File fileToWrite, String fileContent) {
            this.fileManager = fileManager;
            this.fileToWrite = fileToWrite;
            this.fileContent = fileContent;
        }

        @Override
        public void run() {
            this.fileManager.writeToFile(fileToWrite, fileContent);
        }
    }
}
