package application.mealplanner;

import javafx.scene.control.CheckBox;

public class ShoppingMealsTable {
    public String recName;
    public String recDate;
    public CheckBox check;
    public  String recid;

    public ShoppingMealsTable(String recName, String recDate, CheckBox check, String recid) {
        this.recName = recName;
        this.recDate = recDate;
        this.check = check;
        this.recid = recid;
    }

    public String getRecName() {
        return recName;
    }

    public void setRecName(String recName) {
        this.recName = recName;
    }

    public String getRecDate() {
        return recDate;
    }

    public void setRecDate(String recDate) {
        this.recDate = recDate;
    }

    public CheckBox getCheck() {
        return check;
    }

    public void setCheck(CheckBox check) {
        this.check = check;
    }

    public String getRecid() {
        return recid;
    }

    public void setRecid(String recid) {
        this.recid = recid;
    }
}
