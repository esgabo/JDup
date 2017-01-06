package net.guajava.jdupmaster.services.impl;

import net.guajava.jdupmaster.fxml.models.SearchDirectory;
import net.guajava.jdupmaster.services.FileDuplicateService;
import net.guajava.jdupmaster.services.FileRetrieverService;
import net.guajava.jdupmaster.utils.FileDigest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component("file-duplicate-delegate")
public class FileDuplicateServiceImpl implements FileDuplicateService {

    private Logger logger = LoggerFactory.getLogger("FILE-DUPLICATE-SERVICE");

    @Autowired
    private FileDigest fileDigest;

    @Autowired
    private FileRetrieverService fileRetrieverService;

    @Override
    public List<List<File>> getDuplicateFiles(List<File> files) {

        if (files.size() == 1) {
            List<List<File>> duplicatedFiles = new LinkedList<>();
            duplicatedFiles.add(files);
            return duplicatedFiles;
        }

        Map<Long, List<File>> filesBySize = files.stream()
                .collect(Collectors.groupingByConcurrent(File::length));

        Map<Boolean, List<List<File>>> filesByMultipleElements = filesBySize.values().stream().collect(Collectors.partitioningBy(list -> list.size() > 1));

        List<List<File>> listWithOneElement = filesByMultipleElements.get(false);
        List<List<File>> listWithMultipleElement = filesByMultipleElements.get(true);

        Stream<Map<String, List<File>>> filesByHash = listWithMultipleElement.stream()
                .map(list -> list.stream()
                            .collect(Collectors.groupingByConcurrent(fileDigest::digest)));

        List<List<File>> separatedSetOfDuplicates = filesByHash
                .flatMap(map -> map.values().stream())
                .collect(Collectors.toList());

        List<List<File>> duplicatedFiles = new LinkedList<>();
        duplicatedFiles.addAll(listWithOneElement);
        duplicatedFiles.addAll(separatedSetOfDuplicates);

        return duplicatedFiles;
    }

    @Override
    public List<List<File>> findDuplicateFiles(List<SearchDirectory> searchDirectories) {
        Map<Boolean, List<SearchDirectory>> dirByRecursive = searchDirectories.stream()
                .collect(Collectors.partitioningBy(SearchDirectory::getRecursiveSearch));

        Stream<File> fileStreamToProcess = dirByRecursive.get(Boolean.FALSE).stream()
                .map(searchDirectory -> Paths.get(searchDirectory.getDirectory()))
                .map(fileRetrieverService::getFiles)
                .flatMap(List::stream);

        Stream<File> fileStreamFromRecursiveToProcess = dirByRecursive.get(Boolean.TRUE).stream()
                .map(searchDirectory -> Paths.get(searchDirectory.getDirectory()))
                .map(fileRetrieverService::getFilesRecursively)
                .flatMap(List::stream);

        List<File> filesToScan = Stream.concat(fileStreamToProcess, fileStreamFromRecursiveToProcess)
                .collect(Collectors.toList());

        return getDuplicateFiles(filesToScan);
    }

    @Override
    public List<SearchDirectory> pruneSearchDirectories(List<SearchDirectory> searchDirectories) {
        Map<Boolean, List<SearchDirectory>> directoriesByRecursiveness = searchDirectories.stream()
                .collect(Collectors.partitioningBy(SearchDirectory::getRecursiveSearch));

        List<SearchDirectory> dirsToSearch = directoriesByRecursiveness.get(Boolean.TRUE);
        List<SearchDirectory> prunedDirs = directoriesByRecursiveness.get(Boolean.FALSE).stream()
                .filter(searchDirectory -> dirsToSearch.stream()
                        .noneMatch(searchDirectory::isSubDirectory))
                .collect(Collectors.toList());

        dirsToSearch.addAll(prunedDirs);
        return dirsToSearch;
    }
}
