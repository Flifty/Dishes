CREATE TABLE dish (                          
                      id SERIAL PRIMARY KEY,
                      name VARCHAR(255) NOT NULL,                      
                      country VARCHAR(255),
                      category VARCHAR(255),                      
                      instruction TEXT
);

CREATE TABLE image (   
                       id SERIAL PRIMARY KEY,
                       picture VARCHAR(255) NOT NULL, 
                       dish_id BIGINT REFERENCES dish(id)
);

CREATE TABLE ingredient (                            
                            id SERIAL PRIMARY KEY,
                            name VARCHAR(255) NOT NULL
);

CREATE TABLE dish_ingredient_mapping (
                                         dish_id BIGINT REFERENCES dish(id),                                         
                                         ingredient_id BIGINT REFERENCES ingredient(id),
                                         PRIMARY KEY (dish_id, ingredient_id)
);
