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
        String fileFullPath = PERSISTED_FILES_PATH + File.separator + fileName;
        File file = new File(fileFullPath);
        file.createNewFile();
        try (OutputStream os = new FileOutputStream(file)){
            os.write(fileContent);
        }

        return file;
    }
}
