module com.example.projectjavafx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires org.apache.logging.log4j;

    opens com.example.projectjavafx to javafx.fxml;
    exports com.example.projectjavafx;

    opens com.example.projectjavafx.controller to javafx.fxml;

    exports com.example.projectjavafx.model;
    exports com.example.projectjavafx.controller;
}