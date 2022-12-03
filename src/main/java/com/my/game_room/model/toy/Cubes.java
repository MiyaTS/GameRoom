package com.my.game_room.model.toy;

import com.my.game_room.constant.AgeCategory;
import com.my.game_room.constant.SizeType;
import com.my.game_room.constant.ToyType;

public class Cubes extends AbstractToy{
    private int amount;
    private double priceByOne;
    private double weight;

    public Cubes() {
    }

    public Cubes(long id, String name, String description, SizeType size, AgeCategory age, int amount,
                 double priceByOne, double weight) {
        super(id, name, description, ToyType.CUBES, size, age);
        this.amount = amount;
        this.priceByOne = priceByOne;
        this.weight = weight;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getPriceByOne() {
        return priceByOne;
    }

    public void setPriceByOne(double priceByOne) {
        this.priceByOne = priceByOne;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public double calcPrice() {
        return amount * priceByOne;
    }

    @Override
    public String toString() {
        return "Toy {Type='" + ToyType.CUBES + "', " +
                getStringTemplate() +
                ", amount='" + amount + '\'' +
                ", priceByOne='" + priceByOne + '\'' +
                ", weight='" + weight + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Cubes cubes = (Cubes) o;

        if (amount != cubes.amount) return false;
        if (Double.compare(cubes.priceByOne, priceByOne) != 0) return false;
        return Double.compare(cubes.weight, weight) == 0;
    }

}
