package com.my.game_room.controller.toy;

import com.my.game_room.ScreenController;
import com.my.game_room.constant.AgeCategory;
import com.my.game_room.constant.SceneName;
import com.my.game_room.constant.SizeType;
import com.my.game_room.constant.ToyType;
import com.my.game_room.model.toy.Musical;
import com.my.game_room.service.ToyService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class MusicalController implements Initializable {
    private static final Logger LOG = LogManager.getLogger();

    //Common ToyData
    @FXML
    private TextField toyName;
    @FXML
    private TextField toyDescription;
    @FXML
    private TextField toyPrice;
    @FXML
    private ChoiceBox<String> age;
    @FXML
    private ChoiceBox<String> size;

    @FXML
    private TextField autoMusical;
    @FXML
    private TextField typeMusical;
    @FXML
    private TextField materialMusical;

    private Musical musical;
    private final ToyService toyService;

    public MusicalController() {
        toyService = ToyService.getInstance();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        size.getItems().addAll(Arrays.stream(SizeType.values()).map(Enum::toString).toList());
        age.getItems().addAll(Arrays.stream(AgeCategory.values()).map(Enum::toString).toList());
        var toy = toyService.getSelectedToy();
        if (toy != null && ToyType.MUSICAL == toy.getToyType()) {
            parseToy((Musical) toy);
        } else {
            this.musical = new Musical();
            musical.setId(-1L);
            musical.setToyType(ToyType.MUSICAL);
        }
    }

    private void parseToy(Musical toy) {
        toyName.setText(toy.getName());
        toyDescription.setText(toy.getDescription());
        toyPrice.setText(String.valueOf(toy.getPrice()));
        age.setValue(toy.getAge().toString());
        size.setValue(toy.getSize().toString());
        autoMusical.setText(String.valueOf(toy.isAutoPlayEnabled()));
        typeMusical.setText(toy.getMusicType());
        materialMusical.setText(toy.getMaterial());
        this.musical = toy;
    }

    @FXML
    public void switchToToyTypeMenuController() {
        ScreenController.activate(SceneName.TOY_MENU.getValue());
    }


    @FXML
    public void createMusical() {
        LOG.info("Створення музикальної іграшки!");
        musical.setName(toyName.getText());
        musical.setDescription(toyDescription.getText());
        musical.setPrice(Double.parseDouble(toyPrice.getText()));
        musical.setSize(SizeType.valueOf(size.getValue()));
        musical.setAge(AgeCategory.valueOf(age.getValue()));
        musical.setAutoPlayEnabled(Boolean.parseBoolean(autoMusical.getText()));
        musical.setMusicType(typeMusical.getText());
        musical.setMaterial(materialMusical.getText());
        if (musical.getId() >= 0) {
            if (!toyService.edit(musical)) {
                LOG.warn("Помилка оновлення");
                return;
            }
        } else {
            if (!toyService.create(musical)) {
                LOG.warn("Помилка створення");
                return;
            }
        }
        LOG.info("Музикальна іграшка збережена успішно");
        switchToToyTypeMenuController();
    }
}
