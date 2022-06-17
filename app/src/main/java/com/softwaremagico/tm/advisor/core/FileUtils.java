package com.softwaremagico.tm.advisor.core;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.OpenableColumns;
import android.webkit.MimeTypeMap;

import com.softwaremagico.tm.advisor.log.AdvisorLog;
import com.softwaremagico.tm.log.MachineLog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

public final class FileUtils {
    public static final String CHARACTER_FILE_EXTENSION = ".tma";
    private static final String GOOGLE_DRIVE_HOST = "com.google.android.apps.docs.storage";

    private FileUtils() {

    }

    public static String readFile(final Context context, Uri uri) {
        //Get the text file
        if (Objects.equals(uri.getHost(), GOOGLE_DRIVE_HOST)) {
            return readFile(Uri.fromFile(downloadFile(context, uri)).getPath(), true);
        }
        return readFile(uri.getPath());
    }

    public static String readFile(String path) {
        return readFile(path, false);
    }

    public static String readFile(String path, boolean deleteOnRead) {
        //Get the text file
        File file = new File(path);

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
            AdvisorLog.warning(FileUtils.class.getName(), "Accessing to file '" + path + "'");
            AdvisorLog.errorMessage(FileUtils.class.getName(), e);
        }
        if (deleteOnRead) {
            if (file.delete()) {
                MachineLog.debug(FileUtils.class.getName(), "File deleted");
            }
        }
        return text.toString();
    }

    /**
     * Downloads a file from Google Drive.
     *
     * @param context the context.
     * @param uri     the URI of the file.
     * @return a temporal file.
     */
    public static File downloadFile(final Context context, final Uri uri) {
        ContentResolver contentResolver = context.getContentResolver();
        try {
            Cursor returnCursor =
                    contentResolver.query(uri, null, null, new String[]{
                            MimeTypeMap.getSingleton().getExtensionFromMimeType("jpg")}, null);
            int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
            returnCursor.moveToFirst();
            String fileName = returnCursor.getString(nameIndex);
            InputStream inputStream = contentResolver.openInputStream(uri);
            returnCursor.close();


            File tempFile = File.createTempFile(fileName, "");
            tempFile.deleteOnExit();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Files.copy(inputStream, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } else {
                //Copy as the old times...
                FileOutputStream out = new FileOutputStream(tempFile);
                byte[] buffer = new byte[102400];
                int len;
                while ((len = inputStream.read(buffer)) != -1) {
                    out.write(buffer, 0, len);
                }
                inputStream.close();
                out.close();
            }
            return tempFile;
        } catch (Exception e) {
            AdvisorLog.errorMessage(FileUtils.class.getName(), e);
        }
        return null;
    }
}
