package com.my.game_room.util;

import com.my.game_room.constant.AgeCategory;
import com.my.game_room.constant.SizeType;
import com.my.game_room.model.toy.*;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Auxiliary utilitarian class
 * parser of toys from database
 */
public class ToyParser {

    public static Car parseCar(long id, ResultSet toySet, ResultSet carSet) throws SQLException {
        return new Car(id,
                toySet.getString("name"),
                toySet.getString("description"),
                toySet.getDouble("price"),
                SizeType.valueOf(toySet.getString("size")),
                AgeCategory.valueOf(toySet.getString("age")),
                carSet.getDouble("speed"),
                carSet.getString("material"),
                carSet.getString("color"));
    }

    public static Doll parseDoll(long id, ResultSet toySet, ResultSet dollSet) throws SQLException {
        return new Doll(id,
                toySet.getString("name"),
                toySet.getString("description"),
                toySet.getDouble("price"),
                SizeType.valueOf(toySet.getString("size")),
                AgeCategory.valueOf(toySet.getString("age")),
                dollSet.getString("hair_color"),
                dollSet.getString("special_ability"),
                dollSet.getString("jewellery"));
    }

    public static Cubes parseCubes(long id, ResultSet toySet, ResultSet cubesSet) throws SQLException {
        return new Cubes(id,
                toySet.getString("name"),
                toySet.getString("description"),
                SizeType.valueOf(toySet.getString("size")),
                AgeCategory.valueOf(toySet.getString("age")),
                cubesSet.getInt("amount"),
                cubesSet.getDouble("price_by_one"),
                cubesSet.getDouble("weight"));
    }

    public static Musical parseMusical(long id, ResultSet toySet, ResultSet musicalSet) throws SQLException {
        return new Musical(id,
                toySet.getString("name"),
                toySet.getString("description"),
                toySet.getDouble("price"),
                SizeType.valueOf(toySet.getString("size")),
                AgeCategory.valueOf(toySet.getString("age")),
                musicalSet.getBoolean("is_auto_play_enabled"),
                musicalSet.getString("music_type"),
                musicalSet.getString("material"));
    }
}
