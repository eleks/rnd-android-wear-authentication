package com.eleks.securedatastorage.sdk.model;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Serhiy.Krasovskyy on 17.06.2015.
 */
public class DataHolder {

    @SerializedName("Data")
    private EntityHolder[] mData;

    public String getEntityValue(String entityName) {
        String result = null;
        if (mData != null && !TextUtils.isEmpty(entityName)) {
            for (EntityHolder entityHolder : mData) {
                if (entityHolder.getEntityName().equals(entityName)) {
                    result = entityHolder.getEntityValue();
                    break;
                }
            }
        }
        return result;
    }

    public void setEntity(String entityName, String entityValue) {
        ArrayList<EntityHolder> dataList = getDataListFromDataArray(mData);
        if (dataList.contains(entityName)) {
            dataList = updateDataList(dataList, entityName, entityValue);
        } else {
            dataList.add(new EntityHolder(entityName, entityValue));
        }
        mData = dataList.toArray(new EntityHolder[dataList.size()]);
    }

    private ArrayList<EntityHolder> updateDataList(
            ArrayList<EntityHolder> dataList, String entityName, String entityValue) {
        boolean updated = false;
        for (EntityHolder entityHolder : dataList) {
            if (entityHolder.getEntityName().equals(entityName)) {
                updated = true;
                entityHolder.setEntityValue(entityValue);
                break;
            }
        }
        if (!updated) {
            dataList.add(new EntityHolder(entityName, entityValue));
        }
        return dataList;
    }

    private ArrayList<EntityHolder> getDataListFromDataArray(EntityHolder[] data) {
        ArrayList<EntityHolder> dataList;
        if (data != null) {
            dataList = new ArrayList<>(Arrays.asList(mData));
        } else {
            dataList = new ArrayList<>();
        }
        return dataList;
    }
}
