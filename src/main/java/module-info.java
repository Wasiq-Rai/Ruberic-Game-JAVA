module com.example.javagame {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.javagame to javafx.fxml;
    exports com.example.javagame;
}