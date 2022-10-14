package application.mealplanner;

import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

public class ProductTable {
    public String product;
    public TextField amount;
    public CheckBox checkbox;

    public ProductTable(String product, TextField amount, CheckBox checkbox) {
        this.product = product;
        this.amount = amount;
        this.checkbox = checkbox;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public TextField getAmount() {
        return amount;
    }

    public void setAmount(TextField amount) {
        this.amount = amount;
    }

    public CheckBox getCheckbox() {
        return checkbox;
    }

    public void setCheckbox(CheckBox checkbox) {
        this.checkbox = checkbox;
    }
}
