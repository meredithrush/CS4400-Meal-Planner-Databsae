package application.mealplanner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MyProductsController {
    private Stage stage;
    private FrameControllerClass loginInfo;

    public TableView<MyProductTable> myProductsTable;
    public TableColumn<MyProductTable, String> myProductsName;
    public TableColumn<MyProductTable, String> myProductsTypes;
    public TableColumn<MyProductTable, Integer> myProductsAmountOwned;
    public TableColumn<MyProductTable, String> myProductsUnits;
    public TableColumn<MyProductTable, CheckBox> myProductsSelectToUpdate;
    public Pane mainpane;
    private ObservableList<MyProductTable> data;
    private ArrayList<TextField> types = new ArrayList<>();
    private Button button;
    private double typebuttonPos = 76;
    private double typetextX = 80;
    private final double typetextY = 119;
    public TextField selectName;
    public TextField updateAmt;


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

    public void addType(ActionEvent event) {
        if (typebuttonPos <= 152) {
            button = (Button) event.getSource();
            button.setTranslateX(typebuttonPos);
            typebuttonPos += 76;
            TextField text = new TextField();
            text.setMinWidth(70);
            text.setMaxWidth(70);
            text.setMinHeight(26);
            text.setMaxHeight(26);
            text.setLayoutX(typetextX);
            text.setLayoutY(typetextY);
            typetextX += 76;
            mainpane.getChildren().add(text);
            types.add(text);

        }
    }

    public void buildData() {
        DataBaseConnector connection = new DataBaseConnector();
        Connection connectDB = connection.getConnection();
        data = FXCollections.observableArrayList();
        String viewQueryDrop1 = "DROP VIEW if exists product_types;";
        String viewQuery1 = "CREATE VIEW product_types AS SELECT P_Name AS 'Name', GROUP_CONCAT(Ing_Type SEPARATOR ', ') AS 'Types' FROM Ingredient_Type GROUP BY P_Name;";
        String connectQuery = "SELECT HC_Owns_Product.P_Name AS 'Name', product_types.types AS 'Types', HC_Owns_Product.Amount AS 'Amount Owned', Product.Ing_Units AS 'Units' From HC_Owns_Product JOIN Product ON HC_Owns_Product.P_Name = Product.P_Name INNER JOIN product_types ON HC_Owns_Product.P_Name = product_types.name WHERE HC_Owns_Product.HC_Email = '" + loginInfo.getUserEmail() + "';";
        try {
            Statement statement = connectDB.createStatement();
            statement.addBatch(viewQueryDrop1);
            statement.addBatch(viewQuery1);

            statement.executeBatch();

            ResultSet queryOutput = statement.executeQuery(connectQuery);

            while (queryOutput.next()) {
                CheckBox check = new CheckBox();
                data.add(new MyProductTable(queryOutput.getString("Name"), queryOutput.getString("Types"), queryOutput.getInt("Amount Owned"), queryOutput.getString("Units"), check));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        myProductsName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        myProductsAmountOwned.setCellValueFactory(new PropertyValueFactory<>("productAmountOwned"));
        myProductsTypes.setCellValueFactory(new PropertyValueFactory<>("productTypes"));
        myProductsUnits.setCellValueFactory(new PropertyValueFactory<>("productUnits"));
        myProductsSelectToUpdate.setCellValueFactory(new PropertyValueFactory<>("productSelectUpdate"));

        myProductsTable.setItems(data);
    }

    public void filter(ActionEvent event) {
        if (types.isEmpty()) {
            return;
        } else {
            String string = "";
            for (TextField type : types) {
                if (type.getText().strip().equals("") || type.getText() == null) {
                    continue;
                } else {
                    String conc = "AND product_types.types LIKE '%" + type.getText() + "%' ";
                    string = string + conc;
                }
            }
            if (string.equals("") || string == null) {
                return;
            }

            DataBaseConnector connection = new DataBaseConnector();
            Connection connectDB = connection.getConnection();
            data = FXCollections.observableArrayList();
            String viewQueryDrop1 = "DROP VIEW if exists product_types;";
            String viewQuery1 = "CREATE VIEW product_types AS SELECT P_Name AS 'Name', GROUP_CONCAT(Ing_Type SEPARATOR ', ') AS 'Types' FROM Ingredient_Type GROUP BY P_Name;";
            String connectQuery = "SELECT HC_Owns_Product.P_Name AS 'Name', product_types.types AS 'Types', HC_Owns_Product.Amount AS 'Amount Owned', Product.Ing_Units AS 'Units' From HC_Owns_Product JOIN Product ON HC_Owns_Product.P_Name = Product.P_Name INNER JOIN product_types ON HC_Owns_Product.P_Name = product_types.name WHERE HC_Owns_Product.HC_Email = '" + loginInfo.getUserEmail() + "' " + string + ";";
            try {
                Statement statement = connectDB.createStatement();
                statement.addBatch(viewQueryDrop1);
                statement.addBatch(viewQuery1);

                statement.executeBatch();

                ResultSet queryOutput = statement.executeQuery(connectQuery);

                while(queryOutput.next()) {
                    CheckBox check = new CheckBox();
                    data.add(new MyProductTable(queryOutput.getString("Name"), queryOutput.getString("Types"), queryOutput.getInt("Amount Owned"), queryOutput.getString("Units"), check));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            myProductsName.setCellValueFactory(new PropertyValueFactory<>("productName"));
            myProductsAmountOwned.setCellValueFactory(new PropertyValueFactory<>("productAmountOwned"));
            myProductsTypes.setCellValueFactory(new PropertyValueFactory<>("productTypes"));
            myProductsUnits.setCellValueFactory(new PropertyValueFactory<>("productUnits"));
            myProductsSelectToUpdate.setCellValueFactory(new PropertyValueFactory<>("productSelectUpdate"));

            myProductsTable.setItems(data);
        }
    }

    public void selectByName(ActionEvent event) {
        if (selectName.getText().strip().equals("")) {
            return;
        } else {
            DataBaseConnector connection = new DataBaseConnector();
            Connection connectDB = connection.getConnection();
            data = FXCollections.observableArrayList();
            String viewQueryDrop1 = "DROP VIEW if exists product_types;";
            String viewQuery1 = "CREATE VIEW product_types AS SELECT P_Name AS 'Name', GROUP_CONCAT(Ing_Type SEPARATOR ', ') AS 'Types' FROM Ingredient_Type GROUP BY P_Name;";
            String connectQuery = "SELECT HC_Owns_Product.P_Name AS 'Name', product_types.types AS 'Types', HC_Owns_Product.Amount AS 'Amount Owned', Product.Ing_Units AS 'Units' From HC_Owns_Product JOIN Product ON HC_Owns_Product.P_Name = Product.P_Name INNER JOIN product_types ON HC_Owns_Product.P_Name = product_types.name WHERE HC_Owns_Product.HC_Email = '" + loginInfo.getUserEmail() + "' AND HC_Owns_Product.P_Name = '" + selectName.getText() + "';";
            try {
                Statement statement = connectDB.createStatement();
                statement.addBatch(viewQueryDrop1);
                statement.addBatch(viewQuery1);

                statement.executeBatch();

                ResultSet queryOutput = statement.executeQuery(connectQuery);

                while (queryOutput.next()) {
                    CheckBox check = new CheckBox();
                    data.add(new MyProductTable(queryOutput.getString("Name"), queryOutput.getString("Types"), queryOutput.getInt("Amount Owned"), queryOutput.getString("Units"), check));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            myProductsName.setCellValueFactory(new PropertyValueFactory<>("productName"));
            myProductsAmountOwned.setCellValueFactory(new PropertyValueFactory<>("productAmountOwned"));
            myProductsTypes.setCellValueFactory(new PropertyValueFactory<>("productTypes"));
            myProductsUnits.setCellValueFactory(new PropertyValueFactory<>("productUnits"));
            myProductsSelectToUpdate.setCellValueFactory(new PropertyValueFactory<>("productSelectUpdate"));

            myProductsTable.setItems(data);
        }
    }

    public void update(ActionEvent event) throws SQLException {
        ObservableList<TableColumn<MyProductTable, ?>> columns = myProductsTable.getColumns();
        for (Object row: myProductsTable.getItems()) {
            List<Object> values = new ArrayList<>();
            for (TableColumn column: columns) {
                values.add(column.getCellObservableValue(row).getValue());
            }
            String prodName = (String) values.get(0);
            CheckBox prodCheck = (CheckBox) values.get(4);
            if (prodCheck.isSelected()) {
                DataBaseConnector connection = new DataBaseConnector();
                Connection connectDB = connection.getConnection();
                String connectQuery = "UPDATE HC_Owns_Product SET Amount = " + Integer.parseInt(updateAmt.getText()) + " WHERE P_Name = '" + prodName + "' AND HC_Email = '" + loginInfo.getUserEmail() + "';";
                try {
                    Statement statement = connectDB.createStatement();
                    statement.executeUpdate(connectQuery);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                buildData();
                connectDB.close();
            }
        }
    }

    private Stage getCurrentStage(ActionEvent event) {
        return (Stage) ((Node) event.getSource()).getScene().getWindow();
    }

    public void setLoginInfo(FrameControllerClass loginInfo) {
        this.loginInfo = loginInfo;
    }
}
