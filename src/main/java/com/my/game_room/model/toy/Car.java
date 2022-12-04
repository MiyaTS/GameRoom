package com.my.game_room.model.toy;

import com.my.game_room.constant.AgeCategory;
import com.my.game_room.constant.SizeType;
import com.my.game_room.constant.ToyType;

public class Car extends AbstractToy {
    private double speed;
    private String material;
    private String color;

    public Car() {
    }

    public Car(long id, String name, String description, double price, SizeType size, AgeCategory age, double speed, String material, String color) {
        super(id, name, description, price, ToyType.CAR, size, age);
        this.speed = speed;
        this.material = material;
        this.color = color;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public double calcPrice() {
        return getPrice();
    }

    @Override
    public String toString() {
        return "Toy {Type='" + ToyType.CAR + "', " +
                getStringTemplate() +
                ", speed='" + speed + '\'' +
                ", material='" + material + '\'' +
                ", color='" + color + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Car car = (Car) o;

        if (Double.compare(car.speed, speed) != 0) return false;
        if (!material.equals(car.material)) return false;
        return color.equals(car.color);
    }

}
