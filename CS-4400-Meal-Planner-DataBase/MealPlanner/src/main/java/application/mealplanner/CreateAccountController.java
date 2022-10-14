package application.mealplanner;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


public class CreateAccountController {
    @FXML
    private Stage stage;
    @FXML
    private TextField email;
    @FXML
    private TextField password;
    @FXML
    private TextField name;
    @FXML
    private TextField address;

    public Label error;
    @FXML
    public void back(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MPApp.class.getResource("Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage = getCurrentStage(event);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void createAccount(ActionEvent event) throws SQLException {
        if (password.getText().length() < 8) {
            error.setText("Password must be more than 8 characters.");
            return;
        }
        if (!email.getText().contains("@")) {
            error.setText("Enter valid email.");
            return;
        }

        DataBaseConnector connection = new DataBaseConnector();
        Connection connectDB = connection.getConnection();
        String connectQuery = "INSERT INTO MP_User VALUES ('" + email.getText() + "', '" + name.getText() + "', '" + password.getText() + "', '" + address.getText() + "');";

        try{
            Statement statement = connectDB.createStatement();
            statement.executeUpdate(connectQuery);
            FXMLLoader fxmlLoader = new FXMLLoader(MPApp.class.getResource("login.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage = getCurrentStage(event);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        connectDB.close();
    }
    private Stage getCurrentStage(ActionEvent event) {
        return (Stage) ((Node) event.getSource()).getScene().getWindow();
    }
}
