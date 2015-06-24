package com.eleks.securedatastorage.sdk.storage;

import com.eleks.securedatastorage.sdk.model.SecureAttributes;
import com.eleks.securedatastorage.sdk.security.SecurityKeyBuilder;

/**
 * Created by Serhiy.Krasovskyy on 24.06.2015.
 */
public class SecureAttributesBuilder {

    public static SecureAttributes build(SecurityKeyBuilder securityKeyBuilder, String deviceId) {
        SecureAttributes secureAttributes = new SecureAttributes();
        secureAttributes.setDeviceId(deviceId);
        secureAttributes.setInitialVector(securityKeyBuilder.getInitialVector());
        secureAttributes.setSalt(securityKeyBuilder.getSalt());
        secureAttributes.setPhoneHalfOfKey(securityKeyBuilder.getPhoneHalfOfKey());
        return secureAttributes;
    }
}
