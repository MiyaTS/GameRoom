package com.my.game_room.controller.game_room;

import com.my.game_room.ScreenController;
import com.my.game_room.constant.SceneName;
import com.my.game_room.model.GameRoom;
import com.my.game_room.service.GameRoomService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.util.List;

public class GameRoomController {

    private final GameRoomService gameRoomService;
    @FXML
    private ListView<String> gameRoomsView;
    @FXML
    private Text errorMessage;

    public GameRoomController() {
        gameRoomService = GameRoomService.getInstance();
    }

    @FXML
    public void switchToMenu() {
        ScreenController.activate(SceneName.GAME_ROOM_MENU.getValue());
    }

    @FXML
    public void remove() {
        int selectedId = gameRoomsView.getSelectionModel().getSelectedIndex();
        var isDeleted = gameRoomService.delete(selectedId);
        if (isDeleted) {
            gameRoomsView.getItems().remove(selectedId);
        }
        errorMessage.setText("");
    }

    @FXML
    public void show() {
        fillListView(gameRoomService.viewRooms());
        errorMessage.setText("");
    }

    private void fillListView(List<GameRoom> gameRooms) {
        gameRoomsView.getItems().clear();
        for (GameRoom gameRoom : gameRooms) {
            gameRoomsView.getItems().add(gameRoom.toString());
        }
        errorMessage.setText("");
    }

    @FXML
    public void edit() {
        int selectedId = gameRoomsView.getSelectionModel().getSelectedIndex();
        gameRoomService.viewInfo(selectedId).ifPresentOrElse(this::loadEditMode, () -> {
            errorMessage.setText("Некоректний індекс. Обновіть список кімнат");
        });
    }

    private void loadEditMode(GameRoom gameRoom) {
        if (gameRoom == null) {
            return;
        }
        ScreenController.loadAndShowScene(SceneName.GAME_ROOM_CREATOR.getValue());
    }

    @FXML
    public void copy() {
        int selectedId = gameRoomsView.getSelectionModel().getSelectedIndex();
        gameRoomService
                .copyRoom(selectedId)
                .ifPresent(this::openRenameMenu);
        errorMessage.setText("");
    }

    private void openRenameMenu(GameRoom room) {
        ScreenController.loadAndShowScene(SceneName.GAME_ROOM_COPY.getValue());
    }

}
