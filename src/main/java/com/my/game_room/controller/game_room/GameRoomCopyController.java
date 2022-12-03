package com.my.game_room.controller.game_room;

import com.my.game_room.ScreenController;
import com.my.game_room.constant.SceneName;
import com.my.game_room.model.GameRoom;
import com.my.game_room.service.GameRoomService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class GameRoomCopyController implements Initializable {
    private final GameRoomService gameRoomService;
    private GameRoom gameRoom;
    @FXML
    private TextArea gameRoomInfo;
    @FXML
    private TextField newName;

    @FXML
    private Text errorMessage;

    public GameRoomCopyController() {
        gameRoomService = GameRoomService.getInstance();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gameRoom = gameRoomService.getSelectedRoom();
        gameRoomInfo.setText(gameRoom.toString());
        newName.setText(gameRoom.getName());
        errorMessage.setText("");
    }

    @FXML
    public void switchBack() {
        ScreenController.activate(SceneName.GAME_ROOM_LIST.getValue());
    }

    @FXML
    public void saveGameRoom() {
        if (gameRoomService.renameRoom(gameRoom, newName.getText())) {
            switchBack();
        } else {
            errorMessage.setText("Помилка при збереженні. Некоректне ім'я");
        }

    }
}
