package com.my.game_room.controller.toy;

import com.my.game_room.ScreenController;
import com.my.game_room.constant.AgeCategory;
import com.my.game_room.constant.SceneName;
import com.my.game_room.constant.SizeType;
import com.my.game_room.constant.ToyType;
import com.my.game_room.model.toy.Car;
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

public class CarController implements Initializable {
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
    private TextField speedCar;
    @FXML
    private TextField materialCar;
    @FXML
    private TextField colorCar;
    private Car car;

    private final ToyService toyService;

    public CarController() {
        toyService = ToyService.getInstance();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        size.getItems().addAll(Arrays.stream(SizeType.values()).map(Enum::toString).toList());
        age.getItems().addAll(Arrays.stream(AgeCategory.values()).map(Enum::toString).toList());
        var toy = toyService.getSelectedToy();
        if (toy != null && ToyType.CAR == toy.getToyType()) {
            parseToy((Car) toy);
        } else {
            this.car = new Car();
            car.setId(-1L);
            car.setToyType(ToyType.CAR);
        }
    }

    private void parseToy(Car toy) {
        toyName.setText(toy.getName());
        toyDescription.setText(toy.getDescription());
        toyPrice.setText(String.valueOf(toy.getPrice()));
        age.setValue(toy.getAge().toString());
        size.setValue(toy.getSize().toString());
        speedCar.setText(String.valueOf(toy.getSpeed()));
        materialCar.setText(toy.getMaterial());
        colorCar.setText(toy.getColor());
        this.car = toy;
    }

    @FXML
    public void switchToToyTypeMenuController() {
        ScreenController.activate(SceneName.TOY_MENU.getValue());
    }

    @FXML
    public void createCar() {
        LOG.info("Створення машинки!");
        car.setName(toyName.getText());
        car.setDescription(toyDescription.getText());
        car.setPrice(Double.parseDouble(toyPrice.getText()));
        car.setSize(SizeType.valueOf(size.getValue()));
        car.setAge(AgeCategory.valueOf(age.getValue()));
        car.setSpeed(Double.parseDouble(speedCar.getText()));
        car.setMaterial(materialCar.getText());
        car.setColor(colorCar.getText());
        if (car.getId() >= 0) {
            if (!toyService.edit(car)) {
                LOG.warn("Помилка оновлення");
                return;
            }
        } else {
            if (!toyService.create(car)) {
                LOG.warn("Помилка створення");
                return;
            }
        }
        LOG.info("Машинка збережена успішно");
        switchToToyTypeMenuController();
    }

}
