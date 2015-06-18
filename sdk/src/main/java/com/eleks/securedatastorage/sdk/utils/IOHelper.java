package com.eleks.securedatastorage.sdk.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
}
