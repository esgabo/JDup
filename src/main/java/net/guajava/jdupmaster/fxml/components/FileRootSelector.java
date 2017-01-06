package net.guajava.jdupmaster.fxml.components;

import java.io.File;
import java.util.List;

public interface FileRootSelector {
    File selectRoot(List<File> files);
}
