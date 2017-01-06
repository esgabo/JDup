package net.guajava.jdupmaster.utils;

import java.io.File;

public interface FileDigest {
    String digest(File file);
}
