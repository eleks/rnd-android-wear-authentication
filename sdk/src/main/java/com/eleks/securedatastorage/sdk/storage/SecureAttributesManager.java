package com.eleks.securedatastorage.sdk.storage;

import android.content.Context;

import com.eleks.securedatastorage.sdk.model.SecureAttributes;
import com.eleks.securedatastorage.sdk.utils.Constants;
import com.eleks.securedatastorage.sdk.utils.IOHelper;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.File;
import java.io.IOException;

/**
 * Created by Serhiy.Krasovskyy on 24.06.2015.
 */
public class SecureAttributesManager {

    public static void storeSecureAttributes(Context context, SecureAttributes secureAttributes)
            throws IOException {
        Gson gson = new Gson();
        String json = gson.toJson(secureAttributes);
        File secureAttributesFile =
                new File(context.getFilesDir(), Constants.Security.ATTRIBUTES_FILE_NAME);
        IOHelper.writeFileSources(secureAttributesFile, json);
    }

    public static SecureAttributes loadSecureAttributes(Context context) {
        File secureAttributesFile =
                new File(context.getFilesDir(), Constants.Security.ATTRIBUTES_FILE_NAME);
        SecureAttributes result = null;
        if (secureAttributesFile.exists()) {
            try {
                String json = IOHelper.getFileSources(secureAttributesFile);
                Gson gson = new Gson();
                result = gson.fromJson(json, SecureAttributes.class);
            } catch (IOException | JsonSyntaxException e) {
                //do nothing
            }
        }
        return result;
    }
}
