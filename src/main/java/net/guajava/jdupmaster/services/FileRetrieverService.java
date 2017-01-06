package net.guajava.jdupmaster.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface FileRetrieverService {
    List<File> getFiles(Path dir);
    List<File> getFilesRecursively(Path start);
}
