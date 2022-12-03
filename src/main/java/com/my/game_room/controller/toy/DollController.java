package com.my.game_room.controller.toy;

import com.my.game_room.ScreenController;
import com.my.game_room.constant.AgeCategory;
import com.my.game_room.constant.SceneName;
import com.my.game_room.constant.SizeType;
import com.my.game_room.constant.ToyType;
import com.my.game_room.model.toy.Doll;
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

public class DollController implements Initializable {
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
    private TextField hairColorDoll;
    @FXML
    private TextField specialAbilityDoll;
    @FXML
    private TextField jewelleryDoll;

    private Doll doll;

    private final ToyService toyService;

    public DollController() {
        toyService = ToyService.getInstance();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        size.getItems().addAll(Arrays.stream(SizeType.values()).map(Enum::toString).toList());
        age.getItems().addAll(Arrays.stream(AgeCategory.values()).map(Enum::toString).toList());
        var toy = toyService.getSelectedToy();
        if (toy != null && ToyType.DOLL == toy.getToyType()) {
            parseToy((Doll) toy);
        } else {
            this.doll = new Doll();
            doll.setId(-1L);
            doll.setToyType(ToyType.DOLL);
        }
    }

    private void parseToy(Doll toy) {
        toyName.setText(toy.getName());
        toyDescription.setText(toy.getDescription());
        toyPrice.setText(String.valueOf(toy.getPrice()));
        age.setValue(toy.getAge().toString());
        size.setValue(toy.getSize().toString());
        hairColorDoll.setText(toy.getHairColor());
        specialAbilityDoll.setText(toy.getSpecialAbility());
        jewelleryDoll.setText(toy.getJewellery());
        this.doll = toy;
    }

    @FXML
    public void switchToToyTypeMenuController() {
        ScreenController.activate(SceneName.TOY_MENU.getValue());
    }

    @FXML
    public void createDoll() {
        LOG.info("Створення ляльки!");
        doll.setName(toyName.getText());
        doll.setDescription(toyDescription.getText());
        doll.setPrice(Double.parseDouble(toyPrice.getText()));
        doll.setSize(SizeType.valueOf(size.getValue()));
        doll.setAge(AgeCategory.valueOf(age.getValue()));
        doll.setHairColor(hairColorDoll.getText());
        doll.setSpecialAbility(specialAbilityDoll.getText());
        doll.setJewellery(jewelleryDoll.getText());
        if (doll.getId() >= 0) {
            if (!toyService.edit(doll)) {
                LOG.warn("Помилка оновлення");
                return;
            }
        } else {
            if (!toyService.create(doll)) {
                LOG.warn("Помилка створення");
                return;
            }
        }
        LOG.info("Лялька збережена успішно");
        switchToToyTypeMenuController();
    }
}
