# Spring Batch with PostgreSQL

This project demonstrates **Spring Batch processing** by reading employee data from a CSV file and storing it in a **PostgreSQL database**.

## 📦 Setup Instructions

### 1️⃣ Prerequisites
- **Java 8+**
- **Maven**
- **PostgreSQL 13+**

### 2️⃣ Database Setup
1. **Create a PostgreSQL database:**
   ```sql
   CREATE DATABASE springbatch;
   ```

2. **Update `application.properties` if needed:**
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/springbatch
   spring.datasource.username=postgres
   spring.datasource.password=admin
   ```

### 3️⃣ Run the Application
```bash
mvn clean install
mvn spring-boot:run
```

### 4️⃣ Verify Data in PostgreSQL
```sql
SELECT * FROM employee;
```

## 🏗️ Technologies Used
- **Spring Boot**
- **Spring Batch**
- **PostgreSQL**
- **Maven**
