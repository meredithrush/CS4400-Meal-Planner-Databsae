package application.mealplanner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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

public class WriteRecipeController {
    private Stage stage;
    private FrameControllerClass loginInfo;
    public  Pane mainpane;
    private double dtbuttonPos = 76;
    private double cuisbuttonPos = 76;
    private double dttextX = 151;
    private double cuistextX = 151;
    private final double dttextY = 152;
    private final double cuistextY = 183;
    private Button button;
    public TextField name;
    public TextArea instructions;

    private ArrayList<TextField> dietTags = new ArrayList<>();
    private ArrayList<TextField> cuisineTags = new ArrayList<>();
    public TableView<ProductTable> productTable;
    public TableColumn<ProductTable, String> product;
    public TableColumn<ProductTable, TextField> amount;
    public TableColumn<ProductTable, CheckBox> checkbox;
    private ObservableList<ProductTable> data;

    @FXML
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

    @FXML
    public void logOff(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MPApp.class.getResource("Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage = getCurrentStage(event);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void addProduct(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MPApp.class.getResource("AddProduct.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage = getCurrentStage(event);
        stage.setScene(scene);
        AddProductController newController = fxmlLoader.getController();
        newController.setLoginInfo(loginInfo);
        stage.show();
    }

    @FXML
    public void addDT(ActionEvent event) {
        if (dtbuttonPos <= 228) {
            button = (Button) event.getSource();
            button.setTranslateX(dtbuttonPos);
            dtbuttonPos += 76;
            TextField text = new TextField();
            text.setMinWidth(70);
            text.setMaxWidth(70);
            text.setMinHeight(26);
            text.setMaxHeight(26);
            text.setLayoutX(dttextX);
            text.setLayoutY(dttextY);
            dttextX += 76;
            mainpane.getChildren().add(text);
            dietTags.add(text);

        }
    }

    @FXML
    public void addCuis(ActionEvent event) {
        if (cuisbuttonPos <= 228) {
            button = (Button) event.getSource();
            button.setTranslateX(cuisbuttonPos);
            cuisbuttonPos += 76;
            TextField text = new TextField();
            text.setMinWidth(70);
            text.setMaxWidth(70);
            text.setMinHeight(26);
            text.setMaxHeight(26);
            text.setLayoutX(cuistextX);
            text.setLayoutY(cuistextY);
            cuistextX += 76;
            mainpane.getChildren().add(text);
            cuisineTags.add(text);
        }
    }

    public void save(ActionEvent event) throws SQLException {
        int id;
        DataBaseConnector connection = new DataBaseConnector();
        Connection connectDB = connection.getConnection();
        String connectQuery = "INSERT INTO Recipe (Rec_Name, Rec_Instructions, C_Email) VALUES ('" + name.getText() + "', '" + instructions.getText() + "', '" + loginInfo.getUserEmail() + "');";
        try {
            Statement statement = connectDB.createStatement();
            statement.executeUpdate(connectQuery);
            id = findId();
            saveDT(id);
            saveCuis(id);
            saveProd(id);
            FXMLLoader fxmlLoader = new FXMLLoader(MPApp.class.getResource("ContributerHome.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage = getCurrentStage(event);
            stage.setScene(scene);
            ContributerHomeController newController = fxmlLoader.getController();
            newController.setLoginInfo(loginInfo);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        connectDB.close();
    }
    private int findId() throws SQLException {
        int id = 0;
        DataBaseConnector connection = new DataBaseConnector();
        Connection connectDB = connection.getConnection();
        String connectQuery = "SELECT Rec_ID FROM Recipe WHERE Rec_Instructions = '" + instructions.getText() + "';";
        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectQuery);
            while (queryOutput.next()) {
                id = queryOutput.getInt("Rec_ID");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        connectDB.close();
        return id;
    }
    private void saveDT(int id) throws SQLException {
        for (TextField dietTag: dietTags) {
            if ((dietTags.isEmpty()) || ((dietTag.getText().strip().equals("")) || (dietTag.getText() == null))) {
                continue;
            } else {
                DataBaseConnector connection = new DataBaseConnector();
                Connection connectDB = connection.getConnection();
                String connectQuery = "INSERT INTO Recipe_Diet_Tag VALUES (" + id + ",'" + dietTag.getText() + "');";
                try {
                    Statement statement = connectDB.createStatement();
                    statement.executeUpdate(connectQuery);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                connectDB.close();
            }
        }
    }
    private void saveCuis(int id) throws SQLException {
        for (TextField cuis: cuisineTags) {
            if ((cuisineTags.isEmpty()) || ((cuis.getText().strip().equals("")) || (cuis.getText() == null))) {
                continue;
            } else {
                System.out.println(cuis.getText());
                DataBaseConnector connection = new DataBaseConnector();
                Connection connectDB = connection.getConnection();
                String connectQuery = "INSERT INTO Recipe_Cuisine VALUES (" + id + ",'" + cuis.getText() + "');";
                try {
                    Statement statement = connectDB.createStatement();
                    statement.executeUpdate(connectQuery);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                connectDB.close();
            }
        }
    }

    private void saveProd(int id) throws SQLException {
        ObservableList<TableColumn<ProductTable, ?>> columns = productTable.getColumns();
        for (Object row: productTable.getItems()) {
            List<Object> values = new ArrayList<>();
            for (TableColumn column: columns) {
                values.add(column.getCellObservableValue(row).getValue());
            }
            String prodName = (String) values.get(0);
            TextField prodAmount = (TextField) values.get(1);
            CheckBox check = (CheckBox) values.get(2);
            if (check.isSelected()) {
                DataBaseConnector connection = new DataBaseConnector();
                Connection connectDB = connection.getConnection();
                String connectQuery = "INSERT INTO Recipe_Uses_Product VALUES ('" + prodName + "'," + id + "," + Integer.parseInt(prodAmount.getText()) + ");";
                try {
                    Statement statement = connectDB.createStatement();
                    statement.executeUpdate(connectQuery);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                connectDB.close();
            }
        }
    }

    public void createProductTable() {
        DataBaseConnector connection = new DataBaseConnector();
        Connection connectDB = connection.getConnection();
        data = FXCollections.observableArrayList();
        String connectQuery = "SELECT P_Name AS Name FROM Product;";
        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectQuery);

            while (queryOutput.next()) {
                TextField text = new TextField();
                CheckBox check = new CheckBox();
                data.add(new ProductTable(queryOutput.getString("Name"), text, check));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        product.setCellValueFactory(new PropertyValueFactory<>("product"));
        amount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        checkbox.setCellValueFactory(new PropertyValueFactory<>("checkbox"));

        productTable.setItems(data);
    }

    private Stage getCurrentStage(ActionEvent event) {
        return (Stage) ((Node) event.getSource()).getScene().getWindow();
    }

    public void setLoginInfo(FrameControllerClass loginInfo) {
        this.loginInfo = loginInfo;
    }
}
