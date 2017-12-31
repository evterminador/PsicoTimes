package com.example.formandocodigo.psicotimes.entity;

import java.sql.Timestamp;

/**
 * Created by FormandoCodigo on 13/12/2017.
 */

public class StateUser {
    private Integer appId;
    private Integer userId;
    private Long timeUse;
    private Integer quantity;
    private Timestamp lastUseTime;
    private Timestamp created_at;
    private Timestamp updated_at;

    /* Constructor */
    public StateUser() {
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

    public Timestamp getLastUseTime() {
        return lastUseTime;
    }

    public void setLastUseTime(Timestamp lastUseTime) {
        this.lastUseTime = lastUseTime;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public Timestamp getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Timestamp updated_at) {
        this.updated_at = updated_at;
    }
}
