package application.mealplanner;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class HomeScreenController {
    @FXML
    private Stage stage;
    private FrameControllerClass loginInfo;
    @FXML
    public void logOff(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MPApp.class.getResource("Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage = getCurrentStage(event);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void contributor(ActionEvent event) throws SQLException {
        DataBaseConnector connection = new DataBaseConnector();
        Connection connectDB = connection.getConnection();
        String connectQuery = "INSERT IGNORE INTO Contributor VALUES ('"+ loginInfo.getUserEmail() + "');";
        try {
            Statement statement = connectDB.createStatement();
            statement.executeUpdate(connectQuery);
            FXMLLoader fxmlLoader = new FXMLLoader(MPApp.class.getResource("ContributerHome.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage = getCurrentStage(event);
            stage.setScene(scene);
            ContributerHomeController newController = fxmlLoader.getController();
            newController.setLoginInfo(loginInfo);
            newController.showAvg();
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        connectDB.close();
    }

    @FXML
    public void chef(ActionEvent event) throws SQLException {
        DataBaseConnector connection = new DataBaseConnector();
        Connection connectDB = connection.getConnection();
        String connectQuery = "INSERT IGNORE INTO Home_Chef VALUES ('"+ loginInfo.getUserEmail() + "');";
        try {
            Statement statement = connectDB.createStatement();
            statement.executeUpdate(connectQuery);
            FXMLLoader fxmlLoader = new FXMLLoader(MPApp.class.getResource("ChefHome.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage = getCurrentStage(event);
            stage.setScene(scene);
            ChefHomeController newController = fxmlLoader.getController();
            newController.setLoginInfo(loginInfo);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        connectDB.close();
    }

    private Stage getCurrentStage(ActionEvent event) {
        return (Stage) ((Node) event.getSource()).getScene().getWindow();
    }

    public void setLoginInfo(FrameControllerClass loginInfo) {
        this.loginInfo = loginInfo;
    }
}
