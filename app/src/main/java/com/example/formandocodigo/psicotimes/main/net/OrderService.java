package com.example.formandocodigo.psicotimes.main.net;

import com.example.formandocodigo.psicotimes.main.net.entity.AppOrderResponse;
import com.example.formandocodigo.psicotimes.main.net.entity.StateUserOrder;
import com.example.formandocodigo.psicotimes.main.net.entity.StateUserOrderResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by FormandoCodigo on 03/12/2017.
 */

public interface OrderService {

    @POST("state/user")
    Call<StateUserOrderResponse> stateUserOrder(@Body StateUserOrder stateUserOrder);

    @GET("fetch/app")
    Call<AppOrderResponse> appOrder();
}
