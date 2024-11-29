package com.tu.musichub.util;

import com.tu.musichub.staticData.Constants;
import org.springframework.stereotype.Component;
import java.io.*;

@Component
public class FileUtil {

    private static final String PERSISTED_FILES_FOLDER = "temp";

    private static final String PERSISTED_FILES_PATH =
            Constants.ROOT_PATH + File.separator + PERSISTED_FILES_FOLDER;

    private void createPersistedFilesFolder() {
        File tempFolder = new File(PERSISTED_FILES_PATH);
        tempFolder.mkdir();
    }

    public File createFile(byte[] fileContent, String fileName) throws IOException {
        this.createPersistedFilesFolder();
        // Use when upload file from Microsoft Edge or Internet Explorer,
        // because that browsers send path to file as a file name, not just a name
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
