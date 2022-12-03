package com.my.game_room.controller.toy;

import com.my.game_room.ScreenController;
import com.my.game_room.constant.SceneName;
import com.my.game_room.constant.SortRule;
import com.my.game_room.constant.SortType;
import com.my.game_room.model.toy.AbstractToy;
import com.my.game_room.service.ToyService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class ToyController implements Initializable {
    private final ToyService toyService;
    @FXML
    private ListView<String> listToyView;

    @FXML
    private ChoiceBox<String> sortTypeChoiceBox;
    @FXML
    private ChoiceBox<String> sortRuleChoiceBox;

    @FXML
    private Text errorMessage;

    public ToyController() {
        toyService = ToyService.getInstance();
    }

    @FXML
    public void switchToMenu() {
        ScreenController.activate(SceneName.TOY_MENU.getValue());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        errorMessage.setText("");
        sortTypeChoiceBox.getItems().addAll(Arrays.stream(SortType.values()).map(Enum::toString).toList());
        sortRuleChoiceBox.getItems().addAll(Arrays.stream(SortRule.values()).map(Enum::toString).toList());
    }

    @FXML
    public void remove() {
        int selectedId = listToyView.getSelectionModel().getSelectedIndex();
        var isDeleted = toyService.delete(selectedId);
        if (isDeleted) {
            listToyView.getItems().remove(selectedId);
        }
        errorMessage.setText("");
    }

    @FXML
    public void show() {
        fillListView(toyService.viewAllToys());
        errorMessage.setText("");
    }

    private void fillListView(List<AbstractToy> toys) {
        listToyView.getItems().clear();
        for (AbstractToy toy : toys) {
            listToyView.getItems().add(toy.toString());
        }
        errorMessage.setText("");
    }

    @FXML
    public void sort() {
        var sortType = sortTypeChoiceBox.getValue();
        var sortRule = sortRuleChoiceBox.getValue();
        if (sortType == null || sortRule == null) {
            errorMessage.setText("Оберіть всі параметри!");
            return;
        }
        var isDescending = SortRule.ByDecrease == SortRule.valueOf(sortRule);
        switch (SortType.valueOf(sortType)) {
            case ByAge -> {
                fillListView(toyService.sortToysByAge(isDescending));
            }
            case BySize -> {
                fillListView(toyService.sortToysBySize(isDescending));
            }
            case ByPrice -> {
                fillListView(toyService.sortToysByPrice(isDescending));
            }
        }
        errorMessage.setText("");
    }

    @FXML
    public void edit() {
        int selectedId = listToyView.getSelectionModel().getSelectedIndex();
        toyService.viewInfo(selectedId).ifPresentOrElse(this::loadEditModeByType, () -> {
            errorMessage.setText("Помилковий індекс, оновіть список");
        });
    }

    private void loadEditModeByType(AbstractToy toy) {
        switch (toy.getToyType()) {
            case CAR -> ScreenController.loadAndShowScene(SceneName.CAR_MENU.getValue());
            case DOLL -> ScreenController.loadAndShowScene(SceneName.DOLL_MENU.getValue());
            case CUBES -> ScreenController.loadAndShowScene(SceneName.CUBES_MENU.getValue());
            case MUSICAL -> ScreenController.loadAndShowScene(SceneName.MUSICAL_MENU.getValue());
        }
        errorMessage.setText("");
    }
}
