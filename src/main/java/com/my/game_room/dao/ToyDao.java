package com.my.game_room.dao;

import com.my.game_room.constant.ToyType;
import com.my.game_room.model.toy.*;
import com.my.game_room.util.DatabaseManager;
import com.my.game_room.util.ToyParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ToyDao implements Dao<AbstractToy> {
    private static final Logger LOG = LogManager.getLogger();
    private static final String GET_ALL_TOYS = "select * from toy";
    private static final String GET_TOY = "select * from toy where id = ?";
    private static final String GET_CAR = "select * from car where toy_id = ?";
    private static final String GET_DOLL = "select * from doll where toy_id = ?";
    private static final String GET_CUBES = "select * from cubes where toy_id = ?";
    private static final String GET_MUSICAL = "select * from musical where toy_id = ?";
    private static final String DELETE_TOY = "delete from toy where id = ?";
    private static final String UPDATE_TOY = "update toy set name = ?, description = ?, price = ?, size = ? where id = ?";
    private static final String UPDATE_CAR = "update car set speed = ?, material = ?, color = ? where toy_id = ?";
    private static final String UPDATE_DOLL = "update doll set hair_color = ?, special_ability = ?, jewellery = ? where toy_id = ?";
    private static final String UPDATE_CUBES = "update cubes set amount = ?, price_by_one = ?, weight = ? where toy_id = ?";
    private static final String UPDATE_MUSICAL = "update musical set is_auto_play_enabled = ?, music_type = ?, material = ? where toy_id = ?";
    private static final String SAVE_TOY = "insert into toy (name, type, description, price, size, age) values (?, ?, ?, ?, ?, ?) returning id";
    private static final String GET_ID = "select max(id) from toy";
    private static final String SAVE_CAR = "insert into car (toy_id, speed, material, color) values (?, ?, ?, ?)";
    private static final String SAVE_DOLL = "insert into doll (toy_id, hair_color, special_ability, jewellery) values (?, ?, ?, ?)";
    private static final String SAVE_CUBES = "insert into cubes (toy_id, amount, price_by_one, weight) values (?, ?, ?, ?)";
    private static final String SAVE_MUSICAL = "insert into musical (toy_id, is_auto_play_enabled, music_type, material) values (?, ?, ?, ?)";

    @Override
    public Optional<AbstractToy> getById(long id) {
        LOG.info("Optional<AbstractToy> getById(long {})", id);
        try {
            Connection connection = DatabaseManager.getConnection();
            PreparedStatement getToyStatement = connection.prepareStatement(GET_TOY);
            getToyStatement.setLong(1, id);
            ResultSet toySet = getToyStatement.executeQuery();
            if (!toySet.next()) {
                System.err.println("Не знайдено такої іграшки");
                throw new IllegalArgumentException();
            }
            Map<ToyType, PreparedStatement> statements = getToyTypePreparedStatementMap(connection);
            return initToy(id, toySet, statements);
        } catch (SQLException ex) {
            LOG.error("Помилка при зчитуванні іграшки з БД. ", ex.fillInStackTrace());
        }
        LOG.warn("Іграшку з айді {} не ініціалізована", id);
        return Optional.empty();
    }

    private static Map<ToyType, PreparedStatement> getToyTypePreparedStatementMap(Connection connection) throws SQLException {
        Map<ToyType, PreparedStatement> statements = new HashMap<>();
        PreparedStatement getCarStatement = connection.prepareStatement(GET_CAR);
        PreparedStatement getDollStatement = connection.prepareStatement(GET_DOLL);
        PreparedStatement getCubesStatement = connection.prepareStatement(GET_CUBES);
        PreparedStatement getMusicalStatement = connection.prepareStatement(GET_MUSICAL);
        statements.put(ToyType.CAR, getCarStatement);
        statements.put(ToyType.DOLL, getDollStatement);
        statements.put(ToyType.CUBES, getCubesStatement);
        statements.put(ToyType.MUSICAL, getMusicalStatement);
        return statements;
    }

    private static Optional<AbstractToy> initToy(long id, ResultSet toySet, Map<ToyType, PreparedStatement> toyStatements) throws SQLException {
        ToyType type = ToyType.valueOf(toySet.getString("type"));
        var statement = toyStatements.get(type);
        statement.setLong(1, id);
        ResultSet set = statement.executeQuery();
        if (!set.next()) {
            return Optional.empty();
        }
        switch (type) {
            case CAR -> {
                return Optional.of(ToyParser.parseCar(id, toySet, set));
            }
            case DOLL -> {
                return Optional.of(ToyParser.parseDoll(id, toySet, set));
            }
            case CUBES -> {
                return Optional.of(ToyParser.parseCubes(id, toySet, set));
            }
            case MUSICAL -> {
                return Optional.of(ToyParser.parseMusical(id, toySet, set));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<AbstractToy> getAll() {
        LOG.info("List<AbstractToy> getAll()");
        List<AbstractToy> toys = new ArrayList<>();
        try {
            Connection connection = DatabaseManager.getConnection();
            PreparedStatement getToyStatement = connection.prepareStatement(GET_ALL_TOYS);
            Map<ToyType, PreparedStatement> statements = getToyTypePreparedStatementMap(connection);
            ResultSet toySet = getToyStatement.executeQuery();
            while (toySet.next()) {
                initToy(toySet.getInt("id"), toySet, statements).ifPresent(toys::add);
            }
        } catch (SQLException ex) {
            LOG.error("Помилка при зчитуванні іграшки з БД. ", ex.fillInStackTrace());
        }
        return toys;
    }

    @Override
    public boolean save(AbstractToy toy) {
        LOG.info("boolean save(AbstractToy {})", toy);
        try {
            Connection connection = DatabaseManager.getConnection();
            PreparedStatement saveToyStatement = connection.prepareStatement(SAVE_TOY);
            PreparedStatement getToyIdStatement = connection.prepareStatement(GET_ID);
            PreparedStatement saveCarStatement = connection.prepareStatement(SAVE_CAR);
            PreparedStatement saveDollStatement = connection.prepareStatement(SAVE_DOLL);
            PreparedStatement saveCubesStatement = connection.prepareStatement(SAVE_CUBES);
            PreparedStatement saveMusicalStatement = connection.prepareStatement(SAVE_MUSICAL);
            saveToyStatement.setString(1, toy.getName());
            saveToyStatement.setString(2, toy.getToyType().name());
            saveToyStatement.setString(3, toy.getDescription());
            saveToyStatement.setDouble(4, toy.calcPrice());
            saveToyStatement.setString(5, toy.getSize().name());
            saveToyStatement.setString(6, toy.getAge().name());
            saveToyStatement.execute();
            ResultSet generatedKeys = getToyIdStatement.executeQuery();
            if (!generatedKeys.next()) {
                System.err.println("Не згенеровано id");
            }
            long id = generatedKeys.getLong("max");
            switch (toy.getToyType()) {
                case CAR -> {
                    var car = (Car) toy;
                    saveCarStatement.setLong(1, id);
                    saveCarStatement.setDouble(2, car.getSpeed());
                    saveCarStatement.setString(3, car.getMaterial());
                    saveCarStatement.setString(4, car.getColor());
                    saveCarStatement.execute();
                }
                case DOLL -> {
                    var doll = (Doll) toy;
                    saveDollStatement.setLong(1, id);
                    saveDollStatement.setString(2, doll.getHairColor());
                    saveDollStatement.setString(3, doll.getSpecialAbility());
                    saveDollStatement.setString(4, doll.getJewellery());
                    saveDollStatement.execute();
                }
                case CUBES -> {
                    var cubes = (Cubes) toy;
                    saveCubesStatement.setLong(1, id);
                    saveCubesStatement.setInt(2, cubes.getAmount());
                    saveCubesStatement.setDouble(3, cubes.getPriceByOne());
                    saveCubesStatement.setDouble(4, cubes.getWeight());
                    saveCubesStatement.execute();
                }
                case MUSICAL -> {
                    var musical = (Musical) toy;
                    saveMusicalStatement.setLong(1, id);
                    saveMusicalStatement.setBoolean(2, musical.isAutoPlayEnabled());
                    saveMusicalStatement.setString(3, musical.getMusicType());
                    saveMusicalStatement.setString(4, musical.getMaterial());
                    saveMusicalStatement.execute();
                }
            }
        } catch (SQLException ex) {
            LOG.error("Помилка при зчитуванні іграшки з БД. ", ex.fillInStackTrace());
            return false;
        }
        LOG.info("Іграшку збережено");
        return true;
    }

    @Override
    public boolean update(AbstractToy toy) {
        LOG.info("boolean update(AbstractToy {})", toy);
        try {
            Connection connection = DatabaseManager.getConnection();
            PreparedStatement updateToyStatement = connection.prepareStatement(UPDATE_TOY);
            PreparedStatement updateCarStatement = connection.prepareStatement(UPDATE_CAR);
            PreparedStatement updateDollStatement = connection.prepareStatement(UPDATE_DOLL);
            PreparedStatement updateCubesStatement = connection.prepareStatement(UPDATE_CUBES);
            PreparedStatement updateMusicalStatement = connection.prepareStatement(UPDATE_MUSICAL);
            updateToyStatement.setString(1, toy.getName());
            updateToyStatement.setString(2, toy.getDescription());
            updateToyStatement.setDouble(3, toy.calcPrice());
            updateToyStatement.setString(4, toy.getSize().name());
            updateToyStatement.setLong(5, toy.getId());
            updateToyStatement.execute();
            switch (toy.getToyType()) {
                case CAR -> {
                    var car = (Car) toy;
                    updateCarStatement.setDouble(1, car.getSpeed());
                    updateCarStatement.setString(2, car.getMaterial());
                    updateCarStatement.setString(3, car.getColor());
                    updateCarStatement.setLong(4, car.getId());
                    updateCarStatement.execute();
                }
                case DOLL -> {
                    var doll = (Doll) toy;
                    updateDollStatement.setString(1, doll.getHairColor());
                    updateDollStatement.setString(2, doll.getSpecialAbility());
                    updateDollStatement.setString(3, doll.getJewellery());
                    updateDollStatement.setLong(4, doll.getId());
                    updateDollStatement.execute();
                }
                case CUBES -> {
                    var cubes = (Cubes) toy;
                    updateDollStatement.setInt(1, cubes.getAmount());
                    updateDollStatement.setDouble(2, cubes.getPriceByOne());
                    updateDollStatement.setDouble(3, cubes.getWeight());
                    updateDollStatement.setLong(4, cubes.getId());
                    updateCubesStatement.execute();
                }
                case MUSICAL -> {
                    var musical = (Musical) toy;
                    updateDollStatement.setBoolean(1, musical.isAutoPlayEnabled());
                    updateDollStatement.setString(2, musical.getMusicType());
                    updateDollStatement.setString(3, musical.getMaterial());
                    updateDollStatement.setLong(4, musical.getId());
                    updateMusicalStatement.execute();
                }
            }
        } catch (SQLException ex) {
            LOG.error("Помилка при зчитуванні іграшки з БД. ", ex.fillInStackTrace());
            return false;
        }
        LOG.info("Іграшку збережено");
        return true;
    }

    @Override
    public boolean delete(long id) {
        LOG.info("boolean delete(long {})", id);
        try {
            Connection connection = DatabaseManager.getConnection();
            PreparedStatement statement = connection.prepareStatement(DELETE_TOY);
            statement.setLong(1, id);
            statement.execute();
        } catch (SQLException ex) {
            LOG.error("Помилка при зчитуванні іграшки з БД. ", ex.fillInStackTrace());
            return false;
        }
        LOG.info("Іграшку видалено");
        return true;
    }
}
