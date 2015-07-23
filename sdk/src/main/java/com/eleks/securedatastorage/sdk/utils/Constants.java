package com.eleks.securedatastorage.sdk.utils;

/**
 * Created by Sergey on 09.04.2015.
 */
@SuppressWarnings("SpellCheckingInspection")
public class Constants {

    public static class Security {
        public static final String SECURED_FILE_NAME = "SecuredInfo.dat";
        public static final int INITIAL_VECTOR_LENGTH = 16;
        public static final int MAX_BYTE_VALUE = 255;
        public static final int SALT_LENGTH = 50;
        public static final String SALT =
                "UFlopGJQgp" +
                        "eeXjGwlGTi" +
                        "6MY6tC6Pxj" +
                        "J31veOrdL4" +
                        "356mArKLq3";
        public static final String ATTRIBUTES_FILE_NAME = "Attributes.dat";
        public static final String DEFAULT_CHARSET_NAME = "UTF-8";
    }

    public static class AndroidWatch {
        public static final long CONNECTION_TIME_OUT_MS = 1000;
        public static final long DEVICE_OPERATION_DELAY = 30000;
    }

    public static class MockDevice {
        public static final String DEVICE_ID = "sdgdgghe13ks";
        public static final String KEY_FILE_NAME = "MockKey.dat";
    }
}
