package net.guajava.jdupmaster.fxml.components;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import net.guajava.jdupmaster.fxml.models.FileResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FileResultContextMenu extends ContextMenu {

    @Autowired
    private List<MenuItemResolver<TreeItem<FileResult>>> menuItemResolvers;

    private List<MenuItem> dynamicItems;

    public FileResultContextMenu() {
        this.dynamicItems = new LinkedList<>();
    }

    public void addPermanentItem(MenuItem menuItem) {
        this.getItems().add(menuItem);
    }

    public void addDynamicItem(MenuItem menuItem) {
        this.dynamicItems.add(menuItem);
        this.getItems().add(menuItem);
    }

    private void removeDynamicItems() {
        this.getItems().removeAll(this.dynamicItems);
        this.dynamicItems.clear();
    }

    public void resolveDynamicMenuItems(TreeItem<FileResult> item) {
        if (item == null) {
            return ;
        }

        final List<String> existentMenuItemIds = this.getItems().stream()
                .map(menuItem -> menuItem.getId())
                .collect(Collectors.toList());

        removeDynamicItems();
        menuItemResolvers.stream()
                .map(treeItemMenuItemResolver -> treeItemMenuItemResolver.resolveMenuItem(item))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(menuItem -> menuItem.getId() != null && !existentMenuItemIds.contains(menuItem.getId()))
                .forEach(this::addDynamicItem);
    }
}
