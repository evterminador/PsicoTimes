package com.example.formandocodigo.psicotimes.data.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.formandocodigo.psicotimes.data.entity.StateUseEntity;
import com.example.formandocodigo.psicotimes.data.entity.mapper.StateUseEntityJsonMapper;
import com.example.formandocodigo.psicotimes.data.exception.NetworkConnectionException;

import java.net.MalformedURLException;
import java.util.List;

import io.reactivex.Observable;

/**
 * Created by FormandoCodigo on 21/11/2017.
 */

public class RestApiImpl implements RestApi {
    private final Context context;
    private final StateUseEntityJsonMapper stateUseEntityJsonMapper;

    public RestApiImpl(Context context, StateUseEntityJsonMapper stateUseEntityJsonMapper) {
        if (context == null || stateUseEntityJsonMapper == null) {
            throw new IllegalArgumentException("The constructor parameters cannot be null!!!");
        }
        this.context = context;
        this.stateUseEntityJsonMapper = stateUseEntityJsonMapper;
    }

    @Override
    public Observable<List<StateUseEntity>> stateUseList() {
        return Observable.create(emitter -> {
            if (isThereInternetConnection()) {
                try {
                    String responseStateUse = getStateUseFromApi();
                    if (responseStateUse != null) {
                        emitter.onNext(stateUseEntityJsonMapper.transformStateUseCollection(
                                responseStateUse));
                        emitter.onComplete();
                    } else {
                        emitter.onError(new NetworkConnectionException());
                    }
                } catch (Exception e) {
                    emitter.onError(new NetworkConnectionException(e.getCause()));
                }
            } else {
                emitter.onError(new NetworkConnectionException());
            }
        });
    }

    @Override
    public Observable<StateUseEntity> stateUseById(final int stateUseId) {
        return Observable.create(emitter -> {
            if (isThereInternetConnection()) {
                try {
                    String responseStateUseDetails = getStateUseDetailsFromApi(stateUseId);
                    if (responseStateUseDetails != null) {
                        emitter.onNext(stateUseEntityJsonMapper.transformStateUse(
                                responseStateUseDetails));
                        emitter.onComplete();
                    } else {
                        emitter.onError(new NetworkConnectionException());
                    }
                } catch (Exception e) {
                    emitter.onError(new NetworkConnectionException(e.getCause()));
                }
            } else {
                emitter.onError(new NetworkConnectionException());
            }
        });
    }


    private String getStateUseFromApi() throws MalformedURLException {
        return ApiConnection.createGET(API_URL_GET_USER_LIST).requestSyncCall();
    }

    private String getStateUseDetailsFromApi(int userId) throws MalformedURLException {
        String apiUrl = API_URL_GET_USER_DETAILS + userId + ".json";
        return ApiConnection.createGET(apiUrl).requestSyncCall();
    }

    /**
     * Checks if the device has any active internet connection.
     *
     * @return true device with internet connection, otherwise false.
     */
    private boolean isThereInternetConnection() {
        boolean isConnected;

        ConnectivityManager connectivityManager =
                (ConnectivityManager) this.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        isConnected = (networkInfo != null && networkInfo.isConnectedOrConnecting());

        return isConnected;
    }
}