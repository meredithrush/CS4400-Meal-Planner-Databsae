package application.mealplanner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RecipeReviewsHCController {

    private FrameControllerClass loginInfo;
    private int rec_ID;
    public TableView<RecipeReviewTable> RecipeReviewTable;
    public TableColumn<RecipeReviewTable, Integer> RecipeReviewID;
    public TableColumn<RecipeReviewTable, String> RecipeReviewUser;
    public TableColumn<RecipeReviewTable, Integer> RecipeReviewRating;
    public TableColumn<RecipeReviewTable, Integer> RecipeReviewComment;
    public Label RecipeReviewName;
    private ObservableList<RecipeReviewTable> data;
    private Stage stage;

    public void logOff(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MPApp.class.getResource("Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage = getCurrentStage(event);
        stage.setScene(scene);
        stage.show();
    }

    public void back(ActionEvent event) throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(MPApp.class.getResource("RecipeHC.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage = getCurrentStage(event);
        stage.setScene(scene);
        RecipeHCController newController = fxmlLoader.getController();
        newController.setRec_ID(rec_ID);
        newController.setLoginInfo(loginInfo);
        newController.showAvg();
        newController.showName();
        newController.showAuthor();
        newController.showInstructions();
        newController.showDT();
        newController.showCuis();
        newController.createTable();
        stage.show();
    }

    public void buildTable() {
        DataBaseConnector connection = new DataBaseConnector();
        Connection connectDB = connection.getConnection();
        data = FXCollections.observableArrayList();
        String connectQuery = "SELECT Rev_ID AS ID, Review.C_Email AS 'User', Rev_Rating AS Rating, Rev_Comment AS 'Comment' FROM Review WHERE Review.Rec_ID = " +rec_ID+ ";";
        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectQuery);

            while (queryOutput.next()) {
                data.add(new RecipeReviewTable(queryOutput.getInt("ID"), queryOutput.getString("User"), queryOutput.getInt("Rating"), queryOutput.getString("Comment")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        RecipeReviewID.setCellValueFactory(new PropertyValueFactory<>("recipeReviewTableID"));
        RecipeReviewUser.setCellValueFactory(new PropertyValueFactory<>("recipeReviewTableUser"));
        RecipeReviewRating.setCellValueFactory(new PropertyValueFactory<>("recipeReviewTableRating"));
        RecipeReviewComment.setCellValueFactory(new PropertyValueFactory<>("recipeReviewTableComment"));

        RecipeReviewTable.setItems(data);
    }

    public void showRecipeName() throws SQLException {
        DataBaseConnector connection = new DataBaseConnector();
        Connection connectDB = connection.getConnection();
        String connectQuery = "SELECT DISTINCT Rec_Name AS 'Recipe Name' FROM Recipe JOIN Review ON Recipe.Rec_ID = Review.Rec_ID WHERE Recipe.Rec_ID = " + rec_ID + ";";
        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectQuery);
            while (queryOutput.next()) {
                RecipeReviewName.setText(queryOutput.getString("Recipe Name"));
            }
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
