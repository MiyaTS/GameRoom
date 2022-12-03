package com.my.game_room.model;

import com.my.game_room.constant.AgeCategory;
import com.my.game_room.constant.SizeType;
import com.my.game_room.model.toy.AbstractToy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class GameRoom {
    private static final Logger LOG = LogManager.getLogger();
    private long id;
    private String name;
    private String description;
    private double totalPrice;
    private SizeType size;
    private List<AgeCategory> ages;
    private List<AbstractToy> toys;

    public GameRoom() {
    }

    public GameRoom(long id, String name, String description, double totalPrice, SizeType size) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.totalPrice = totalPrice;
        this.size = size;
        this.ages = new ArrayList<>();
        this.toys = new ArrayList<>();
    }

    public GameRoom(long id, String name, String description, double totalPrice, SizeType size, List<AgeCategory> ages, List<AbstractToy> toys) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.totalPrice = totalPrice;
        this.size = size;
        this.ages = ages;
        this.toys = toys;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public SizeType getSize() {
        return size;
    }

    public void setSize(SizeType size) {
        this.size = size;
    }

    public List<AgeCategory> getAges() {
        return ages;
    }

    public void setAges(List<AgeCategory> ages) {
        this.ages = ages;
    }

    public List<AbstractToy> getToys() {
        return toys;
    }

    public void setToys(List<AbstractToy> toys) {
        this.toys = toys;
    }

    /**
     * validation
     * @return boolean if room has correct business state
     */
    public boolean validate() {
        double toyPrice = 0;
        if (name.isBlank()) {
            LOG.warn("Порожнє імя!");
            return false;
        }
        for (AbstractToy toy : getToys()) {
            toyPrice += toy.calcPrice();
            if (toy.getSize().ordinal() > getSize().ordinal()) {
                LOG.warn("Неправильний розмір!");
                return false;
            }
            if (!getAges().contains(toy.getAge())) {
                LOG.warn("Неправильний вік!");
                return false;
            }
        }
        if (toyPrice > getTotalPrice()) {
            LOG.warn("Занадто велика сукупна вартість!");
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "GameRoom{" + System.lineSeparator() +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", totalPrice='" + totalPrice + '\'' +
                ", size='" + size + "', " +
                System.lineSeparator() +
                "ages: '" + agesToString() + "'," +
                System.lineSeparator() +
                "toys: " + toysToString() +
                System.lineSeparator() +
                '}';
    }

    private String agesToString() {
        StringBuilder line = new StringBuilder();
        for (AgeCategory ageCategory : ages) {
            line.append(ageCategory).append(" ");
        }
        return line.toString();
    }

    private String toysToString() {
        StringBuilder line = new StringBuilder();
        for (AbstractToy toy : toys) {
            line.append(System.lineSeparator());
            line.append(toy);
        }
        return line.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameRoom room = (GameRoom) o;

        if (id != room.id) return false;
        if (Double.compare(room.totalPrice, totalPrice) != 0) return false;
        if (!name.equals(room.name)) return false;
        if (!description.equals(room.description)) return false;
        if (size != room.size) return false;
        if (!ages.equals(room.ages)) return false;
        return toys.equals(room.toys);
    }

}
