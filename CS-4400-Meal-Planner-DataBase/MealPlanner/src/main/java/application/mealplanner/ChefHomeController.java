package application.mealplanner;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ChefHomeController {
    private Stage stage;
    private FrameControllerClass loginInfo;

    public void logOff(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MPApp.class.getResource("Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage = getCurrentStage(event);
        stage.setScene(scene);
        stage.show();
    }

    public void back(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MPApp.class.getResource("HomeScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage = getCurrentStage(event);
        stage.setScene(scene);
        HomeScreenController newController = fxmlLoader.getController();
        newController.setLoginInfo(loginInfo);
        stage.show();
    }

    public void seeMyProducts(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MPApp.class.getResource("MyProducts.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        MyProductsController newController = fxmlLoader.getController();
        newController.setLoginInfo(loginInfo);
        newController.buildData();
        stage = getCurrentStage(event);
        stage.setScene(scene);
    }

    public void browseRecipe(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MPApp.class.getResource("BrowseRecipesChef.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        BrowseRecipeChefController newController = fxmlLoader.getController();
        newController.setLoginInfo(loginInfo);
        newController.buildData();
        stage = getCurrentStage(event);
        stage.setScene(scene);
    }

    public void groceryRun(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MPApp.class.getResource("GroceryRuns.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        GroceryRunController newController = fxmlLoader.getController();
        newController.setLoginInfo(loginInfo);
        newController.buildTable();
        stage = getCurrentStage(event);
        stage.setScene(scene);
    }

    private Stage getCurrentStage(ActionEvent event) {
        return (Stage) ((Node) event.getSource()).getScene().getWindow();
    }

    public void setLoginInfo(FrameControllerClass loginInfo) {
        this.loginInfo = loginInfo;
    }
}
