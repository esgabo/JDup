package net.guajava.jdupmaster.fxml.components.impl;

import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import net.guajava.jdupmaster.fxml.components.MenuItemResolver;
import net.guajava.jdupmaster.fxml.models.FileResult;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class SetOriginalDynamicMenuItem implements MenuItemResolver<TreeItem<FileResult>> {
    @Override
    public Optional<MenuItem> resolveMenuItem(TreeItem<FileResult> item) {
        if (item != null && item.isLeaf()) {
            MenuItem setParentItem = new MenuItem("Set as Original");
            setParentItem.setId("setAsOriginal");
            setParentItem.setOnAction(event -> {
                FileResult parentValue = item.getParent().getValue();

                item.getParent().setValue(item.getValue());
                item.setValue(parentValue);
            });

            return Optional.of(setParentItem);
        }

        return Optional.empty();
    }
}
