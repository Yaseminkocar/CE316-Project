module com.ieuapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;
    requires java.desktop;


    opens com.ieuapp to javafx.fxml;
    exports com.ieuapp;
}