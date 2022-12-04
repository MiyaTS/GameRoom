package com.my.game_room;

import com.my.game_room.constant.SceneName;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;

/**
 * the main class that runs the program
 */

public class App extends Application {
    private static final Logger LOG = LogManager.getLogger();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        LOG.info("Program started");
        stage.setTitle("Game Room");
        stage.getIcons().add(getImage());
        stage.setWidth(850);
        stage.setHeight(680);
        initStaticControllers();
        stage.setScene(ScreenController.getScene());
        stage.show();
    }

    /**
     * Load static scenes and store it in memory for quick access from all places.
     *
     * @throws IOException If fxml file is not available
     */
    private void initStaticControllers() throws IOException {
        ScreenController.addScene(SceneName.MENU.getValue(),
                FXMLLoader.load(getClass().getResource(SceneName.MENU.getValue() + ".fxml")));

        ScreenController.addScene(SceneName.GAME_ROOM_MENU.getValue(),
                FXMLLoader.load(getClass().getResource(SceneName.GAME_ROOM_MENU.getValue() + ".fxml")));
        ScreenController.addScene(SceneName.GAME_ROOM_LIST.getValue(),
                FXMLLoader.load(getClass().getResource(SceneName.GAME_ROOM_LIST.getValue() + ".fxml")));

        ScreenController.addScene(SceneName.TOY_MENU.getValue(),
                FXMLLoader.load(getClass().getResource(SceneName.TOY_MENU.getValue() + ".fxml")));
        ScreenController.addScene(SceneName.TOY_TYPE.getValue(),
                FXMLLoader.load(getClass().getResource(SceneName.TOY_TYPE.getValue() + ".fxml")));

        ScreenController.activate(SceneName.MENU.getValue());
    }

    private Image getImage() {
        InputStream icon = getClass().getResourceAsStream("img/game_room.png");
        Image image = null;
        if (icon != null) {
            image = new Image(icon);
        }
        return image;
    }
}