package net.guajava.jdupmaster.fxml.components;

import java.util.Optional;

public interface MenuItemResolver<T> {
    Optional<javafx.scene.control.MenuItem> resolveMenuItem(T item);
}
