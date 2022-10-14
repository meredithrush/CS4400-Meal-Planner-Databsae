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

public class BrowseRecipesContributorController {
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
    public TableView<BrowseRecipeContributerTable> recipeTable;
    public TableColumn<BrowseRecipeContributerTable, Hyperlink> tableId;
    public TableColumn<BrowseRecipeContributerTable, String> tableName;
    public TableColumn<BrowseRecipeContributerTable, String>  tableAuthor;
    public TableColumn<BrowseRecipeContributerTable, String>  tableCuisines;
    public TableColumn<BrowseRecipeContributerTable, String>  tableDietTags;
    public TableColumn<BrowseRecipeContributerTable, String>  tableAvgRating;
    private ObservableList<BrowseRecipeContributerTable> data;



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
        String viewQueryDrop2 = "DROP VIEW if exists diet_tags;";
        String viewQueryDrop3 = "DROP VIEW if exists Recipe_Average;";
        String viewQueryDrop4 = "DROP VIEW if exists cuisines;";
        String viewQuery1 = "CREATE VIEW C_BrowseRecipes AS SELECT Recipe.Rec_ID AS 'ID', Recipe.Rec_Name AS 'Name', Recipe.C_Email AS Author FROM Recipe ORDER BY 'ID';";
        String viewQuery2 = "CREATE VIEW diet_tags AS SELECT Rec_ID AS 'ID', GROUP_CONCAT(Rec_Diet_Tag SEPARATOR ', ') AS 'DietTags' FROM Recipe_Diet_Tag GROUP BY Rec_ID;";
        String viewQuery3 = "CREATE VIEW Recipe_Average AS SELECT Recipe.Rec_ID AS 'ID', AVG(Rev_Rating) AS 'AverageRating' FROM Recipe JOIN Review ON Recipe.Rec_ID = Review.Rec_ID GROUP BY Recipe.Rec_ID;\n";
        String viewQuery4 = "CREATE VIEW cuisines AS SELECT Rec_ID AS 'ID', GROUP_CONCAT(Rec_Cuisine SEPARATOR ', ') AS 'Cuisine' FROM Recipe_Cuisine GROUP BY Rec_ID;";
        String connectQuery = "SELECT browse.ID AS 'ID', browse.Name AS 'Name', browse.Author AS 'Author', c.cuisine AS 'Cuisines', dt.diettags AS 'Diet Tags', ravg.averagerating AS 'Average Rating' FROM Meal_Planner.c_browserecipes AS browse LEFT JOIN Meal_Planner.recipe_average AS ravg ON browse.ID = ravg.ID LEFT JOIN Meal_Planner.cuisines AS c ON browse.ID = c.ID LEFT JOIN Meal_Planner.diet_tags AS dt ON browse.ID = dt.ID;";
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

            statement.executeBatch();

            ResultSet queryOutput = statement.executeQuery(connectQuery);

            while (queryOutput.next()) {
                int id = Integer.parseInt(queryOutput.getString("ID"));
                Hyperlink link = new Hyperlink(queryOutput.getString("ID"));
                data.add(new BrowseRecipeContributerTable(link, queryOutput.getString("Name"), queryOutput.getString("Author"), queryOutput.getString("Cuisines"), queryOutput.getString("Diet Tags"), queryOutput.getString("Average Rating")));
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

        tableId.setCellValueFactory(new PropertyValueFactory<>("browserID"));
        tableName.setCellValueFactory(new PropertyValueFactory<>("browserName"));
        tableAuthor.setCellValueFactory(new PropertyValueFactory<>("browserAuthor"));
        tableCuisines.setCellValueFactory(new PropertyValueFactory<>("browserCuisines"));
        tableDietTags.setCellValueFactory(new PropertyValueFactory<>("browserDietTags"));
        tableAvgRating.setCellValueFactory(new PropertyValueFactory<>("browserAvgRating"));

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

            DataBaseConnector connection = new DataBaseConnector();
            Connection connectDB = connection.getConnection();
            data = FXCollections.observableArrayList();
            String viewQueryDrop1 = "DROP VIEW if exists C_BrowseRecipes;";
            String viewQueryDrop2 = "DROP VIEW if exists diet_tags;";
            String viewQueryDrop3 = "DROP VIEW if exists Recipe_Average;";
            String viewQueryDrop4 = "DROP VIEW if exists cuisines;";
            String viewQuery1 = "CREATE VIEW C_BrowseRecipes AS SELECT Recipe.Rec_ID AS 'ID', Recipe.Rec_Name AS 'Name', Recipe.C_Email AS Author FROM Recipe ORDER BY 'ID';";
            String viewQuery2 = "CREATE VIEW diet_tags AS SELECT Rec_ID AS 'ID', GROUP_CONCAT(Rec_Diet_Tag SEPARATOR ', ') AS 'DietTags' FROM Recipe_Diet_Tag GROUP BY Rec_ID;";
            String viewQuery3 = "CREATE VIEW Recipe_Average AS SELECT Recipe.Rec_ID AS 'ID', AVG(Rev_Rating) AS 'AverageRating' FROM Recipe JOIN Review ON Recipe.Rec_ID = Review.Rec_ID GROUP BY Recipe.Rec_ID;\n";
            String viewQuery4 = "CREATE VIEW cuisines AS SELECT Rec_ID AS 'ID', GROUP_CONCAT(Rec_Cuisine SEPARATOR ', ') AS 'Cuisine' FROM Recipe_Cuisine GROUP BY Rec_ID;";
            String connectQuery = "SELECT browse.ID AS 'ID', browse.Name AS 'Name', browse.Author AS 'Author', c.cuisine AS 'Cuisines', dt.diettags AS 'Diet Tags', ravg.averagerating AS 'Average Rating' FROM Meal_Planner.c_browserecipes AS browse INNER JOIN Meal_Planner.recipe_average AS ravg INNER JOIN Meal_Planner.cuisines AS c INNER JOIN Meal_Planner.diet_tags AS dt WHERE browse.ID = ravg.ID AND browse.ID = c.ID AND browse.ID = dt.ID " + string;
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

                statement.executeBatch();

                ResultSet queryOutput = statement.executeQuery(connectQuery);

                while (queryOutput.next()) {
                    int id = Integer.parseInt(queryOutput.getString("ID"));
                    Hyperlink link = new Hyperlink(queryOutput.getString("ID"));
                    data.add(new BrowseRecipeContributerTable(link, queryOutput.getString("Name"), queryOutput.getString("Author"), queryOutput.getString("Cuisines"), queryOutput.getString("Diet Tags"), queryOutput.getString("Average Rating")));
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

            tableId.setCellValueFactory(new PropertyValueFactory<>("browserID"));
            tableName.setCellValueFactory(new PropertyValueFactory<>("browserName"));
            tableAuthor.setCellValueFactory(new PropertyValueFactory<>("browserAuthor"));
            tableCuisines.setCellValueFactory(new PropertyValueFactory<>("browserCuisines"));
            tableDietTags.setCellValueFactory(new PropertyValueFactory<>("browserDietTags"));
            tableAvgRating.setCellValueFactory(new PropertyValueFactory<>("browserAvgRating"));

            recipeTable.setItems(data);
        }
    }

    public void ascRating(ActionEvent event) {
        DataBaseConnector connection = new DataBaseConnector();
        Connection connectDB = connection.getConnection();
        data = FXCollections.observableArrayList();
        String viewQueryDrop1 = "DROP VIEW if exists C_BrowseRecipes;";
        String viewQueryDrop2 = "DROP VIEW if exists diet_tags;";
        String viewQueryDrop3 = "DROP VIEW if exists Recipe_Average;";
        String viewQueryDrop4 = "DROP VIEW if exists cuisines;";
        String viewQuery1 = "CREATE VIEW C_BrowseRecipes AS SELECT Recipe.Rec_ID AS 'ID', Recipe.Rec_Name AS 'Name', Recipe.C_Email AS Author FROM Recipe ORDER BY 'ID';";
        String viewQuery2 = "CREATE VIEW diet_tags AS SELECT Rec_ID AS 'ID', GROUP_CONCAT(Rec_Diet_Tag SEPARATOR ', ') AS 'DietTags' FROM Recipe_Diet_Tag GROUP BY Rec_ID;";
        String viewQuery3 = "CREATE VIEW Recipe_Average AS SELECT Recipe.Rec_ID AS 'ID', AVG(Rev_Rating) AS 'AverageRating' FROM Recipe JOIN Review ON Recipe.Rec_ID = Review.Rec_ID GROUP BY Recipe.Rec_ID;\n";
        String viewQuery4 = "CREATE VIEW cuisines AS SELECT Rec_ID AS 'ID', GROUP_CONCAT(Rec_Cuisine SEPARATOR ', ') AS 'Cuisine' FROM Recipe_Cuisine GROUP BY Rec_ID;";
        String connectQuery = "SELECT browse.ID AS 'ID', browse.Name AS 'Name', browse.Author AS 'Author', c.cuisine AS 'Cuisines', dt.diettags AS 'Diet Tags', ravg.averagerating AS 'Average Rating' FROM Meal_Planner.c_browserecipes AS browse LEFT JOIN Meal_Planner.recipe_average AS ravg ON browse.ID = ravg.ID LEFT JOIN Meal_Planner.cuisines AS c ON browse.ID = c.ID LEFT JOIN Meal_Planner.diet_tags AS dt ON browse.ID = dt.ID ORDER BY ravg.averagerating;\n";
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

            statement.executeBatch();

            ResultSet queryOutput = statement.executeQuery(connectQuery);

            while (queryOutput.next()) {
                int id = Integer.parseInt(queryOutput.getString("ID"));
                Hyperlink link = new Hyperlink(queryOutput.getString("ID"));
                data.add(new BrowseRecipeContributerTable(link, queryOutput.getString("Name"), queryOutput.getString("Author"), queryOutput.getString("Cuisines"), queryOutput.getString("Diet Tags"), queryOutput.getString("Average Rating")));
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

        tableId.setCellValueFactory(new PropertyValueFactory<>("browserID"));
        tableName.setCellValueFactory(new PropertyValueFactory<>("browserName"));
        tableAuthor.setCellValueFactory(new PropertyValueFactory<>("browserAuthor"));
        tableCuisines.setCellValueFactory(new PropertyValueFactory<>("browserCuisines"));
        tableDietTags.setCellValueFactory(new PropertyValueFactory<>("browserDietTags"));
        tableAvgRating.setCellValueFactory(new PropertyValueFactory<>("browserAvgRating"));

        recipeTable.setItems(data);
    }

    public void descRating(ActionEvent event) {
        DataBaseConnector connection = new DataBaseConnector();
        Connection connectDB = connection.getConnection();
        data = FXCollections.observableArrayList();
        String viewQueryDrop1 = "DROP VIEW if exists C_BrowseRecipes;";
        String viewQueryDrop2 = "DROP VIEW if exists diet_tags;";
        String viewQueryDrop3 = "DROP VIEW if exists Recipe_Average;";
        String viewQueryDrop4 = "DROP VIEW if exists cuisines;";
        String viewQuery1 = "CREATE VIEW C_BrowseRecipes AS SELECT Recipe.Rec_ID AS 'ID', Recipe.Rec_Name AS 'Name', Recipe.C_Email AS Author FROM Recipe ORDER BY 'ID';";
        String viewQuery2 = "CREATE VIEW diet_tags AS SELECT Rec_ID AS 'ID', GROUP_CONCAT(Rec_Diet_Tag SEPARATOR ', ') AS 'DietTags' FROM Recipe_Diet_Tag GROUP BY Rec_ID;";
        String viewQuery3 = "CREATE VIEW Recipe_Average AS SELECT Recipe.Rec_ID AS 'ID', AVG(Rev_Rating) AS 'AverageRating' FROM Recipe JOIN Review ON Recipe.Rec_ID = Review.Rec_ID GROUP BY Recipe.Rec_ID;\n";
        String viewQuery4 = "CREATE VIEW cuisines AS SELECT Rec_ID AS 'ID', GROUP_CONCAT(Rec_Cuisine SEPARATOR ', ') AS 'Cuisine' FROM Recipe_Cuisine GROUP BY Rec_ID;";
        String connectQuery = "SELECT browse.ID AS 'ID', browse.Name AS 'Name', browse.Author AS 'Author', c.cuisine AS 'Cuisines', dt.diettags AS 'Diet Tags', ravg.averagerating AS 'Average Rating' FROM Meal_Planner.c_browserecipes AS browse LEFT JOIN Meal_Planner.recipe_average AS ravg ON browse.ID = ravg.ID LEFT JOIN Meal_Planner.cuisines AS c ON browse.ID = c.ID LEFT JOIN Meal_Planner.diet_tags AS dt ON browse.ID = dt.ID ORDER BY ravg.averagerating DESC;";
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

            statement.executeBatch();

            ResultSet queryOutput = statement.executeQuery(connectQuery);

            while (queryOutput.next()) {
                int id = Integer.parseInt(queryOutput.getString("ID"));
                Hyperlink link = new Hyperlink(queryOutput.getString("ID"));
                data.add(new BrowseRecipeContributerTable(link, queryOutput.getString("Name"), queryOutput.getString("Author"), queryOutput.getString("Cuisines"), queryOutput.getString("Diet Tags"), queryOutput.getString("Average Rating")));
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

        tableId.setCellValueFactory(new PropertyValueFactory<>("browserID"));
        tableName.setCellValueFactory(new PropertyValueFactory<>("browserName"));
        tableAuthor.setCellValueFactory(new PropertyValueFactory<>("browserAuthor"));
        tableCuisines.setCellValueFactory(new PropertyValueFactory<>("browserCuisines"));
        tableDietTags.setCellValueFactory(new PropertyValueFactory<>("browserDietTags"));
        tableAvgRating.setCellValueFactory(new PropertyValueFactory<>("browserAvgRating"));

        recipeTable.setItems(data);
    }

    private Stage getCurrentStage(ActionEvent event) {
        return (Stage) ((Node) event.getSource()).getScene().getWindow();
    }

    public void setLoginInfo(FrameControllerClass loginInfo) {
        this.loginInfo = loginInfo;
    }
}
