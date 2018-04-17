package com.softuni.musichub.util;

import com.softuni.musichub.staticData.Constants;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.io.File;
import java.io.IOException;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class FileUtilTests {

    private static final String TEST_FILE_NAME = "test.mp3";
    private static final String TEST_FOLDER_NAME = "temp";
    private static final String TEST_FILE_PATH = Constants.ROOT_PATH +
            File.separator + TEST_FILE_NAME;
    private static final String TEST_FOLDER_PATH = Constants.ROOT_PATH +
            File.separator + TEST_FOLDER_NAME;

    @Autowired
    private FileUtil fileUtil;

    private byte[] testFileContent;

    private void fillTestFileContent() {
        this.testFileContent = new byte[]{0};
    }

    @Before
    public void setUp() {
        this.fileUtil = new FileUtil();
        this.fillTestFileContent();
    }

    @After
    public void clearData() {
        //Empty test folder content
        File tempFolder = new File(TEST_FOLDER_PATH);
        for (File file : tempFolder.listFiles()) {
            if (file != null) {
                file.delete();
            }
        }
    }

    @Test
    public void testCreateFile_withPathToFileAsFileName_returnsCreatedFileWithName()
            throws IOException {
        File file = this.fileUtil.createFile(this.testFileContent, TEST_FILE_PATH);

        Assert.assertEquals(file.getName(), TEST_FILE_NAME);
    }

    @Test
    public void testCreateFile_withFileName_returnsCreatedFileWithName()
            throws IOException {
        File file = this.fileUtil.createFile(this.testFileContent, TEST_FILE_NAME);

        Assert.assertEquals(file.getName(), TEST_FILE_NAME);
    }
}
