package application.mealplanner;

public class RecipeProductTable {
    public String productTableName;
    public String productTableAmount;

    public RecipeProductTable(String productTableName, String productTableAmount) {
        this.productTableName = productTableName;
        this.productTableAmount = productTableAmount;
    }

    public String getProductTableName() {
        return productTableName;
    }

    public void setProductTableName(String productTableName) {
        this.productTableName = productTableName;
    }

    public String getProductTableAmount() {
        return productTableAmount;
    }

    public void setProductTableAmount(String productTableAmount) {
        this.productTableAmount = productTableAmount;
    }
}
