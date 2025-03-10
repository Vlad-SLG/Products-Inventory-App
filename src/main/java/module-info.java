module com.example.test___ {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires org.xerial.sqlitejdbc;

    opens com.example.test___ to javafx.fxml;
    exports com.example.test___;
}