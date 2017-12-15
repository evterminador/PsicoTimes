package com.example.formandocodigo.psicotimes.entity;

import java.sql.Timestamp;

/**
 * Created by FormandoCodigo on 13/12/2017.
 */

public class StateUser {
    private Integer id_app;
    private Integer id_users;
    private Long timeUse;
    private Integer quantity;
    private Timestamp lastUseTime;
    private Timestamp created_at;
    private Timestamp updated_at;

    /* Constructor */
    public StateUser() {
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
