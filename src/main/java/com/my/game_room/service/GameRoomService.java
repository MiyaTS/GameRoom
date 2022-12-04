package com.my.game_room.service;

import com.my.game_room.dao.Dao;
import com.my.game_room.dao.GameRoomDao;
import com.my.game_room.model.GameRoom;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

/**
 * game room service class.
 * <p>
 * implement CRUD operations for GameRoom business object and its states.
 * <p>
 * receiver class
 */
public class GameRoomService {
    private static final Logger LOG = LogManager.getLogger();
    private static GameRoomService gameRoomService;
    private Dao<GameRoom> gameRoomDao;
    private List<GameRoom> gameRooms;
    private GameRoom gameRoom;

    private GameRoomService() {
        gameRoomDao = new GameRoomDao();
    }

    public static GameRoomService getInstance() {
        if (gameRoomService == null) {
            gameRoomService = new GameRoomService();
        }
        return gameRoomService;
    }

    /**
     * Return GameRoom instance from cache. Clear cache memory.
     *
     * @return game room
     */
    public GameRoom getSelectedRoom() {
        var selectedRoom = gameRoom;
        gameRoom = null;
        return selectedRoom;
    }

    /**
     * validation index.
     * copying a room.
     * giving the copied room a new name.
     * saving copied room in cache.
     *
     * @param index of game room in memory
     */
    public Optional<GameRoom> copyRoom(int index) {
        LOG.info("Копіювання кімнати за індексом {}", index);
        if (index < 0 || index >= gameRooms.size()) {
            LOG.error("Неправильний індекс");
            return Optional.empty();
        }
        var room = gameRooms.get(index);
        var copy = new GameRoom();
        copy.setName(room.getName() + " Copy");
        copy.setDescription(room.getDescription());
        copy.setTotalPrice(room.getTotalPrice());
        copy.setSize(room.getSize());
        copy.setAges(room.getAges());
        copy.setToys(room.getToys());
        gameRoom = copy;
        return Optional.of(copy);
    }

    /**
     * renaming game room and save in database.
     *
     * @param room    instance from controller
     * @param newName String value with new name of room
     * @return true if new name is set
     */
    public boolean renameRoom(GameRoom room, String newName) {
        LOG.info("Зміна назви для кімнати {}", room);
        room.setName(newName);
        if (!room.validate()) {
            return false;
        }
        gameRoomDao.save(room);
        return true;
    }

    /**
     * outputs all game rooms from the database.
     * additionally save list in cache.
     *
     * @return true if this list contains no elements
     */
    public List<GameRoom> viewRooms() {
        gameRooms = gameRoomDao.getAll();
        return gameRooms;
    }

    /**
     * Game creation editor,
     * creating a game room.
     * saving the game room to cache.
     *
     * @param room instance of new GameRoom without toys
     */
    public void prepareRoom(GameRoom room) {
        LOG.info("Створення кімнати");
        if (room == null) {
            LOG.warn("Кімната не ініціалізована");
            throw new IllegalArgumentException();
        }
        gameRoom = room;
    }

    /**
     * Validating data. Saving game room instance in database.
     *
     * @param room instance of new GameRoom with list of toys
     * @return true if room with toys was successfully saved
     */
    public boolean save(GameRoom room) {
        if (!room.validate()) {
            LOG.warn("Некоректні іграшки");
            return false;
        }
        if (room.getId() >= 0) {
            return gameRoomDao.update(room);
        }
        return gameRoomDao.save(room);
    }

    /**
     * deleting game room from database
     *
     * @param index of game room in cache
     * @return true if room was deleted
     */
    public boolean delete(int index) {
        LOG.info("Видалення кімнати за індексом {}", index);
        if (index < 0 || index >= gameRooms.size()) {
            LOG.warn("Не вдалось видалити");
            return false;
        }
        return gameRoomDao.delete(gameRooms.get(index).getId());
    }

    /**
     * view info about game room,
     * saving room into cache
     *
     * @param index of room instance in memory
     * @return value of game room
     */
    public Optional<GameRoom> viewInfo(int index) {
        LOG.info("Перегляд даних за індексом {}", index);
        if (!validateIndex(index)) return Optional.empty();
        gameRoom = gameRooms.get(index);
        return Optional.of(gameRoom);
    }

    /**
     * Verify index against available list of room in memory
     *
     * @param index of room in cache
     * @return true if index is valid
     */
    private boolean validateIndex(int index) {
        if (index < 0 || index >= gameRooms.size()) {
            LOG.warn("Неправильний індекс");
            return false;
        }
        return true;
    }

    /**
     * Save game room to cache memory
     *
     * @param room to save
     */
    public void saveToMemory(GameRoom room) {
        if (room == null) {
            return;
        }
        this.gameRoom = room;
    }

    public void setGameRoomDao(Dao<GameRoom> gameRoomDao) {
        this.gameRoomDao = gameRoomDao;
    }
}
