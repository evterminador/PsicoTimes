package com.example.formandocodigo.psicotimes.data.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by FormandoCodigo on 26/12/2017.
 */

public class HistoricStateEntity {

    @SerializedName("id")
    private int id;

    @SerializedName("app_top")
    private String nameTop;

    @SerializedName("quantity_app_use")
    private int quantity;

    @SerializedName("time_use")
    private long timeUse;

    @SerializedName("created_at")
    private String created_at;

    @SerializedName("updated_at")
    private String updated_at;

    public HistoricStateEntity() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameTop() {
        return nameTop;
    }

    public void setNameTop(String nameTop) {
        this.nameTop = nameTop;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public long getTimeUse() {
        return timeUse;
    }

    public void setTimeUse(long timeUse) {
        this.timeUse = timeUse;
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
