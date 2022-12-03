package com.my.game_room;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

/**
 * Utility class for processing .fxml scenes
 */
public final class ScreenController {
    private static final Logger LOG = LogManager.getLogger();
    private static final HashMap<String, Parent> screenMap = new HashMap<>();
    private static Scene scene;

    private ScreenController() {
    }

    public static Scene getScene() {
        return scene;
    }


    /**
     * Load dynamic scene to show clear non-static data to user
     *
     * @param name of scene for dynamic loading
     */
    public static void loadAndShowScene(String name) {
        try {
            Parent parent = FXMLLoader.load(Objects.requireNonNull(ScreenController.class.getResource(name + ".fxml")));
            if (scene == null) {
                scene = new Scene(parent);
            }
            scene.setRoot(parent);
        } catch (IOException ex) {
            LOG.error("Cannot load page.", ex);
        }
    }

    public static void addScene(String name, Parent parent) {
        screenMap.put(name, parent);
    }

    /**
     * Checkout on static scene by name
     *
     * @param name of the scene
     */
    public static void activate(String name) {
        var parent = screenMap.get(name);
        if (scene == null) {
            scene = new Scene(parent);
        }
        scene.setRoot(parent);
    }
}
