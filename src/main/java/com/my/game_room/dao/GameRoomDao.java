package com.my.game_room.dao;

import com.my.game_room.constant.AgeCategory;
import com.my.game_room.constant.SizeType;
import com.my.game_room.model.GameRoom;
import com.my.game_room.model.toy.AbstractToy;
import com.my.game_room.util.DatabaseManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GameRoomDao implements Dao<GameRoom> {
    private static final Logger LOG = LogManager.getLogger();
    public static final String GET_ALL = "select * from game_room";
    public static final String GET_BY_ID = "select * from game_room gr where gr.id = ?";
    public static final String GET_AGES = "select age from ages where game_room_id = ?";
    public static final String GET_TOYS = "select toy_id, type from toy inner join toys t on toy.id = t.toy_id where game_room_id = ?";
    public static final String UPDATE_ROOM = "update game_room gr set name = ?, description = ?, total_price = ?, size = ? where id = ?";
    public static final String DELETE_AGES = "delete from ages WHERE game_room_id = ?";
    public static final String DELETE_TOYS = "delete from toys WHERE game_room_id = ?";
    public static final String SAVE_ROOM = "insert into game_room (name, description, total_price, size) values (?, ?, ?, ?)";
    private static final String GET_ID = "select max(id) from game_room";
    public static final String SAVE_AGES = "insert into ages (game_room_id, age) values (?, ?)";
    public static final String SAVE_TOYS = "insert into toys (game_room_id, toy_id) values (?, ?)";
    public static final String DELETE_ROOM = "delete from public.game_room where id = ?";

    private final Dao<AbstractToy> toyDao;

    public GameRoomDao() {
        toyDao = new ToyDao();
    }

    @Override
    public Optional<GameRoom> getById(long id) {
        LOG.info("Optional<GameRoom> getById(long {})", id);
        try {
            Connection connection = DatabaseManager.getConnection();
            PreparedStatement getGameRoomStatement = connection.prepareStatement(GET_BY_ID);
            getGameRoomStatement.setLong(1, id);
            var set = getGameRoomStatement.executeQuery();
            if (set.next()) {
                return getGameRoom(id, connection, set);
            }
            return Optional.empty();
        } catch (SQLException ex) {
            LOG.error("Помилка при зчитуванні іграшки з БД. ", ex.fillInStackTrace());
        }
        return Optional.empty();
    }

    private Optional<GameRoom> getGameRoom(long id, Connection connection, ResultSet roomSet) throws SQLException {
        PreparedStatement getAgesStatement = connection.prepareStatement(GET_AGES);
        PreparedStatement getToysStatement = connection.prepareStatement(GET_TOYS);
        getAgesStatement.setLong(1, id);
        getToysStatement.setLong(1, id);
        Optional<GameRoom> room = Optional.of(initRoom(roomSet));
        room.get().setAges(parseAges(getAgesStatement.executeQuery()));
        List<AbstractToy> toys = new ArrayList<>();
        var set = getToysStatement.executeQuery();
        while (set.next()) {
            var toyId = set.getLong("toy_id");
            toyDao.getById(toyId).ifPresent(toys::add);
        }
        room.get().setToys(toys);
        return room;
    }

    @Override
    public List<GameRoom> getAll() {
        LOG.info("List<GameRoom> getAll()");
        List<GameRoom> list = new ArrayList<>();
        try {
            Connection connection = DatabaseManager.getConnection();
            PreparedStatement getGameRoomStatement = connection.prepareStatement(GET_ALL);
            ResultSet set = getGameRoomStatement.executeQuery();
            while (set.next()) {
                getGameRoom(set.getLong("id"), connection, set).ifPresent(list::add);
            }
        } catch (SQLException ex) {
            LOG.error("Помилка при зчитуванні іграшки з БД. ", ex.fillInStackTrace());
        }
        return list;
    }

    @Override
    public boolean save(GameRoom gameRoom) {
        LOG.info("boolean save(GameRoom {})", gameRoom);
        try {
            Connection connection = DatabaseManager.getConnection();
            PreparedStatement saveRoomStatement = connection.prepareStatement(SAVE_ROOM);
            PreparedStatement getRoomIdStatement = connection.prepareStatement(GET_ID);
            PreparedStatement saveAgesStatement = connection.prepareStatement(SAVE_AGES);
            PreparedStatement saveToysStatement = connection.prepareStatement(SAVE_TOYS);
            saveRoomStatement.setString(1, gameRoom.getName());
            saveRoomStatement.setString(2, gameRoom.getDescription());
            saveRoomStatement.setDouble(3, gameRoom.getTotalPrice());
            saveRoomStatement.setString(4, gameRoom.getSize().name());
            saveRoomStatement.execute();
            ResultSet generatedKeys = getRoomIdStatement.executeQuery();
            if (!generatedKeys.next()) {
                LOG.error("Не згенеровано жодної id для кімнати");
                return false;
            }
            long id = generatedKeys.getLong("max");
            updateAges(gameRoom, saveAgesStatement, id);
            updateToys(gameRoom, saveToysStatement, id);
        } catch (SQLException ex) {
            LOG.error("Помилка при зчитуванні іграшки з БД. ", ex.fillInStackTrace());
        }
        LOG.info("Кімнату збережено");
        return true;
    }

    private static void updateToys(GameRoom gameRoom, PreparedStatement saveToysStatement, long id) throws SQLException {
        for (AbstractToy toy : gameRoom.getToys()) {
            saveToysStatement.setLong(1, id);
            saveToysStatement.setLong(2, toy.getId());
            saveToysStatement.execute();
        }
    }

    private static void updateAges(GameRoom gameRoom, PreparedStatement saveAgesStatement, long id) throws SQLException {
        for (AgeCategory age : gameRoom.getAges()) {
            saveAgesStatement.setLong(1, id);
            saveAgesStatement.setString(2, age.toString());
            saveAgesStatement.execute();
        }
    }

    @Override
    public boolean update(GameRoom gameRoom) {
        LOG.info("boolean update(GameRoom {})", gameRoom);
        try {
            Connection connection = DatabaseManager.getConnection();
            PreparedStatement updateRoomStatement = connection.prepareStatement(UPDATE_ROOM);
            PreparedStatement saveAgesStatement = connection.prepareStatement(SAVE_AGES);
            PreparedStatement saveToysStatement = connection.prepareStatement(SAVE_TOYS);
            PreparedStatement clearAgesStatement = connection.prepareStatement(DELETE_AGES);
            PreparedStatement clearToysStatement = connection.prepareStatement(DELETE_TOYS);
            updateRoomStatement.setString(1, gameRoom.getName());
            updateRoomStatement.setString(2, gameRoom.getDescription());
            updateRoomStatement.setDouble(3, gameRoom.getTotalPrice());
            updateRoomStatement.setString(4, gameRoom.getSize().name());
            updateRoomStatement.setLong(5, gameRoom.getId());
            updateRoomStatement.execute();
            clearAgesStatement.setLong(1, gameRoom.getId());
            clearAgesStatement.execute();
            clearToysStatement.setLong(1, gameRoom.getId());
            clearToysStatement.execute();
            updateAges(gameRoom, saveAgesStatement, gameRoom.getId());
            updateToys(gameRoom, saveToysStatement, gameRoom.getId());
        } catch (SQLException ex) {
            LOG.error("Помилка при зчитуванні іграшки з БД. ", ex.fillInStackTrace());
        }
        LOG.info("Кімнату збережено");
        return true;
    }

    @Override
    public boolean delete(long id) {
        LOG.info("boolean delete(long {})", id);
        try {
            Connection connection = DatabaseManager.getConnection();
            PreparedStatement statement = connection.prepareStatement(DELETE_ROOM);
            statement.setLong(1, id);
            statement.execute();
        } catch (SQLException ex) {
            LOG.error("Помилка при зчитуванні іграшки з БД. ", ex.fillInStackTrace());
            return false;
        }
        LOG.info("Кімнату видалено");
        return true;
    }

    private GameRoom initRoom(ResultSet row) throws SQLException {
        return new GameRoom(
                row.getLong("id"),
                row.getString("name"),
                row.getString("description"),
                row.getDouble("total_price"),
                SizeType.valueOf(row.getString("size")));
    }

    private List<AgeCategory> parseAges(ResultSet rows) throws SQLException {
        var list = new ArrayList<AgeCategory>();
        while (rows.next()) {
            list.add(AgeCategory.valueOf(rows.getString("age")));
        }
        return list;
    }
}
