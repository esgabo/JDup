package net.guajava.jdupmaster.services.impl;

import net.guajava.jdupmaster.fxml.models.SearchDirectory;
import net.guajava.jdupmaster.services.FileDuplicateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Collections;
import java.util.List;

@Component
@Primary
public class DefensiveFileDuplicateServiceImpl implements FileDuplicateService {

    private Logger logger = LoggerFactory.getLogger("FILE-DUPLICATE-SERVICE");

    @Autowired
    @Qualifier("file-duplicate-delegate")
    private FileDuplicateService fileDuplicateService;

    @Override
    public List<List<File>> getDuplicateFiles(List<File> files) {
        if (files == null || files.isEmpty()) {
            logger.warn("files list is null or empty at pruneSearchDirectories function");
            return Collections.emptyList();
        }

        return fileDuplicateService.getDuplicateFiles(files);
    }

    @Override
    public List<List<File>> findDuplicateFiles(List<SearchDirectory> searchDirectories) {
        if (searchDirectories == null || searchDirectories.isEmpty()) {
            logger.warn("searchDirectories list is null or empty at pruneSearchDirectories function");
            return Collections.emptyList();
        }

        return fileDuplicateService.findDuplicateFiles(searchDirectories);
    }

    @Override
    public List<SearchDirectory> pruneSearchDirectories(List<SearchDirectory> searchDirectories) {
        if (searchDirectories == null || searchDirectories.isEmpty()) {
            logger.warn("searchDirectories list is null or empty at pruneSearchDirectories function");
            return Collections.emptyList();
        }

        return fileDuplicateService.pruneSearchDirectories(searchDirectories);
    }
}
