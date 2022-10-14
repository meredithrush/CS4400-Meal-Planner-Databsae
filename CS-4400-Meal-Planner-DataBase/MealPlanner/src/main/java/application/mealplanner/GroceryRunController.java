package application.mealplanner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class GroceryRunController {
    private Stage stage;
    private FrameControllerClass loginInfo;

    public TableView<GroceryRunTable> groceryRunTable;
    public TableColumn<GroceryRunTable, Hyperlink> tableID;
    public TableColumn<GroceryRunTable, String> tableDate;

    private ObservableList<GroceryRunTable> data;

    public void logOff(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MPApp.class.getResource("Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage = getCurrentStage(event);
        stage.setScene(scene);
        stage.show();
    }

    public void back(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MPApp.class.getResource("ChefHome.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage = getCurrentStage(event);
        stage.setScene(scene);
        ChefHomeController newController = fxmlLoader.getController();
        newController.setLoginInfo(loginInfo);
        stage.show();
    }

    public void buildTable() {
        DataBaseConnector connection = new DataBaseConnector();
        Connection connectDB = connection.getConnection();
        data = FXCollections.observableArrayList();
        String connectQuery = "SELECT Grc_Run_ID AS 'ID', Grc_Run_Date AS 'DATE' FROM Grocery_Run WHERE Grocery_Run.HC_Email = '" + loginInfo.getUserEmail() + "' ORDER BY 'ID';";
        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectQuery);

            while (queryOutput.next()) {
                int id = Integer.parseInt(queryOutput.getString("ID"));
                Hyperlink link = new Hyperlink(queryOutput.getString("ID"));
                data.add(new GroceryRunTable(link, queryOutput.getString("DATE")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        tableID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        tableDate.setCellValueFactory(new PropertyValueFactory<>("Date"));

        groceryRunTable.setItems(data);
    }

    public void newRun(ActionEvent event) throws IOException {
        java.util.Date date = new java.util.Date();
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());

        DataBaseConnector connection = new DataBaseConnector();
        Connection connectDB = connection.getConnection();
        String connectQuery = "INSERT INTO Grocery_Run (Grc_Run_Date, Str_ID, HC_Email) VALUES ('" + sqlDate + "', " + 001 + ", '" + loginInfo.getUserEmail() + "');";
        try {
            Statement statement = connectDB.createStatement();
            statement.executeUpdate(connectQuery);
            FXMLLoader fxmlLoader = new FXMLLoader(MPApp.class.getResource("GroceryRunAddingProduct.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage = getCurrentStage(event);
            stage.setScene(scene);
            GroceryRunAddingProductController newController = fxmlLoader.getController();
            newController.setLoginInfo(loginInfo);
            newController.buildMealTable();
            newController.setDateGet(date);
            newController.showDate();
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Stage getCurrentStage(ActionEvent event) {
        return (Stage) ((Node) event.getSource()).getScene().getWindow();
    }

    public void setLoginInfo(FrameControllerClass loginInfo) {
        this.loginInfo = loginInfo;
    }
}
