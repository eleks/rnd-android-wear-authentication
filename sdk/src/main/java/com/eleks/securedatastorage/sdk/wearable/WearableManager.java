package com.eleks.securedatastorage.sdk.wearable;

import android.content.Context;

import com.eleks.securedatastorage.sdk.interfaces.OnGetHalfOfKeyListener;
import com.eleks.securedatastorage.sdk.interfaces.WearableSecureDataInterface;

/**
 * Created by Serhiy.Krasovskyy on 19.06.2015.
 */
public class WearableManager {

    private final Context mContext;
    private final WearableSecureDataInterface mWearableInterface;
    private final OnGetHalfOfKeyListener mGetHalfOfKeyListener;

    public WearableManager(Context context, WearableSecureDataInterface wearableInterface,
                           OnGetHalfOfKeyListener getHalfOfKeyListener) {
        this.mContext = context;
        this.mWearableInterface = wearableInterface;
        this.mGetHalfOfKeyListener = getHalfOfKeyListener;
    }
}
