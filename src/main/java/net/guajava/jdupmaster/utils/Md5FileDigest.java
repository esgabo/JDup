package net.guajava.jdupmaster.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Md5FileDigest implements FileDigest {

    Logger logger = LoggerFactory.getLogger("FILE-DIGEST");

    @Override
    public String digest(File file) {
        try(FileInputStream fis = new FileInputStream(file))  {
            return DigestUtils.md5Hex(fis);
        } catch (IOException io) {
            logger.error("An error has occurred trying to calculate MD5 hash for file " + file, io);
            return null;
        }
    }
}
