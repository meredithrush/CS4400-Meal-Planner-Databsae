package application.mealplanner;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;

public class ProductCheckListTable {
    public String prodName;
    public String prodAmt;
    public Button check;


    public ProductCheckListTable(String prodName, String prodAmt, Button check) {
        this.prodName = prodName;
        this.prodAmt = prodAmt;
        this.check = check;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getProdAmt() {
        return prodAmt;
    }

    public void setProdAmt(String prodAmt) {
        this.prodAmt = prodAmt;
    }

    public Button getCheck() {
        return check;
    }

    public void setCheck(Button check) {
        this.check = check;
    }
}
