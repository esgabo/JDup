package net.guajava.jdupmaster.utils;

import javafx.scene.control.Alert;

public class UIHelper {
    public Alert createAlert(Alert.AlertType alertType, String title, String headerText, String contextText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contextText);
        return alert;
    }
}
