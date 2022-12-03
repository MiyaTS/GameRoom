package com.my.game_room.controller.game_room;

import com.my.game_room.ScreenController;
import com.my.game_room.constant.AgeCategory;
import com.my.game_room.constant.SceneName;
import com.my.game_room.constant.SizeType;
import com.my.game_room.constant.ToyType;
import com.my.game_room.model.GameRoom;
import com.my.game_room.model.toy.Car;
import com.my.game_room.service.GameRoomService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class GameRoomCreatorController implements Initializable {
    private static final Logger LOG = LogManager.getLogger();
    private final GameRoomService gameRoomService;
    @FXML
    private Label errorMessage;
    @FXML
    private TextField gameRoomName;
    @FXML
    private TextField gameRoomDescription;

    @FXML
    private TextField gameRoomPrice;

    @FXML
    private ChoiceBox<String> sizeGameRoom;
    @FXML
    private ChoiceBox<String> ageGameRoom;

    private GameRoom gameRoom;

    public GameRoomCreatorController() {
        gameRoomService = GameRoomService.getInstance();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        errorMessage.setText("");
        sizeGameRoom.getItems().addAll(Arrays.stream(SizeType.values()).map(Enum::toString).toList());
        ageGameRoom.getItems().addAll(Arrays.stream(AgeCategory.values()).map(Enum::toString).toList());
        var room = gameRoomService.getSelectedRoom();
        if (room != null) {
            parse(room);
        } else {
            this.gameRoom = new GameRoom();
            gameRoom.setId(-1L);
            gameRoom.setToys(new ArrayList<>());
        }
    }

    @FXML
    public void switchToMenu() {
        ScreenController.activate(SceneName.GAME_ROOM_MENU.getValue());
    }

    @FXML
    public void saveGameRoom() {
        formRoom();
        if (gameRoom.getName().isBlank()) {
            LOG.warn("Неправильна назва");
            errorMessage.setText("Неправильна назва!");
            return;
        }
        gameRoomService.prepareRoom(gameRoom);
        ScreenController.loadAndShowScene(SceneName.ADD_TOY.getValue());
    }

    private void parse(GameRoom gameRoom) {
        gameRoomName.setText(gameRoom.getName());
        gameRoomDescription.setText(gameRoom.getDescription());
        gameRoomPrice.setText(String.valueOf(gameRoom.getTotalPrice()));
        sizeGameRoom.setValue(gameRoom.getSize().toString());
        var ages = gameRoom.getAges();
        ageGameRoom.setValue(ages.get(ages.size() - 1).toString());
        this.gameRoom = gameRoom;
    }

    private void formRoom() {
        gameRoom.setName(gameRoomName.getText());
        gameRoom.setDescription(gameRoomDescription.getText());
        gameRoom.setTotalPrice(Double.parseDouble(gameRoomPrice.getText()));
        gameRoom.setSize(SizeType.valueOf(sizeGameRoom.getValue()));
        var age = AgeCategory.valueOf(ageGameRoom.getValue());
        gameRoom.setAges(
                Arrays.stream(AgeCategory.values())
                        .filter((element) -> element.ordinal() <= age.ordinal())
                        .collect(Collectors.toList()));

    }
}
