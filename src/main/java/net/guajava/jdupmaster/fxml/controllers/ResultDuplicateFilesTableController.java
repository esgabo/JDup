package net.guajava.jdupmaster.fxml.controllers;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;
import net.guajava.jdupmaster.fxml.components.FileResultRowFactory;
import net.guajava.jdupmaster.fxml.components.FileRootSelector;
import net.guajava.jdupmaster.fxml.models.FileResult;
import net.guajava.jdupmaster.services.FileProcessorService;
import net.guajava.jdupmaster.utils.FileSizeFormatter;
import net.guajava.jdupmaster.utils.UIHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Component
public class ResultDuplicateFilesTableController {
    @FXML
    TreeTableView<FileResult> resultTable;

    @FXML
    TreeTableColumn<FileResult, String> nameColumn;

    @FXML
    TreeTableColumn<FileResult, String> folderColumn;

    @FXML
    TreeTableColumn<FileResult, String> sizeColumn;

    @Autowired
    private FileSizeFormatter fileSizeFormatter;

    @Autowired
    private FileRootSelector fileRootSelector;

    @Autowired
    private Callback fileResultRowFactory;

    private TreeItem<FileResult> root;

    @Autowired
    private UIHelper uiHelper;


    @FXML
    private void initialize() {
        adjustColumnWidth();
        resultTable.setRowFactory(fileResultRowFactory);

        nameColumn.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<FileResult, String> param) ->
                        new ReadOnlyStringWrapper((param.getValue().getValue() == null) ? "" : param.getValue().getValue().getName())
        );

        folderColumn.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<FileResult, String> param) ->
                        new ReadOnlyStringWrapper((param.getValue().getValue() == null) ? "" : param.getValue().getValue().getFolder())
        );

        sizeColumn.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<FileResult, String> param) ->
                        new ReadOnlyStringWrapper((param.getValue().getValue() == null) ? "" : fileSizeFormatter.format(param.getValue().getValue().getSize()))
        );

        root = new TreeItem<FileResult>(null);
        resultTable.setShowRoot(false);
        resultTable.setRoot(root);
    }

    private void adjustColumnWidth() {
        DoubleBinding availableSpace = resultTable.widthProperty().subtract(sizeColumn.getPrefWidth());
        nameColumn.prefWidthProperty().bind(availableSpace.multiply(0.5));
        folderColumn.prefWidthProperty().bind(availableSpace.multiply(0.5));
    }

    public void showDuplicates(List<List<File>> duplicates) {
        root.getChildren().clear();

        List<TreeItem<FileResult>> duplicateElements = duplicates.stream()
                .filter(duplicateList -> duplicateList.size() > 1) //TODO: perhaps make filter based on properties. Something like "Show files with no duplicate also"
                .map(this::convertDuplicateFileListIntoTreeItem)
                .collect(Collectors.toList());

        if (!duplicateElements.isEmpty()) {
            root.getChildren().addAll(duplicateElements);
        } else {
            uiHelper.createAlert(Alert.AlertType.INFORMATION,
                    "No duplicate files found",
                    null,
                    "No duplicated files exist to be processed")
                    .showAndWait();
        }
    }

    private TreeItem<FileResult> convertDuplicateFileListIntoTreeItem(List<File> duplicateList) {
        File rootDuplicateFile = fileRootSelector.selectRoot(duplicateList);
        duplicateList.remove(rootDuplicateFile);

        List<TreeItem<FileResult>> items = duplicateList.stream()
                .map(file -> new TreeItem<FileResult>(new FileResult(file)))
                .collect(Collectors.toList());

        TreeItem<FileResult> rootElem = new TreeItem<FileResult>(new FileResult(rootDuplicateFile));
        rootElem.getChildren().addAll(items);
        return rootElem;
    }

    private List<File> getFilesFromModel() {
        return root.getChildren().stream()
                .flatMap(fileResultTreeItem -> fileResultTreeItem.getChildren().stream())
                .map(fileResultTreeItem -> fileResultTreeItem.getValue().getFile()).collect(Collectors.toList());
    }

    private void removeProcessedFiles() {
        root.getChildren().stream()
                .forEach(fileResultTreeItem -> fileResultTreeItem.getChildren().clear());
    }

    /**
     * Function to process files present in the ResultTable.
     * @param fileProcessor Implementation that processes the files. i.e, an implementation deletes de files while
     *                      a different implementation moves the files to a different directory.
     */
    public void processFileGroups(FileProcessorService fileProcessor) {
        if (fileProcessor.process(getFilesFromModel())) {
            uiHelper.createAlert(Alert.AlertType.INFORMATION,
                    "Process Completed",
                    null,
                    "All files were processed successfully")
                    .showAndWait();

            removeProcessedFiles();
        } else {
            uiHelper.createAlert(Alert.AlertType.ERROR,
                    "Process Unsuccessful",
                    null,
                    "An error has occurred while processing the files")
                    .showAndWait();
        }
    }

    public boolean hasElements() {
        return !this.root.getChildren().isEmpty();
    }
}
