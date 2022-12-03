package com.my.game_room.controller.toy;

import com.my.game_room.ScreenController;
import com.my.game_room.constant.AgeCategory;
import com.my.game_room.constant.SceneName;
import com.my.game_room.constant.SizeType;
import com.my.game_room.constant.ToyType;
import com.my.game_room.model.toy.Cubes;
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

public class CubesController implements Initializable {
    private static final Logger LOG = LogManager.getLogger();

    //Common ToyData
    @FXML
    private TextField toyName;
    @FXML
    private TextField toyDescription;
    @FXML
    private ChoiceBox<String> age;
    @FXML
    private ChoiceBox<String> size;

    @FXML
    private TextField amountCubes;
    @FXML
    private TextField priceByOneCubes;
    @FXML
    private TextField weightCubes;

    private Cubes cubes;
    private final ToyService toyService;

    public CubesController() {
        toyService = ToyService.getInstance();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        size.getItems().addAll(Arrays.stream(SizeType.values()).map(Enum::toString).toList());
        age.getItems().addAll(Arrays.stream(AgeCategory.values()).map(Enum::toString).toList());
        var toy = toyService.getSelectedToy();
        if (toy != null && ToyType.CUBES == toy.getToyType()) {
            parseToy((Cubes) toy);
        } else {
            this.cubes = new Cubes();
            cubes.setId(-1L);
            cubes.setToyType(ToyType.CUBES);
        }
    }

    private void parseToy(Cubes toy) {
        toyName.setText(toy.getName());
        toyDescription.setText(toy.getDescription());
        age.setValue(toy.getAge().toString());
        size.setValue(toy.getSize().toString());
        amountCubes.setText(String.valueOf(toy.getAmount()));
        priceByOneCubes.setText(String.valueOf(toy.getPriceByOne()));
        weightCubes.setText(String.valueOf(toy.getWeight()));
        this.cubes = toy;
    }

    @FXML
    public void switchToToyTypeMenuController() {
        ScreenController.activate(SceneName.TOY_MENU.getValue());
    }

    @FXML
    public void createCubes() {
        LOG.info("Створення кубиків!");
        cubes.setName(toyName.getText());
        cubes.setDescription(toyDescription.getText());
        cubes.setSize(SizeType.valueOf(size.getValue()));
        cubes.setAge(AgeCategory.valueOf(age.getValue()));
        cubes.setAmount(Integer.parseInt(amountCubes.getText()));
        cubes.setPriceByOne(Double.parseDouble(priceByOneCubes.getText()));
        cubes.setWeight(Double.parseDouble(weightCubes.getText()));
        if (cubes.getId() >= 0) {
            if (!toyService.edit(cubes)) {
                LOG.warn("Помилка оновлення");
                return;
            }
        } else {
            if (!toyService.create(cubes)) {
                LOG.warn("Помилка створення");
                return;
            }
        }
        LOG.info("Кубик збережено успішно");
        switchToToyTypeMenuController();
    }

}
