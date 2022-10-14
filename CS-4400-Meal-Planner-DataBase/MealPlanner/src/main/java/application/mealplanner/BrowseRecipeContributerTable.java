package application.mealplanner;

import javafx.scene.control.Hyperlink;

public class BrowseRecipeContributerTable {
    public Hyperlink browserID;
    public String browserName;
    public String browserAuthor;
    public String browserCuisines;
    public String browserDietTags;
    public String browserAvgRating;

    public BrowseRecipeContributerTable(Hyperlink browserID, String browserName, String browserAuthor, String browserCuisines, String browserDietTags, String browserAvgRating) {
        this.browserID = browserID;
        this.browserName = browserName;
        this.browserAuthor = browserAuthor;
        this.browserCuisines = browserCuisines;
        this.browserDietTags = browserDietTags;
        this.browserAvgRating = browserAvgRating;
    }

    public Hyperlink getBrowserID() {
        return browserID;
    }

    public void setBrowserID(Hyperlink browserID) {
        this.browserID = browserID;
    }

    public String getBrowserName() {
        return browserName;
    }

    public void setBrowserName(String browserName) {
        this.browserName = browserName;
    }

    public String getBrowserAuthor() {
        return browserAuthor;
    }

    public void setBrowserAuthor(String browserAuthor) {
        this.browserAuthor = browserAuthor;
    }

    public String getBrowserCuisines() {
        return browserCuisines;
    }

    public void setBrowserCuisines(String browserCuisines) {
        this.browserCuisines = browserCuisines;
    }

    public String getBrowserDietTags() {
        return browserDietTags;
    }

    public void setBrowserDietTags(String browserDietTags) {
        this.browserDietTags = browserDietTags;
    }

    public String getBrowserAvgRating() {
        return browserAvgRating;
    }

    public void setBrowserAvgRating(String browserAvgRating) {
        this.browserAvgRating = browserAvgRating;
    }
}
