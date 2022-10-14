package application.mealplanner;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class AddProductController {
    private Stage stage;
    public Pane mainpane;
    private double tybuttonPos = 76;
    private double tytextX = 189;
    private final double tytextY = 210;
    private Button button;
    private FrameControllerClass loginInfo;
    public TextField name;
    public TextField units;
    public ToggleButton toolYes;
    public ToggleButton toolNo;
    private ArrayList<TextField> types = new ArrayList<>();
    @FXML
    public void back(ActionEvent event) throws IOException {
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
    public void logOff(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MPApp.class.getResource("Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage = getCurrentStage(event);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void addTy(ActionEvent event) {
        if (tybuttonPos <= 228) {
            button = (Button) event.getSource();
            button.setTranslateX(tybuttonPos);
            tybuttonPos += 76;
            TextField text = new TextField();
            text.setMinWidth(70);
            text.setMaxWidth(70);
            text.setMinHeight(26);
            text.setMaxHeight(26);
            text.setLayoutX(tytextX);
            text.setLayoutY(tytextY);
            tytextX += 76;
            mainpane.getChildren().add(text);
            types.add(text);
        }
    }

    public void save(ActionEvent event) throws SQLException {
       String prodname = name.getText();
       String prodUnit = units.getText();
       if (prodUnit == null || prodUnit.strip().equals("")) {
           prodUnit = "unit";
       }
        Boolean tFlag;
        Boolean iFlag;
        if (toolYes.isSelected()) {
            tFlag = true;
            iFlag = false;
       } else {
            tFlag = false;
            iFlag = true;
       }
        DataBaseConnector connection = new DataBaseConnector();
        Connection connectDB = connection.getConnection();
        String connectQuery = "INSERT INTO Product VALUES ('" + prodname + "','" + prodUnit + "'," + iFlag + "," + tFlag + ");";
        try {
            Statement statement = connectDB.createStatement();
            statement.executeUpdate(connectQuery);
            saveType();
            FXMLLoader fxmlLoader = new FXMLLoader(MPApp.class.getResource("WriteRecipe.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage = getCurrentStage(event);
            stage.setScene(scene);
            WriteRecipeController newController = fxmlLoader.getController();
            newController.setLoginInfo(loginInfo);
            newController.createProductTable();
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        connectDB.close();
    }

    private void saveType() throws SQLException {
        for (TextField type: types) {
            if ((types.isEmpty()) || ((type.getText().strip().equals("")) || (type.getText() == null))) {
                continue;
            } else {
                DataBaseConnector connection = new DataBaseConnector();
                Connection connectDB = connection.getConnection();
                String connectQuery = "INSERT INTO Ingredient_Type VALUES ('" + name.getText() + "','" + type.getText() + "');";
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
    private Stage getCurrentStage(ActionEvent event) {
        return (Stage) ((Node) event.getSource()).getScene().getWindow();
    }

    public void setLoginInfo(FrameControllerClass loginInfo) {
        this.loginInfo = loginInfo;
    }
}
