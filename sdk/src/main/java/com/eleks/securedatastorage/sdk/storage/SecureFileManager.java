package com.eleks.securedatastorage.sdk.storage;

import android.content.Context;
import android.text.TextUtils;

import com.eleks.securedatastorage.sdk.model.DataFile;
import com.eleks.securedatastorage.sdk.model.DataHolder;
import com.eleks.securedatastorage.sdk.model.EntityHolder;
import com.eleks.securedatastorage.sdk.model.SecureAttributes;
import com.eleks.securedatastorage.sdk.security.Encryption;
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

    private final File mSecuredFile;
    private final SecureAttributes mSecureAttributes;

    public SecureFileManager(Context context, SecureAttributes secureAttributes) {
        this.mSecureAttributes = secureAttributes;
        mSecuredFile = new File(context.getFilesDir(), Constants.Security.SECURED_FILE_NAME);
    }

    public void storeData(final ArrayList<EntityHolder> entities) {
        if (entities != null) {
            DataHolder dataHolder;
            if (mSecuredFile.exists() && isSecuredFileCorrect(mSecuredFile)) {
                dataHolder = getDecryptedData(mSecuredFile);
            } else {
                dataHolder = new DataHolder();
            }
            for (EntityHolder entity : entities) {
                dataHolder.setEntity(entity.getEntityName(), entity.getEntityValue());
            }
            String json = getJsonFromObject(dataHolder);
            String encryptedData = encryptData(json);
            DataFile dataFile = new DataFile(encryptedData);
            json = getJsonFromObject(dataFile);
            try {
                IOHelper.writeFileSources(mSecuredFile, json);
            } catch (IOException e) {
                //do nothing
            }
        }
    }

    @SuppressWarnings("ConstantConditions")
    private String encryptData(String data) {
        Encryption encryption = Encryption.getDefault(
                mSecureAttributes.getSalt(), mSecureAttributes.getInitialVector());
        return encryption.encryptOrNull(mSecureAttributes.getSecretKey(), data);
    }

    public String getData(String entityName) {
        String result = null;
        if (mSecuredFile.exists() && isSecuredFileCorrect(mSecuredFile)) {
            DataHolder data = getDecryptedData(mSecuredFile);
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

    @SuppressWarnings("ConstantConditions")
    private String decryptData(String data) {
        String result = null;
        if (mSecureAttributes != null && mSecureAttributes.getSecretKey() != null) {
            Encryption encryption = Encryption.getDefault(
                    mSecureAttributes.getSalt(), mSecureAttributes.getInitialVector());
            result = encryption.decryptOrNull(mSecureAttributes.getSecretKey(), data);
        } else {
            //TODO need to process
        }
        return result;
    }

    private DataHolder getDataHolder(String decryptedData) {
        DataHolder result = null;
        if (!TextUtils.isEmpty(decryptedData)) {
            result = (DataHolder) getObjectFromJson(decryptedData, DataHolder.class);
        }
        return result;
    }

    private boolean isSecuredFileCorrect(File securedFile) {
        boolean result = false;
        try {
            String fileSources = IOHelper.getFileSources(securedFile);
            DataFile dataFile = (DataFile) getObjectFromJson(fileSources, DataFile.class);
            if (dataFile != null && dataFile.isDataValid()) {
                result = true;
            }
        } catch (IOException e) {
            //do nothing
        }
        return result;
    }

    private String getJsonFromObject(Object object) {
        String result = null;
        if (object != null) {
            Gson gson = new Gson();
            result = gson.toJson(object);
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
