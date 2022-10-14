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
import java.sql.Statement;
import java.util.ArrayList;

public class BrowseRecipeChefController {
    private Stage stage;
    public Pane mainpane;
    private FrameControllerClass loginInfo;
    private double dtbuttonPos = 76;
    private double cuisbuttonPos = 76;
    private double dttextX = 161;
    private double cuistextX = 161;
    private final double dttextY = 126;
    private final double cuistextY = 155;
    private ArrayList<TextField> dietTags = new ArrayList<>();
    private ArrayList<TextField> cuisineTags = new ArrayList<>();
    private Button button;
    public TableView<BrowseRecipeChefTable> recipeTable;
    public TableColumn<BrowseRecipeChefTable, Hyperlink> tableID;
    public TableColumn<BrowseRecipeChefTable, String> tableName;
    public TableColumn<BrowseRecipeChefTable, String>  tableAuthor;
    public TableColumn<BrowseRecipeChefTable, String>  tableCuisines;
    public TableColumn<BrowseRecipeChefTable, String>  tableDietTags;
    public TableColumn<BrowseRecipeChefTable, String>  tableAvgRating;
    public TableColumn<BrowseRecipeChefTable, Double>  tablePercentOwned;
    private ObservableList<BrowseRecipeChefTable> data;

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

    public void buildData() {
        DataBaseConnector connection = new DataBaseConnector();
        Connection connectDB = connection.getConnection();
        data = FXCollections.observableArrayList();

        String viewQueryDrop1 = "DROP VIEW if exists C_BrowseRecipes;";
        String viewQueryDrop2 = "DROP VIEW if exists Recipe_Average;";
        String viewQueryDrop3 = "DROP VIEW if exists diet_tags;";
        String viewQueryDrop4 = "DROP VIEW if exists cuisines;";
        String viewQueryDrop5 = "DROP VIEW if exists hc_owns;";
        String viewQueryDrop6 = "DROP VIEW if exists hc_owns_enough;";
        String viewQueryDrop7 = "DROP VIEW if exists numIng;";
        String viewQueryDrop8 = "DROP VIEW if exists percentageOwned;";
        String viewQuery1 = "CREATE VIEW C_BrowseRecipes AS SELECT Recipe.Rec_ID AS 'ID', Recipe.Rec_Name AS 'Name', Recipe.C_Email AS Author FROM Recipe ORDER BY 'ID';";
        String viewQuery2 = "CREATE VIEW Recipe_Average AS SELECT Recipe.Rec_ID AS 'ID', AVG(Rev_Rating) AS 'AverageRating' FROM Recipe JOIN Review ON Recipe.Rec_ID = Review.Rec_ID GROUP BY Recipe.Rec_ID;";
        String viewQuery3 = "CREATE VIEW diet_tags AS SELECT Rec_ID AS 'ID', GROUP_CONCAT(Rec_Diet_Tag SEPARATOR ', ') AS 'DietTags' FROM Recipe_Diet_Tag GROUP BY Rec_ID;";
        String viewQuery4 = "CREATE VIEW cuisines AS SELECT Rec_ID AS 'ID', GROUP_CONCAT(Rec_Cuisine SEPARATOR ', ') AS 'Cuisine' FROM Recipe_Cuisine GROUP BY Rec_ID;";
        String viewQuery5 = "CREATE VIEW hc_owns AS SELECT rp.Rec_ID AS 'RecipeID', rp.P_Name AS 'Name', hc.Amount AS 'Owned Amount', rp.P_Needed_Amt AS 'Recipe Amount', (hc.Amount - rp.P_Needed_Amt) AS 'Difference' FROM HC_Owns_Product hc RIGHT JOIN Recipe_Uses_Product rp ON hc.P_Name = rp.P_Name WHERE hc.HC_Email = '" + loginInfo.getUserEmail() + "';";
        String viewQuery6 = "CREATE VIEW hc_owns_enough AS SELECT hc_owns.RecipeID AS 'RecID', COUNT(Difference) AS 'OwnsEnough' FROM hc_owns WHERE hc_owns.Difference >= 0 GROUP BY hc_owns.RecipeID;";
        String viewQuery7 = "CREATE VIEW numIng AS SELECT Rec_ID, COUNT(P_Name) AS 'NumberOfIng' FROM Recipe_Uses_Product GROUP BY Rec_ID;";
        String viewQuery8 = "CREATE VIEW percentageOwned AS SELECT numIng.Rec_ID AS 'ID', (hc_owns_enough.ownsEnough * 100 / numIng.numberOfIng) AS 'Percentage' FROM hc_owns_enough JOIN numIng ON numIng.Rec_ID = hc_owns_enough.RecID;";
        String connectQuery = "SELECT browse.ID AS 'ID', browse.Name AS 'Name', browse.Author AS 'Author', c.cuisine AS 'Cuisines', dt.diettags AS 'Diet Tags', ravg.averagerating AS 'Average Rating', po.percentage AS 'Percentage Owned' FROM Meal_Planner.c_browserecipes AS browse LEFT JOIN Meal_Planner.recipe_average AS ravg ON browse.ID = ravg.ID LEFT JOIN Meal_Planner.cuisines AS c ON browse.ID = c.ID LEFT JOIN Meal_Planner.diet_tags AS dt ON browse.ID = dt.ID LEFT JOIN Meal_Planner.percentageowned AS po ON browse.ID = po.ID;";
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
            statement.addBatch(viewQueryDrop7);
            statement.addBatch(viewQuery7);
            statement.addBatch(viewQueryDrop8);
            statement.addBatch(viewQuery8);

            statement.executeBatch();

            ResultSet queryOutput = statement.executeQuery(connectQuery);

            while(queryOutput.next()) {
                int id = Integer.parseInt(queryOutput.getString("ID"));
                Hyperlink link = new Hyperlink(queryOutput.getString("ID"));
                data.add(new BrowseRecipeChefTable(link, queryOutput.getString("Name"), queryOutput.getString("Author"), queryOutput.getString("Cuisines"), queryOutput.getString("Diet Tags"), queryOutput.getString("Average Rating"), queryOutput.getDouble("Percentage Owned")));
                link.setOnAction(evt -> {
                    FXMLLoader fxmlLoader = new FXMLLoader(MPApp.class.getResource("RecipeHC.fxml"));
                    try {
                        Scene scene = new Scene(fxmlLoader.load());
                        stage = getCurrentStage(evt);
                        stage.setScene(scene);
                        RecipeHCController newController = fxmlLoader.getController();
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        tableID.setCellValueFactory(new PropertyValueFactory<>("browseID"));
        tableName.setCellValueFactory(new PropertyValueFactory<>("browseName"));
        tableAuthor.setCellValueFactory(new PropertyValueFactory<>("browseAuthor"));
        tableCuisines.setCellValueFactory(new PropertyValueFactory<>("browseCuisines"));
        tableDietTags.setCellValueFactory(new PropertyValueFactory<>("browseDietTags"));
        tableAvgRating.setCellValueFactory(new PropertyValueFactory<>("browseAvgRating"));
        tablePercentOwned.setCellValueFactory(new PropertyValueFactory<>("browsePercOwned"));

        recipeTable.setItems(data);
    }

    public void filter(ActionEvent event) {
        if (dietTags.isEmpty() && cuisineTags.isEmpty()) {
            return;
        } else {
            String string = "";
            for (TextField dietTag : dietTags) {
                if (dietTag.getText().strip().equals("") || dietTag.getText() == null) {
                    continue;
                } else {
                    String conc = "AND dt.diettags LIKE '%" + dietTag.getText() + "%' ";
                    string = string + conc;
                }
            }
            for (TextField cuis : cuisineTags) {
                if (cuis.getText().strip().equals("") || cuis.getText() == null) {
                    continue;
                } else {
                    String conc = "AND c.cuisine LIKE '%" + cuis.getText() + "%' ";
                    string = string + conc;
                }
            }
            if (string.equals("") || string == null) {
                return;
            }
            System.out.println(string);
            DataBaseConnector connection = new DataBaseConnector();
            Connection connectDB = connection.getConnection();
            data = FXCollections.observableArrayList();
            String viewQueryDrop1 = "DROP VIEW if exists C_BrowseRecipes;";
            String viewQueryDrop2 = "DROP VIEW if exists Recipe_Average;";
            String viewQueryDrop3 = "DROP VIEW if exists diet_tags;";
            String viewQueryDrop4 = "DROP VIEW if exists cuisines;";
            String viewQueryDrop5 = "DROP VIEW if exists hc_owns;";
            String viewQueryDrop6 = "DROP VIEW if exists hc_owns_enough;";
            String viewQueryDrop7 = "DROP VIEW if exists numIng;";
            String viewQueryDrop8 = "DROP VIEW if exists percentageOwned;";
            String viewQuery1 = "CREATE VIEW C_BrowseRecipes AS SELECT Recipe.Rec_ID AS 'ID', Recipe.Rec_Name AS 'Name', Recipe.C_Email AS Author FROM Recipe ORDER BY 'ID';";
            String viewQuery2 = "CREATE VIEW Recipe_Average AS SELECT Recipe.Rec_ID AS 'ID', AVG(Rev_Rating) AS 'AverageRating' FROM Recipe JOIN Review ON Recipe.Rec_ID = Review.Rec_ID GROUP BY Recipe.Rec_ID;";
            String viewQuery3 = "CREATE VIEW diet_tags AS SELECT Rec_ID AS 'ID', GROUP_CONCAT(Rec_Diet_Tag SEPARATOR ', ') AS 'DietTags' FROM Recipe_Diet_Tag GROUP BY Rec_ID;";
            String viewQuery4 = "CREATE VIEW cuisines AS SELECT Rec_ID AS 'ID', GROUP_CONCAT(Rec_Cuisine SEPARATOR ', ') AS 'Cuisine' FROM Recipe_Cuisine GROUP BY Rec_ID;";
            String viewQuery5 = "CREATE VIEW hc_owns AS SELECT rp.Rec_ID AS 'RecipeID', rp.P_Name AS 'Name', hc.Amount AS 'Owned Amount', rp.P_Needed_Amt AS 'Recipe Amount', (hc.Amount - rp.P_Needed_Amt) AS 'Difference' FROM HC_Owns_Product hc RIGHT JOIN Recipe_Uses_Product rp ON hc.P_Name = rp.P_Name WHERE hc.HC_Email = '" + loginInfo.getUserEmail() + "';";
            String viewQuery6 = "CREATE VIEW hc_owns_enough AS SELECT hc_owns.RecipeID AS 'RecID', COUNT(Difference) AS 'OwnsEnough' FROM hc_owns WHERE hc_owns.Difference >= 0 GROUP BY hc_owns.RecipeID;";
            String viewQuery7 = "CREATE VIEW numIng AS SELECT Rec_ID, COUNT(P_Name) AS 'NumberOfIng' FROM Recipe_Uses_Product GROUP BY Rec_ID;";
            String viewQuery8 = "CREATE VIEW percentageOwned AS SELECT numIng.Rec_ID AS 'ID', (hc_owns_enough.ownsEnough * 100 / numIng.numberOfIng) AS 'Percentage' FROM hc_owns_enough JOIN numIng ON numIng.Rec_ID = hc_owns_enough.RecID;";
            String connectQuery = "SELECT browse.ID AS 'ID', browse.Name AS 'Name', browse.Author AS 'Author', c.cuisine AS 'Cuisines', dt.diettags AS 'Diet Tags', ravg.averagerating AS 'Average Rating', po.percentage AS 'Percentage Owned' FROM Meal_Planner.c_browserecipes AS browse INNER JOIN Meal_Planner.recipe_average AS ravg ON browse.ID = ravg.ID INNER JOIN Meal_Planner.cuisines AS c ON browse.ID = c.ID INNER JOIN Meal_Planner.diet_tags AS dt ON browse.ID = dt.ID INNER JOIN Meal_Planner.percentageowned AS po WHERE browse.ID = po.ID " + string + ";";
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
                statement.addBatch(viewQueryDrop7);
                statement.addBatch(viewQuery7);
                statement.addBatch(viewQueryDrop8);
                statement.addBatch(viewQuery8);

                statement.executeBatch();

                ResultSet queryOutput = statement.executeQuery(connectQuery);

                while (queryOutput.next()) {
                    int id = Integer.parseInt(queryOutput.getString("ID"));
                    Hyperlink link = new Hyperlink(queryOutput.getString("ID"));
                    data.add(new BrowseRecipeChefTable(link, queryOutput.getString("Name"), queryOutput.getString("Author"), queryOutput.getString("Cuisines"), queryOutput.getString("Diet Tags"), queryOutput.getString("Average Rating"), queryOutput.getDouble("Percentage Owned")));
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
            } catch (Exception e) {
                e.printStackTrace();
            }

            tableID.setCellValueFactory(new PropertyValueFactory<>("browseID"));
            tableName.setCellValueFactory(new PropertyValueFactory<>("browseName"));
            tableAuthor.setCellValueFactory(new PropertyValueFactory<>("browseAuthor"));
            tableCuisines.setCellValueFactory(new PropertyValueFactory<>("browseCuisines"));
            tableDietTags.setCellValueFactory(new PropertyValueFactory<>("browseDietTags"));
            tableAvgRating.setCellValueFactory(new PropertyValueFactory<>("browseAvgRating"));

            recipeTable.setItems(data);
        }
    }

    public void sortAscRat(ActionEvent event) {
        DataBaseConnector connection = new DataBaseConnector();
        Connection connectDB = connection.getConnection();
        data = FXCollections.observableArrayList();

        String viewQueryDrop1 = "DROP VIEW if exists C_BrowseRecipes;";
        String viewQueryDrop2 = "DROP VIEW if exists Recipe_Average;";
        String viewQueryDrop3 = "DROP VIEW if exists diet_tags;";
        String viewQueryDrop4 = "DROP VIEW if exists cuisines;";
        String viewQueryDrop5 = "DROP VIEW if exists hc_owns;";
        String viewQueryDrop6 = "DROP VIEW if exists hc_owns_enough;";
        String viewQueryDrop7 = "DROP VIEW if exists numIng;";
        String viewQueryDrop8 = "DROP VIEW if exists percentageOwned;";
        String viewQuery1 = "CREATE VIEW C_BrowseRecipes AS SELECT Recipe.Rec_ID AS 'ID', Recipe.Rec_Name AS 'Name', Recipe.C_Email AS Author FROM Recipe ORDER BY 'ID';";
        String viewQuery2 = "CREATE VIEW Recipe_Average AS SELECT Recipe.Rec_ID AS 'ID', AVG(Rev_Rating) AS 'AverageRating' FROM Recipe JOIN Review ON Recipe.Rec_ID = Review.Rec_ID GROUP BY Recipe.Rec_ID;";
        String viewQuery3 = "CREATE VIEW diet_tags AS SELECT Rec_ID AS 'ID', GROUP_CONCAT(Rec_Diet_Tag SEPARATOR ', ') AS 'DietTags' FROM Recipe_Diet_Tag GROUP BY Rec_ID;";
        String viewQuery4 = "CREATE VIEW cuisines AS SELECT Rec_ID AS 'ID', GROUP_CONCAT(Rec_Cuisine SEPARATOR ', ') AS 'Cuisine' FROM Recipe_Cuisine GROUP BY Rec_ID;";
        String viewQuery5 = "CREATE VIEW hc_owns AS SELECT rp.Rec_ID AS 'RecipeID', rp.P_Name AS 'Name', hc.Amount AS 'Owned Amount', rp.P_Needed_Amt AS 'Recipe Amount', (hc.Amount - rp.P_Needed_Amt) AS 'Difference' FROM HC_Owns_Product hc RIGHT JOIN Recipe_Uses_Product rp ON hc.P_Name = rp.P_Name WHERE hc.HC_Email = '" + loginInfo.getUserEmail() + "';";
        String viewQuery6 = "CREATE VIEW hc_owns_enough AS SELECT hc_owns.RecipeID AS 'RecID', COUNT(Difference) AS 'OwnsEnough' FROM hc_owns WHERE hc_owns.Difference >= 0 GROUP BY hc_owns.RecipeID;";
        String viewQuery7 = "CREATE VIEW numIng AS SELECT Rec_ID, COUNT(P_Name) AS 'NumberOfIng' FROM Recipe_Uses_Product GROUP BY Rec_ID;";
        String viewQuery8 = "CREATE VIEW percentageOwned AS SELECT numIng.Rec_ID AS 'ID', (hc_owns_enough.ownsEnough * 100 / numIng.numberOfIng) AS 'Percentage' FROM hc_owns_enough JOIN numIng ON numIng.Rec_ID = hc_owns_enough.RecID;";
        String connectQuery = "SELECT browse.ID AS 'ID', browse.Name AS 'Name', browse.Author AS 'Author', c.cuisine AS 'Cuisines', dt.diettags AS 'Diet Tags', ravg.averagerating AS 'Average Rating', po.percentage AS 'Percentage Owned' FROM Meal_Planner.c_browserecipes AS browse LEFT JOIN Meal_Planner.recipe_average AS ravg ON browse.ID = ravg.ID LEFT JOIN Meal_Planner.cuisines AS c ON browse.ID = c.ID LEFT JOIN Meal_Planner.diet_tags AS dt ON browse.ID = dt.ID LEFT JOIN Meal_Planner.percentageowned AS po ON browse.ID = po.ID ORDER BY ravg.averagerating;";
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
            statement.addBatch(viewQueryDrop7);
            statement.addBatch(viewQuery7);
            statement.addBatch(viewQueryDrop8);
            statement.addBatch(viewQuery8);

            statement.executeBatch();

            ResultSet queryOutput = statement.executeQuery(connectQuery);

            while(queryOutput.next()) {
                int id = Integer.parseInt(queryOutput.getString("ID"));
                Hyperlink link = new Hyperlink(queryOutput.getString("ID"));
                data.add(new BrowseRecipeChefTable(link, queryOutput.getString("Name"), queryOutput.getString("Author"), queryOutput.getString("Cuisines"), queryOutput.getString("Diet Tags"), queryOutput.getString("Average Rating"), queryOutput.getDouble("Percentage Owned")));
                link.setOnAction(evt -> {
                    FXMLLoader fxmlLoader = new FXMLLoader(MPApp.class.getResource("RecipeHC.fxml"));
                    try {
                        Scene scene = new Scene(fxmlLoader.load());
                        stage = getCurrentStage(evt);
                        stage.setScene(scene);
                        RecipeHCController newController = fxmlLoader.getController();
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        tableID.setCellValueFactory(new PropertyValueFactory<>("browseID"));
        tableName.setCellValueFactory(new PropertyValueFactory<>("browseName"));
        tableAuthor.setCellValueFactory(new PropertyValueFactory<>("browseAuthor"));
        tableCuisines.setCellValueFactory(new PropertyValueFactory<>("browseCuisines"));
        tableDietTags.setCellValueFactory(new PropertyValueFactory<>("browseDietTags"));
        tableAvgRating.setCellValueFactory(new PropertyValueFactory<>("browseAvgRating"));
        tablePercentOwned.setCellValueFactory(new PropertyValueFactory<>("browsePercOwned"));

        recipeTable.setItems(data);
    }

    public void sortDescRat(ActionEvent event) {
        DataBaseConnector connection = new DataBaseConnector();
        Connection connectDB = connection.getConnection();
        data = FXCollections.observableArrayList();

        String viewQueryDrop1 = "DROP VIEW if exists C_BrowseRecipes;";
        String viewQueryDrop2 = "DROP VIEW if exists Recipe_Average;";
        String viewQueryDrop3 = "DROP VIEW if exists diet_tags;";
        String viewQueryDrop4 = "DROP VIEW if exists cuisines;";
        String viewQueryDrop5 = "DROP VIEW if exists hc_owns;";
        String viewQueryDrop6 = "DROP VIEW if exists hc_owns_enough;";
        String viewQueryDrop7 = "DROP VIEW if exists numIng;";
        String viewQueryDrop8 = "DROP VIEW if exists percentageOwned;";
        String viewQuery1 = "CREATE VIEW C_BrowseRecipes AS SELECT Recipe.Rec_ID AS 'ID', Recipe.Rec_Name AS 'Name', Recipe.C_Email AS Author FROM Recipe ORDER BY 'ID';";
        String viewQuery2 = "CREATE VIEW Recipe_Average AS SELECT Recipe.Rec_ID AS 'ID', AVG(Rev_Rating) AS 'AverageRating' FROM Recipe JOIN Review ON Recipe.Rec_ID = Review.Rec_ID GROUP BY Recipe.Rec_ID;";
        String viewQuery3 = "CREATE VIEW diet_tags AS SELECT Rec_ID AS 'ID', GROUP_CONCAT(Rec_Diet_Tag SEPARATOR ', ') AS 'DietTags' FROM Recipe_Diet_Tag GROUP BY Rec_ID;";
        String viewQuery4 = "CREATE VIEW cuisines AS SELECT Rec_ID AS 'ID', GROUP_CONCAT(Rec_Cuisine SEPARATOR ', ') AS 'Cuisine' FROM Recipe_Cuisine GROUP BY Rec_ID;";
        String viewQuery5 = "CREATE VIEW hc_owns AS SELECT rp.Rec_ID AS 'RecipeID', rp.P_Name AS 'Name', hc.Amount AS 'Owned Amount', rp.P_Needed_Amt AS 'Recipe Amount', (hc.Amount - rp.P_Needed_Amt) AS 'Difference' FROM HC_Owns_Product hc RIGHT JOIN Recipe_Uses_Product rp ON hc.P_Name = rp.P_Name WHERE hc.HC_Email = '" + loginInfo.getUserEmail() + "';";
        String viewQuery6 = "CREATE VIEW hc_owns_enough AS SELECT hc_owns.RecipeID AS 'RecID', COUNT(Difference) AS 'OwnsEnough' FROM hc_owns WHERE hc_owns.Difference >= 0 GROUP BY hc_owns.RecipeID;";
        String viewQuery7 = "CREATE VIEW numIng AS SELECT Rec_ID, COUNT(P_Name) AS 'NumberOfIng' FROM Recipe_Uses_Product GROUP BY Rec_ID;";
        String viewQuery8 = "CREATE VIEW percentageOwned AS SELECT numIng.Rec_ID AS 'ID', (hc_owns_enough.ownsEnough * 100 / numIng.numberOfIng) AS 'Percentage' FROM hc_owns_enough JOIN numIng ON numIng.Rec_ID = hc_owns_enough.RecID;";
        String connectQuery = "SELECT browse.ID AS 'ID', browse.Name AS 'Name', browse.Author AS 'Author', c.cuisine AS 'Cuisines', dt.diettags AS 'Diet Tags', ravg.averagerating AS 'Average Rating', po.percentage AS 'Percentage Owned' FROM Meal_Planner.c_browserecipes AS browse LEFT JOIN Meal_Planner.recipe_average AS ravg ON browse.ID = ravg.ID LEFT JOIN Meal_Planner.cuisines AS c ON browse.ID = c.ID LEFT JOIN Meal_Planner.diet_tags AS dt ON browse.ID = dt.ID LEFT JOIN Meal_Planner.percentageowned AS po ON browse.ID = po.ID ORDER BY ravg.averagerating DESC;";
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
            statement.addBatch(viewQueryDrop7);
            statement.addBatch(viewQuery7);
            statement.addBatch(viewQueryDrop8);
            statement.addBatch(viewQuery8);

            statement.executeBatch();

            ResultSet queryOutput = statement.executeQuery(connectQuery);

            while(queryOutput.next()) {
                int id = Integer.parseInt(queryOutput.getString("ID"));
                Hyperlink link = new Hyperlink(queryOutput.getString("ID"));
                data.add(new BrowseRecipeChefTable(link, queryOutput.getString("Name"), queryOutput.getString("Author"), queryOutput.getString("Cuisines"), queryOutput.getString("Diet Tags"), queryOutput.getString("Average Rating"), queryOutput.getDouble("Percentage Owned")));
                link.setOnAction(evt -> {
                    FXMLLoader fxmlLoader = new FXMLLoader(MPApp.class.getResource("RecipeHC.fxml"));
                    try {
                        Scene scene = new Scene(fxmlLoader.load());
                        stage = getCurrentStage(evt);
                        stage.setScene(scene);
                        RecipeHCController newController = fxmlLoader.getController();
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        tableID.setCellValueFactory(new PropertyValueFactory<>("browseID"));
        tableName.setCellValueFactory(new PropertyValueFactory<>("browseName"));
        tableAuthor.setCellValueFactory(new PropertyValueFactory<>("browseAuthor"));
        tableCuisines.setCellValueFactory(new PropertyValueFactory<>("browseCuisines"));
        tableDietTags.setCellValueFactory(new PropertyValueFactory<>("browseDietTags"));
        tableAvgRating.setCellValueFactory(new PropertyValueFactory<>("browseAvgRating"));
        tablePercentOwned.setCellValueFactory(new PropertyValueFactory<>("browsePercOwned"));

        recipeTable.setItems(data);
    }

    public void sortAscP(ActionEvent event) {
        DataBaseConnector connection = new DataBaseConnector();
        Connection connectDB = connection.getConnection();
        data = FXCollections.observableArrayList();

        String viewQueryDrop1 = "DROP VIEW if exists C_BrowseRecipes;";
        String viewQueryDrop2 = "DROP VIEW if exists Recipe_Average;";
        String viewQueryDrop3 = "DROP VIEW if exists diet_tags;";
        String viewQueryDrop4 = "DROP VIEW if exists cuisines;";
        String viewQueryDrop5 = "DROP VIEW if exists hc_owns;";
        String viewQueryDrop6 = "DROP VIEW if exists hc_owns_enough;";
        String viewQueryDrop7 = "DROP VIEW if exists numIng;";
        String viewQueryDrop8 = "DROP VIEW if exists percentageOwned;";
        String viewQuery1 = "CREATE VIEW C_BrowseRecipes AS SELECT Recipe.Rec_ID AS 'ID', Recipe.Rec_Name AS 'Name', Recipe.C_Email AS Author FROM Recipe ORDER BY 'ID';";
        String viewQuery2 = "CREATE VIEW Recipe_Average AS SELECT Recipe.Rec_ID AS 'ID', AVG(Rev_Rating) AS 'AverageRating' FROM Recipe JOIN Review ON Recipe.Rec_ID = Review.Rec_ID GROUP BY Recipe.Rec_ID;";
        String viewQuery3 = "CREATE VIEW diet_tags AS SELECT Rec_ID AS 'ID', GROUP_CONCAT(Rec_Diet_Tag SEPARATOR ', ') AS 'DietTags' FROM Recipe_Diet_Tag GROUP BY Rec_ID;";
        String viewQuery4 = "CREATE VIEW cuisines AS SELECT Rec_ID AS 'ID', GROUP_CONCAT(Rec_Cuisine SEPARATOR ', ') AS 'Cuisine' FROM Recipe_Cuisine GROUP BY Rec_ID;";
        String viewQuery5 = "CREATE VIEW hc_owns AS SELECT rp.Rec_ID AS 'RecipeID', rp.P_Name AS 'Name', hc.Amount AS 'Owned Amount', rp.P_Needed_Amt AS 'Recipe Amount', (hc.Amount - rp.P_Needed_Amt) AS 'Difference' FROM HC_Owns_Product hc RIGHT JOIN Recipe_Uses_Product rp ON hc.P_Name = rp.P_Name WHERE hc.HC_Email = '" + loginInfo.getUserEmail() + "';";
        String viewQuery6 = "CREATE VIEW hc_owns_enough AS SELECT hc_owns.RecipeID AS 'RecID', COUNT(Difference) AS 'OwnsEnough' FROM hc_owns WHERE hc_owns.Difference >= 0 GROUP BY hc_owns.RecipeID;";
        String viewQuery7 = "CREATE VIEW numIng AS SELECT Rec_ID, COUNT(P_Name) AS 'NumberOfIng' FROM Recipe_Uses_Product GROUP BY Rec_ID;";
        String viewQuery8 = "CREATE VIEW percentageOwned AS SELECT numIng.Rec_ID AS 'ID', (hc_owns_enough.ownsEnough * 100 / numIng.numberOfIng) AS 'Percentage' FROM hc_owns_enough JOIN numIng ON numIng.Rec_ID = hc_owns_enough.RecID;";
        String connectQuery = "SELECT browse.ID AS 'ID', browse.Name AS 'Name', browse.Author AS 'Author', c.cuisine AS 'Cuisines', dt.diettags AS 'Diet Tags', ravg.averagerating AS 'Average Rating', po.percentage AS 'Percentage Owned' FROM Meal_Planner.c_browserecipes AS browse LEFT JOIN Meal_Planner.recipe_average AS ravg ON browse.ID = ravg.ID LEFT JOIN Meal_Planner.cuisines AS c ON browse.ID = c.ID LEFT JOIN Meal_Planner.diet_tags AS dt ON browse.ID = dt.ID LEFT JOIN Meal_Planner.percentageowned AS po ON browse.ID = po.ID ORDER BY po.percentage;";
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
            statement.addBatch(viewQueryDrop7);
            statement.addBatch(viewQuery7);
            statement.addBatch(viewQueryDrop8);
            statement.addBatch(viewQuery8);

            statement.executeBatch();

            ResultSet queryOutput = statement.executeQuery(connectQuery);

            while(queryOutput.next()) {
                int id = Integer.parseInt(queryOutput.getString("ID"));
                Hyperlink link = new Hyperlink(queryOutput.getString("ID"));
                data.add(new BrowseRecipeChefTable(link, queryOutput.getString("Name"), queryOutput.getString("Author"), queryOutput.getString("Cuisines"), queryOutput.getString("Diet Tags"), queryOutput.getString("Average Rating"), queryOutput.getDouble("Percentage Owned")));
                link.setOnAction(evt -> {
                    FXMLLoader fxmlLoader = new FXMLLoader(MPApp.class.getResource("RecipeHC.fxml"));
                    try {
                        Scene scene = new Scene(fxmlLoader.load());
                        stage = getCurrentStage(evt);
                        stage.setScene(scene);
                        RecipeHCController newController = fxmlLoader.getController();
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        tableID.setCellValueFactory(new PropertyValueFactory<>("browseID"));
        tableName.setCellValueFactory(new PropertyValueFactory<>("browseName"));
        tableAuthor.setCellValueFactory(new PropertyValueFactory<>("browseAuthor"));
        tableCuisines.setCellValueFactory(new PropertyValueFactory<>("browseCuisines"));
        tableDietTags.setCellValueFactory(new PropertyValueFactory<>("browseDietTags"));
        tableAvgRating.setCellValueFactory(new PropertyValueFactory<>("browseAvgRating"));
        tablePercentOwned.setCellValueFactory(new PropertyValueFactory<>("browsePercOwned"));

        recipeTable.setItems(data);
    }

    public void sortDescP(ActionEvent event) {
        DataBaseConnector connection = new DataBaseConnector();
        Connection connectDB = connection.getConnection();
        data = FXCollections.observableArrayList();

        String viewQueryDrop1 = "DROP VIEW if exists C_BrowseRecipes;";
        String viewQueryDrop2 = "DROP VIEW if exists Recipe_Average;";
        String viewQueryDrop3 = "DROP VIEW if exists diet_tags;";
        String viewQueryDrop4 = "DROP VIEW if exists cuisines;";
        String viewQueryDrop5 = "DROP VIEW if exists hc_owns;";
        String viewQueryDrop6 = "DROP VIEW if exists hc_owns_enough;";
        String viewQueryDrop7 = "DROP VIEW if exists numIng;";
        String viewQueryDrop8 = "DROP VIEW if exists percentageOwned;";
        String viewQuery1 = "CREATE VIEW C_BrowseRecipes AS SELECT Recipe.Rec_ID AS 'ID', Recipe.Rec_Name AS 'Name', Recipe.C_Email AS Author FROM Recipe ORDER BY 'ID';";
        String viewQuery2 = "CREATE VIEW Recipe_Average AS SELECT Recipe.Rec_ID AS 'ID', AVG(Rev_Rating) AS 'AverageRating' FROM Recipe JOIN Review ON Recipe.Rec_ID = Review.Rec_ID GROUP BY Recipe.Rec_ID;";
        String viewQuery3 = "CREATE VIEW diet_tags AS SELECT Rec_ID AS 'ID', GROUP_CONCAT(Rec_Diet_Tag SEPARATOR ', ') AS 'DietTags' FROM Recipe_Diet_Tag GROUP BY Rec_ID;";
        String viewQuery4 = "CREATE VIEW cuisines AS SELECT Rec_ID AS 'ID', GROUP_CONCAT(Rec_Cuisine SEPARATOR ', ') AS 'Cuisine' FROM Recipe_Cuisine GROUP BY Rec_ID;";
        String viewQuery5 = "CREATE VIEW hc_owns AS SELECT rp.Rec_ID AS 'RecipeID', rp.P_Name AS 'Name', hc.Amount AS 'Owned Amount', rp.P_Needed_Amt AS 'Recipe Amount', (hc.Amount - rp.P_Needed_Amt) AS 'Difference' FROM HC_Owns_Product hc RIGHT JOIN Recipe_Uses_Product rp ON hc.P_Name = rp.P_Name WHERE hc.HC_Email = '" + loginInfo.getUserEmail() + "';";
        String viewQuery6 = "CREATE VIEW hc_owns_enough AS SELECT hc_owns.RecipeID AS 'RecID', COUNT(Difference) AS 'OwnsEnough' FROM hc_owns WHERE hc_owns.Difference >= 0 GROUP BY hc_owns.RecipeID;";
        String viewQuery7 = "CREATE VIEW numIng AS SELECT Rec_ID, COUNT(P_Name) AS 'NumberOfIng' FROM Recipe_Uses_Product GROUP BY Rec_ID;";
        String viewQuery8 = "CREATE VIEW percentageOwned AS SELECT numIng.Rec_ID AS 'ID', (hc_owns_enough.ownsEnough * 100 / numIng.numberOfIng) AS 'Percentage' FROM hc_owns_enough JOIN numIng ON numIng.Rec_ID = hc_owns_enough.RecID;";
        String connectQuery = "SELECT browse.ID AS 'ID', browse.Name AS 'Name', browse.Author AS 'Author', c.cuisine AS 'Cuisines', dt.diettags AS 'Diet Tags', ravg.averagerating AS 'Average Rating', po.percentage AS 'Percentage Owned' FROM Meal_Planner.c_browserecipes AS browse LEFT JOIN Meal_Planner.recipe_average AS ravg ON browse.ID = ravg.ID LEFT JOIN Meal_Planner.cuisines AS c ON browse.ID = c.ID LEFT JOIN Meal_Planner.diet_tags AS dt ON browse.ID = dt.ID LEFT JOIN Meal_Planner.percentageowned AS po ON browse.ID = po.ID ORDER BY po.percentage DESC;\n";
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
            statement.addBatch(viewQueryDrop7);
            statement.addBatch(viewQuery7);
            statement.addBatch(viewQueryDrop8);
            statement.addBatch(viewQuery8);

            statement.executeBatch();

            ResultSet queryOutput = statement.executeQuery(connectQuery);

            while(queryOutput.next()) {
                int id = Integer.parseInt(queryOutput.getString("ID"));
                Hyperlink link = new Hyperlink(queryOutput.getString("ID"));
                data.add(new BrowseRecipeChefTable(link, queryOutput.getString("Name"), queryOutput.getString("Author"), queryOutput.getString("Cuisines"), queryOutput.getString("Diet Tags"), queryOutput.getString("Average Rating"), queryOutput.getDouble("Percentage Owned")));
                link.setOnAction(evt -> {
                    FXMLLoader fxmlLoader = new FXMLLoader(MPApp.class.getResource("RecipeHC.fxml"));
                    try {
                        Scene scene = new Scene(fxmlLoader.load());
                        stage = getCurrentStage(evt);
                        stage.setScene(scene);
                        RecipeHCController newController = fxmlLoader.getController();
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        tableID.setCellValueFactory(new PropertyValueFactory<>("browseID"));
        tableName.setCellValueFactory(new PropertyValueFactory<>("browseName"));
        tableAuthor.setCellValueFactory(new PropertyValueFactory<>("browseAuthor"));
        tableCuisines.setCellValueFactory(new PropertyValueFactory<>("browseCuisines"));
        tableDietTags.setCellValueFactory(new PropertyValueFactory<>("browseDietTags"));
        tableAvgRating.setCellValueFactory(new PropertyValueFactory<>("browseAvgRating"));
        tablePercentOwned.setCellValueFactory(new PropertyValueFactory<>("browsePercOwned"));

        recipeTable.setItems(data);
    }

    private Stage getCurrentStage(ActionEvent event) {
        return (Stage) ((Node) event.getSource()).getScene().getWindow();
    }

    public void setLoginInfo(FrameControllerClass loginInfo) {
        this.loginInfo = loginInfo;
    }
}
