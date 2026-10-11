-- ============================================================
-- PART 1: DATABASE AND TABLE CREATION
-- ============================================================

CREATE DATABASE IF NOT EXISTS employee_management_project;
USE employee_management_project;

CREATE TABLE IF NOT EXISTS Employee (
    emp_id INT PRIMARY KEY,
    emp_name VARCHAR(50),
    department VARCHAR(30),
    salary DECIMAL(10,2),
    city VARCHAR(30),
    joining_date DATE
);

CREATE TABLE IF NOT EXISTS Customers (
    customer_id INT PRIMARY KEY,
    customer_name VARCHAR(50),
    city VARCHAR(30)
);

CREATE TABLE IF NOT EXISTS Orders (
    order_id INT PRIMARY KEY,
    customer_id INT,
    amount DECIMAL(10,2),
    order_date DATE,
    FOREIGN KEY (customer_id) REFERENCES Customers(customer_id)
);

CREATE TABLE IF NOT EXISTS Students (
    student_id INT PRIMARY KEY,
    student_name VARCHAR(50),
    department VARCHAR(30),
    marks INT
);


-- ============================================================
-- PART 2: INSERT SAMPLE DATA
-- ============================================================

INSERT INTO Employee
(emp_id, emp_name, department, salary, city, joining_date)
VALUES
(101, 'John', 'IT', 60000, 'Chennai', '2022-01-15'),
(102, 'David', 'HR', 45000, 'Bangalore', '2021-03-10'),
(103, 'Smith', 'IT', 70000, 'Chennai', '2020-07-12'),
(104, 'Mary', 'Finance', 55000, 'Mumbai', '2023-01-20'),
(105, 'James', 'HR', 48000, 'Delhi', '2022-05-05'),
(106, 'Linda', 'Finance', 65000, 'Mumbai', '2021-08-18');

INSERT INTO Customers
(customer_id, customer_name, city)
VALUES
(1, 'Rahul', 'Bangalore'),
(2, 'Priya', 'Chennai'),
(3, 'Arun', 'Mysore'),
(4, 'Sneha', 'Mumbai'),
(5, 'Kiran', 'Delhi'),
(6, 'Meena', 'Bangalore');

INSERT INTO Orders
(order_id, customer_id, amount, order_date)
VALUES
(101, 1, 2500, '2025-01-10'),
(102, 1, 3000, '2025-02-15'),
(103, 1, 2000, '2025-03-20'),
(104, 1, 3500, '2025-04-05'),
(105, 2, 4500, '2025-01-12'),
(106, 2, 3000, '2025-02-18'),
(107, 2, 2500, '2025-03-22'),
(108, 3, 6000, '2025-01-25'),
(109, 3, 1500, '2025-02-10'),
(110, 4, 8000, '2025-02-14'),
(111, 4, 2500, '2025-03-19'),
(112, 5, 1800, '2025-01-30'),
(113, 6, 5000, '2025-02-08'),
(114, 6, 4000, '2025-03-12'),
(115, 6, 1500, '2025-04-16');

INSERT INTO Students
(student_id, student_name, department, marks)
VALUES
(1, 'Amit', 'Computer Science', 85),
(2, 'Neha', 'Computer Science', 92),
(3, 'Rahul', 'Computer Science', 78),
(4, 'Priya', 'Computer Science', 88),
(5, 'Kiran', 'Computer Science', 72),
(6, 'Sneha', 'Computer Science', 95),
(7, 'Arun', 'Electronics', 76),
(8, 'Meena', 'Electronics', 82),
(9, 'Ravi', 'Electronics', 69),
(10, 'Pooja', 'Electronics', 91),
(11, 'Manoj', 'Electronics', 74),
(12, 'Divya', 'Electronics', 87),
(13, 'Suresh', 'Mechanical', 65),
(14, 'Anu', 'Mechanical', 73),
(15, 'Vijay', 'Mechanical', 81),
(16, 'Kavya', 'Mechanical', 94),
(17, 'Nithin', 'Civil', 70),
(18, 'Asha', 'Civil', 84);


-- ============================================================
-- PART 3: ADD YOUR QUESTIONS
-- ============================================================

USE employee_management_project;

-- Q1. Total number of employees in each department
SELECT department, COUNT(*) AS total_employees
FROM Employee
GROUP BY department;

-- Q2. Average salary in each department
SELECT department, AVG(salary) AS average_salary
FROM Employee
GROUP BY department;

-- Q3. Departments having more than one employee
SELECT department, COUNT(*) AS total_employees
FROM Employee
GROUP BY department
HAVING COUNT(*) > 1;

-- Q4. Highest salary in each department
SELECT department, MAX(salary) AS highest_salary
FROM Employee
GROUP BY department;

-- Q5. Lowest salary in each department
SELECT department, MIN(salary) AS lowest_salary
FROM Employee
GROUP BY department;


-- Q6. Departments whose average salary is greater than 50000
SELECT department, AVG(salary) AS average_salary
FROM Employee
GROUP BY department
HAVING AVG(salary) > 50000;

-- Q7. Total salary expenditure for each department
SELECT department, SUM(salary) AS total_salary
FROM Employee
GROUP BY department;

-- Q8. Employees sorted by salary, highest first
SELECT * FROM Employee
ORDER BY salary DESC;

-- Q9. Sort by department and then salary, highest first
SELECT * FROM Employee
ORDER BY department ASC, salary DESC;

-- Q10. Cities having more than one employee
SELECT city, COUNT(*) AS total_employees
FROM Employee
GROUP BY city
HAVING COUNT(*) > 1;


-- Q11. Total salary paid in each city
SELECT city, SUM(salary) AS total_salary
FROM Employee
GROUP BY city;

-- Q12. Departments ordered by total salary, highest first
SELECT department, SUM(salary) AS total_salary
FROM Employee
GROUP BY department
ORDER BY total_salary DESC;

-- Q13. Count employees in each department earning above 50000
SELECT department, COUNT(*) AS employee_count
FROM Employee
WHERE salary > 50000
GROUP BY department;

-- Q14. Difference between highest and lowest salary in each department
SELECT department,
       MAX(salary) - MIN(salary) AS salary_difference
FROM Employee
GROUP BY department;

-- Q15. Top 3 highest-paid employees
SELECT * FROM Employee
ORDER BY salary DESC
LIMIT 3;

-- Q16. Total order amount for each customer
SELECT c.customer_id, c.customer_name,
       COALESCE(SUM(o.amount), 0) AS total_purchase
FROM Customers c
LEFT JOIN Orders o ON c.customer_id = o.customer_id
GROUP BY c.customer_id, c.customer_name;

-- Q17. Customers who placed more than 3 orders
SELECT c.customer_id, c.customer_name,
       COUNT(o.order_id) AS total_orders
FROM Customers c
JOIN Orders o ON c.customer_id = o.customer_id
GROUP BY c.customer_id, c.customer_name
HAVING COUNT(o.order_id) > 3;

-- Q18. Average order amount for each customer
SELECT c.customer_id, c.customer_name,
       AVG(o.amount) AS average_order_amount
FROM Customers c
JOIN Orders o ON c.customer_id = o.customer_id
GROUP BY c.customer_id, c.customer_name;

-- Q19. Highest order amount for each customer
SELECT c.customer_id, c.customer_name,
       MAX(o.amount) AS highest_order
FROM Customers c
JOIN Orders o ON c.customer_id = o.customer_id
GROUP BY c.customer_id, c.customer_name;

-- Q20. Customers sorted by total purchase amount
SELECT c.customer_id, c.customer_name,
       SUM(o.amount) AS total_purchase
FROM Customers c
JOIN Orders o ON c.customer_id = o.customer_id
GROUP BY c.customer_id, c.customer_name
ORDER BY total_purchase DESC;

-- Q21. Customers whose total purchases exceed 10000
SELECT c.customer_id, c.customer_name,
       SUM(o.amount) AS total_purchase
FROM Customers c
JOIN Orders o ON c.customer_id = o.customer_id
GROUP BY c.customer_id, c.customer_name
HAVING SUM(o.amount) > 10000;

-- Q22. Customer names and total number of orders
SELECT c.customer_name,
       COUNT(o.order_id) AS total_orders
FROM Customers c
LEFT JOIN Orders o ON c.customer_id = o.customer_id
GROUP BY c.customer_id, c.customer_name;

-- Q23. Customer who spent the highest amount
SELECT c.customer_id, c.customer_name,
       SUM(o.amount) AS total_purchase
FROM Customers c
JOIN Orders o ON c.customer_id = o.customer_id
GROUP BY c.customer_id, c.customer_name
ORDER BY total_purchase DESC
LIMIT 1;

-- Q24. Customer who placed the maximum number of orders
SELECT c.customer_id, c.customer_name,
       COUNT(o.order_id) AS total_orders
FROM Customers c
JOIN Orders o ON c.customer_id = o.customer_id
GROUP BY c.customer_id, c.customer_name
ORDER BY total_orders DESC
LIMIT 1;

-- Q25. Customers whose average order amount exceeds 2000
SELECT c.customer_id, c.customer_name,
       AVG(o.amount) AS average_order_amount
FROM Customers c
JOIN Orders o ON c.customer_id = o.customer_id
GROUP BY c.customer_id, c.customer_name
HAVING AVG(o.amount) > 2000;

-- Q26. Top 5 customers by total purchase amount
SELECT c.customer_id, c.customer_name,
       SUM(o.amount) AS total_purchase
FROM Customers c
JOIN Orders o ON c.customer_id = o.customer_id
GROUP BY c.customer_id, c.customer_name
ORDER BY total_purchase DESC
LIMIT 5;

-- Q27. Minimum order amount for each customer
SELECT c.customer_id, c.customer_name,
       MIN(o.amount) AS minimum_order
FROM Customers c
JOIN Orders o ON c.customer_id = o.customer_id
GROUP BY c.customer_id, c.customer_name;

-- Q28. Customers whose total purchases exceed 5000
SELECT c.customer_id, c.customer_name,
       SUM(o.amount) AS total_purchase
FROM Customers c
JOIN Orders o ON c.customer_id = o.customer_id
GROUP BY c.customer_id, c.customer_name
HAVING SUM(o.amount) > 5000;

-- Q29. Customer-wise total orders and purchase amount
SELECT c.customer_id, c.customer_name,
       COUNT(o.order_id) AS total_orders,
       COALESCE(SUM(o.amount), 0) AS total_purchase
FROM Customers c
LEFT JOIN Orders o ON c.customer_id = o.customer_id
GROUP BY c.customer_id, c.customer_name;

-- Q30. Customers with more than 2 orders and purchases above 8000
SELECT c.customer_id, c.customer_name,
       COUNT(o.order_id) AS total_orders,
       SUM(o.amount) AS total_purchase
FROM Customers c
JOIN Orders o ON c.customer_id = o.customer_id
GROUP BY c.customer_id, c.customer_name
HAVING COUNT(o.order_id) > 2
   AND SUM(o.amount) > 8000;
   
-- Q31. Average marks in each department
SELECT department, AVG(marks) AS average_marks
FROM Students
GROUP BY department;

-- Q32. Departments with average marks above 75
SELECT department, AVG(marks) AS average_marks
FROM Students
GROUP BY department
HAVING AVG(marks) > 75;

-- Q33. Highest mark in each department
SELECT department, MAX(marks) AS highest_marks
FROM Students
GROUP BY department;

-- Q34. Total number of students in each department
SELECT department, COUNT(*) AS total_students
FROM Students
GROUP BY department;

-- Q35. Departments having more than 5 students
SELECT department, COUNT(*) AS total_students
FROM Students
GROUP BY department
HAVING COUNT(*) > 5;

-- Q36. Departments ordered by average marks, highest first
SELECT department, AVG(marks) AS average_marks
FROM Students
GROUP BY department
ORDER BY average_marks DESC;

-- Q37. Top 3 departments based on average marks
SELECT department, AVG(marks) AS average_marks
FROM Students
GROUP BY department
ORDER BY average_marks DESC
LIMIT 3;

-- Q38. Departments whose average marks are between 70 and 90
SELECT department, AVG(marks) AS average_marks
FROM Students
GROUP BY department
HAVING AVG(marks) BETWEEN 70 AND 90;

-- Q39. Total marks scored in each department
SELECT department, SUM(marks) AS total_marks
FROM Students
GROUP BY department;

-- Q40. Departments sorted by total number of students
SELECT department, COUNT(*) AS total_students
FROM Students
GROUP BY department
ORDER BY total_students DESC;

-- Q41. Lowest mark in each department
SELECT department, MIN(marks) AS lowest_marks
FROM Students
GROUP BY department;

-- Q42. Departments where the highest mark is above 90
SELECT department, MAX(marks) AS highest_marks
FROM Students
GROUP BY department
HAVING MAX(marks) > 90;

-- Q43. Count students scoring above 80 in each department
SELECT department, COUNT(*) AS students_above_80
FROM Students
WHERE marks > 80
GROUP BY department;

-- Q44. Departments where more than 3 students scored above 75
SELECT department, COUNT(*) AS students_above_75
FROM Students
WHERE marks > 75
GROUP BY department
HAVING COUNT(*) > 3;

-- Q45. Departments ordered by highest mark, descending
SELECT department, MAX(marks) AS highest_marks
FROM Students
GROUP BY department
ORDER BY highest_marks DESC;

-- ============================================================
-- PART 4: VERIFY ALL TABLES AND SAMPLE DATA
-- ============================================================

USE employee_management_project;
SHOW TABLES;
SELECT * FROM Employee;
SELECT * FROM Customers;
SELECT * FROM Orders;
SELECT * FROM Students;