package net.guajava.jdupmaster.fxml.controllers;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import net.guajava.jdupmaster.fxml.components.FolderChooser;
import net.guajava.jdupmaster.fxml.models.SearchDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class SearchDirectoryTableController {

    Logger logger = LoggerFactory.getLogger("UI-CONTROLLERS");

    @FXML
    private TableView<SearchDirectory> directoryTable;

    @FXML
    private TableColumn<SearchDirectory, String> directoryColumn;

    @FXML
    private TableColumn<SearchDirectory, Boolean> recursiveSearchColumn;

    private ObservableList<SearchDirectory> directoriesList;
    private final FolderChooser folderChooser;

    @Autowired
    public SearchDirectoryTableController(FolderChooser folderChooser) {
        this.folderChooser = folderChooser;
        this.directoriesList = FXCollections.observableArrayList();
    }

    @FXML
    private void initialize() {
        adjustColumnWidth();
        directoryColumn.setCellValueFactory(cellData -> cellData.getValue().directoryProperty());
        recursiveSearchColumn.setCellValueFactory(cellData -> cellData.getValue().recursiveSearchProperty());
        recursiveSearchColumn.setCellFactory(param -> new CheckBoxTableCell<SearchDirectory,Boolean>());

        directoryTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        directoryTable.setItems(directoriesList);
    }

    private void adjustColumnWidth() {
        ObservableValue width = directoryTable.widthProperty().subtract(recursiveSearchColumn.getPrefWidth()+2);
        directoryColumn.prefWidthProperty().bind(width);
    }

    public void chooseFolders() {
        List<File> chosenFolders = this.folderChooser.chooseFolders();

        if (!chosenFolders.isEmpty()) {
            chosenFolders.stream().forEach(file -> {
                try {
                    SearchDirectory chosen = new SearchDirectory(file.getCanonicalPath());

                    if (!directoriesList.contains(chosen)) {
                        directoriesList.add(chosen);
                    }
                } catch (IOException e) {
                    logger.error("Error getting canonical Path of file " + file, e);
                }
            });
            
            this.folderChooser.setInitialDirectory(chosenFolders.get(0).getParentFile());
        }
    }

    public void removeSelectedFolders() {
        directoriesList.removeAll(directoryTable.getSelectionModel().getSelectedItems());
    }

    public List<SearchDirectory> getSearchDirectories() {
        return directoriesList;
    }

    public boolean hasElements() {
        return !getSearchDirectories().isEmpty();
    }
}
