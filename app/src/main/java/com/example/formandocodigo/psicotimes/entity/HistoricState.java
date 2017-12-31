package com.example.formandocodigo.psicotimes.entity;

import java.sql.Timestamp;

/**
 * Created by FormandoCodigo on 26/12/2017.
 */

public class    HistoricState {
    private Integer id;
    private String nameAppTop;
    private int quantity;
    private long timeUse;
    private Timestamp created_at;
    private Timestamp updated_at;

    public HistoricState() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNameAppTop() {
        return nameAppTop;
    }

    public void setNameAppTop(String nameAppTop) {
        this.nameAppTop = nameAppTop;
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
