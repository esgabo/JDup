package net.guajava.jdupmaster.fxml.components.impl;

import net.guajava.jdupmaster.fxml.components.FileRootSelector;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Component
public class FirstElementFileRootSelector implements FileRootSelector {

    @Override
    public File selectRoot(List<File> files) {
        if (files == null || files.isEmpty()) {
            return null;
        }

        return files.get(0);
    }
}
