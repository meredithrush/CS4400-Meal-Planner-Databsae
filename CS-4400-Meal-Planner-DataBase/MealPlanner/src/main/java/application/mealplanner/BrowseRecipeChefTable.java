package application.mealplanner;

import javafx.scene.control.Hyperlink;

public class BrowseRecipeChefTable {

public Hyperlink browseID;
public String browseName;
public String browseAuthor;
public String browseCuisines;
public String browseDietTags;
public String browseAvgRating;
public double browsePercOwned;

    public BrowseRecipeChefTable(Hyperlink browseID, String browseName, String browseAuthor, String browseCuisines, String browseDietTags, String browseAvgRating, double browsePercOwned) {
        this.browseID = browseID;
        this.browseName = browseName;
        this.browseAuthor = browseAuthor;
        this.browseCuisines = browseCuisines;
        this.browseDietTags = browseDietTags;
        this.browseAvgRating = browseAvgRating;
        this.browsePercOwned = browsePercOwned;
    }

    public Hyperlink getBrowseID() {
        return browseID;
    }

    public void setBrowseID(Hyperlink browseID) {
        this.browseID = browseID;
    }

    public String getBrowseName() {
        return browseName;
    }

    public void setBrowseName(String browseName) {
        this.browseName = browseName;
    }

    public String getBrowseAuthor() {
        return browseAuthor;
    }

    public void setBrowseAuthor(String browseAuthor) {
        this.browseAuthor = browseAuthor;
    }

    public String getBrowseCuisines() {
        return browseCuisines;
    }

    public void setBrowseCuisines(String browseCuisines) {
        this.browseCuisines = browseCuisines;
    }

    public String getBrowseDietTags() {
        return browseDietTags;
    }

    public void setBrowseDietTags(String browseDietTags) {
        this.browseDietTags = browseDietTags;
    }

    public String getBrowseAvgRating() {
        return browseAvgRating;
    }

    public void setBrowseAvgRating(String browseAvgRating) {
        this.browseAvgRating = browseAvgRating;
    }

    public double getBrowsePercOwned() {
        return browsePercOwned;
    }

    public void setBrowsePercOwned(double browsePercOwned) {
        this.browsePercOwned = browsePercOwned;
    }
}
