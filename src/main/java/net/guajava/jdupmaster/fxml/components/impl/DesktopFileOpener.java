package net.guajava.jdupmaster.fxml.components.impl;

import net.guajava.jdupmaster.fxml.components.FileOpener;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.File;

@Component("desktop-file-opener")
public class DesktopFileOpener implements FileOpener {
    @Override
    public boolean open(File file) {
        try {
            if (!Desktop.isDesktopSupported()) {
//                logErr("Platform is not supported.");
                return false;
            }

            if (!Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
//                logErr("OPEN is not supported.");
                return false;
            }

            Desktop.getDesktop().open(file);

            return true;
        } catch (Throwable t) {
//            logErr("Error using desktop open.", t);
            return false;
        }
    }
}
