package com.example.formandocodigo.psicotimes.data.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by FormandoCodigo on 15/12/2017.
 */

public class AppEntity {
    @SerializedName("id")
    private Integer id;

    @SerializedName("name_application")
    private String name;

    @SerializedName("relevance")
    private Integer relevance;

    @SerializedName("image")
    private String image;

    @SerializedName("description")
    private String description;

    @SerializedName("created_at")
    private String created_at;

    @SerializedName("updated_at")
    private String updated_at;

    public AppEntity() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRelevance() {
        return relevance;
    }

    public void setRelevance(Integer relevance) {
        this.relevance = relevance;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
