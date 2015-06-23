package com.eleks.securedatastorage.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Sergey on 22.06.2015.
 */
public class IOHelper1 {

    public static void writeFileSources(File file, byte[] data) {
        BufferedOutputStream outputStream = null;
        try {
            outputStream = new BufferedOutputStream(new FileOutputStream(file));
            outputStream.write(data);
            outputStream.flush();
        } catch (IOException e) {
            //do nothing
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    //do nothing
                }
            }
        }
    }

    public static byte[] loadFileSources(File file) {
        byte[] result = null;
        if (file.exists()) {
            result = new byte[(int) file.length()];
            BufferedInputStream inputStream = null;
            try {
                inputStream = new BufferedInputStream(new FileInputStream(file));
                //noinspection ResultOfMethodCallIgnored
                inputStream.read(result);
            } catch (IOException e) {
                result = null;
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        //do nothing
                    }
                }
            }
        }
        return result;
    }
}
