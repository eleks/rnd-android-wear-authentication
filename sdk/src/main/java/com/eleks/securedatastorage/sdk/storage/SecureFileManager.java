package com.eleks.securedatastorage.sdk.storage;

import android.content.Context;
import android.text.TextUtils;

import com.eleks.securedatastorage.sdk.model.DataFile;
import com.eleks.securedatastorage.sdk.model.DataHolder;
import com.eleks.securedatastorage.sdk.model.EntityHolder;
import com.eleks.securedatastorage.sdk.model.SecureAttributes;
import com.eleks.securedatastorage.sdk.utils.Constants;
import com.eleks.securedatastorage.sdk.utils.IOHelper;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Serhiy.Krasovskyy on 18.06.2015.
 */
public class SecureFileManager {

    private final Context mContext;
    private final WearDeviceSecureDataInterface mWearInterface;
    private final SecureAttributes mSecureAttributes;

    public SecureFileManager(Context context, WearDeviceSecureDataInterface wearInterface) {
        this.mContext = context;
        this.mWearInterface = wearInterface;
        this.mSecureAttributes = getSecureAttributes();
    }

    private SecureAttributes getSecureAttributes() {
        File secureAttributesFile =
                new File(mContext.getFilesDir(), Constants.Security.ATTRIBUTES_FILE_NAME);
        SecureAttributes result = null;
        if (secureAttributesFile.exists()) {
            try {
                String fileSources = IOHelper.getFileSources(secureAttributesFile);
                result = (SecureAttributes) getObjectFromJson(fileSources, SecureAttributes.class);
            } catch (IOException e) {
                //TODO need to implement process of this exception
            }
        } else {
            result = new SecureAttributes(null);
        }
        return result;
    }

    public void storeData(ArrayList<EntityHolder> entities) {

    }

    public String getData(String entityName) {
        String result = null;
        File securedFile = new File(mContext.getFilesDir(), Constants.Security.SECURED_FILE_NAME);
        if (securedFile.exists() && securedFileIsCorrect(securedFile)) {
            DataHolder data = getDecryptedData(securedFile);
            result = data.getEntityValue(entityName);
        }
        return result;
    }

    private DataHolder getDecryptedData(File securedFile) {
        DataHolder result = null;
        try {
            String fileSources = IOHelper.getFileSources(securedFile);
            DataFile dataFile = (DataFile) getObjectFromJson(fileSources, DataFile.class);

            String decryptedData = decryptData(dataFile.getData());
            result = getDataHolder(decryptedData);
        } catch (IOException e) {
            //do nothing
        }
        return result;
    }

    private String decryptData(String data) {

    }

    private DataHolder getDataHolder(String decryptedData) {
        DataHolder result = null;
        if (!TextUtils.isEmpty(decryptedData)) {
            result = (DataHolder) getObjectFromJson(decryptedData, DataHolder.class);
        }
        return result;
    }

    private boolean securedFileIsCorrect(File securedFile) {
        boolean result = false;
        try {
            String fileSources = IOHelper.getFileSources(securedFile);
            DataFile dataFile = (DataFile) getObjectFromJson(fileSources, DataFile.class);
            if (dataFile.isDataValid()) {
                result = true;
            }
        } catch (IOException e) {
            //do nothing
        }
        return result;
    }

    private Object getObjectFromJson(String json, Class classOfT) {
        Gson gson = new Gson();
        Object result = null;
        try {
            result = gson.fromJson(json, classOfT);
        } catch (JsonSyntaxException e) {
            //do nothing
        }
        return result;
    }
}
