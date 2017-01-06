package net.guajava.jdupmaster.services;

import net.guajava.jdupmaster.fxml.models.SearchDirectory;

import java.io.File;
import java.util.List;

public interface FileDuplicateService {
    List<List<File>> getDuplicateFiles(List<File> files);
    List<List<File>> findDuplicateFiles(List<SearchDirectory> searchDirectories);
    List<SearchDirectory> pruneSearchDirectories(List<SearchDirectory> searchDirectories);
}
