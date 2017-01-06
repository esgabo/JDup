package net.guajava.jdupmaster.services.impl;

import net.guajava.jdupmaster.services.FileRetrieverService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

@Component
@Primary
public class DefensiveFileRetrieverServiceImpl implements FileRetrieverService {

    private Logger logger = LoggerFactory.getLogger("FILE-RETRIEVER-SERVICE");

    @Autowired
    @Qualifier("file-retriever-delegate")
    private FileRetrieverService fileRetrieverService;

    @Override
    public List<File> getFiles(Path dir) {
        if (dir == null || !Files.isDirectory(dir)) {
            return Collections.emptyList();
        }

        return fileRetrieverService.getFiles(dir);
    }

    @Override
    public List<File> getFilesRecursively(Path start) {
        if (start == null || !Files.isDirectory(start)) {
            logger.warn("searchDirectories list is null or empty at pruneSearchDirectories function");
            return Collections.emptyList();
        }

        return fileRetrieverService.getFilesRecursively(start);
    }
}
