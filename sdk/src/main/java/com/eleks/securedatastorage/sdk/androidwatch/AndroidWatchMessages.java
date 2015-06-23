package com.eleks.securedatastorage.sdk.androidwatch;

/**
 * Created by Serhiy.Krasovskyy on 22.06.2015.
 */
public class AndroidWatchMessages {

    public static class Requests {
        public static final String SHOULD_USE_THIS_DEVICE_FOR_SECURE_STORAGE =
                "should_use_this_device_for_secure_storage_request";
        public static final String GET_DEVICE_HALF_OF_KEY = "get_device_half_of_key_request";
        public static final String SET_DEVICE_HALF_OF_KEY = "set_device_half_of_key_request";
    }

    public static class Responses {
        public static final String SHOULD_USE_THIS_DEVICE_FOR_SECURE_STORAGE =
                "should_use_this_device_for_secure_storage_response";
        public static final String GET_DEVICE_HALF_OF_KEY = "get_device_half_of_key_response";
        public static final String SET_DEVICE_HALF_OF_KEY = "set_device_half_of_key_response";
    }

}
