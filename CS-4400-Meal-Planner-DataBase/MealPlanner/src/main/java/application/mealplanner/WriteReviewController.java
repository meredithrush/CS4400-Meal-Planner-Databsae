package application.mealplanner;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class WriteReviewController {
    private int rec_ID;
    private FrameControllerClass loginInfo;
    private Stage stage;
    public TextField rating;
    public TextArea comment;
    public void logOff(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MPApp.class.getResource("Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage = getCurrentStage(event);
        stage.setScene(scene);
        stage.show();
    }
    public void back(ActionEvent event) throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(MPApp.class.getResource("RecipeReviews.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage = getCurrentStage(event);
        stage.setScene(scene);
        RecipeReviewsController newController = fxmlLoader.getController();
        newController.setRec_ID(rec_ID);
        newController.setLoginInfo(loginInfo);
        newController.buildTable();
        newController.showRecipeName();
        stage.show();
    }

    public void save(ActionEvent event) throws SQLException {
        DataBaseConnector connection = new DataBaseConnector();
        Connection connectDB = connection.getConnection();
        String connectQuery = "INSERT INTO Review (Rev_Comment, Rev_Rating, C_Email, Rec_ID) VALUES ('" + comment.getText() + "', " + Integer.parseInt(rating.getText()) + ", '" + loginInfo.getUserEmail() + "', " + rec_ID + ");";
        try {
            Statement statement = connectDB.createStatement();
            statement.executeUpdate(connectQuery);
            FXMLLoader fxmlLoader = new FXMLLoader(MPApp.class.getResource("RecipeReviews.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage = getCurrentStage(event);
            stage.setScene(scene);
            RecipeReviewsController newController = fxmlLoader.getController();
            newController.setRec_ID(rec_ID);
            newController.setLoginInfo(loginInfo);
            newController.buildTable();
            newController.showRecipeName();
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        connectDB.close();
    }

    public void setRec_ID(int rec_ID) {
        this.rec_ID = rec_ID;
    }
    public void setLoginInfo(FrameControllerClass loginInfo) {
        this.loginInfo = loginInfo;
    }

    private Stage getCurrentStage(ActionEvent event) {
        return (Stage) ((Node) event.getSource()).getScene().getWindow();
    }
}
