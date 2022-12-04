package com.my.game_room.service;

import com.my.game_room.dao.Dao;
import com.my.game_room.model.GameRoom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class GameRoomServiceTest {

    GameRoomService gameRoomService;

    GameRoom gameRoom;

    @Mock
    Dao<GameRoom> gameRoomDao;

    @BeforeEach
    void setup() {
        gameRoomService = GameRoomService.getInstance();
        gameRoomService.setGameRoomDao(gameRoomDao);
        gameRoom = new GameRoom();
        gameRoom.setName("New name");
        gameRoom.setToys(new ArrayList<>());
    }

    @Test
    void should_getInstance() {
        var gameRoomServiceInstance = GameRoomService.getInstance();
        Assertions.assertNotNull(gameRoomServiceInstance);
        Assertions.assertInstanceOf(GameRoomService.class, gameRoomServiceInstance);
    }

    @Test
    void should_getSelectedRoom() {
        var expectedRoom = new GameRoom();
        gameRoomService.saveToMemory(expectedRoom);
        var room = gameRoomService.getSelectedRoom();
        Assertions.assertNotNull(room);
        Assertions.assertEquals(expectedRoom, room);
    }

    @Test
    void shouldNot_saveToMemory() {
        gameRoomService.getSelectedRoom();
        gameRoomService.saveToMemory(null);
        var room = gameRoomService.getSelectedRoom();
        Assertions.assertNull(room);
    }

    @Test
    void should_copyRoom() {
        Mockito.when(gameRoomDao.getAll()).thenReturn(List.of(gameRoom));
        gameRoomService.viewRooms();
        var res = gameRoomService.copyRoom(0);
        Assertions.assertTrue(res.isPresent());
        Assertions.assertNotEquals(gameRoom, res.get());
    }

    @Test
    void should_copyRoom_returnEmpty() {
        int index = -10;
        var expected = Optional.empty();
        var result = gameRoomService.copyRoom(index);
        Assertions.assertEquals(expected, result);
    }

    @Test
    void should_renameRoom() {
        String newName = "My new name";
        var result = gameRoomService.renameRoom(gameRoom, newName);
        Assertions.assertTrue(result);
    }

    @Test
    void should_returnFalse_renameRoom() {
        String empty = "    ";
        var result = gameRoomService.renameRoom(gameRoom, empty);
        Assertions.assertFalse(result);
    }

    @Test
    void viewRooms() {
        var expected = List.of(gameRoom);
        Mockito.when(gameRoomDao.getAll()).thenReturn(expected);
        var actual = gameRoomService.viewRooms();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void prepareRoom_throwException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            gameRoomService.prepareRoom(null);
        });

    }

    @Test
    void prepareRoom() {
        gameRoomService.prepareRoom(gameRoom);
        var actual = gameRoomService.getSelectedRoom();
        Assertions.assertEquals(gameRoom, actual);
    }

    @Test
    void save() {
        gameRoomService.save(gameRoom);
        Mockito.verify(gameRoomDao).update(gameRoom);
    }

    @Test
    void delete() {
        Mockito.when(gameRoomDao.getAll()).thenReturn(List.of(gameRoom));
        gameRoomService.viewRooms();
        gameRoomService.delete(0);
        Mockito.verify(gameRoomDao).delete(0);
    }

    @Test
    void shouldFailValidation_delete() {
        var res = gameRoomService.delete(-10);
        Assertions.assertFalse(res);
    }

    @Test
    void viewInfo() {
        Mockito.when(gameRoomDao.getAll()).thenReturn(List.of(gameRoom));
        gameRoomService.viewRooms();
        var res = gameRoomService.viewInfo(0);
        Assertions.assertTrue(res.isPresent());
        Assertions.assertEquals(gameRoom, res.get());
    }

}