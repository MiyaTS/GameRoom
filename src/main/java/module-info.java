module com.my.game_room {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.logging.log4j;
    requires java.sql;

    opens com.my.game_room to javafx.fxml;
    exports com.my.game_room;
    opens com.my.game_room.service to javafx.fxml;
    exports com.my.game_room.service;
    opens com.my.game_room.controller to javafx.fxml;
    exports com.my.game_room.controller;
    opens com.my.game_room.controller.toy to javafx.fxml;
    exports com.my.game_room.controller.toy;
    exports com.my.game_room.controller.game_room;
    opens com.my.game_room.controller.game_room to javafx.fxml;
}