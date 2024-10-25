
# Product and Category Management System


This application is a Java Swing-based desktop application that allows users to manage Products and Categories through a user-friendly interface. The application supports CRUD operations for both entities, which are stored in a database accessed through JPA. Users can add, view, update, delete, and filter products by category.



## Features

- Product Management: Create, update, delete, and view product details.
- Category Management: Create, update, delete, and view category details.
- Filtering: Filter products based on the selected category.
- GUI: Intuitive and interactive UI with navigation and CRUD operation buttons.
- Database Integration: Entity relationships handled via JPA with EntityManager.



## Technologies Used

- Java Swing: For creating the graphical user interface.
- Java Persistence API (JPA): For database operations and object-relational mapping.
- EntityManager: For managing entity instances and database transactions.

# Getting Started

## Prerequisites

- Java JDK 11 or higher installed.
- Maven for dependency management.
- A Database mysql with JPA, set up with tables for Product and Category.
## Database Setup

    CREATE TABLE Category (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT
    );

    CREATE TABLE Product (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    description TEXT,
    price DECIMAL(10, 2),
    quantity INT,
    sdr BIGINT,
    category_id BIGINT,
    FOREIGN KEY (category_id) REFERENCES Category   (id)
    );

## Installation

Clone the repository:

    git clone https://github.com/your-username/product-category-management.git
    cd product-category-management
Install dependencies:

    Use Maven to install project dependencies:
    mvn clean install
    Configure the Database:

Configure your database connection settings in persistence.xml:

    <property name="javax.persistence.jdbc.url" value="jdbc:mysql://localhost:3306/your_database"/>
    <property name="javax.persistence.jdbc.user" value="your_username"/>
    <property name="javax.persistence.jdbc.password" value="your_password"/>

Run the Application:

Compile and run the project in your IDE or using Maven:

    mvn exec:java -Dexec.mainClass="your.main.class"
