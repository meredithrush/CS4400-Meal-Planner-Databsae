package application.mealplanner;

import java.sql.Connection;
import java.sql.DriverManager;


public class DataBaseConnector {
    public Connection databaseLink;

    public Connection getConnection(){
        String databaseName = "Meal_Planner";
        String databaseUser = "root";
        String databasePassword = "Ed65796!";
        String url = "jdbc:mysql://localhost/" + databaseName;

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            databaseLink = DriverManager.getConnection(url, databaseUser, databasePassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return databaseLink;
    }
}
