module ClearSkiesCharterCompany {
    requires javafx.fxml;
    requires javafx.controls;
    requires java.sql;
    requires sqlite.jdbc;

    opens login;
    opens HomePage;
    opens HomePage.Data;
    opens HomePage.TabControllers;
}