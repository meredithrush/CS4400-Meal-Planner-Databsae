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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RecipeHCController {

    private Stage stage;
    private FrameControllerClass loginInfo;
    private int rec_ID;
    public Label name;
    public Label avgRating;
    public Label author;
    public Label instructions;
    public Label dietTag;
    public Label cuisines;
    public TableView<RecipeProductTable> productTable;
    public TableColumn<RecipeProductTable, String> productName;
    public TableColumn<RecipeProductTable, String> productAmount;
    private ObservableList<RecipeProductTable> data;
    public TextField day;
    public TextField month;
    public TextField year;



    public void logOff(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MPApp.class.getResource("Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage = getCurrentStage(event);
        stage.setScene(scene);
        stage.show();
    }

    public void back(ActionEvent event) throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(MPApp.class.getResource("ChefHome.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage = getCurrentStage(event);
        stage.setScene(scene);
        ChefHomeController newController = fxmlLoader.getController();
        newController.setLoginInfo(loginInfo);
        stage.show();
    }

    public void showAvg() throws SQLException {
        DataBaseConnector connection = new DataBaseConnector();
        Connection connectDB = connection.getConnection();
        String connectQuery = "SELECT AVG(Rev_Rating) AS 'Average Rating' FROM Recipe JOIN Review ON Recipe.Rec_ID = Review.Rec_ID WHERE Recipe.Rec_ID = " + rec_ID + " GROUP BY Recipe.Rec_ID;";
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

    public void showName() throws SQLException {
        DataBaseConnector connection = new DataBaseConnector();
        Connection connectDB = connection.getConnection();
        String connectQuery = "SELECT Recipe.Rec_Name FROM Recipe WHERE Rec_ID = " + rec_ID + ";";
        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectQuery);
            while (queryOutput.next()) {
                name.setText(queryOutput.getString("Rec_Name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        connectDB.close();
    }

    public void showInstructions() throws SQLException {
        DataBaseConnector connection = new DataBaseConnector();
        Connection connectDB = connection.getConnection();
        String connectQuery = "SELECT Recipe.Rec_Instructions FROM Recipe WHERE Recipe.Rec_ID = " + rec_ID + ";";
        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectQuery);
            while (queryOutput.next()) {
                instructions.setText(queryOutput.getString("Rec_Instructions"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        connectDB.close();
    }

    public void showAuthor() throws SQLException {
        DataBaseConnector connection = new DataBaseConnector();
        Connection connectDB = connection.getConnection();
        String connectQuery = "SELECT DISTINCT Usr_Name AS Author FROM Recipe, MP_User WHERE Recipe.C_Email = MP_User.Usr_Email AND Recipe.Rec_ID =" + rec_ID + ";";
        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectQuery);
            while (queryOutput.next()) {
                author.setText(queryOutput.getString("Author"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        connectDB.close();
    }

    public void showDT() throws SQLException {
        String dietTags = "";
        DataBaseConnector connection = new DataBaseConnector();
        Connection connectDB = connection.getConnection();
        String connectQuery = "SELECT Recipe.Rec_Name, Rec_Diet_Tag FROM Recipe NATURAL JOIN Recipe_Diet_Tag WHERE Recipe.Rec_ID = " + rec_ID + ";";
        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectQuery);
            while (queryOutput.next()) {
                dietTags = dietTags + queryOutput.getString("Rec_Diet_Tag") + ", ";
            }
            dietTag.setText(dietTags);
        } catch (Exception e) {
            e.printStackTrace();
        }
        connectDB.close();
    }

    public void showCuis() throws SQLException {
        String cuisineString = "";
        DataBaseConnector connection = new DataBaseConnector();
        Connection connectDB = connection.getConnection();
        String connectQuery = "SELECT Recipe.Rec_Name, Rec_Cuisine FROM Recipe NATURAL JOIN Recipe_Cuisine WHERE Recipe.Rec_ID = " + rec_ID + ";";
        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectQuery);
            while (queryOutput.next()) {
                cuisineString = cuisineString + queryOutput.getString("Rec_Cuisine") + ", ";
            }
            cuisines.setText(cuisineString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        connectDB.close();
    }

    public void createTable() {
        DataBaseConnector connection = new DataBaseConnector();
        Connection connectDB = connection.getConnection();
        data = FXCollections.observableArrayList();
        String connectQuery = "SELECT Recipe_Uses_Product.P_Name AS Name, P_Needed_Amt AS Quantity, Ing_Units as Units FROM Recipe_Uses_Product JOIN Product ON Recipe_Uses_Product.P_Name = Product.P_Name WHERE Recipe_Uses_Product.Rec_ID = " + rec_ID + ";";
        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectQuery);

            while (queryOutput.next()) {
                String amountOutput = queryOutput.getString("Quantity") + " " + queryOutput.getString("Units");
                data.add(new RecipeProductTable(queryOutput.getString("Name"), amountOutput));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        productName.setCellValueFactory(new PropertyValueFactory<>("productTableName"));
        productAmount.setCellValueFactory(new PropertyValueFactory<>("productTableAmount"));

        productTable.setItems(data);
    }

    public void seeReviews(ActionEvent event) throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(MPApp.class.getResource("RecipeReviewsHC.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage = getCurrentStage(event);
        stage.setScene(scene);
        RecipeReviewsHCController newController = fxmlLoader.getController();
        newController.setLoginInfo(loginInfo);
        newController.setRec_ID(rec_ID);
        newController.buildTable();
        newController.showRecipeName();
        stage.show();
    }

    public void planMeal(ActionEvent event) {
        if (year.getText().strip().equals("") || month.getText().strip().equals("") || day.getText().strip().equals("")) {
            return;
        } else {
            String date = year.getText() + "-" + month.getText() + "-" + day.getText();
            DataBaseConnector connection = new DataBaseConnector();
            Connection connectDB = connection.getConnection();
            String connectQuery = "INSERT INTO Meal VALUES('" + date + "', '" + loginInfo.getUserEmail() + "', " + rec_ID + ");";
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
        }
    }

    private Stage getCurrentStage(ActionEvent event) {
        return (Stage) ((Node) event.getSource()).getScene().getWindow();
    }

    public void setRec_ID(int rec_ID) {
        this.rec_ID = rec_ID;
    }
    public void setLoginInfo(FrameControllerClass loginInfo) {
        this.loginInfo = loginInfo;
    }
}
