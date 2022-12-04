package com.my.game_room.controller.game_room;

import com.my.game_room.ScreenController;
import com.my.game_room.constant.*;
import com.my.game_room.model.GameRoom;
import com.my.game_room.model.toy.AbstractToy;
import com.my.game_room.service.FilterService;
import com.my.game_room.service.GameRoomService;
import com.my.game_room.service.ToyService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class ToySectionController implements Initializable {

    private static final Logger LOG = LogManager.getLogger();
    private final GameRoomService gameRoomService;
    private final ToyService toyService;
    private final FilterService filterService;
    private GameRoom room;

    @FXML
    private ListView<String> toysInGameRoomListView;
    @FXML
    private ListView<String> toyStoreListView;

    @FXML
    private TextField price;

    @FXML
    private ChoiceBox<String> filterType;
    @FXML
    private ChoiceBox<String> filterSize;
    @FXML
    private ChoiceBox<String> filterAge;
    @FXML
    private ChoiceBox<String> filterRule;

    @FXML
    private Text errorMessage;

    public ToySectionController() {
        toyService = ToyService.getInstance();
        filterService = FilterService.getInstance();
        gameRoomService = GameRoomService.getInstance();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        errorMessage.setText("");
        filterType.getItems().addAll(Arrays.stream(ToyType.values()).map(Enum::toString).toList());
        filterSize.getItems().addAll(Arrays.stream(SizeType.values()).map(Enum::toString).toList());
        filterAge.getItems().addAll(Arrays.stream(AgeCategory.values()).map(Enum::toString).toList());
        filterRule.getItems().addAll(Arrays.stream(SortRule.values()).map(Enum::toString).toList());
        room = gameRoomService.getSelectedRoom();
    }

    @FXML
    public void switchToMenu() {
        gameRoomService.saveToMemory(room);
        ScreenController.loadAndShowScene(SceneName.GAME_ROOM_CREATOR.getValue());
    }

    @FXML
    public void save() {
        if (!gameRoomService.save(room)) {
            LOG.warn("Помилка збереження");
            errorMessage.setText("Некоректні іграшки!");
            return;
        }
        ScreenController.activate(SceneName.GAME_ROOM_MENU.getValue());
    }

    @FXML
    public void add() {
        var indices = toyStoreListView.getSelectionModel().getSelectedIndices();
        for (int index : indices) {
            toyService.getToyFromStore(index).ifPresent(this::addToRoomToyList);
        }
    }

    private void addToRoomToyList(AbstractToy toy) {
        if (room.getToys().contains(toy)) {
            LOG.warn("Така іграшка вже добавлена");
            errorMessage.setText("Така іграшка вже добавлена");
            return;
        }
        room.getToys().add(toy);
        toysInGameRoomListView.getItems().add(toy.toString());
        errorMessage.setText("");
    }

    @FXML
    public void remove() {
        var indices = toysInGameRoomListView.getSelectionModel().getSelectedIndices();
        for (int index : indices) {
            if (index >= 0 && room.getToys().size() > index) {
                removeFromRoomToyList(room.getToys().get(index));
            }
        }
    }

    private void removeFromRoomToyList(AbstractToy toy) {
        if (!room.getToys().contains(toy)) {
            LOG.warn("Такої іграшки немає");
            errorMessage.setText("Такої іграшки немає");
            return;
        }
        room.getToys().remove(toy);
        toysInGameRoomListView.getItems().remove(toy.toString());
        errorMessage.setText("");
    }

    @FXML
    public void filter() {
        var currentList = toyService.viewAllToys();
        var typeToFilter = filterType.getValue();
        var sizeToFilter = filterSize.getValue();
        var ageToFilter = filterAge.getValue();
        var priceToOrder = price.getText();
        if (typeToFilter != null) {
            currentList = filterService.filterByType(currentList, ToyType.valueOf(typeToFilter));
        }
        if (sizeToFilter != null) {
            currentList = filterService.filterBySize(currentList, SizeType.valueOf(sizeToFilter));
        }
        if (ageToFilter != null) {
            currentList = filterService.filterByAge(currentList, AgeCategory.valueOf(ageToFilter));
        }
        if (!priceToOrder.isBlank()) {
            var isDescending = SortRule.ByDecrease.equals(SortRule.valueOf(filterRule.getValue()));
            currentList = filterService.filterByPrice(currentList, Double.parseDouble(priceToOrder), isDescending);
        }
        toyStoreListView.getItems().clear();
        fillListView(currentList, toyStoreListView);
        errorMessage.setText("");
    }

    @FXML
    public void resetFilter() {
        filterType.setValue(null);
        filterSize.setValue(null);
        filterAge.setValue(null);
        filterRule.setValue(null);
        price.setText("");
        errorMessage.setText("");
    }

    @FXML
    public void show() {
        fillListView(toyService.viewAllToys(), toyStoreListView);
        fillListView(room.getToys(), toysInGameRoomListView);
        errorMessage.setText("");
    }


    private void fillListView(List<AbstractToy> toys, ListView<String> listView) {
        listView.getItems().clear();
        for (AbstractToy toy : toys) {
            listView.getItems().add(toy.toString());
        }
    }
}
