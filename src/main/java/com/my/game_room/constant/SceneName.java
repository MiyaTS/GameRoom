package com.my.game_room.constant;

/**
 * Enum with all available .fxml file declarations
 */
public enum SceneName {
    MENU("main"),
    GAME_ROOM_MENU("gameRoomMenu"),
    GAME_ROOM_LIST("gameRoomsListCommand"),
    GAME_ROOM_CREATOR("createGameRoom"),
    GAME_ROOM_COPY("gameRoomCopyMenu"),
    ADD_TOY("addToy"),
    TOY_MENU("toysMenu"),
    TOY_LIST("toysListCommand"),
    TOY_TYPE("toyTypeMenu"),
    CAR_MENU("createCar"),
    DOLL_MENU("createDoll"),
    CUBES_MENU("createCubes"),
    MUSICAL_MENU("createMusical");

    private final String value;

    SceneName(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
