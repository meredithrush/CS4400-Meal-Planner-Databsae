-- Inserting into Meal Planner User
INSERT INTO MP_User VALUES
('wbraga3@gatech.edu', 'Wila', 'hunter2', '101 Carrer de Sardenya BCN, ES'),
('gburdell9@gatech.edu', 'George', 'gatech1930', '711 Techwood Dr NW ATL, US'),
('acabrera1@gatech.edu', 'Angel', 'num1boss', '100 President''s House ATL, US'),
('temp@gmail.com', 'Temp', 'zzz', '4000 Usr_CityUsr_CountryWeston Rd Weston, US');

-- Inserting into HomeChef
INSERT INTO Home_Chef VALUES
('wbraga3@gatech.edu'), 
('temp@gmail.com');

-- Inserting into Contributor
INSERT INTO Contributor VALUES
('wbraga3@gatech.edu'), 
('gburdell9@gatech.edu'), 
('acabrera1@gatech.edu');

-- Inserting into Store
INSERT INTO Store VALUES
('Supermercat', 'Mercadona', 3, 'Placa Fort Pienc', 'BCN', 'ES', 001), 
('Centre Comercial', 'El Corte Ingles', 617, 'Avinguda Diagonal', 'BCN', 'ES', 002);

-- Inserting into Grocery Run
INSERT INTO Grocery_Run (Grc_Run_Date, Str_ID, HC_Email) VALUES
('2022-06-19', 001, 'wbraga3@gatech.edu');

-- Insert into Product
INSERT INTO PRODUCT VALUES 
('Olive Oil', 'mL', true, false),
('Onion', 'unit', true, false),
('Bell Pepper', 'unit', true, false),
('Garlic', 'clove', true, false),
('Tomato', 'unit', true, false),
('Bay Leaf', 'unit', true, false), 
('Paprika', 'g', true, false),
('Saffron', 'g', true, false),
('White Wine', 'mL', true, false), 
('Chicken Thigh', 'unit', true, false), 
('Parsley', 'g', true, false),
('Spanish Rice', 'g', true, false), 
('Chicken Broth', 'mL', true, false), 
('Shrimp', 'g', true, false), 
('Lemon', 'unit', true, false),
('Potato', 'unit', true, false),
('Flour', 'g', true, false),
('Sunflower Oil', 'mL', true, false),
('Deep Fryer', 'unit', false, true), 
('Beef', 'g', true, false), 
('Shallot', 'unit', true, false),
('Egg', 'unit', true, false), 
('Bread Crumbs', 'g', true, false);

-- Inserting into Ingredient Types
INSERT INTO Ingredient_Type VALUES
('Olive Oil', 'Oil'),
('Onion', 'Produce'),
('Bell Pepper', 'Produce'),
('Bell Pepper', 'Refrigerated'),
('Tomato', 'Produce'),
('Bay Leaf', 'Produce'),
('Bay Leaf', 'Spice'),
('Paprika', 'Spice'),
('Saffron', 'Spice'),
('White Wine', 'Drink'),
('Chicken Thigh', 'Meat'),
('Chicken Thigh', 'Refrigerated'),
('Parsley', 'Produce'),
('Parsley', 'Refrigerated'),
('Spanish Rice', 'Grain'),
('Chicken Broth', 'Stock'),
('Shrimp', 'Meat'),
('Shrimp', 'Refrigerated'),
('Lemon', 'Produce'),
('Potato', 'Produce'),
('Garlic', 'Produce'),
('Flour', 'Grain'),
('Sunflower Oil', 'Oil'),
('Beef', 'Meat'),
('Beef', 'Refrigerated'),
('Shallot', 'Produce'),
('Egg', 'Meat'),
('Egg', 'Refrigerated'),
('Bread Crumbs', 'Grain'),
('Deep Fryer', 'Tool');

-- Inserting into HomeChef Diet Restrictions
INSERT INTO Home_Chef_Diet_Restriction VALUES
('temp@gmail.com', 'Vegan'),
('temp@gmail.com', 'Low-cal');

-- Inserting into Recipe
INSERT INTO Recipe (Rec_Name, Rec_Instructions, C_Email) VALUES
('Paella', 'https://tastesbetterfromscratch.com/paella/', 'wbraga3@gatech.edu'),
('Patatas Bravas', 'https://www.pequerecetas.com/receta/patatas-bravas-tapas/', 'wbraga3@gatech.edu'),
('Bomba', 'https://www.recetasderechupete.com/bombas-de-patata-a-la-barceloneta/19929/', 'gburdell9@gatech.edu');

-- Inserting into Recipe Diet Tags
INSERT INTO Recipe_Diet_Tag
VALUES (1, 'Mediterranean'),
(1, 'No-nuts'),
(2, 'No-nuts'),
(3, 'No-nuts');

-- Inserting into Recipe Cuisines
INSERT INTO Recipe_Cuisine VALUES
(1, 'Spanish'),
(2, 'Spanish'),
(3, 'Spanish'),
(3, 'Catalan');

-- Inserting into Recipe Uses Product
INSERT INTO Recipe_Uses_Product VALUES
('Olive Oil', 1, 50),
('Onion', 1, 1),
('Bell Pepper', 1, 1),
('Garlic', 1, 16),
('Tomato', 1, 3),
('Bay Leaf', 1, 1),
('Paprika', 1, 2),
('Saffron', 1, 1),
('White Wine', 1, 50),
('Chicken Thigh', 1, 4),
('Parsley', 1, 5),
('Spanish Rice', 1, 400),
('Chicken Broth', 1, 1000),
('Shrimp', 1, 1000),
('Lemon', 1, 1), 
('Olive Oil', 2, 20),
('Potato', 2, 10),
('Garlic', 2, 5),
('Flour', 2, 10),
('Paprika', 2, 10),
('Chicken Broth', 2, 250),
('Sunflower Oil', 2, 700), 
('Deep Fryer', 2, 1),
('Potato', 3, 3),
('Beef', 3, 40),
('Shallot', 3, 1),
('White Wine', 3, 50),
('Egg', 3, 1),
('Flour', 3, 80),
('Bread Crumbs', 3, 80),
('Olive Oil', 3, 50),
('Sunflower Oil', 3, 700),
('Deep Fryer', 3, 1);

-- Inserting into Review
INSERT INTO Review (Rev_Comment, Rev_Rating, C_Email, Rec_ID) VALUES
('16 cloves of garlic?', 3, 'acabrera1@gatech.edu', 1),
('Good, but not traditional…', 4, 'gburdell9@gatech.edu', 1),
('Just like the ones at UPC!', 5, 'gburdell9@gatech.edu', 2),
(NULL, 5, 'wbraga3@gatech.edu', 3);

-- Inserting into Meal
INSERT INTO Meal VALUES
('2022-06-20', 'wbraga3@gatech.edu', 3);

-- Inserting into Product Found at a Store
INSERT INTO Product_FoundAt_Store VALUES
('Saffron', 001, 8),
('Deep Fryer', 002, 150);

-- Inserting into HomeChef Owning a Product
INSERT INTO HC_Owns_Product VALUES
('wbraga3@gatech.edu', 'Potato', 12),
('wbraga3@gatech.edu', 'Beef', 100),
('wbraga3@gatech.edu', 'Shallot', 6),
('wbraga3@gatech.edu', 'White Wine', 1000),
('wbraga3@gatech.edu', 'Egg', 12),
('wbraga3@gatech.edu', 'Flour', 1000),
('wbraga3@gatech.edu', 'Bread Crumbs', 250),
('wbraga3@gatech.edu', 'Olive Oil', 1000),
('wbraga3@gatech.edu', 'Sunflower Oil', 1000),
('wbraga3@gatech.edu', 'Deep Fryer', 1);

-- Inserting into Grocery Run Obtains a Product
INSERT INTO Grocery_Run_Obtains_Product VALUES
('Potato', 1, 3, 12),
('Shallot', 1, 3, 4),
('Beef', 1, 5, 100), 
('White Wine', 1, 6, 1000), 
('Egg', 1, 4, 12);

-- Inserting into Grocery Run Sources a Meal
INSERT INTO Grocery_Run_Sources_Meal VALUES
(1, 'wbraga3@gatech.edu', 3, '2022-06-20');