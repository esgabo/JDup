package net.guajava.jdupmaster.services.impl;

import net.guajava.jdupmaster.services.FileRetrieverService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component("file-retriever-delegate")
class FileRetrieverServiceImpl implements FileRetrieverService {

    private Logger logger = LoggerFactory.getLogger("FILE-RETRIEVER-SERVICE");

    @Override
    public List<File> getFiles(Path dir) {
        try {

            Stream<File> regularFilesStream = Files.list(dir)
                    .filter(Files::isRegularFile)
                    .map(Path::toFile);

            if (logger.isDebugEnabled()) {
                regularFilesStream = regularFilesStream.peek(file -> logger.debug("Getting File " + file));
            }

            return regularFilesStream.collect(Collectors.toList());
        } catch (IOException e) {
            logger.error("An error occurred trying the get files from directory " + dir, e);
            return Collections.emptyList();
        }


    }

    @Override
    public List<File> getFilesRecursively(Path start) {
        try {
            Stream<File> regularFilesStream = Files.walk(start)
                    .filter(Files::isRegularFile)
                    .map(Path::toFile);

            if (logger.isDebugEnabled()) {
                regularFilesStream = regularFilesStream.peek(file -> logger.debug("Getting File " + file));
            }

            return regularFilesStream
                    .collect(Collectors.toList());
        } catch (IOException e) {
            logger.error("An error occurred trying the get files from directory " + start, e);
            return Collections.emptyList();
        }
    }
}
