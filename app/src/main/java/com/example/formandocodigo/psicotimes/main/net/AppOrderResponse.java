package com.example.formandocodigo.psicotimes.main.net;

import com.example.formandocodigo.psicotimes.data.entity.AppEntity;
import com.squareup.moshi.Json;

import java.util.List;

/**
 * Created by FormandoCodigo on 15/12/2017.
 */

public class AppOrderResponse {

    @Json(name = "applications")
    List<AppEntity> applications;

    public List<AppEntity> getApplications() {
        return applications;
    }

    public void setApplications(List<AppEntity> applications) {
        this.applications = applications;
    }
}
