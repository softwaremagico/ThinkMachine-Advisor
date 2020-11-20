package com.softwaremagico.tm.advisor.core;

import android.net.Uri;

import com.softwaremagico.tm.advisor.log.AdvisorLog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public final class FileUtils {

    public static String readFile(Uri uri) {
        //Get the text file
        File file = new File(uri.getPath());

        //Read text from file
        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
            }
            br.close();
        } catch (IOException e) {
            AdvisorLog.errorMessage(FileUtils.class.getName(), e);
        }
        return text.toString();
    }
}
