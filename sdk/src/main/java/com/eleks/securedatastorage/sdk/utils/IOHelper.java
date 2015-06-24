package com.eleks.securedatastorage.sdk.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Serhiy.Krasovskyy on 18.06.2015.
 */
public class IOHelper {

    public static String getFileSources(File file) throws IOException {
        StringBuilder text = new StringBuilder();

        if (file.exists()) {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
            }
        }
        return text.toString();
    }

    public static byte[] getFileSourcesToByteArray(File file) {
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

    public static void writeFileSources(File file, String sources) throws IOException {
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(sources);
        fileWriter.close();
    }

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
}
