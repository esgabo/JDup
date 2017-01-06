package net.guajava.jdupmaster.services.impl;

import net.guajava.jdupmaster.services.FileProcessorService;
import org.springframework.stereotype.Component;
import com.sun.jna.platform.FileUtils;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Component
public class TrashDeleteFileProcessor implements FileProcessorService {
    @Override
    public boolean process(List<File> files) {
        FileUtils fileUtils = FileUtils.getInstance();

        if (!fileUtils.hasTrash()) {
            throw new UnsupportedOperationException("Platform does not support moving files to trash");
        }

        File[] filesArray = new File[files.size()];
        try {
            fileUtils.moveToTrash(files.toArray(filesArray));
        } catch (IOException e) {
            throw new RuntimeException("Error while moving files to trash", e);
        }

        return true;
    }
}
