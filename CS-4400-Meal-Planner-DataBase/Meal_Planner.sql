USE Meal_Planner;

-- Strong Entity User
CREATE TABLE MP_User (
	Usr_Email VARCHAR(30),
    Usr_Name VARCHAR(20) NOT NULL,
    Usr_Password VARCHAR(15) NOT NULL,
    Usr_Address VARCHAR(200) NOT NULL,
    PRIMARY KEY (Usr_Email)
);

-- Strong Entity Home Chef Overlapping User
CREATE TABLE Home_Chef (
	HC_Email VARCHAR(30),
    PRIMARY KEY (HC_Email),
    FOREIGN KEY (HC_Email) REFERENCES MP_User(Usr_Email)
);

-- Strong Entity Contributor Overlapping User
CREATE TABLE Contributor (
	C_Email VARCHAR(30),
    PRIMARY KEY (C_Email),
    FOREIGN KEY (C_Email) REFERENCES MP_User(Usr_Email)
);

-- Table for Home Chef's Multi-valued Attribute
CREATE TABLE Home_Chef_Diet_Restriction (
	HC_Email VARCHAR(30),
	Diet_Restriction VARCHAR(15),
    PRIMARY KEY (HC_Email, Diet_Restriction),
    FOREIGN KEY (HC_Email) REFERENCES Home_Chef(HC_Email)
);

-- Strong Entity Recipe
CREATE TABLE Recipe (
	Rec_ID INT UNSIGNED auto_increment,
    Rec_Name VARCHAR(20) NOT NULL,
    Rec_Instructions VARCHAR(700) UNIQUE,
    C_Email VARCHAR(30) NOT NULL,
    PRIMARY KEY (Rec_ID),
    FOREIGN KEY (C_Email) REFERENCES Contributor(C_Email)
);

-- Strong Entity Review
CREATE TABLE Review (
	Rev_ID INT UNSIGNED auto_increment,
    Rev_Comment VARCHAR(100),
    Rev_Rating SMALLINT UNSIGNED NOT NULL CHECK (Rev_Rating > 0 AND Rev_Rating < 6),
    C_Email VARCHAR(30) NOT NULL,
    Rec_ID INT UNSIGNED NOT NULL,
    PRIMARY KEY (Rev_ID),
	FOREIGN KEY (C_Email) REFERENCES Contributor(C_Email),
    FOREIGN KEY (Rec_ID) REFERENCES Recipe(Rec_ID)
);

-- Table for Recipie's Multi-value Attribute
CREATE TABLE Recipe_Cuisine (
	Rec_ID INT UNSIGNED,
	Rec_Cuisine VARCHAR(20),
    PRIMARY KEY (Rec_ID, Rec_Cuisine),
    FOREIGN KEY (Rec_ID) REFERENCES Recipe(Rec_ID)
);

-- Table for Recipie's Multi-value Attribute
CREATE TABLE Recipe_Diet_Tag (
	Rec_ID INT UNSIGNED,
	Rec_Diet_Tag VARCHAR(20),
    PRIMARY KEY (Rec_ID, Rec_Diet_Tag),
    FOREIGN KEY (Rec_ID) REFERENCES Recipe(Rec_ID)
);

-- Strong Entity Product
CREATE TABLE Product (
	P_Name VARCHAR(20),
    Ing_Units VARCHAR(20) DEFAULT 'unit', 
    I_Flag BOOLEAN, 
    T_Flag BOOLEAN,
    PRIMARY KEY (P_Name),
    CHECK (I_Flag <> T_Flag)
);

-- Table for Ingredients Multi-value Attribute
CREATE TABLE Ingredient_Type (
	P_Name VARCHAR(20),
    Ing_Type VARCHAR(20),
    PRIMARY KEY (P_Name, Ing_Type), 
    FOREIGN KEY (P_Name) REFERENCES Product(P_Name)
);

-- Strong Entity Store 
CREATE TABLE Store (
	Str_Type VARCHAR(20) NOT NULL,
    Str_Name VARCHAR(20) NOT NULL,
    Str_Num SMALLINT UNSIGNED NOT NULL,
    Str_Street VARCHAR(60) NOT NULL,
    Str_City VARCHAR(20) NOT NULL,
    Str_Country CHAR(2) NOT NULL,
    Str_ID INT UNSIGNED UNIQUE NOT NULL, 
    PRIMARY KEY (Str_Name, Str_Num, Str_Street, Str_City, Str_Country)
);

-- Strong Entity Grocery_Run
CREATE TABLE Grocery_Run (
	Grc_Run_ID INT UNSIGNED AUTO_INCREMENT,
    Grc_Run_Date DATE NOT NULL,
    Str_ID INT UNSIGNED NOT NULL,
    HC_Email VARCHAR(30) NOT NULL,
    PRIMARY KEY (Grc_Run_ID),
    FOREIGN KEY (Str_ID) REFERENCES Store(Str_ID),
    FOREIGN KEY (HC_Email) REFERENCES Home_Chef(HC_Email)
);

-- Weak Entity Meal Dependent on Recepie and User
CREATE TABLE Meal (
	Meal_Prep_Date DATE,
    HC_Email VARCHAR(30),
    Rec_ID INT UNSIGNED,
    PRIMARY KEY (Meal_Prep_Date, HC_Email, Rec_ID), 
    FOREIGN KEY (Rec_ID) REFERENCES Recipe(Rec_ID),
    FOREIGN KEY (HC_Email) REFERENCES Home_Chef(HC_Email)
);

-- Many-to-Many Relationship between Product and Store
CREATE TABLE Product_FoundAt_Store (
	P_Name VARCHAR(20),
    Str_ID INT UNSIGNED, 
    Unit_Price DECIMAL(9,2) NOT NULL,
    PRIMARY KEY (P_Name, Str_ID),
    FOREIGN KEY (P_Name) REFERENCES Product(P_Name),
    FOREIGN KEY (Str_ID) REFERENCES Store(Str_ID)
);

-- Many-to-Many Relationship between Grocery Run and Product
CREATE TABLE Grocery_Run_Obtains_Product (
	P_Name VARCHAR(20),
    Grc_Run_ID INT UNSIGNED,
    Total_Price DECIMAL(9,2) NOT NULL,
    Amount SMALLINT UNSIGNED NOT NULL,
    PRIMARY KEY (P_Name, Grc_Run_ID), 
    FOREIGN KEY (P_Name) REFERENCES Product(P_Name),
    FOREIGN KEY (Grc_Run_ID) REFERENCES Grocery_Run(Grc_Run_ID)
);

-- Many-to-Many Relationship between Recipe and Product
-- The ER diagram does not depict how to show the amount of a product needed in a recipe,
-- so we have created an attribute in this relationship to represent that (P_Needed_Amt).
CREATE TABLE Recipe_Uses_Product (
	P_Name VARCHAR(20), 
    Rec_ID INT UNSIGNED,
    P_Needed_Amt SMALLINT UNSIGNED NOT NULL,
    PRIMARY KEY (P_Name, Rec_ID), 
    FOREIGN KEY (P_Name) REFERENCES Product(P_Name),
    FOREIGN KEY (Rec_ID) REFERENCES Recipe(Rec_ID)
);

-- Many-to-Many Relationship between Grocery Run and Meal
CREATE TABLE Grocery_Run_Sources_Meal (
	Grc_Run_ID INT UNSIGNED, 
    HC_Email VARCHAR(30), 
    Rec_ID INT UNSIGNED, 
    Preparation_Date DATE,
    PRIMARY KEY (Grc_Run_ID, HC_Email, Rec_ID, Preparation_Date),
	FOREIGN KEY (HC_Email) REFERENCES Home_Chef(HC_Email),
    FOREIGN KEY (Grc_Run_ID) REFERENCES Grocery_Run(Grc_Run_ID),
    FOREIGN KEY (Rec_ID) REFERENCES Recipe(Rec_ID)
);

-- One-to-Many Relationship between HomeChef and Product
CREATE TABLE HC_Owns_Product (
	HC_Email VARCHAR(30),
    P_Name VARCHAR(20), 
    Amount SMALLINT UNSIGNED NOT NULL, 
    PRIMARY KEY (HC_Email, P_Name), 
    FOREIGN KEY (HC_Email) REFERENCES Home_Chef(HC_Email),
	FOREIGN KEY (P_Name) REFERENCES Product(P_Name)
);
