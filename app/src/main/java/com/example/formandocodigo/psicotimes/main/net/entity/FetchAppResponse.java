package com.example.formandocodigo.psicotimes.main.net.entity;

import com.example.formandocodigo.psicotimes.entity.ApplicationEntity;
import com.squareup.moshi.Json;

import java.util.ArrayList;

/**
 * Created by FormandoCodigo on 13/12/2017.
 */

public class FetchAppResponse {

    @Json(name = "applications")
    ArrayList<ApplicationEntity> applications;

    public ArrayList<ApplicationEntity> getApplications() {
        return applications;
    }

    public void setApplications(ArrayList<ApplicationEntity> applications) {
        this.applications = applications;
    }
}
