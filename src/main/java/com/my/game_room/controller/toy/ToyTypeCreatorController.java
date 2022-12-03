package com.my.game_room.controller.toy;

import com.my.game_room.ScreenController;
import com.my.game_room.constant.SceneName;
import javafx.fxml.FXML;

public class ToyTypeCreatorController {
    @FXML
    public void back() {
        ScreenController.activate(SceneName.TOY_MENU.getValue());
    }

    @FXML
    public void setCar() {
        ScreenController.loadAndShowScene(SceneName.CAR_MENU.getValue());
    }

    @FXML
    public void setCubes() {
        ScreenController.loadAndShowScene(SceneName.CUBES_MENU.getValue());
    }

    @FXML
    public void setDoll() {
        ScreenController.loadAndShowScene(SceneName.DOLL_MENU.getValue());
    }

    @FXML
    public void setMusical() {
        ScreenController.loadAndShowScene(SceneName.MUSICAL_MENU.getValue());
    }
}
