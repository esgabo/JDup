package net.guajava.jdupmaster.fxml.components.impl;

import net.guajava.jdupmaster.fxml.components.FileRootSelector;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;

@Component
@Primary
public class OlderElementFileRootSelector implements FileRootSelector {

    @Override
    public File selectRoot(List<File> files) {
        if (files == null || files.isEmpty()) {
            return null;
        }

        return files.stream()
                .min(Comparator.comparing(file -> {
                    try {
                        return Files.readAttributes(file.toPath(), BasicFileAttributes.class).creationTime();
                    } catch (IOException e) {
                        //TODO: log
                        return FileTime.from(Instant.now());
                    }
                }))
                .get();
    }
}
