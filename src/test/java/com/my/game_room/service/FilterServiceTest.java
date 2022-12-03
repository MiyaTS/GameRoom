package com.my.game_room.service;

import com.my.game_room.constant.AgeCategory;
import com.my.game_room.constant.SizeType;
import com.my.game_room.constant.ToyType;
import com.my.game_room.model.toy.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class FilterServiceTest {

    FilterService filterService;

    List<AbstractToy> starterList;
    Car sortHugeCar;
    Car starterMediumCar;
    Doll starterSmallDoll;
    Cubes starterHugeCubes;
    Musical starterBigMusical;

    @BeforeEach
    public void setup() {
        filterService = FilterService.getInstance();
        starterMediumCar = new Car(2L, "Name2", "Big Desc2r", 1.4, SizeType.MEDIUM, AgeCategory.TEEN, 404.1, "Silver", "Blue");
        starterSmallDoll = new Doll(1L, "Text", "Text", 300.4, SizeType.SMALL, AgeCategory.ADULT, "Text", "Text", "");
        starterHugeCubes = new Cubes(2L, "Text", "Text", SizeType.HUGE, AgeCategory.TEEN, 20, 20.0, 120.5);
        starterBigMusical = new Musical(3L, "Text", "Text", 1000.4, SizeType.BIG, AgeCategory.TODDLER, true, "Text", "Text");
        starterList = List.of(
                starterMediumCar,
                starterSmallDoll,
                starterHugeCubes,
                starterBigMusical
        );
    }


    @Test
    void filterByType() {
        List<AbstractToy> expectedList = List.of(starterMediumCar);
        var actualList = filterService.filterByType(starterList, ToyType.CAR);
        Assertions.assertEquals(expectedList, actualList);
    }

    @Test
    void filterByPrice() {
        var price = 20.0;
        List<AbstractToy> smallList = List.of(starterMediumCar);
        var actualSmallList = filterService.filterByPrice(starterList, price, true);
        Assertions.assertEquals(smallList, actualSmallList);

        List<AbstractToy> bigList = List.of(starterSmallDoll, starterHugeCubes, starterBigMusical);
        var actualBigList = filterService.filterByPrice(starterList, price, false);
        Assertions.assertEquals(bigList, actualBigList);
    }

    @Test
    void filterBySize() {
        List<AbstractToy> expectedList = List.of(starterSmallDoll);
        var actualList = filterService.filterBySize(starterList, SizeType.SMALL);
        Assertions.assertEquals(expectedList, actualList);
    }

    @Test
    void filterByAge() {
        List<AbstractToy> expectedList = List.of(starterMediumCar, starterHugeCubes);
        var actualList = filterService.filterByAge(starterList, AgeCategory.TEEN);
        Assertions.assertEquals(expectedList, actualList);
    }

    @Test
    void sortByPrice() {
        List<AbstractToy> expectedList = List.of(starterMediumCar, starterHugeCubes, starterSmallDoll, starterBigMusical);
        boolean isDescending = false;
        var actualList = filterService.sortByPrice(starterList, isDescending);
        Assertions.assertEquals(expectedList, actualList);
    }

    @Test
    void sortBySize() {
        List<AbstractToy> expectedList = List.of(starterHugeCubes, starterBigMusical, starterMediumCar, starterSmallDoll);
        boolean isDescending = true;
        var actualList = filterService.sortBySize(starterList, isDescending);
        Assertions.assertEquals(expectedList, actualList);
    }

    @Test
    void sortByAge() {
        List<AbstractToy> expectedList = List.of(starterSmallDoll, starterMediumCar, starterHugeCubes, starterBigMusical);
        boolean isDescending = true;
        var actualList = filterService.sortByAge(starterList, isDescending);
        Assertions.assertEquals(expectedList, actualList);
    }
}