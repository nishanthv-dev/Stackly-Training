CREATE DATABASE IF NOT EXISTS banking_railway_project;
USE banking_railway_project;

-- ============================================================
-- PART 1: DATABASE AND TABLE CREATION
-- ============================================================

CREATE TABLE IF NOT EXISTS customers (
    customer_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50),
    city VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS accounts (
    account_id INT PRIMARY KEY AUTO_INCREMENT,
    customer_id INT,
    account_type VARCHAR(30),
    balance DECIMAL(10,2),
    CONSTRAINT fk_accounts_customer
        FOREIGN KEY (customer_id) REFERENCES customers(customer_id)
);

CREATE TABLE IF NOT EXISTS trains (
    train_id INT PRIMARY KEY AUTO_INCREMENT,
    train_name VARCHAR(100),
    source VARCHAR(50),
    destination VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS bookings (
    booking_id INT PRIMARY KEY AUTO_INCREMENT,
    train_id INT,
    passenger_name VARCHAR(50),
    fare DECIMAL(10,2),
    status VARCHAR(20),
    CONSTRAINT fk_bookings_train
        FOREIGN KEY (train_id) REFERENCES trains(train_id)
);

-- ============================================================
-- PART 2: INSERT SAMPLE DATA
-- ============================================================

INSERT IGNORE INTO customers (customer_id, name, city) VALUES
(1, 'Janani', 'Chennai'),
(2, 'Arun', 'Coimbatore'),
(3, 'Priya', 'Madurai'),
(4, 'Karthik', 'Salem');

INSERT IGNORE INTO accounts (account_id, customer_id, account_type, balance) VALUES
(1, 1, 'Savings', 50000),
(2, 1, 'Current', 20000),
(3, 2, 'Savings', 30000),
(4, 3, 'Savings', 15000),
(5, 4, 'Current', 40000);

INSERT IGNORE INTO trains (train_id, train_name, source, destination) VALUES
(1, 'Express1', 'Chennai', 'Madurai'),
(2, 'Express2', 'Coimbatore', 'Salem'),
(3, 'Express3', 'Madurai', 'Chennai');

INSERT IGNORE INTO bookings (booking_id, train_id, passenger_name, fare, status) VALUES
(1, 1, 'Janani', 500, 'Confirmed'),
(2, 1, 'Arun', 500, 'Waiting'),
(3, 2, 'Priya', 300, 'Confirmed'),
(4, 3, 'Karthik', 450, 'Cancelled'),
(5, 2, 'Meena', 300, 'Confirmed');

-- ============================================================
-- PART 3: BANKING QUESTIONS Q1 TO Q40
-- ============================================================

-- Q1. Accounts with balance greater than 20000
SELECT * FROM accounts WHERE balance > 20000;

-- Q2. Customers from Chennai
SELECT * FROM customers WHERE city = 'Chennai';

-- Q3. Accounts with balance between 20000 and 50000
SELECT * FROM accounts WHERE balance BETWEEN 20000 AND 50000;

-- Q4. Customers whose names start with J
SELECT * FROM customers WHERE name LIKE 'J%';

-- Q5. Accounts of type Savings or Current
SELECT * FROM accounts WHERE account_type IN ('Savings', 'Current');

-- Q6. Accounts that are not Savings accounts
SELECT * FROM accounts WHERE account_type <> 'Savings';

-- Q7. Customers whose names contain the letter a
SELECT * FROM customers WHERE name LIKE '%a%';

-- Q8. Accounts with balance less than or equal to 30000
SELECT * FROM accounts WHERE balance <= 30000;

-- Q9. Customers not from Madurai
SELECT * FROM customers WHERE city <> 'Madurai';

-- Q10. Accounts with balance outside the range 10000 to 40000
SELECT * FROM accounts WHERE balance NOT BETWEEN 10000 AND 40000;

-- Q11. Customers whose names end with i
SELECT * FROM customers WHERE name LIKE '%i';

-- Q12. Accounts with balance exactly 50000
SELECT * FROM accounts WHERE balance = 50000;

-- Q13. Customers from Chennai or Salem
SELECT * FROM customers WHERE city IN ('Chennai', 'Salem');

-- Q14. Accounts with balance greater than 10000 and less than 40000
SELECT * FROM accounts WHERE balance > 10000 AND balance < 40000;

-- Q15. Accounts that are not Current accounts
SELECT * FROM accounts WHERE account_type NOT IN ('Current');

-- Q16. Accounts ordered by balance, highest first
SELECT * FROM accounts ORDER BY balance DESC;

-- Q17. Customers ordered alphabetically by name
SELECT * FROM customers ORDER BY name ASC;

-- Q18. Sort by account type, then balance highest first
SELECT * FROM accounts ORDER BY account_type ASC, balance DESC;

-- Q19. Total balance of all accounts
SELECT SUM(balance) AS total_balance FROM accounts;

-- Q20. Average balance of all accounts
SELECT AVG(balance) AS average_balance FROM accounts;

-- Q21. Maximum account balance
SELECT MAX(balance) AS maximum_balance FROM accounts;

-- Q22. Minimum account balance
SELECT MIN(balance) AS minimum_balance FROM accounts;

-- Q23. Total number of customers
SELECT COUNT(*) AS total_customers FROM customers;

-- Q24. Total balance for each account type
SELECT account_type, SUM(balance) AS total_balance
FROM accounts
GROUP BY account_type;

-- Q25. Average balance for each account type
SELECT account_type, AVG(balance) AS average_balance
FROM accounts
GROUP BY account_type;

-- Q26. Account types whose average balance is above 20000
SELECT account_type, AVG(balance) AS average_balance
FROM accounts
GROUP BY account_type
HAVING AVG(balance) > 20000;

-- Q27. Count accounts for each customer
SELECT c.customer_id, c.name, COUNT(a.account_id) AS total_accounts
FROM customers c
LEFT JOIN accounts a ON c.customer_id = a.customer_id
GROUP BY c.customer_id, c.name;

-- Q28. Customers who have more than one account
SELECT c.customer_id, c.name, COUNT(a.account_id) AS total_accounts
FROM customers c
LEFT JOIN accounts a ON c.customer_id = a.customer_id
GROUP BY c.customer_id, c.name
HAVING COUNT(a.account_id) > 1;

-- Q29. Display customer names and their account balances
SELECT c.name, a.balance
FROM customers c
JOIN accounts a ON c.customer_id = a.customer_id;

-- Q30. Display all customers and their accounts, if available
SELECT c.name, a.account_type, a.balance
FROM customers c
LEFT JOIN accounts a ON c.customer_id = a.customer_id;

-- Q31. Display account details with customer names
SELECT a.account_id, c.name, a.account_type, a.balance
FROM accounts a
JOIN customers c ON a.customer_id = c.customer_id;

-- Q32. Display customers whose account balance is above 20000
SELECT c.name, a.account_type, a.balance
FROM customers c
JOIN accounts a ON c.customer_id = a.customer_id
WHERE a.balance > 20000;

-- Q33. Total balance of each customer
SELECT c.customer_id, c.name, COALESCE(SUM(a.balance), 0) AS total_balance
FROM customers c
LEFT JOIN accounts a ON c.customer_id = a.customer_id
GROUP BY c.customer_id, c.name;

-- Q34. Display customers and balances in descending order
SELECT c.name, a.balance
FROM customers c
JOIN accounts a ON c.customer_id = a.customer_id
ORDER BY a.balance DESC;

-- Q35. Count the number of accounts for each city
SELECT c.city, COUNT(a.account_id) AS total_accounts
FROM customers c
LEFT JOIN accounts a ON c.customer_id = a.customer_id
GROUP BY c.city;

-- Q36. Accounts with balance above the average balance
SELECT *
FROM accounts
WHERE balance > (SELECT AVG(balance) FROM accounts);

-- Q37. Customers who have at least one account
SELECT *
FROM customers
WHERE customer_id IN (SELECT customer_id FROM accounts);

-- Q38. Customers who do not have any accounts
SELECT *
FROM customers
WHERE customer_id NOT IN (SELECT customer_id FROM accounts);

-- Q39. Account or accounts with the highest balance
SELECT *
FROM accounts
WHERE balance = (SELECT MAX(balance) FROM accounts);

-- Q40. Customers whose total balance exceeds 40000
SELECT c.customer_id, c.name, SUM(a.balance) AS total_balance
FROM customers c
JOIN accounts a ON c.customer_id = a.customer_id
GROUP BY c.customer_id, c.name
HAVING SUM(a.balance) > 40000;

-- ============================================================
-- PART 4: RAILWAY QUESTIONS Q41 TO Q50
-- ============================================================

-- Q41. Bookings with fare greater than 400
SELECT * FROM bookings WHERE fare > 400;

-- Q42. Bookings that are not Confirmed
SELECT * FROM bookings WHERE status <> 'Confirmed';

-- Q43. Trains starting from Chennai
SELECT * FROM trains WHERE source = 'Chennai';

-- Q44. Bookings with fare between 300 and 500
SELECT * FROM bookings WHERE fare BETWEEN 300 AND 500;

-- Q45. Passengers whose names start with A
SELECT * FROM bookings WHERE passenger_name LIKE 'A%';

-- Q46. Display train names and passenger names
SELECT t.train_name, b.passenger_name
FROM trains t
JOIN bookings b ON t.train_id = b.train_id;

-- Q47. Count bookings for each train
SELECT t.train_id, t.train_name, COUNT(b.booking_id) AS total_bookings
FROM trains t
LEFT JOIN bookings b ON t.train_id = b.train_id
GROUP BY t.train_id, t.train_name;

-- Q48. Total confirmed fare for each train
SELECT t.train_name,
       SUM(CASE WHEN b.status = 'Confirmed' THEN b.fare ELSE 0 END) AS total_confirmed_fare
FROM trains t
LEFT JOIN bookings b ON t.train_id = b.train_id
GROUP BY t.train_id, t.train_name;

-- Q49. Booking or bookings with the highest fare
SELECT *
FROM bookings
WHERE fare = (SELECT MAX(fare) FROM bookings);

-- Q50. Trains with more than one booking
SELECT t.train_name, COUNT(b.booking_id) AS total_bookings
FROM trains t
JOIN bookings b ON t.train_id = b.train_id
GROUP BY t.train_id, t.train_name
HAVING COUNT(b.booking_id) > 1;

