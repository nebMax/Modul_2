module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;


    opens Modul_2 to javafx.fxml;
    exports Modul_2;
}