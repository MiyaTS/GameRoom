package com.my.game_room.model;

import com.my.game_room.constant.AgeCategory;
import com.my.game_room.constant.SizeType;
import com.my.game_room.model.toy.Car;
import com.my.game_room.model.toy.Doll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameRoomTest {

    @Test
    void equals() {
        var mediumTeenCar = new Car(1L, "ToyName", "Text", 100.4,
                SizeType.MEDIUM, AgeCategory.TEEN, 400.4, "Silver", "Gold");
        var bigBabyDoll = new Doll(1L, "Doll", "Doll", 140.9,
                SizeType.BIG, AgeCategory.BABY, "Red", "Silver", "Song");
        var actualRoom = new GameRoom(
                1L, "RoomName", "Descr", 2300.0,
                SizeType.BIG, List.of(AgeCategory.ADULT, AgeCategory.TEEN, AgeCategory.BABY),
                List.of(mediumTeenCar, bigBabyDoll));
        var expectedRoom = new GameRoom(
                1L, "RoomName", "Descr", 2300.0,
                SizeType.BIG, List.of(AgeCategory.ADULT, AgeCategory.TEEN, AgeCategory.BABY),
                List.of(mediumTeenCar, bigBabyDoll));
        assertEquals(expectedRoom, actualRoom);
    }

    @Test
    void equals_false() {
        var mediumTeenCar = new Car(1L, "ToyName", "Text", 100.4,
                SizeType.MEDIUM, AgeCategory.TEEN, 400.4, "Silver", "Gold");
        var bigBabyDoll = new Doll(1L, "Doll", "Doll", 140.9,
                SizeType.BIG, AgeCategory.BABY, "Red", "Silver", "Song");
        var actualRoom = new GameRoom(
                1L, "RoomName", "Descr", 2300.0,
                SizeType.BIG, List.of(AgeCategory.ADULT, AgeCategory.TEEN, AgeCategory.BABY),
                List.of(mediumTeenCar, bigBabyDoll));
        var expectedRoom = new GameRoom(
                1L, "RoomName", "randomText", 2300.0,
                SizeType.SMALL, List.of(AgeCategory.TEEN, AgeCategory.BABY),
                List.of(bigBabyDoll));
        assertNotEquals(expectedRoom, actualRoom);
    }

    @Test
    void validate() {
        var mediumTeenCar = new Car(1L, "ToyName", "Text", 100.4,
                SizeType.MEDIUM, AgeCategory.TEEN, 400.4, "Silver", "Gold");
        var bigBabyDoll = new Doll(1L, "Doll", "Doll", 140.9,
                SizeType.BIG, AgeCategory.BABY, "Red", "Silver", "Song");
        var gameRoom = new GameRoom(
                1L, "RoomName", "Descr", 2300.0,
                SizeType.BIG, List.of(AgeCategory.ADULT, AgeCategory.TEEN, AgeCategory.BABY),
                List.of(mediumTeenCar, bigBabyDoll));
        assertTrue(gameRoom.validate());
    }

    @Test
    void validate_nameIsBlank() {
        var gameRoom = new GameRoom(
                1L, "     ", "Descr", 2300.0,
                SizeType.BIG, List.of(AgeCategory.ADULT, AgeCategory.TEEN, AgeCategory.BABY),
                new ArrayList<>());
        assertFalse(gameRoom.validate());
    }

    @Test
    void validate_incorrectToySize() {
        var mediumTeenCar = new Car(1L, "ToyName", "Text", 100.4,
                SizeType.MEDIUM, AgeCategory.TEEN, 400.4, "Silver", "Gold");
        var gameRoom = new GameRoom(
                1L, "RoomName", "Descr", 2300.0,
                SizeType.SMALL, List.of(AgeCategory.ADULT, AgeCategory.TEEN, AgeCategory.BABY),
                List.of(mediumTeenCar));
        assertFalse(gameRoom.validate());
    }

    @Test
    void validate_incorrectToyAge() {
        var mediumTeenCar = new Car(1L, "ToyName", "Text", 100.4,
                SizeType.MEDIUM, AgeCategory.TEEN, 400.4, "Silver", "Gold");
        var gameRoom = new GameRoom(
                1L, "RoomName", "Descr", 2300.0,
                SizeType.BIG, List.of(AgeCategory.BABY),
                List.of(mediumTeenCar));
        assertFalse(gameRoom.validate());
    }

    @Test
    void validate_incorrectTotalToyPrice() {
        var mediumTeenCar = new Car(1L, "ToyName", "Text", 100.4,
                SizeType.MEDIUM, AgeCategory.TEEN, 400.4, "Silver", "Gold");
        var bigBabyDoll = new Doll(1L, "Doll", "Doll", 140.9,
                SizeType.BIG, AgeCategory.BABY, "Red", "Silver", "Song");
        var gameRoom = new GameRoom(
                1L, "RoomName", "Descr", 1.0,
                SizeType.BIG, List.of(AgeCategory.ADULT, AgeCategory.TEEN, AgeCategory.BABY),
                List.of(mediumTeenCar, bigBabyDoll));
        assertFalse(gameRoom.validate());
    }

}