package com.eleks.securedatastorage.utils;

/**
 * Created by Sergey on 09.04.2015.
 */
@SuppressWarnings("SpellCheckingInspection")
public class Constants {

    public static final String DEFAULT_ENCRYPTION_KEY = "erjkerjakl2qop12921ebjsaas;lor909054";
    public static final String DEFAULT_ENCRYPTION_SALT = "le3podfjknwegcxs   werojipreugyewffweq";
    public static final byte[] DEFAULT_ENCRYPTION_IV = new byte[16];

    public static class Extras {
        public static final String PASSWORD_ENTITY = "password";
        public static final String USER_NAME_ENTITY = "user_name";
    }
}
