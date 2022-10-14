package application.mealplanner;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ContributerHomeController {

    private Stage stage;
    private FrameControllerClass loginInfo;
    @FXML
    public Label avgRating;
    public void showAvg() throws SQLException {
        DataBaseConnector connection = new DataBaseConnector();
        Connection connectDB = connection.getConnection();
        String connectQuery = "SELECT AVG(Rev_Rating) AS 'Average Rating' FROM Recipe JOIN Review ON Recipe.Rec_ID = Review.Rec_ID WHERE Recipe.C_Email ='" + loginInfo.getUserEmail() + "' GROUP BY Recipe.C_Email ;";
        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectQuery);
            while (queryOutput.next()) {
                avgRating.setText(queryOutput.getString("Average Rating"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        connectDB.close();
    }
    @FXML
    public void back(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MPApp.class.getResource("HomeScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage = getCurrentStage(event);
        stage.setScene(scene);
        HomeScreenController newController = fxmlLoader.getController();
        newController.setLoginInfo(loginInfo);
        stage.show();
    }

    @FXML
    public void logOff(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MPApp.class.getResource("Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage = getCurrentStage(event);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void writeRecipe(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MPApp.class.getResource("WriteRecipe.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage = getCurrentStage(event);
        stage.setScene(scene);
        WriteRecipeController newController = fxmlLoader.getController();
        newController.setLoginInfo(loginInfo);
        newController.createProductTable();
        stage.show();
    }

    @FXML
    public void browseRecipes(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MPApp.class.getResource("BrowseRecipesContributor.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage = getCurrentStage(event);
        stage.setScene(scene);
        BrowseRecipesContributorController newController = fxmlLoader.getController();
        newController.setLoginInfo(loginInfo);
        newController.buildData();
        stage.show();
    }

    @FXML
    public void myRecipe(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MPApp.class.getResource("MyRecipe.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage = getCurrentStage(event);
        stage.setScene(scene);
        MyRecipeController newController = fxmlLoader.getController();
        newController.setLoginInfo(loginInfo);
        newController.buildData();
        stage.show();
    }

    private Stage getCurrentStage(ActionEvent event) {
        return (Stage) ((Node) event.getSource()).getScene().getWindow();
    }

    public void setLoginInfo(FrameControllerClass loginInfo) {
        this.loginInfo = loginInfo;
    }
}
