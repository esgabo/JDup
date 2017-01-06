package net.guajava.jdupmaster.services.impl;

import net.guajava.jdupmaster.configuration.Config;
import net.guajava.jdupmaster.services.FileDuplicateService;
import net.guajava.jdupmaster.services.FileRetrieverService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Config.class)
public class FileDuplicateServiceTest {

    @Autowired
    FileDuplicateService fileDuplicateService;

    private Path tmpDir;
    private Random rand = new Random();
    private static final String TMP_DIR_PREFIX = "jdupmaster_FileDuplicateServiceImplTest_";

    @Before
    public void setUp() throws Exception {
        tmpDir = Files.createTempDirectory(TMP_DIR_PREFIX + Long.toString(System.nanoTime()));
    }

    @After
    public void tearDown() throws IOException {
        Files.walk(tmpDir).forEach(path -> {try {Files.delete(path);} catch (IOException e) {}});
        Files.deleteIfExists(tmpDir);
        tmpDir = null;
    }

    private File generateRandomFile(int length) throws IOException {
        Path tmpFile = Files.createTempFile(tmpDir, Long.toString(System.nanoTime()) + rand.nextInt(), null);
        byte[] content = new byte[length];
        rand.nextBytes(content);
        Files.write(tmpFile, content);
        return tmpFile.toFile();
    }

    private File copyFile(File original) throws IOException {
        Path tmpFile = Paths.get(tmpDir.toString(), Long.toString(System.nanoTime()) + rand.nextInt());
        Files.copy(original.toPath(), tmpFile);
        return tmpFile.toFile();
    }

    @Test
    public void returnsOneListWithSingleFileWhenOneFilePresent() throws Exception {
        File file = generateRandomFile(20);

        List<List<File>> duplicates = fileDuplicateService.getDuplicateFiles(Arrays.asList(file));
        assertEquals(1, duplicates.size());
    }

    @Test
    public void returnsTwoListWithSingleFileWhenTwoDifferentFilesPresent() throws Exception {
        File file = generateRandomFile(20);
        File file2 = generateRandomFile(21);

        List<List<File>> duplicates = fileDuplicateService.getDuplicateFiles(Arrays.asList(file, file2));
        assertEquals(2, duplicates.size());
        assertEquals(1, duplicates.get(0).size());
        assertEquals(1, duplicates.get(1).size());
    }

    @Test
    public void returnsOneListWithTwoElementsWhenTwoDuplicateFiles() throws Exception {
        File file = generateRandomFile(20);
        File copy = copyFile(file);

        List<List<File>> duplicates = fileDuplicateService.getDuplicateFiles(Arrays.asList(file, copy));
        assertEquals(1, duplicates.size());
        assertEquals(2, duplicates.get(0).size());
    }

    @Test
    public void returnsTwoListWithSingleFileAndDuplicateWhenTwoIdenticalFilesPresentAndOneDifferent() throws Exception {
        File file = generateRandomFile(20);
        File file2 = generateRandomFile(21);
        File copyFile2 = copyFile(file2);

        List<List<File>> duplicates = fileDuplicateService.getDuplicateFiles(Arrays.asList(file, file2, copyFile2));
        assertEquals(2, duplicates.size());
        assertTrue(duplicates.stream().anyMatch(list -> list.size() == 1));
        assertTrue(duplicates.stream().anyMatch(list -> list.size() == 2));
    }

}