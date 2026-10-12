-- ============================================
-- HOTEL BOOKING MANAGEMENT SYSTEM
-- PART 1: DATABASE, TABLES AND SAMPLE DATA
-- ============================================

CREATE DATABASE IF NOT EXISTS HotelBookingDB;
USE HotelBookingDB;
CREATE TABLE Hotels (
    hotel_id INT PRIMARY KEY AUTO_INCREMENT,
    hotel_name VARCHAR(50),
    city VARCHAR(30),
    star_rating INT
);
INSERT INTO Hotels (hotel_name, city, star_rating)
VALUES
('Grand Palace', 'Chennai', 5),
('Royal Inn', 'Bangalore', 4),
('Blue Moon', 'Hyderabad', 3);


CREATE TABLE Rooms (
    room_id INT PRIMARY KEY AUTO_INCREMENT,
    hotel_id INT,
    room_number VARCHAR(10),
    room_type VARCHAR(20),
    price DECIMAL(10,2),
    status VARCHAR(20),

    FOREIGN KEY (hotel_id)
        REFERENCES Hotels(hotel_id)
);

INSERT INTO Rooms
(hotel_id, room_number, room_type, price, status)
VALUES
(1, '101', 'Standard', 2500, 'Available'),
(1, '102', 'Deluxe', 4000, 'Occupied'),
(1, '103', 'Suite', 7000, 'Occupied'),
(2, '201', 'Standard', 2200, 'Available'),
(2, '202', 'Deluxe', 3800, 'Occupied'),
(3, '301', 'Standard', 1800, 'Available'),
(3, '302', 'Suite', 6000, 'Occupied');



CREATE TABLE Guests (
    guest_id INT PRIMARY KEY AUTO_INCREMENT,
    guest_name VARCHAR(50),
    phone VARCHAR(15),
    city VARCHAR(30)
);

INSERT INTO Guests (guest_name, phone, city)
VALUES
('Rahul', '9876543210', 'Chennai'),
('Priya', '9876543211', 'Bangalore'),
('Arun', '9876543212', 'Hyderabad'),
('Sneha', '9876543213', 'Coimbatore'),
('Karthik', '9876543214', 'Mumbai');




CREATE TABLE Bookings (
    booking_id INT PRIMARY KEY AUTO_INCREMENT,
    guest_id INT,
    room_id INT,
    check_in DATE,
    check_out DATE,
    booking_status VARCHAR(20),

    FOREIGN KEY (guest_id)
        REFERENCES Guests(guest_id),

    FOREIGN KEY (room_id)
        REFERENCES Rooms(room_id)
);

INSERT INTO Bookings
(guest_id, room_id, check_in, check_out, booking_status)
VALUES
(1, 2, '2026-07-25', '2026-07-30', 'Completed'),
(2, 3, '2026-07-28', '2026-08-02', 'Active'),
(3, 5, '2026-07-29', '2026-08-01', 'Active'),
(4, 7, '2026-07-20', '2026-07-22', 'Completed'),
(1, 1, '2026-08-05', '2026-08-08', 'Booked'),
(5, 4, '2026-07-31', '2026-08-03', 'Cancelled');



CREATE TABLE Payments (
    payment_id INT PRIMARY KEY AUTO_INCREMENT,
    booking_id INT,
    amount DECIMAL(10,2),
    payment_status VARCHAR(20),

    FOREIGN KEY (booking_id)
        REFERENCES Bookings(booking_id)
);

INSERT INTO Payments
(booking_id, amount, payment_status)
VALUES
(1, 20000, 'Paid'),
(2, 35000, 'Paid'),
(3, 12000, 'Pending'),
(4, 15000, 'Paid'),
(5, 7500, 'Pending'),
(6, 0, 'Refunded');


-- VERIFY TABLES AND DATA

SHOW TABLES;

SELECT * FROM Hotels;
SELECT * FROM Rooms;
SELECT * FROM Guests;
SELECT * FROM Bookings;
SELECT * FROM Payments;



-- ============================================
-- PART 2: PRACTICE QUERIES 1 TO 10
-- ============================================

USE HotelBookingDB;


-- QUERY 1: Display Available Rooms

SELECT
    Rooms.room_number,
    Rooms.room_type,
    Rooms.price,
    Hotels.hotel_name
FROM Rooms
INNER JOIN Hotels
    ON Rooms.hotel_id = Hotels.hotel_id
WHERE Rooms.status = 'Available';


-- QUERY 2: Find Guests Staying Today

SELECT
    Guests.guest_name,
    Hotels.hotel_name,
    Rooms.room_number,
    Bookings.check_in,
    Bookings.check_out
FROM Guests
INNER JOIN Bookings
    ON Guests.guest_id = Bookings.guest_id
INNER JOIN Rooms
    ON Bookings.room_id = Rooms.room_id
INNER JOIN Hotels
    ON Rooms.hotel_id = Hotels.hotel_id
WHERE CURDATE() BETWEEN Bookings.check_in
                    AND Bookings.check_out
  AND Bookings.booking_status = 'Active';


-- QUERY 3: Calculate Total Revenue

SELECT
    SUM(amount) AS Total_Revenue
FROM Payments
WHERE payment_status = 'Paid';


-- QUERY 4: Display Bookings Between Two Dates

SELECT
    Bookings.booking_id,
    Guests.guest_name,
    Hotels.hotel_name,
    Bookings.check_in,
    Bookings.check_out
FROM Bookings
INNER JOIN Guests
    ON Bookings.guest_id = Guests.guest_id
INNER JOIN Rooms
    ON Bookings.room_id = Rooms.room_id
INNER JOIN Hotels
    ON Rooms.hotel_id = Hotels.hotel_id
WHERE Bookings.check_in
      BETWEEN '2026-07-25' AND '2026-07-31';


-- QUERY 5: Find the Most Booked Room Type

SELECT
    Rooms.room_type,
    COUNT(*) AS Total_Bookings
FROM Rooms
INNER JOIN Bookings
    ON Rooms.room_id = Bookings.room_id
GROUP BY Rooms.room_type
ORDER BY Total_Bookings DESC
LIMIT 1;


-- QUERY 6: Calculate Occupancy Rate

SELECT
    ROUND(
        COUNT(
            CASE
                WHEN status = 'Occupied' THEN 1
            END
        ) * 100.0 / COUNT(*),
        2
    ) AS Occupancy_Rate
FROM Rooms;


-- QUERY 7: Display Cancelled Bookings

SELECT
    Bookings.booking_id,
    Guests.guest_name,
    Hotels.hotel_name,
    Rooms.room_number
FROM Bookings
INNER JOIN Guests
    ON Bookings.guest_id = Guests.guest_id
INNER JOIN Rooms
    ON Bookings.room_id = Rooms.room_id
INNER JOIN Hotels
    ON Rooms.hotel_id = Hotels.hotel_id
WHERE Bookings.booking_status = 'Cancelled';


-- QUERY 8: Find Customers with Multiple Bookings

SELECT
    Guests.guest_name,
    COUNT(Bookings.booking_id) AS Total_Bookings
FROM Guests
INNER JOIN Bookings
    ON Guests.guest_id = Bookings.guest_id
GROUP BY Guests.guest_id, Guests.guest_name
HAVING COUNT(Bookings.booking_id) > 1;


-- QUERY 9: Display Average Room Price

SELECT
    AVG(price) AS Average_Room_Price
FROM Rooms;


-- QUERY 10: Find Hotels with More Than 100 Rooms

SELECT
    Hotels.hotel_name,
    COUNT(Rooms.room_id) AS Total_Rooms
FROM Hotels
INNER JOIN Rooms
    ON Hotels.hotel_id = Rooms.hotel_id
GROUP BY Hotels.hotel_id, Hotels.hotel_name
HAVING COUNT(Rooms.room_id) > 100;



-- ============================================
-- PART 3: ADVANCED PRACTICE QUERIES 11 TO 15
-- ============================================

USE HotelBookingDB;


-- QUERY 11: Find the Highest-Paying Guest

SELECT
    g.guest_name,
    SUM(p.amount) AS Total_Spent
FROM Guests g
INNER JOIN Bookings b
    ON g.guest_id = b.guest_id
INNER JOIN Payments p
    ON b.booking_id = p.booking_id
WHERE p.payment_status = 'Paid'
GROUP BY g.guest_id, g.guest_name
ORDER BY Total_Spent DESC
LIMIT 1;


-- QUERY 12: Hotel-wise Revenue

SELECT
    h.hotel_name,
    SUM(p.amount) AS Revenue
FROM Hotels h
INNER JOIN Rooms r
    ON h.hotel_id = r.hotel_id
INNER JOIN Bookings b
    ON r.room_id = b.room_id
INNER JOIN Payments p
    ON b.booking_id = p.booking_id
WHERE p.payment_status = 'Paid'
GROUP BY h.hotel_id, h.hotel_name;


-- QUERY 13: Most Expensive Room

SELECT
    room_number,
    room_type,
    price
FROM Rooms
ORDER BY price DESC
LIMIT 1;


-- QUERY 14: Guests Who Have Never Made a Booking

SELECT
    g.guest_name
FROM Guests g
LEFT JOIN Bookings b
    ON g.guest_id = b.guest_id
WHERE b.booking_id IS NULL;


-- QUERY 15: Rank Hotels by Revenue

SELECT
    h.hotel_name,
    SUM(p.amount) AS Revenue,
    RANK() OVER (
        ORDER BY SUM(p.amount) DESC
    ) AS Revenue_Rank
FROM Hotels h
INNER JOIN Rooms r
    ON h.hotel_id = r.hotel_id
INNER JOIN Bookings b
    ON r.room_id = b.room_id
INNER JOIN Payments p
    ON b.booking_id = p.booking_id
WHERE p.payment_status = 'Paid'
GROUP BY h.hotel_id, h.hotel_name;













