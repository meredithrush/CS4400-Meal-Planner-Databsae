package application.mealplanner;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

import java.util.ArrayList;

public class MyProductTable {
    public String productName;
    public String productTypes;
    public int productAmountOwned;
    public String productUnits;
    public CheckBox productSelectUpdate;

    public MyProductTable(String productName, String productTypes, int productAmountOwned, String productUnits, CheckBox productSelectUpdate) {
        this.productName = productName;
        this.productTypes = productTypes;
        this.productAmountOwned = productAmountOwned;
        this.productUnits = productUnits;
        this.productSelectUpdate = productSelectUpdate;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductTypes() {
        return productTypes;
    }

    public void setProductTypes(String productTypes) {
        this.productTypes = productTypes;
    }

    public int getProductAmountOwned() {
        return productAmountOwned;
    }

    public void setProductAmountOwned(int productAmountOwned) {
        this.productAmountOwned = productAmountOwned;
    }

    public String getProductUnits() {
        return productUnits;
    }

    public void setProductUnits(String productUnits) {
        this.productUnits = productUnits;
    }

    public CheckBox getProductSelectUpdate() {
        return productSelectUpdate;
    }

    public void setProductSelectUpdate(CheckBox productSelectUpdate) {
        this.productSelectUpdate = productSelectUpdate;
    }
}
