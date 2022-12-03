package com.my.game_room.model.toy;

import com.my.game_room.constant.AgeCategory;
import com.my.game_room.constant.SizeType;
import com.my.game_room.constant.ToyType;

import java.util.Objects;

public class Doll extends AbstractToy {
    private String hairColor;
    private String specialAbility;
    private String jewellery;

    public Doll() {
    }

    public Doll(long id, String name, String description, double price, SizeType size, AgeCategory age, String hairColor, String specialAbility, String jewellery) {
        super(id, name, description, price, ToyType.DOLL, size, age);
        this.hairColor = hairColor;
        this.specialAbility = specialAbility;
        this.jewellery = jewellery;
    }

    public String getHairColor() {
        return hairColor;
    }

    public void setHairColor(String hairColor) {
        this.hairColor = hairColor;
    }

    public String getJewellery() {
        return jewellery;
    }

    public void setJewellery(String jewellery) {
        this.jewellery = jewellery;
    }

    public String getSpecialAbility() {
        return specialAbility;
    }

    public void setSpecialAbility(String specialAbility) {
        this.specialAbility = specialAbility;
    }

    @Override
    public double calcPrice() {
        if (!Objects.equals(jewellery, "-")) {
            return getPrice() * 2;
        }
        return getPrice();
    }

    @Override
    public String toString() {
        return "Toy {Type='" + ToyType.DOLL + "', " +
                getStringTemplate() +
                ", hairColor='" + hairColor + '\'' +
                ", specialAbility='" + specialAbility + '\'' +
                ", jewellery='" + jewellery + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Doll doll = (Doll) o;

        if (!hairColor.equals(doll.hairColor)) return false;
        if (!specialAbility.equals(doll.specialAbility)) return false;
        return Objects.equals(jewellery, doll.jewellery);
    }

}
