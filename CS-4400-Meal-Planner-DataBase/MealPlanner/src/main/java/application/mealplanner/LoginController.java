package application.mealplanner;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LoginController {
    private FrameControllerClass loginInfo;
    @FXML
    private Stage stage;
    @FXML
    private TextField email;
    @FXML
    private TextField password;

    @FXML
    public void dontHaveAccount(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MPApp.class.getResource("CreateAccount.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage = getCurrentStage(event);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void login(ActionEvent event) throws SQLException {
        DataBaseConnector connection = new DataBaseConnector();
        Connection connectDB = connection.getConnection();
        String connectQuery = "SELECT MP_User.Usr_Email, MP_User.Usr_Password FROM MP_User WHERE MP_User.Usr_Email = '" + email.getText() + "' AND MP_User.Usr_Password = '" + password.getText() + "';";
        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectQuery);

            while (queryOutput.next()) {
                loginInfo = new FrameControllerClass(queryOutput.getString("Usr_Email"), queryOutput.getString("Usr_Password"));
                FXMLLoader fxmlLoader = new FXMLLoader(MPApp.class.getResource("HomeScreen.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                stage = getCurrentStage(event);
                stage.setScene(scene);
                HomeScreenController newController = fxmlLoader.getController();
                newController.setLoginInfo(loginInfo);
                stage.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        connectDB.close();
    }
    private Stage getCurrentStage(ActionEvent event) {
        return (Stage) ((Node) event.getSource()).getScene().getWindow();
    }
}
