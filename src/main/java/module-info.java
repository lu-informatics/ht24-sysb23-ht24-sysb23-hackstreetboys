module maven.jdbc.arcticbyte {

    opens se.lu.ics.controllers to javafx.fxml;

    exports se.lu.ics.models;
    exports se.lu.ics;
    exports se.lu.ics.data;
    exports se.lu.ics.controllers;

    requires transitive java.sql;
    requires javafx.fxml;
    requires javafx.controls;
    requires transitive javafx.graphics;
    requires javafx.base;
}
