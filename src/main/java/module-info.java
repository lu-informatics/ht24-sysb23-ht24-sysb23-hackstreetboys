module maven.jdbc.arcticbyte {
    exports se.lu.ics.models;
    exports se.lu.ics;
    exports se.lu.ics.data;

    requires transitive java.sql;
    requires javafx.fxml;
    requires javafx.controls;
    requires transitive javafx.graphics;
}
