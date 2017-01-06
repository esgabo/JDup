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
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Config.class)
public class FileRetrieverServiceTest {
    private static final String TMP_DIR_PREFIX = "jdupmaster_DirectoryVisitorImplTest_";
    private Random rand = new Random();
    private Path tmpDir;

    @Autowired
    private FileRetrieverService directoryVisitor;

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

    private void generateFiles(Path dir, int number) {
        IntStream.range(0, number).forEach(value -> {
            try {
                Files.createTempFile(tmpDir, Long.toString(System.nanoTime()), null);
            } catch (IOException e) {}
        });
    }

    private Path generateFolder(Path dir, String name) {
        try {
            Files.createTempDirectory(dir, Long.toString(System.nanoTime()));
        } finally {
            return null;
        }
    }

    private void generateFolders(Path dir, int number) {
        IntStream.range(0, number)
                .forEach(value -> {generateFolder(tmpDir, Long.toString(System.nanoTime()));});
    }

    @Test
    public void WhenNullPassedInGetFilesReturnsEmptyList() throws Exception {

        List<File> files = directoryVisitor.getFiles(null);

        assertNotNull(files);
        assertEquals(0, files.size());
    }

    @Test
    public void WhenFilePassedInGetFilesReturnsEmptyList() throws Exception {
        Path file = Files.createTempFile(tmpDir, Long.toString(System.nanoTime()), null);

        List<File> files = directoryVisitor.getFiles(file);

        assertNotNull(files);
        assertEquals(0, files.size());
    }

    @Test
    public void WhenFilesArePresentExpectedGetFilesReturnsAllFiles() throws Exception {
        int randNumFiles = rand.nextInt(10);
        generateFiles(tmpDir, randNumFiles);

        List<File> files = directoryVisitor.getFiles(tmpDir);

        assertEquals(randNumFiles, files.size());
    }

    @Test
    public void WhenFilesArePresentExpectedGetFilesReturnsAllFilesWithoutDirectories() throws Exception {
        int randNumFiles = rand.nextInt(10) + 1;
        int randNumFolders = rand.nextInt(10) + 1;
        generateFiles(tmpDir, randNumFiles);
        generateFolders(tmpDir, randNumFolders);

        List<File> files = directoryVisitor.getFiles(tmpDir);

        assertEquals(randNumFiles, files.size());
    }

    @Test
    public void WhenFilesArePresentExpectedGetFilesRecursivelyReturnsAllFiles() throws Exception {
        int randNumFiles = rand.nextInt(10) + 1;
        generateFiles(tmpDir, randNumFiles);

        List<File> files = directoryVisitor.getFilesRecursively(tmpDir);

        assertEquals(randNumFiles, files.size());
    }

    @Test
    public void WhenSubFolderPresentExpectedGetFilesRecursivelyReturnsAllFilesWithoutDirectories() throws Exception {
        int randNumFiles = rand.nextInt(10) + 1;
        int randNumFolders = rand.nextInt(10) + 1;
        generateFiles(tmpDir, randNumFiles);
        generateFolders(tmpDir, randNumFolders);
        Path subFolder = generateFolder(tmpDir, "subFolder1");
        generateFiles(subFolder, randNumFiles);
        generateFolders(subFolder, randNumFolders);

        List<File> files = directoryVisitor.getFilesRecursively(tmpDir);

        assertEquals(randNumFiles * 2, files.size());
    }

}