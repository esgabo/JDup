package net.guajava.jdupmaster.fxml.components;


import javafx.scene.control.TreeTableRow;
import net.guajava.jdupmaster.fxml.models.FileResult;

public class FileResultTreeTableRow extends TreeTableRow<FileResult> {

    private final FileResultContextMenu rowMenu;

    public FileResultTreeTableRow(FileResultContextMenu rowMenu) {
        this.rowMenu = rowMenu;
    }

    @Override
    public void updateItem(FileResult item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setContextMenu(null);
        } else {
            rowMenu.resolveDynamicMenuItems(this.treeItemProperty().get());
            setContextMenu(rowMenu);
        }
    }
}
