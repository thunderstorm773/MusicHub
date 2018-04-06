package com.softuni.musichub.util;

import com.softuni.musichub.staticData.Constants;
import org.springframework.stereotype.Component;
import java.io.*;

@Component
public class FileUtil {

    private static final String PERSISTED_FILES_FOLDER = "temp";

    private static final String PERSISTED_FILES_PATH =
            Constants.ROOT_PATH + File.separator + PERSISTED_FILES_FOLDER;

    public File createFile(byte[] fileContent, String fileName) throws IOException {
        // Use when upload file from Microsoft Edge or Internet Explorer,
        // because this browsers send path to file as a file name, not as an exact name
        if (fileName.contains(File.separator)) {
            fileName = new File(fileName).getName();
        }

        String fileFullPath = PERSISTED_FILES_PATH + File.separator + fileName;
        File file = new File(fileFullPath);
        file.createNewFile();
        try (OutputStream os = new FileOutputStream(file)) {
            os.write(fileContent);
        }

        return file;
    }
}
