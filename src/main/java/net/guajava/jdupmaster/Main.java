package net.guajava.jdupmaster;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import net.guajava.jdupmaster.configuration.Config;
import net.guajava.jdupmaster.utils.ResourceManager;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

public class Main extends Application {

    private ConfigurableApplicationContext springContext;
    private Parent rootNode;

    public static void main(final String[] args) {
        Application.launch(args);
    }

    @Override
    public void init() throws Exception {
        springContext = SpringApplication.run(Config.class);
        ResourceManager resourceManager = springContext.getBean(ResourceManager.class);
        FXMLLoader fxmlLoader = new FXMLLoader(resourceManager.getResourceUrl("fxml/Main.fxml").get());
        fxmlLoader.setControllerFactory(springContext::getBean);
        rootNode = fxmlLoader.load();
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(rootNode));
        ResourceManager resourceManager = springContext.getBean(ResourceManager.class);
        stage.getIcons().add(resourceManager.getImage("duplicate.png").get());
        stage.setTitle("JDup");
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        springContext.close();
    }

}