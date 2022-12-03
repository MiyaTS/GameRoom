package com.my.game_room.service;

import com.my.game_room.constant.AgeCategory;
import com.my.game_room.constant.SizeType;
import com.my.game_room.dao.Dao;
import com.my.game_room.model.toy.*;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class ToyServiceTest {

    ToyService toyService;

    @Mock
    Dao<AbstractToy> toyDao;

    List<AbstractToy> starterList;
    Car starterMediumCar;
    Doll starterSmallDoll;
    Cubes starterHugeCubes;
    Musical starterBigMusical;

    @BeforeEach
    public void setup() {
        toyService = ToyService.getInstance();
        toyService.setToyDao(toyDao);
        starterMediumCar = new Car(2L, "Name2", "Big Desc2r", 1.4, SizeType.MEDIUM, AgeCategory.TEEN, 404.1, "Silver", "Blue");
        starterSmallDoll = new Doll(1L, "Text", "Text", 300.4, SizeType.SMALL, AgeCategory.ADULT, "Text", "Text", "");
        starterHugeCubes = new Cubes(2L, "Text", "Text", SizeType.HUGE, AgeCategory.TEEN, 20, 20.0, 120.5);
        starterBigMusical = new Musical(3L, "Text", "Text", 1000.4, SizeType.BIG, AgeCategory.TODDLER, true, "Text", "Text");
    }

    @Test
    void getInstance() {
        var toyService = ToyService.getInstance();
        Assertions.assertNotNull(toyService);
        Assertions.assertInstanceOf(ToyService.class, toyService);

    }

    @Test
    void sortToysByPrice() {
        starterList = new ArrayList<>();
        starterList.add(starterMediumCar);
        starterList.add(starterSmallDoll);
        starterList.add(starterBigMusical);
        starterList.add(starterHugeCubes);
        List<AbstractToy> expectedList = List.of(starterMediumCar, starterHugeCubes, starterSmallDoll, starterBigMusical);
        Mockito.when(toyDao.getAll()).thenReturn(starterList);
        toyService.viewAllToys();
        var actualList = toyService.sortToysByPrice(false);
        Assertions.assertEquals(expectedList, actualList);
    }

    @Test
    void sortToysBySize() {
        starterList = new ArrayList<>();
        starterList.add(starterMediumCar);
        starterList.add(starterSmallDoll);
        starterList.add(starterBigMusical);
        starterList.add(starterHugeCubes);
        List<AbstractToy> expectedList = List.of(starterHugeCubes, starterBigMusical, starterMediumCar, starterSmallDoll);
        Mockito.when(toyDao.getAll()).thenReturn(starterList);
        toyService.viewAllToys();
        var actualList = toyService.sortToysBySize(true);
        Assertions.assertEquals(expectedList, actualList);
    }

    @Test
    void sortToysByAge() {
        starterList = new ArrayList<>();
        starterList.add(starterMediumCar);
        starterList.add(starterSmallDoll);
        starterList.add(starterBigMusical);
        starterList.add(starterHugeCubes);
        List<AbstractToy> expectedList = List.of(starterSmallDoll, starterMediumCar, starterHugeCubes, starterBigMusical);
        Mockito.when(toyDao.getAll()).thenReturn(starterList);
        toyService.viewAllToys();
        var actualList = toyService.sortToysByAge(true);
        Assertions.assertEquals(expectedList, actualList);
    }

    @Test
    void create() {
        toyService.create(starterMediumCar);
        Mockito.verify(toyDao).save(starterMediumCar);
    }

    @Test
    void edit() {
        Mockito.when(toyDao.update(starterMediumCar)).thenReturn(true);
        var res = toyService.edit(starterMediumCar);
        Assertions.assertTrue(res);
        Mockito.verify(toyDao).update(starterMediumCar);
    }

    @Test
    void edit_Invalid() {
        var res = toyService.edit(null);
        Assertions.assertFalse(res);
    }

    @Test
    void delete() {
        var list = new ArrayList<AbstractToy>();
        list.add(starterMediumCar);
        Mockito.when(toyDao.getAll()).thenReturn(list);
        toyService.viewAllToys();
        toyService.delete(0);
        Mockito.verify(toyDao).delete(2L);
    }

    @Test
    void viewInfo() {
        Mockito.when(toyDao.getAll()).thenReturn(List.of(starterMediumCar));
        toyService.viewAllToys();
        toyService.viewAllToys();
        var res = toyService.viewInfo(0);
        Assertions.assertTrue(res.isPresent());
        Assertions.assertEquals(starterMediumCar, res.get());
    }

    @Test
    void shouldFailIndex_viewInfo() {
        Mockito.when(toyDao.getAll()).thenReturn(List.of(starterMediumCar));
        toyService.viewAllToys();
        var res = toyService.viewInfo(-10);
        Assertions.assertTrue(res.isEmpty());
    }

    @Test
    void viewAllToys() {
        var expectedList = List.of(starterMediumCar);
        Mockito.when(toyDao.getAll()).thenReturn(List.of(starterMediumCar));
        var list = toyService.viewAllToys();
        Assertions.assertEquals(expectedList, list);
    }
}