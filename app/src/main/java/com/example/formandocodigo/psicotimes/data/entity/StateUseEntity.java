package com.example.formandocodigo.psicotimes.data.entity;

import com.google.gson.annotations.SerializedName;

import java.sql.Timestamp;

/**
 * Created by FormandoCodigo on 29/11/2017.
 */

public class StateUseEntity {
    @SerializedName("id")
    private Integer id;

    @SerializedName("nameApplication")
    private String nameApplication;

    @SerializedName("useTime")
    private Long useTime;

    @SerializedName("lastUseTime")
    private Timestamp lastUseTime;

    @SerializedName("quantity")
    private Integer quantity;

    @SerializedName("created_at")
    private Timestamp created_at;

    @SerializedName("updated_at")
    private Timestamp updated_at;

    public StateUseEntity() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNameApplication() {
        return nameApplication;
    }

    public void setNameApplication(String nameApplication) {
        this.nameApplication = nameApplication;
    }

    public Long getUseTime() {
        return useTime;
    }

    public void setUseTime(Long useTime) {
        this.useTime = useTime;
    }

    public Timestamp getLastUseTime() {
        return lastUseTime;
    }

    public void setLastUseTime(Timestamp lastUseTime) {
        this.lastUseTime = lastUseTime;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
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
