package net.guajava.jdupmaster.fxml.components;

import java.io.File;
import java.util.List;

public interface FolderChooser {
    void setInitialDirectory(File file);
    List<File> chooseFolders();
}
