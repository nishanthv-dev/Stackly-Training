USE vehicle_service_center;

CREATE TABLE Customers (
    customer_id INT PRIMARY KEY AUTO_INCREMENT,
    customer_name VARCHAR(50),
    phone VARCHAR(15),
    city VARCHAR(30)
);
DESC Customers;

CREATE TABLE Vehicles (
    vehicle_id INT PRIMARY KEY AUTO_INCREMENT,
    customer_id INT,
    vehicle_number VARCHAR(15),
    vehicle_model VARCHAR(50),
    vehicle_type VARCHAR(30),
    FOREIGN KEY (customer_id)
        REFERENCES Customers(customer_id)
);
DESC Vehicles;

CREATE TABLE Mechanics (
    mechanic_id INT PRIMARY KEY AUTO_INCREMENT,
    mechanic_name VARCHAR(50),
    specialization VARCHAR(50),
    experience INT
);
DESC Mechanics;

CREATE TABLE Service_Records (
    service_id INT PRIMARY KEY AUTO_INCREMENT,
    vehicle_id INT,
    mechanic_id INT,
    service_type VARCHAR(50),
    service_date DATE,
    cost DECIMAL(10,2),
    FOREIGN KEY (vehicle_id)
        REFERENCES Vehicles(vehicle_id),
    FOREIGN KEY (mechanic_id)
        REFERENCES Mechanics(mechanic_id)
);

DESC Service_Records;

CREATE TABLE Bills (
    bill_id INT PRIMARY KEY AUTO_INCREMENT,
    service_id INT,
    total_amount DECIMAL(10,2),
    payment_status VARCHAR(20),
    FOREIGN KEY (service_id)
        REFERENCES Service_Records(service_id)
);
DESC Bills;

INSERT INTO Customers (customer_name, phone, city)
VALUES
('Rahul', '9876543210', 'Chennai'),
('Priya', '9876543211', 'Bangalore'),
('Arun', '9876543212', 'Hyderabad'),
('Sneha', '9876543213', 'Coimbatore'),
('Karthik', '9876543214', 'Mumbai');
SELECT * FROM Customers;

INSERT INTO Vehicles
(customer_id, vehicle_number, vehicle_model, vehicle_type)
VALUES
(1, 'TN10AB1234', 'Hyundai i20', 'Car'),
(2, 'KA05XY5678', 'Honda City', 'Car'),
(3, 'TS08PQ4321', 'Royal Enfield', 'Bike'),
(4, 'TN22KL9090', 'Maruti Swift', 'Car'),
(5, 'MH12AA1111', 'TVS Apache', 'Bike');
SELECT * FROM Vehicles;

INSERT INTO Mechanics
(mechanic_name, specialization, experience)
VALUES
('Ramesh', 'Engine', 10),
('Suresh', 'Electrical', 8),
('Mahesh', 'General Service', 6),
('Ganesh', 'Painting', 12);
SELECT * FROM Mechanics;

INSERT INTO Service_Records
(vehicle_id, mechanic_id, service_type, service_date, cost)
VALUES
(1, 1, 'Engine Repair', '2026-07-10', 8000),
(1, 2, 'Electrical Repair', '2026-07-20', 3000),
(2, 3, 'General Service', '2026-07-21', 2500),
(3, 1, 'Engine Repair', '2026-07-15', 5000),
(4, 3, 'General Service', '2026-07-18', 2200),
(5, 2, 'Electrical Repair', '2026-06-30', 1800),
(2, 4, 'Painting', '2026-07-25', 7000),
(3, 3, 'General Service', '2026-07-27', 2000);
SELECT * FROM Service_Records;

INSERT INTO Bills
(service_id, total_amount, payment_status)
VALUES
(1, 8000, 'Paid'),
(2, 3000, 'Paid'),
(3, 2500, 'Pending'),
(4, 5000, 'Paid'),
(5, 2200, 'Paid'),
(6, 1800, 'Pending'),
(7, 7000, 'Paid'),
(8, 2000, 'Pending');
SELECT * FROM Bills;


-- Q1. Display vehicles serviced today
SELECT
    v.vehicle_number,
    v.vehicle_model,
    s.service_type,
    s.service_date
FROM Vehicles v
JOIN Service_Records s
    ON v.vehicle_id = s.vehicle_id
WHERE s.service_date = CURDATE();


-- Q2. Find the mechanic handling the most services
SELECT
    m.mechanic_name,
    COUNT(s.service_id) AS Total_Services
FROM Mechanics m
JOIN Service_Records s
    ON m.mechanic_id = s.mechanic_id
GROUP BY m.mechanic_id, m.mechanic_name
ORDER BY Total_Services DESC
LIMIT 1;


-- Q3. Calculate customer-wise bill amount
SELECT
    c.customer_name,
    SUM(b.total_amount) AS Total_Bill
FROM Customers c
JOIN Vehicles v
    ON c.customer_id = v.customer_id
JOIN Service_Records s
    ON v.vehicle_id = s.vehicle_id
JOIN Bills b
    ON s.service_id = b.service_id
GROUP BY c.customer_id, c.customer_name;


-- Q4. Find vehicles not serviced in the last year
SELECT
    v.vehicle_number,
    v.vehicle_model
FROM Vehicles v
WHERE v.vehicle_id NOT IN (
    SELECT s.vehicle_id
    FROM Service_Records s
    WHERE s.service_date >=
          DATE_SUB(CURDATE(), INTERVAL 1 YEAR)
);


-- Q5. Display service history for a vehicle
-- Example: Vehicle Number = TN10AB1234
SELECT
    v.vehicle_number,
    v.vehicle_model,
    s.service_type,
    s.service_date,
    s.cost
FROM Vehicles v
JOIN Service_Records s
    ON v.vehicle_id = s.vehicle_id
WHERE v.vehicle_number = 'TN10AB1234'
ORDER BY s.service_date ASC;


-- Q6. Count services by type
SELECT
    service_type,
    COUNT(*) AS Total
FROM Service_Records
GROUP BY service_type;


-- Q7. Find customers with more than three visits
SELECT
    c.customer_name,
    COUNT(s.service_id) AS Visits
FROM Customers c
JOIN Vehicles v
    ON c.customer_id = v.customer_id
JOIN Service_Records s
    ON v.vehicle_id = s.vehicle_id
GROUP BY c.customer_id, c.customer_name
HAVING COUNT(s.service_id) > 3;


-- Q8. Display pending payments
SELECT
    c.customer_name,
    v.vehicle_number,
    b.total_amount
FROM Customers c
JOIN Vehicles v
    ON c.customer_id = v.customer_id
JOIN Service_Records s
    ON v.vehicle_id = s.vehicle_id
JOIN Bills b
    ON s.service_id = b.service_id
WHERE b.payment_status = 'Pending';

-- Q9. Find the most common service
SELECT
    service_type,
    COUNT(*) AS Frequency
FROM Service_Records
GROUP BY service_type
ORDER BY Frequency DESC
LIMIT 1;


-- Q10. Calculate monthly service revenue
SELECT
    MONTH(s.service_date) AS Month,
    SUM(b.total_amount) AS Revenue
FROM Service_Records s
JOIN Bills b
    ON s.service_id = b.service_id
GROUP BY MONTH(s.service_date)
ORDER BY Month;

-- Q11. Find the highest bill
SELECT *
FROM Bills
ORDER BY total_amount DESC
LIMIT 1;


-- Q12. Find the mechanic with the highest revenue
SELECT
    m.mechanic_name,
    SUM(b.total_amount) AS Revenue
FROM Mechanics m
JOIN Service_Records s
    ON m.mechanic_id = s.mechanic_id
JOIN Bills b
    ON s.service_id = b.service_id
GROUP BY m.mechanic_id, m.mechanic_name
ORDER BY Revenue DESC
LIMIT 1;


-- Q13. Display customers who own more than one vehicle
SELECT
    c.customer_name,
    COUNT(v.vehicle_id) AS Vehicles
FROM Customers c
JOIN Vehicles v
    ON c.customer_id = v.customer_id
GROUP BY c.customer_id, c.customer_name
HAVING COUNT(v.vehicle_id) > 1;


-- Q14. Find the average service cost
SELECT
    AVG(cost) AS Average_Cost
FROM Service_Records;


-- Q15. Find the most experienced mechanic
SELECT *
FROM Mechanics
ORDER BY experience DESC
LIMIT 1;


-- Q16. Display all engine repairs
SELECT *
FROM Service_Records
WHERE service_type = 'Engine Repair';


-- Q17. Find customers from Chennai
SELECT *
FROM Customers
WHERE city = 'Chennai';


-- Q18. Find all services performed by Ramesh
SELECT
    m.mechanic_name,
    s.service_type,
    s.service_date
FROM Mechanics m
JOIN Service_Records s
    ON m.mechanic_id = s.mechanic_id
WHERE m.mechanic_name = 'Ramesh';


-- Q19. Display unpaid bill details with service information
SELECT
    b.bill_id,
    c.customer_name,
    v.vehicle_number,
    s.service_type,
    s.service_date,
    b.total_amount
FROM Bills b
JOIN Service_Records s
    ON b.service_id = s.service_id
JOIN Vehicles v
    ON s.vehicle_id = v.vehicle_id
JOIN Customers c
    ON v.customer_id = c.customer_id
WHERE b.payment_status = 'Pending';



-- Q20. Rank mechanics based on number of services handled
SELECT
    m.mechanic_name,
    COUNT(s.service_id) AS Total_Services,
    RANK() OVER (
        ORDER BY COUNT(s.service_id) DESC
    ) AS Service_Rank
FROM Mechanics m
LEFT JOIN Service_Records s
    ON m.mechanic_id = s.mechanic_id
GROUP BY m.mechanic_id, m.mechanic_name;







