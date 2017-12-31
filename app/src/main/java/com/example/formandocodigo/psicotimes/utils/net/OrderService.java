package com.example.formandocodigo.psicotimes.utils.net;

import com.example.formandocodigo.psicotimes.main.net.AppOrderResponse;
import com.example.formandocodigo.psicotimes.main.net.StateUserOrder;
import com.example.formandocodigo.psicotimes.main.net.StateUserOrderResponse;
import com.example.formandocodigo.psicotimes.post.net.HistoricStateOrder;
import com.example.formandocodigo.psicotimes.post.net.HistoricStateResponse;

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

    @POST("state/historic")
    Call<HistoricStateResponse> historicOrder(@Body HistoricStateOrder historicStateOrder);
}
