module application.mealplanner {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires java.sql;

    opens application.mealplanner to javafx.fxml;
    exports application.mealplanner;
}