package com.eleks.securedatastorage.sdk.storage;

import android.content.Context;
import android.text.TextUtils;

import com.eleks.securedatastorage.sdk.interfaces.OnGetHalfOfKeyListener;
import com.eleks.securedatastorage.sdk.interfaces.WearableDeviceError;
import com.eleks.securedatastorage.sdk.interfaces.WearableSecureDataInterface;
import com.eleks.securedatastorage.sdk.model.DataFile;
import com.eleks.securedatastorage.sdk.model.DataHolder;
import com.eleks.securedatastorage.sdk.model.EntityHolder;
import com.eleks.securedatastorage.sdk.model.SecureAttributes;
import com.eleks.securedatastorage.sdk.security.Encryption;
import com.eleks.securedatastorage.sdk.utils.Constants;
import com.eleks.securedatastorage.sdk.utils.IOHelper;
import com.eleks.securedatastorage.sdk.wearable.WearableManager;
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
    private final WearableSecureDataInterface mWearableInterface;
    private final SecureAttributes mSecureAttributes;

    public SecureFileManager(Context context, WearableSecureDataInterface wearableInterface) {
        this.mContext = context;
        this.mWearableInterface = wearableInterface;
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

    public void storeData(final ArrayList<EntityHolder> entities) {
        if (mSecureAttributes.getSecretKey() == null) {
            new WearableManager(mContext, mWearableInterface)
                    .getDeviceHalfOfKey(null, new OnGetHalfOfKeyListener() {
                        @Override
                        public void getError(WearableDeviceError error, String errorMessage) {

                        }

                        @Override
                        public void OnGetHalfOfKey(byte[] deviceHalfOfKey) {
                            if (deviceHalfOfKey != null) {
                                mSecureAttributes.setDeviceHalfOfKey(deviceHalfOfKey);
                                storeData(entities);
                            } else {
                                //TODO process it
                            }
                        }
                    });
        } else {
            //TODO process it
        }
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
