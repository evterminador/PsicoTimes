package com.example.formandocodigo.psicotimes.data.net;

import com.example.formandocodigo.psicotimes.data.entity.StateUseEntity;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by FormandoCodigo on 21/11/2017.
 */

public interface RestApi {
    String API_BASE_URL =
            "https://127.0.0.1:83/api/applications/";

    /** Api url for getting all users */
    String API_URL_GET_USER_LIST = API_BASE_URL + "users.json";
    /** Api url for getting a user profile: Remember to concatenate id + 'json' */
    String API_URL_GET_USER_DETAILS = API_BASE_URL + "user_";

    /**
     * Retrieves an which will emit a List of {@link StateUseEntity}.
     */
    Observable<List<StateUseEntity>> stateUseList();

    /**
     * Retrieves an which will emit a {@link StateUseEntity}.
     *
     * @param stateUseId The user id used to get user data.
     */
    Observable<StateUseEntity> stateUseById(final int stateUseId);
}
