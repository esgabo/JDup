package net.guajava.jdupmaster.fxml.components.impl;

import javafx.stage.DirectoryChooser;
import net.guajava.jdupmaster.fxml.components.FolderChooser;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DirectoryFolderChooserImpl implements FolderChooser {

    private final DirectoryChooser directoryChooser;

    public DirectoryFolderChooserImpl() {
        this.directoryChooser = new DirectoryChooser();
    }


    @Override
    public void setInitialDirectory(File file) {
        directoryChooser.setInitialDirectory(file);
    }

    @Override
    public List<File> chooseFolders() {
        File selection = directoryChooser.showDialog(null);
        if (selection == null) {
            return Collections.emptyList();
        }

        List<File> files = new ArrayList<>();
        files.add(selection);
        return files;
    }
}
