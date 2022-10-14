package application.mealplanner;

import javafx.scene.control.Hyperlink;

public class RecipeReviewTable {
    public int recipeReviewTableID;
    public String recipeReviewTableUser;
    public int recipeReviewTableRating;
    public String recipeReviewTableComment;

    public RecipeReviewTable(int recipeReviewTableID, String recipeReviewTableUser, int recipeReviewTableRating, String recipeReviewTableComment) {
        this.recipeReviewTableID = recipeReviewTableID;
        this.recipeReviewTableUser = recipeReviewTableUser;
        this.recipeReviewTableRating = recipeReviewTableRating;
        this.recipeReviewTableComment = recipeReviewTableComment;
    }

    public int getRecipeReviewTableID() {
        return recipeReviewTableID;
    }

    public void setRecipeReviewTableID(int recipeReviewTableID) {
        this.recipeReviewTableID = recipeReviewTableID;
    }

    public String getRecipeReviewTableUser() {
        return recipeReviewTableUser;
    }

    public void setRecipeReviewTableUser(String recipeReviewTableUser) {
        this.recipeReviewTableUser = recipeReviewTableUser;
    }

    public int getRecipeReviewTableRating() {
        return recipeReviewTableRating;
    }

    public void setRecipeReviewTableRating(int recipeReviewTableRating) {
        this.recipeReviewTableRating = recipeReviewTableRating;
    }

    public String getRecipeReviewTableComment() {
        return recipeReviewTableComment;
    }

    public void setRecipeReviewTableComment(String recipeReviewTableComment) {
        this.recipeReviewTableComment = recipeReviewTableComment;
    }
}
