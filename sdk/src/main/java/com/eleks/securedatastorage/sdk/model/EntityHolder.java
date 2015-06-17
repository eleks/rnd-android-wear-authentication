package com.eleks.securedatastorage.sdk.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Serhiy.Krasovskyy on 17.06.2015.
 */
public class EntityHolder {
    @SerializedName("EntityName")
    private String mEntityName;
    @SerializedName("EntityValue")
    private String mEntityValue;

    public EntityHolder(String entityName, String entityValue) {
        this.mEntityName = entityName;
        this.mEntityValue = entityValue;
    }

    public String getEntityName() {
        return mEntityName;
    }

    public String getEntityValue() {
        return mEntityValue;
    }

    public void setEntityValue(String entityValue) {
        this.mEntityValue = entityValue;
    }
}
