package net.guajava.jdupmaster.fxml.components;

import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeTableRow;
import javafx.scene.control.TreeTableView;
import javafx.scene.input.MouseButton;
import javafx.util.Callback;
import net.guajava.jdupmaster.fxml.models.FileResult;
import net.guajava.jdupmaster.utils.UIHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class FileResultRowFactory implements Callback<TreeTableView<FileResult>, TreeTableRow<FileResult>> {

    private Logger logger = LoggerFactory.getLogger("UI-COMPONENTS");

    @Autowired
    private ApplicationContext appContext;

    @Autowired
    private FileOpener fileOpener;

    @Autowired
    private UIHelper uiHelper;

    @Override
    public TreeTableRow<FileResult> call(TreeTableView<FileResult> param) {
        final FileResultContextMenu rowMenu = appContext.getBean(FileResultContextMenu.class);
        TreeTableRow<FileResult> row = new FileResultTreeTableRow(rowMenu);

        MenuItem openFileMenuItem = new MenuItem("Open File");
        Consumer<TreeTableRow<FileResult>> openFileAction = treeTableRow -> {

            if (!treeTableRow.isEmpty()) {
                FileResult rowData = treeTableRow.getItem();

                if (!fileOpener.open(rowData.getFile())) {
                    uiHelper.createAlert(Alert.AlertType.ERROR,
                            "Open Unsuccessful",
                            null,
                            "An error has occurred while trying to open the file")
                            .showAndWait();
                }
            }
        };

        openFileMenuItem.setOnAction(event -> openFileAction.accept(row));
        rowMenu.addPermanentItem(openFileMenuItem);

        row.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                openFileAction.accept(row);
            }
        });

        return row;
    }
}
