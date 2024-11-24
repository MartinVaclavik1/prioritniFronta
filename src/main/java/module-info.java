module com.example.prioritnifronta {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.prioritnifronta to javafx.fxml;
    exports com.example.prioritnifronta;
}