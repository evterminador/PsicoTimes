package com.example.formandocodigo.psicotimes.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.sql.Timestamp;

/**
 * Created by FormandoCodigo on 14/12/2017.
 */

public class StateUserEntity {
    @SerializedName("id_app")
    private Integer id_app;

    @SerializedName("id_users")
    private Integer id_users;

    @SerializedName("timeUse")
    private Long timeUse;

    @SerializedName("quantity")
    private Integer quantity;

    @SerializedName("lastUseTime")
    private String lastUseTime;

    @SerializedName("created_at")
    private String created_at;

    @SerializedName("updated_at")
    private String updated_at;

    public StateUserEntity() {
        super();
    }

    public Integer getId_app() {
        return id_app;
    }

    public void setId_app(Integer id_app) {
        this.id_app = id_app;
    }

    public Integer getId_users() {
        return id_users;
    }

    public void setId_users(Integer id_users) {
        this.id_users = id_users;
    }

    public Long getTimeUse() {
        return timeUse;
    }

    public void setTimeUse(Long timeUse) {
        this.timeUse = timeUse;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getLastUseTime() {
        return lastUseTime;
    }

    public void setLastUseTime(String lastUseTime) {
        this.lastUseTime = lastUseTime;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
