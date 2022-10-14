package application.mealplanner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class MyRecipeController {

    private ObservableList<MyRecipeTable> data;
    private FrameControllerClass loginInfo;
    private Stage stage;
    public TableView<MyRecipeTable> myRecipeTable;
    public TableColumn<MyRecipeTable, Hyperlink> recID;
    public TableColumn<MyRecipeTable, String> recName;
    public TableColumn<MyRecipeTable, String> avgRating;

    public void logOff(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MPApp.class.getResource("Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage = getCurrentStage(event);
        stage.setScene(scene);
        stage.show();
    }

    public void back(ActionEvent event) throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(MPApp.class.getResource("ContributerHome.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage = getCurrentStage(event);
        stage.setScene(scene);
        ContributerHomeController newController = fxmlLoader.getController();
        newController.setLoginInfo(loginInfo);
        newController.showAvg();
        stage.show();
    }

    public void buildData() {
        DataBaseConnector connection = new DataBaseConnector();
        Connection connectDB = connection.getConnection();
        data = FXCollections.observableArrayList();
        String connectQuery = "SELECT Recipe.Rec_ID AS 'ID' , Rec_Name AS 'Name', AVG(Rev_Rating) AS 'Average Rating' FROM Recipe LEFT JOIN Review ON Recipe.Rec_ID = Review.Rec_ID WHERE Recipe.C_Email = '" + loginInfo.getUserEmail() + "' GROUP BY Recipe.Rec_ID ORDER BY Recipe.Rec_ID;";
        try{
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectQuery);

            while (queryOutput.next()) {
                int id = Integer.parseInt(queryOutput.getString("ID"));
                Hyperlink link = new Hyperlink(queryOutput.getString("ID"));
                data.add(new MyRecipeTable(link, queryOutput.getString("Name"), queryOutput.getString("Average Rating")));
                link.setOnAction(evt -> {
                    FXMLLoader fxmlLoader = new FXMLLoader(MPApp.class.getResource("Recipe.fxml"));
                    try{
                        Scene scene = new Scene(fxmlLoader.load());
                        stage = getCurrentStage(evt);
                        stage.setScene(scene);
                        RecipeController newController = fxmlLoader.getController();
                        newController.setRec_ID(id);
                        newController.setLoginInfo(loginInfo);
                        newController.showAvg();
                        newController.showName();
                        newController.showAuthor();
                        newController.showInstructions();
                        newController.showDT();
                        newController.showCuis();
                        newController.createTable();
                        stage.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        recID.setCellValueFactory(new PropertyValueFactory<>("id"));
        recName.setCellValueFactory(new PropertyValueFactory<>("name"));
        avgRating.setCellValueFactory(new PropertyValueFactory<>("rating"));

        myRecipeTable.setItems(data);
    }
    public void ascName(ActionEvent event) {
        DataBaseConnector connection = new DataBaseConnector();
        Connection connectDB = connection.getConnection();
        data = FXCollections.observableArrayList();
        String connectQuery = "SELECT Recipe.Rec_ID AS 'ID' , Rec_Name AS 'Name', AVG(Rev_Rating) AS 'Average Rating' FROM Recipe LEFT JOIN Review ON Recipe.Rec_ID = Review.Rec_ID WHERE Recipe.C_Email = '" + loginInfo.getUserEmail() + "' GROUP BY Recipe.Rec_ID ORDER BY Recipe.Rec_Name;";
        try{
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectQuery);

            while (queryOutput.next()) {
                int id = Integer.parseInt(queryOutput.getString("ID"));
                Hyperlink link = new Hyperlink(queryOutput.getString("ID"));
                data.add(new MyRecipeTable(link, queryOutput.getString("Name"), queryOutput.getString("Average Rating")));
                link.setOnAction(evt -> {
                    FXMLLoader fxmlLoader = new FXMLLoader(MPApp.class.getResource("Recipe.fxml"));
                    try{
                        Scene scene = new Scene(fxmlLoader.load());
                        stage = getCurrentStage(evt);
                        stage.setScene(scene);
                        RecipeController newController = fxmlLoader.getController();
                        newController.setRec_ID(id);
                        newController.setLoginInfo(loginInfo);
                        newController.showAvg();
                        newController.showName();
                        newController.showAuthor();
                        newController.showInstructions();
                        newController.showDT();
                        newController.showCuis();
                        newController.createTable();
                        stage.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        recID.setCellValueFactory(new PropertyValueFactory<>("id"));
        recName.setCellValueFactory(new PropertyValueFactory<>("name"));
        avgRating.setCellValueFactory(new PropertyValueFactory<>("rating"));

        myRecipeTable.setItems(data);
    }

    public void descName(ActionEvent event) {
        DataBaseConnector connection = new DataBaseConnector();
        Connection connectDB = connection.getConnection();
        data = FXCollections.observableArrayList();
        String connectQuery = "SELECT Recipe.Rec_ID AS 'ID' , Rec_Name AS 'Name', AVG(Rev_Rating) AS 'Average Rating' FROM Recipe LEFT JOIN Review ON Recipe.Rec_ID = Review.Rec_ID WHERE Recipe.C_Email = '" + loginInfo.getUserEmail() + "' GROUP BY Recipe.Rec_ID ORDER BY Recipe.Rec_Name DESC;";
        try{
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectQuery);

            while (queryOutput.next()) {
                int id = Integer.parseInt(queryOutput.getString("ID"));
                Hyperlink link = new Hyperlink(queryOutput.getString("ID"));
                data.add(new MyRecipeTable(link, queryOutput.getString("Name"), queryOutput.getString("Average Rating")));
                link.setOnAction(evt -> {
                    FXMLLoader fxmlLoader = new FXMLLoader(MPApp.class.getResource("Recipe.fxml"));
                    try{
                        Scene scene = new Scene(fxmlLoader.load());
                        stage = getCurrentStage(evt);
                        stage.setScene(scene);
                        RecipeController newController = fxmlLoader.getController();
                        newController.setRec_ID(id);
                        newController.setLoginInfo(loginInfo);
                        newController.showAvg();
                        newController.showName();
                        newController.showAuthor();
                        newController.showInstructions();
                        newController.showDT();
                        newController.showCuis();
                        newController.createTable();
                        stage.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        recID.setCellValueFactory(new PropertyValueFactory<>("id"));
        recName.setCellValueFactory(new PropertyValueFactory<>("name"));
        avgRating.setCellValueFactory(new PropertyValueFactory<>("rating"));

        myRecipeTable.setItems(data);
    }

    public void ascRating(ActionEvent event) {
        DataBaseConnector connection = new DataBaseConnector();
        Connection connectDB = connection.getConnection();
        data = FXCollections.observableArrayList();
        String connectQuery = "SELECT Recipe.Rec_ID AS 'ID' , Rec_Name AS 'Name', AVG(Rev_Rating) AS 'Average Rating' FROM Recipe LEFT JOIN Review ON Recipe.Rec_ID = Review.Rec_ID WHERE Recipe.C_Email = '" + loginInfo.getUserEmail() + "' GROUP BY Recipe.Rec_ID ORDER BY 'Average Rating';";
        try{
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectQuery);

            while (queryOutput.next()) {
                int id = Integer.parseInt(queryOutput.getString("ID"));
                Hyperlink link = new Hyperlink(queryOutput.getString("ID"));
                data.add(new MyRecipeTable(link, queryOutput.getString("Name"), queryOutput.getString("Average Rating")));
                link.setOnAction(evt -> {
                    FXMLLoader fxmlLoader = new FXMLLoader(MPApp.class.getResource("Recipe.fxml"));
                    try{
                        Scene scene = new Scene(fxmlLoader.load());
                        stage = getCurrentStage(evt);
                        stage.setScene(scene);
                        RecipeController newController = fxmlLoader.getController();
                        newController.setRec_ID(id);
                        newController.setLoginInfo(loginInfo);
                        newController.showAvg();
                        newController.showName();
                        newController.showAuthor();
                        newController.showInstructions();
                        newController.showDT();
                        newController.showCuis();
                        newController.createTable();
                        stage.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        recID.setCellValueFactory(new PropertyValueFactory<>("id"));
        recName.setCellValueFactory(new PropertyValueFactory<>("name"));
        avgRating.setCellValueFactory(new PropertyValueFactory<>("rating"));

        myRecipeTable.setItems(data);
    }

    public void descRating(ActionEvent event) {
        DataBaseConnector connection = new DataBaseConnector();
        Connection connectDB = connection.getConnection();
        data = FXCollections.observableArrayList();
        String connectQuery = "SELECT Recipe.Rec_ID AS 'ID' , Rec_Name AS 'Name', AVG(Rev_Rating) AS 'Average Rating' FROM Recipe LEFT JOIN Review ON Recipe.Rec_ID = Review.Rec_ID WHERE Recipe.C_Email = '" + loginInfo.getUserEmail() + "' GROUP BY Recipe.Rec_ID ORDER BY AVG(Rev_Rating) DESC ;";
        try{
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectQuery);

            while (queryOutput.next()) {
                int id = Integer.parseInt(queryOutput.getString("ID"));
                Hyperlink link = new Hyperlink(queryOutput.getString("ID"));
                data.add(new MyRecipeTable(link, queryOutput.getString("Name"), queryOutput.getString("Average Rating")));
                link.setOnAction(evt -> {
                    FXMLLoader fxmlLoader = new FXMLLoader(MPApp.class.getResource("Recipe.fxml"));
                    try{
                        Scene scene = new Scene(fxmlLoader.load());
                        stage = getCurrentStage(evt);
                        stage.setScene(scene);
                        RecipeController newController = fxmlLoader.getController();
                        newController.setRec_ID(id);
                        newController.setLoginInfo(loginInfo);
                        newController.showAvg();
                        newController.showName();
                        newController.showAuthor();
                        newController.showInstructions();
                        newController.showDT();
                        newController.showCuis();
                        newController.createTable();
                        stage.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        recID.setCellValueFactory(new PropertyValueFactory<>("id"));
        recName.setCellValueFactory(new PropertyValueFactory<>("name"));
        avgRating.setCellValueFactory(new PropertyValueFactory<>("rating"));

        myRecipeTable.setItems(data);
    }

    private Stage getCurrentStage(ActionEvent event) {
        return (Stage) ((Node) event.getSource()).getScene().getWindow();
    }

    public void setLoginInfo(FrameControllerClass loginInfo) {
        this.loginInfo = loginInfo;
    }

}
