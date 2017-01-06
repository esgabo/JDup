package net.guajava.jdupmaster.fxml.components.impl;

import net.guajava.jdupmaster.fxml.components.FileOpener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@Primary
public class FileOpenerImpl implements FileOpener {

    @Autowired
    @Qualifier("desktop-file-opener")
    private FileOpener desktopFileOpener;

    @Autowired
    @Qualifier("os-file-opener")
    private FileOpener osFileOpener;

    @Override
    public boolean open(File file) {
        if (!desktopFileOpener.open(file)) {
            return osFileOpener.open(file);
        }

        return false;
    }
}
