

-- Insert Retailers
INSERT INTO retailer (id, name, type, api_key) VALUES (1, 'Amazon', 'AMAZON', '$2a$10$YFQ36BZGuBaxvEZEDZVWNuqE9MNGgJxN.0CevAY8zrto/jRoUcWsK'); --original apikey: APIKEY_AMAZON_123
INSERT INTO retailer (id, name, type, api_key) VALUES (2, 'Flipkart', 'FLIPKART', '$2a$10$.osuX4fNPXKEe6Wxam6TCeI/4/aYiWW3NtB8hyNtrEVTymX1a7m2m'); --APIKEY_FLIPKART_456
INSERT INTO retailer (id, name, type, api_key) VALUES (3, 'Walmart', 'WALMART', '$2a$10$LFsfaENp843Wyn1KfiUnDugZwAPC88GdCaBV8mezSeK/CrRytHRvS'); --APIKEY_WALMART_789

-- Assuming retailers with IDs 1, 2, and 3 already exist in the 'retailer' table

INSERT INTO admin_login (user_name, password) VALUES 
('ups_admin', '$2a$10$sklgrKYOReRrH/w16DZqH.BP/S3Uq8nyoryK0tKcWGUX9VbDX4.TS'); -- original password: password123


-- Insert Customers
INSERT INTO customer (id, login_name, first_name, last_name, dob, retailer_id) 
VALUES (1, 'vignesh123', 'Vignesh', 'Rajendran', '1990-01-01', 1);

INSERT INTO customer (id, login_name, first_name, last_name, dob, retailer_id) 
VALUES (2, 'ramesh23', 'Ramesh', 'Raja', '1992-03-15', 2);

INSERT INTO customer (id, login_name, first_name, last_name, dob, retailer_id) 
VALUES (3, 'russelljacob', 'Russell', 'Jacob', '1985-05-27', 3);

INSERT INTO customer (id, login_name, first_name, last_name, dob, retailer_id) 
VALUES (4, 'palvannan12', 'Palvannan', 'Chandrasekaran', '1980-07-12', 1);

INSERT INTO customer (id, login_name, first_name, last_name, dob, retailer_id) 
VALUES (5, 'selvi23', 'Ela', 'Selvi', '1988-11-25', 2);
