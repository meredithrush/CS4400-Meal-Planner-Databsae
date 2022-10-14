-- Makes a table with a Recipe ID/Name/Author. Used in Wireframe 15
DROP VIEW if exists C_BrowseRecipes;
CREATE VIEW C_BrowseRecipes AS
SELECT Recipe.Rec_ID AS 'ID', Recipe.Rec_Name AS 'Name', Recipe.C_Email AS Author
FROM Recipe
ORDER BY 'ID';

-- Makes a table with Recipe ID and its average rating. Used in Wireframe 15
DROP VIEW if exists Recipe_Average;
CREATE VIEW Recipe_Average AS
SELECT Recipe.Rec_ID AS 'ID', AVG(Rev_Rating) AS 'AverageRating'
FROM Recipe
JOIN Review ON Recipe.Rec_ID = Review.Rec_ID
GROUP BY Recipe.Rec_ID;

-- Makes a table with Recipe ID and its diet tags concatenated. Used in Wireframe 15
DROP VIEW if exists diet_tags;
CREATE VIEW diet_tags AS
SELECT Rec_ID AS 'ID', GROUP_CONCAT(Rec_Diet_Tag SEPARATOR ', ') AS 'DietTags'
FROM Recipe_Diet_Tag
GROUP BY Rec_ID;

-- Makes a table with Recipe ID and its cuisines concatenated. Used in Wireframe 15
DROP VIEW if exists cuisines;
CREATE VIEW cuisines AS
SELECT Rec_ID AS 'ID', GROUP_CONCAT(Rec_Cuisine SEPARATOR ', ') AS 'Cuisine'
FROM Recipe_Cuisine
GROUP BY Rec_ID;

-- Makes a table with the Product name and its types. Used in Wireframe 13
DROP VIEW if exists product_types;
CREATE VIEW product_types AS
SELECT P_Name AS 'Name', GROUP_CONCAT(Ing_Type SEPARATOR ', ') AS 'Types'
FROM Ingredient_Type
GROUP BY P_Name;

-- Makes a table with the products that a homechef owns that are used in this recipe.
DROP VIEW if exists hc_owns;
CREATE VIEW hc_owns AS
SELECT rp.Rec_ID AS 'RecipeID', rp.P_Name AS 'Name', hc.Amount AS 'Owned Amount', rp.P_Needed_Amt AS 'Recipe Amount', (hc.Amount - rp.P_Needed_Amt) AS 'Difference'
FROM HC_Owns_Product hc
RIGHT JOIN Recipe_Uses_Product rp ON hc.P_Name = rp.P_Name
WHERE hc.HC_Email = 'wbraga3@gatech.edu';


-- Makes a table with the number of products that the homechef owns enough of for this recipe/
DROP VIEW if exists hc_owns_enough;
CREATE VIEW hc_owns_enough AS
SELECT hc_owns.RecipeID AS 'RecID', COUNT(Difference) AS 'OwnsEnough'
FROM hc_owns
WHERE hc_owns.Difference >= 0
GROUP BY hc_owns.RecipeID;

-- Makes a table with the number of products used in a recipe.
DROP VIEW if exists numIng;
CREATE VIEW numIng AS
SELECT Rec_ID, COUNT(P_Name) AS 'NumberOfIng'
FROM Recipe_Uses_Product
GROUP BY Rec_ID;

-- Makes a table with the percentage owned.
DROP VIEW if exists percentageOwned;
CREATE VIEW percentageOwned AS
SELECT numIng.Rec_ID AS 'ID', (hc_owns_enough.ownsEnough * 100 / numIng.numberOfIng) AS 'Percentage'
FROM hc_owns_enough
JOIN numIng ON numIng.Rec_ID = hc_owns_enough.RecID;

