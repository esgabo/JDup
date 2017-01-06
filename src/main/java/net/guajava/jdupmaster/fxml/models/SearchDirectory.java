package net.guajava.jdupmaster.fxml.models;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.nio.file.Path;
import java.nio.file.Paths;

public class SearchDirectory {
    private final StringProperty directory;
    private SimpleBooleanProperty recursiveSearch;

    public SearchDirectory(String directory) {
        this.directory = new SimpleStringProperty(directory);
        this.recursiveSearch = new SimpleBooleanProperty(true);
    }

    public String getDirectory() {
        return directory.get();
    }

    public Path getPathDirectory() {
        return Paths.get(getDirectory());
    }

    public void setDirectory(String directory) {
        this.directory.set(directory);
    }

    public StringProperty directoryProperty() {
        return directory;
    }

    public SimpleBooleanProperty recursiveSearchProperty() {
        return this.recursiveSearch;
    }

    public Boolean getRecursiveSearch() {
        return this.recursiveSearchProperty().get();
    }

    public void setRecursiveSearch(final java.lang.Boolean recursiveSearch) {
        this.recursiveSearchProperty().set(recursiveSearch);
    }

    public boolean isSubDirectory(SearchDirectory dir) {
        Path potentialParent = Paths.get(dir.getDirectory()).toAbsolutePath();
        return potentialParent.startsWith(getPathDirectory());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SearchDirectory)) return false;

        SearchDirectory that = (SearchDirectory) o;

        return getDirectory() != null ? getDirectory().equals(that.getDirectory()) : that.getDirectory() == null;
    }

    @Override
    public int hashCode() {
        return getDirectory() != null ? getDirectory().hashCode() : 0;
    }
}
