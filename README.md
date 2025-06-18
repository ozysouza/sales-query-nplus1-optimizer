# JPA N+1 Query Problem Solution

This is a simple Spring Boot project that demonstrates how to detect and solve the **N+1 query problem** using JPA with Hibernate.

## 🛠️ Tech Stack

- Java 17
- Spring Boot
- Spring Data JPA
- H2 database (in-memory)
- Hibernate

## 🔍 Entity Relationship

This project models a simple sales reporting system with the following relationship:
![salesdb](https://github.com/user-attachments/assets/697951da-31fd-48c6-bbed-32cd0eb32858)


---

## 📋 Business Rules

### 🧾 1. Sales Report

1. **[IN]** The user may optionally provide:
   - a **start date**
   - an **end date**
   - and a portion of the seller's name

2. **[OUT]** The system returns a **paginated list** of sales that match the criteria, including:
   - the sale's `id`
   - the `date` of the sale
   - the `amount` sold
   - and the seller's `name`

3. **Additional Notes**:
   - If the **end date** is not provided, the system uses the current date as default:
   - If the **start date** is not provided, it defaults to one year before the end date:
   - If the **name** is not provided, an empty string is considered, returning results for all sellers.

---

### 📈 2. Sales Summary by Seller

1. **[IN]** The user may optionally provide:
   - a **start date**
   - and an **end date**

2. **[OUT]** The system returns a **summary list** containing:
   - the seller's name
   - and the **total amount sold** by that seller during the selected period

3. **Additional Notes**:
   - The same default behaviour for missing **dates** and **name filtering** applies as in the Sales Report use case.

---

## Available Endpoints
```postman
GET /sales/report?minDate=2022-05-01&maxDate=2022-05-31&name=odinson
GET /sales/report

GET /sales/summary?minDate=2022-01-01&maxDate=2022-06-30
GET /sales/summary
```
## 🧩 Problem

When retrieving a list of entities and accessing their lazy-loaded relationships (e.g., Sale → Seller), JPA issues one query to fetch the main entities and additional N queries to fetch each related entity. This causes serious performance degradation.

```sh
@Query(value = """
		SELECT obj FROM Sale obj
		JOIN obj.seller
		WHERE obj.date BETWEEN :minDate AND :maxDate
		AND UPPER(obj.seller.name) LIKE UPPER(CONCAT('%', :name, '%'))
		""")
```
Result
```sh
Hibernate: 
    select
        s1_0.id,
        s1_0.amount,
        s1_0.date,
        s1_0.deals,
        s1_0.seller_id,
        s1_0.visited 
    from
        tb_sales s1_0 
    join
        tb_seller s2_0 
            on s2_0.id=s1_0.seller_id 
    where
        s1_0.date between ? and ? 
        and upper(s2_0.name) like upper(('%'||?||'%')) escape '' 
    fetch
        first ? rows only
Hibernate: 
    select
        s1_0.id,
        s1_0.email,
        s1_0.name,
        s1_0.phone 
    from
        tb_seller s1_0 
    where
        s1_0.id=?
Hibernate: 
    select
        s1_0.id,
        s1_0.email,
        s1_0.name,
        s1_0.phone 
    from
        tb_seller s1_0 
    where
        s1_0.id=?
Hibernate: 
    select
        s1_0.id,
        s1_0.email,
        s1_0.name,
        s1_0.phone 
    from
        tb_seller s1_0 
    where
        s1_0.id=?
Hibernate: 
    select
        s1_0.id,
        s1_0.email,
        s1_0.name,
        s1_0.phone 
    from
        tb_seller s1_0 
    where
        s1_0.id=?
Hibernate: 
    select
        s1_0.id,
        s1_0.email,
        s1_0.name,
        s1_0.phone 
    from
        tb_seller s1_0 
    where
        s1_0.id=?
```

## 💡 Solution

The project uses `JOIN FETCH` and DTO projections to eliminate unnecessary queries and optimize database access.
```sh
@Query(value = """
        SELECT obj FROM Sale obj
        JOIN FETCH obj.seller
        WHERE obj.date BETWEEN :minDate AND :maxDate
        AND UPPER(obj.seller.name) LIKE UPPER(CONCAT('%', :name, '%'))
        """, countQuery = """
        SELECT COUNT(obj) FROM Sale obj
        WHERE obj.date BETWEEN :minDate AND :maxDate
        AND UPPER(obj.seller.name) LIKE UPPER(CONCAT('%', :name, '%'))
        """)
```
Result
```sh
	Hibernate: 
    select
        s1_0.id,
        s1_0.amount,
        s1_0.date,
        s1_0.deals,
        s2_0.id,
        s2_0.email,
        s2_0.name,
        s2_0.phone,
        s1_0.visited 
    from
        tb_sales s1_0 
    join
        tb_seller s2_0 
            on s2_0.id=s1_0.seller_id 
    where
        s1_0.date between ? and ? 
        and upper(s2_0.name) like upper(('%'||?||'%')) escape '' 
    fetch
        first ? rows only
```

The app loads initial sample data via import.sql for quick testing.

## ▶️ How to Run the Project

### 1. Clone the Repository

```bash
git clone https://github.com/ozysouza/sales-query-nplus1-optimizer.git
cd sales-query-nplus1-optimizer
```
### 2. Configure your database in application.properties or application-test.properties

### 3. Run the project
```bash
./mvnw spring-boot:run
```


