package net.guajava.jdupmaster.fxml.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import net.guajava.jdupmaster.fxml.models.SearchDirectory;
import net.guajava.jdupmaster.services.FileDuplicateService;
import net.guajava.jdupmaster.services.FileProcessorService;
import net.guajava.jdupmaster.utils.ResourceManager;
import net.guajava.jdupmaster.utils.UIHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Component
public class MainController {
    @Autowired
    private SearchDirectoryTableController searchDirectoryTableController;

    @Autowired
    private ResultDuplicateFilesTableController resultDuplicateFilesTableController;

    @Autowired
    private FileDuplicateService fileDuplicateService;

    @Autowired
    private ResourceManager resourceManager;

    @Autowired
    private FileProcessorService fileProcessorService;

    @Autowired
    private UIHelper uiHelper;

    @FXML
    private Button addFolders;

    @FXML
    private Button removeFolders;

    @FXML
    private Button scanFolders;

    @FXML
    private Button removeDuplicates;


    @FXML
    private void initialize() {
        addFolders.setGraphic(new ImageView(resourceManager.getImage("add-folder.png").get()));
        addFolders.setTooltip(new Tooltip("Add Directory"));
        removeFolders.setGraphic(new ImageView(resourceManager.getImage("remove-folder.png").get()));
        removeFolders.setTooltip(new Tooltip("Remove Directory"));
        scanFolders.setGraphic(new ImageView(resourceManager.getImage("scan.png").get()));
        scanFolders.setTooltip(new Tooltip("Scan for duplicate files"));
        removeDuplicates.setGraphic(new ImageView(resourceManager.getImage("remove-duplicates.png").get()));
        removeDuplicates.setTooltip(new Tooltip("Remove duplicate files"));
    }

    public void addFolders(MouseEvent mouseEvent) {
        searchDirectoryTableController.chooseFolders();
    }

    public void removeSelection(MouseEvent mouseEvent) {
        searchDirectoryTableController.removeSelectedFolders();
    }

    public void scanForDuplicateFiles(MouseEvent mouseEvent) {

        if (searchDirectoryTableController.hasElements()) {
            List<SearchDirectory> searchDirectories = fileDuplicateService
                    .pruneSearchDirectories(searchDirectoryTableController.getSearchDirectories());

            List<List<File>> duplicateFiles = fileDuplicateService.findDuplicateFiles(searchDirectories);
            resultDuplicateFilesTableController.showDuplicates(duplicateFiles);
        } else {
            uiHelper.createAlert(Alert.AlertType.WARNING,
                    "No Directories selected to process",
                    null,
                    "No directories exist to be processed")
                    .showAndWait();
        }
    }

    public void removeDuplicates(MouseEvent mouseEvent) {
        if (resultDuplicateFilesTableController.hasElements()) {
            resultDuplicateFilesTableController.processFileGroups(fileProcessorService);
        } else {
            uiHelper.createAlert(Alert.AlertType.WARNING,
                    "No files selected to process",
                    null,
                    "No files exist to be processed")
                    .showAndWait();
        }
    }
}
