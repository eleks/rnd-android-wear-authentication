package com.eleks.securedatastorage.sdk.utils;

/**
 * Created by Sergey on 09.04.2015.
 */
@SuppressWarnings("SpellCheckingInspection")
public class Constants {

    public static final String DEFAULT_ENCRYPTION_KEY = "erjkerjakl2qop12921ebjsaas;lor909054";
    public static final String DEFAULT_ENCRYPTION_SALT = "le3podfjknwegcxs   werojipreugyewffweq";
    public static final byte[] DEFAULT_ENCRYPTION_IV = new byte[16];
    public static final String DEFAULT_CHARSET_NAME = "UTF-8";

    public static final String TAG = "secure_storage";


    public static class Preferences {
        public static final String SHOULD_USE_SECURE_STORAGE = "should_use_secure_storage";
        public static final int UNSPECIFIED = -1;
        public static final int SHOULD_USE = 1;
        public static final int DONT_USE = 0;
    }

    public static class Security {

        public static final String SECURED_FILE_NAME = "SecuredInfo.dat";
        public static final int INITIAL_VECTOR_LENGTH = 16;
        public static final int MAX_BYTE_VALUE = 255;
        public static final int SALT_LENGTH = 50;
        public static final String ATTRIBUTES_FILE_NAME = "Attributes.dat";
    }

    public static class G_WATCH {
        public static final long CONNECTION_TIME_OUT_MS = 1000;
    }
}
