package com.example.formandocodigo.psicotimes.data.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.sql.Timestamp;

/**
 * Created by FormandoCodigo on 14/12/2017.
 */

public class StateUserEntity {
    @SerializedName("app_id")
    private Integer appId;

    @SerializedName("user_id")
    private Integer userId;

    @SerializedName("time_use")
    private Long timeUse;

    @SerializedName("quantity")
    private Integer quantity;

    @SerializedName("last_use_time")
    private String lastUseTime;

    @SerializedName("created_at")
    private String created_at;

    @SerializedName("updated_at")
    private String updated_at;

    public StateUserEntity() {
        super();
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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
