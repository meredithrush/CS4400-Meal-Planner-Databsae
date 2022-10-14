package application.mealplanner;

import javafx.scene.control.Hyperlink;

public class GroceryRunTable {
    Hyperlink ID;
    String Date;

    public GroceryRunTable(Hyperlink ID, String date) {
        this.ID = ID;
        Date = date;
    }

    public Hyperlink getID() {
        return ID;
    }

    public void setID(Hyperlink ID) {
        this.ID = ID;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}
