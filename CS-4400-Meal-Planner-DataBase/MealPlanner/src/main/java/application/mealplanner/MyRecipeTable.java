package application.mealplanner;

import javafx.scene.control.Hyperlink;

public class MyRecipeTable {

    public Hyperlink id;
    public String name;
    public String rating;

    public MyRecipeTable(Hyperlink id, String recName, String avgRating) {
        this.id = id;
        this.name = recName;
        this.rating = avgRating;
    }

    public Hyperlink getId() {
        return id;
    }

    public void setId(Hyperlink id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
