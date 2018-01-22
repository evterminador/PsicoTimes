package com.example.formandocodigo.psicotimes.utils.net;

import com.example.formandocodigo.psicotimes.post.net.HistoricStateOrder;
import com.example.formandocodigo.psicotimes.post.net.HistoricStateResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by FormandoCodigo on 03/12/2017.
 */

public interface OrderService {

    @POST("state/historic")
    Call<HistoricStateResponse> historicOrder(@Body HistoricStateOrder historicStateOrder);
}
