DELETE FROM ADMIN_LOGIN;
DELETE FROM CUSTOMER;
DELETE FROM RETAILER;

-- Insert Retailers
INSERT INTO retailer ( name, type, api_key) VALUES ( 'Amazon', 'AMAZON', '$2a$10$YFQ36BZGuBaxvEZEDZVWNuqE9MNGgJxN.0CevAY8zrto/jRoUcWsK'); --original apikey: APIKEY_AMAZON_123
INSERT INTO retailer ( name, type, api_key) VALUES ( 'Flipkart', 'FLIPKART', '$2a$10$.osuX4fNPXKEe6Wxam6TCeI/4/aYiWW3NtB8hyNtrEVTymX1a7m2m'); --APIKEY_FLIPKART_456
INSERT INTO retailer ( name, type, api_key) VALUES ( 'Walmart', 'WALMART', '$2a$10$LFsfaENp843Wyn1KfiUnDugZwAPC88GdCaBV8mezSeK/CrRytHRvS'); --APIKEY_WALMART_789


INSERT INTO admin_login (user_name, password) VALUES 
('ups_admin', '$2a$10$sklgrKYOReRrH/w16DZqH.BP/S3Uq8nyoryK0tKcWGUX9VbDX4.TS'); -- original password: password123