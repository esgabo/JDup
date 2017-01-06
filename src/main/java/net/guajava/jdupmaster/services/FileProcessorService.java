package net.guajava.jdupmaster.services;

import java.io.File;
import java.util.List;

public interface FileProcessorService {
    public boolean process(List<File> fileResult);
}
