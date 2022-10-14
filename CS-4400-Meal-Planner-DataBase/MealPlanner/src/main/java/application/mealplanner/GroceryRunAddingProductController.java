package application.mealplanner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GroceryRunAddingProductController {
    private Stage stage;
    private FrameControllerClass loginInfo;

    public TableView<ProductCheckListTable> productCheckTable;
    public TableView<ShoppingMealsTable> shoppingMeals;
    public TableColumn<ProductCheckListTable, String> prodName;
    public TableColumn<ProductCheckListTable, String> prodAmt;
    public TableColumn<ProductCheckListTable, Button> prodCheck;
    public TableColumn<ShoppingMealsTable, String> recipeName;
    public TableColumn<ShoppingMealsTable, String> recipeDate;
    public TableColumn<ShoppingMealsTable, CheckBox> recipeCheck;
    public ObservableList<ShoppingMealsTable> mealData;
    public ObservableList<ProductCheckListTable> prodCheckData;
    public TableColumn<ShoppingMealsTable, String> recipeID;
    public Date dateget;
    public Label date;
    private int groceryRunID = 1;

    public void logOff(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MPApp.class.getResource("Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage = getCurrentStage(event);
        stage.setScene(scene);
        stage.show();
    }

    public void back(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MPApp.class.getResource("GroceryRuns.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage = getCurrentStage(event);
        stage.setScene(scene);
        GroceryRunController newController = fxmlLoader.getController();
        newController.setLoginInfo(loginInfo);
        newController.buildTable();
        stage.show();
    }

    public void buildMealTable() {
        groceryRunID ++;
        DataBaseConnector connection = new DataBaseConnector();
        Connection connectDB = connection.getConnection();
        mealData = FXCollections.observableArrayList();
        String connectQuery = "SELECT Recipe.Rec_Name AS 'Recipe', m.Meal_Prep_Date AS 'Date Planned', m.Rec_ID AS 'ID' FROM Meal m JOIN Recipe ON m.Rec_ID = Recipe.Rec_ID WHERE NOT EXISTS (SELECT * FROM Grocery_Run_Sources_Meal gc WHERE '" + loginInfo.getUserEmail() + "' = gc.HC_Email AND m.Meal_Prep_Date = gc.Preparation_Date AND m.Rec_ID = gc.Rec_ID);";
        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectQuery);

            while(queryOutput.next()) {
                CheckBox check = new CheckBox();
                mealData.add(new ShoppingMealsTable(queryOutput.getString("Recipe"), queryOutput.getString("Date Planned"), check, queryOutput.getString("ID")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        recipeName.setCellValueFactory(new PropertyValueFactory<>("RecName"));
        recipeDate.setCellValueFactory(new PropertyValueFactory<>("RecDate"));
        recipeCheck.setCellValueFactory(new PropertyValueFactory<>("check"));
        recipeID.setCellValueFactory(new PropertyValueFactory<>("recid"));

        shoppingMeals.setItems(mealData);
    }

    public void addProductsToList(ActionEvent event) {
        String queryConcat = "";
        ObservableList<TableColumn<ShoppingMealsTable, ?>> columns = shoppingMeals.getColumns();
        for (Object row: shoppingMeals.getItems()) {
            List<Object> values = new ArrayList<>();
            for (TableColumn column: columns) {
                values.add(column.getCellObservableValue(row).getValue());
            }
            String name = (String) values.get(0);
            String date = (String) values.get(1);
            CheckBox check = (CheckBox) values.get(2);
            String ID = (String) values.get(3);
            if (check.isSelected()) {
                queryConcat = queryConcat + "(Meal.Rec_ID = " + Integer.parseInt(ID) + " AND Meal.Meal_Prep_Date = '" + date + "') OR";
                insertGRSM(groceryRunID, loginInfo.getUserEmail(), Integer.parseInt(ID), date);
            }
            DataBaseConnector connection = new DataBaseConnector();
            Connection connectDB = connection.getConnection();
            prodCheckData = FXCollections.observableArrayList();
            String viewQueryDrop1 = "DROP VIEW if exists info;";
            String viewQuery1 = "CREATE VIEW info AS SELECT DISTINCT Meal_Prep_Date AS 'Date Planned', Meal.Rec_ID, P_Name AS 'ProductName', P_Needed_Amt FROM Meal JOIN Recipe_Uses_Product ON Meal.Rec_ID = Recipe_Uses_Product.Rec_ID WHERE " + queryConcat + "DER BY Rec_ID;";
            String viewQueryDrop2 = "DROP VIEW if exists info2;";
            String viewQuery2 = "CREATE VIEW info2 AS SELECT ProductName, SUM(P_Needed_Amt) AS 'TotalNeeded' FROM info GROUP BY ProductName;";
            String viewQueryDrop3 ="DROP VIEW if exists info3;";
            String viewQuery3 = "CREATE VIEW info3 AS SELECT info2.ProductName AS 'ProdName' FROM Meal_Planner.info2 JOIN HC_Owns_Product hc ON info2.ProductName = hc.P_Name WHERE HC_Email = '" + loginInfo.getUserEmail() + "' AND (info2.TotalNeeded - hc.amount) < 0 OR (info2.TotalNeeded - hc.amount) > 0;";
            String viewQueryDrop4 = "DROP VIEW if exists info4;";
            String viewQuery4 = "CREATE VIEW info4 AS SELECT ProductName, TotalNeeded FROM info2 WHERE info2.ProductName not in (SELECT * FROM info3);";
            String viewQueryDrop5 = "DROP VIEW if exists info5;";
            String viewQuery5 = "CREATE VIEW info5 AS SELECT info2.ProductName AS 'ProdName', hc.Amount AS 'Amount' FROM Meal_Planner.info2 JOIN HC_Owns_Product hc ON info2.ProductName = hc.P_Name WHERE HC_Email = '" + loginInfo.getUserEmail() + "' AND (info2.TotalNeeded - hc.amount) > 0;";
            String viewQueryDrop6 = "DROP VIEW if exists info6;";
            String viewQuery6 = "CREATE VIEW info6 AS SELECT ProductName, (info2.TotalNeeded - info5.Amount) AS 'TotalNeeded' FROM info2 JOIN info5 ON info2.ProductName = info5.ProdName;";
            String connectQuery = "SELECT * FROM info4 UNION SELECT * FROM info6;";
            try {
                Statement statement = connectDB.createStatement();
                statement.addBatch(viewQueryDrop1);
                statement.addBatch(viewQuery1);
                statement.addBatch(viewQueryDrop2);
                statement.addBatch(viewQuery2);
                statement.addBatch(viewQueryDrop3);
                statement.addBatch(viewQuery3);
                statement.addBatch(viewQueryDrop4);
                statement.addBatch(viewQuery4);
                statement.addBatch(viewQueryDrop5);
                statement.addBatch(viewQuery5);
                statement.addBatch(viewQueryDrop6);
                statement.addBatch(viewQuery6);

                statement.executeBatch();

                ResultSet queryOutput = statement.executeQuery(connectQuery);

                while(queryOutput.next()) {
                    String pName = queryOutput.getString("ProductName");
                    String pNeed = queryOutput.getString("TotalNeeded");
                    Button check2 = new Button("Obtained");
                    prodCheckData.add(new ProductCheckListTable(queryOutput.getString("ProductName"), queryOutput.getString("TotalNeeded"), check2));
                    check2.setOnAction( evt -> {
                        DataBaseConnector connection2 = new DataBaseConnector();
                        Connection connectDB2 = connection2.getConnection();
                        String connectQuery2 = "INSERT INTO HC_Owns_Product VALUES ('" + loginInfo.getUserEmail() +"', '" + pName + "', '" + pNeed + "');";
                        try {
                            Statement statement2 = connectDB2.createStatement();
                            statement2.executeUpdate(connectQuery2);
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            prodName.setCellValueFactory(new PropertyValueFactory<>("prodName"));
            prodAmt.setCellValueFactory(new PropertyValueFactory<>("prodAmt"));
            prodCheck.setCellValueFactory(new PropertyValueFactory<>("check"));

            productCheckTable.setItems(prodCheckData);
        }
    }

    public void insertGRSM(int id, String email, int recid, String date) {
        DataBaseConnector connection = new DataBaseConnector();
        Connection connectDB = connection.getConnection();
        String connectQuery = "INSERT INTO Grocery_Run_Sources_Meal VALUES (" + id + ", '" + email + "', " + recid + ", '" + date + "');";
        try {
            Statement statement = connectDB.createStatement();
            statement.executeUpdate(connectQuery);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showDate() {
        date.setText(dateget.toString());
    }

    private Stage getCurrentStage(ActionEvent event) {
        return (Stage) ((Node) event.getSource()).getScene().getWindow();
    }

    public void setLoginInfo(FrameControllerClass loginInfo) {
        this.loginInfo = loginInfo;
    }

    public void setDateGet(Date date) {
        dateget = date;
    }
}
