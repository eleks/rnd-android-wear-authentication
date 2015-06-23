package com.eleks.securedatastorage.sdk.interfaces;

import java.util.List;

/**
 * Created by Serhiy.Krasovskyy on 23.06.2015.
 */
public interface OnGetDeviceList {
    void foundDevices(final List<String> devices);
}
