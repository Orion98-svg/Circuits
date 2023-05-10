module com.example.circuits {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires org.antlr.antlr4.runtime;

    opens com.example.circuits to javafx.fxml;
    exports com.example.circuits;
}