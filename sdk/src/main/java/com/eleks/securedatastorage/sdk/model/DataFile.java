package com.eleks.securedatastorage.sdk.model;

import com.eleks.securedatastorage.sdk.security.Md5Checksum;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Serhiy.Krasovskyy on 17.06.2015.
 */
public class DataFile {
    @SerializedName("CheckSum")
    private String mCheckSum;
    @SerializedName("Data")
    private String mData;

    public DataFile(String data) {
        setData(data);
    }

    public String getData() {
        return mData;
    }

    public void setData(String data) {
        this.mData = data;
        this.mCheckSum = Md5Checksum.calculateMD5(data);
    }

    public boolean isDataValid() {
        return Md5Checksum.checkMD5(mCheckSum, mData);
    }
}
