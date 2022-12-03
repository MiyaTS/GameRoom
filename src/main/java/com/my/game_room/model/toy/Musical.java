package com.my.game_room.model.toy;

import com.my.game_room.constant.AgeCategory;
import com.my.game_room.constant.SizeType;
import com.my.game_room.constant.ToyType;

public class Musical extends AbstractToy {
    private boolean isAutoPlayEnabled;
    private String musicType;
    private String material;

    public Musical() {
    }

    public Musical(long id, String name, String description, double price, SizeType size, AgeCategory age, boolean isAutoPlayEnabled, String musicType, String material) {
        super(id, name, description, price, ToyType.MUSICAL, size, age);
        this.isAutoPlayEnabled = isAutoPlayEnabled;
        this.musicType = musicType;
        this.material = material;
    }

    public boolean isAutoPlayEnabled() {
        return isAutoPlayEnabled;
    }

    public void setAutoPlayEnabled(boolean autoPlayEnabled) {
        isAutoPlayEnabled = autoPlayEnabled;
    }

    public String getMusicType() {
        return musicType;
    }

    public void setMusicType(String musicType) {
        this.musicType = musicType;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    @Override
    public double calcPrice() {
        return isAutoPlayEnabled ? getPrice() * 2 : getPrice();
    }

    @Override
    public String toString() {
        return "Toy {Type='" + ToyType.MUSICAL + "', " +
                getStringTemplate() +
                ", musicType='" + musicType + '\'' +
                ", material='" + material + '\'' +
                ", isAutoPlayEnabled='" + isAutoPlayEnabled + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Musical musical = (Musical) o;

        if (isAutoPlayEnabled != musical.isAutoPlayEnabled) return false;
        if (!musicType.equals(musical.musicType)) return false;
        return material.equals(musical.material);
    }

}
