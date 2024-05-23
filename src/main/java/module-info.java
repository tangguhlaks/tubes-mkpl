module com.example.jafafxlearn {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.graphics;
    requires mysql.connector.j;

    opens com.example.jafafxlearn.model to javafx.base;
    opens com.example.jafafxlearn.controller to javafx.fxml;
    opens com.example.jafafxlearn.controller.Admin to javafx.fxml;
    opens com.example.jafafxlearn.controller.Staff to javafx.fxml;
    opens com.example.jafafxlearn.controller.Supervisor to javafx.fxml;

    exports com.example.jafafxlearn;
    exports com.example.jafafxlearn.controller;
    exports com.example.jafafxlearn.controller.Admin;
    exports com.example.jafafxlearn.controller.Supervisor;
    exports com.example.jafafxlearn.controller.Staff;
}
