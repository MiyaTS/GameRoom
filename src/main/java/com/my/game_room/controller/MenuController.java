package com.my.game_room.controller;

import com.my.game_room.ScreenController;
import com.my.game_room.constant.SceneName;
import javafx.fxml.FXML;

public class MenuController {
    @FXML
    public void switchToMainMenu() {
        ScreenController.activate(SceneName.MENU.getValue());
    }

    @FXML
    public void switchToToysMenu() {
        ScreenController.activate(SceneName.TOY_MENU.getValue());
    }

    @FXML
    public void switchToToysList() {
        ScreenController.loadAndShowScene(SceneName.TOY_LIST.getValue());
    }

    @FXML
    public void switchToToyTypeMenu() {
        ScreenController.activate(SceneName.TOY_TYPE.getValue());
    }

    @FXML
    public void switchToGameRoomMenu() {
        ScreenController.activate(SceneName.GAME_ROOM_MENU.getValue());
    }

    @FXML
    public void switchToListOfGameRooms() {
        ScreenController.activate(SceneName.GAME_ROOM_LIST.getValue());
    }

    @FXML
    public void switchToCreateGameRoom() {
        ScreenController.loadAndShowScene(SceneName.GAME_ROOM_CREATOR.getValue());
    }

}
