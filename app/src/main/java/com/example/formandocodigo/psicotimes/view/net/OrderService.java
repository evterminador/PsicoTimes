package com.example.formandocodigo.psicotimes.view.net;

import com.example.formandocodigo.psicotimes.view.net.entity.AppOrder;
import com.example.formandocodigo.psicotimes.view.net.entity.AppOrderResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by FormandoCodigo on 03/12/2017.
 */

public interface OrderService {

    @POST("app")
    Call<AppOrderResponse> appOrder(@Body AppOrder appOrder);


}
