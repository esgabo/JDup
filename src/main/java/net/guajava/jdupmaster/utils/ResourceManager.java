package net.guajava.jdupmaster.utils;

import javafx.scene.image.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.util.Optional;

public class ResourceManager {

    private ApplicationContext applicationContext;

    private Logger logger = LoggerFactory.getLogger("RESOURCE-MANAGER");

    public ResourceManager(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public Optional<Image> getImage(String path) {
        try {
            return Optional.of(new Image(applicationContext.getResource("classpath:images/".concat(path)).getInputStream()));
        } catch (IOException e) {
            logger.warn("An error occurred trying to get the Image " + path, e);
            return Optional.empty();
        }
    }

    public Optional<java.net.URL> getResourceUrl(String path) {
        try {
            return Optional.of(applicationContext.getResource("classpath:".concat(path)).getURL());
        } catch (IOException e) {
            logger.warn("An error occurred trying to get the Resource URL " + path, e);
            return Optional.empty();
        }
    }
}
