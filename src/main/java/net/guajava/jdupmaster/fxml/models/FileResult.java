package net.guajava.jdupmaster.fxml.models;

import java.io.File;
import java.nio.file.Files;

public class FileResult {
    private final File file;

    public FileResult(File file) {
        this.file = file;
    }

    public String getName() {
        return file.getName();
    }

    public String getFolder() {
        if (Files.isDirectory(file.toPath())) {
            return file.getPath();
        } else {
            return file.getParent();
        }
    }

    public long getSize() {
        return file.length();
    }

    public File getFile() {
        return file;
    }
}
