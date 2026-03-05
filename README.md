# 🛒 Store Management and Point of Sale (PoS) System

A Point of Sale (PoS) management system developed in Java focused on clean architecture, Object-Oriented Programming (OOP), and the application of software design patterns.

This project was developed as part of the Object-Oriented Programming course at Polytechnic University of Madrid, with the goal of modeling a complex domain handling inventory, users, and transactions.

## 🚀 Key Features

* **Catalog Management:** Support for different types of products through inheritance (`Goods`, `Services`, `Events`), each with its own specific business rules.
* **User Management:** Role-based system differentiating between administrators/cashiers (`Cashier`) and clients (`Customer`).
* **Ticket Processing:** Creation, modification, and closing of purchase tickets (receipts) with different output formats.
* **Data Persistence:** Application state saving and loading system using `PersistenceService` and data serialization.

## 🧠 Architecture & Design Patterns

The code is structured aiming for **high cohesion and low coupling**. Some of the key design patterns implemented are:

* **Command Pattern:** Used to decouple user requests (e.g., add product, remove cashier, generate ticket) from the business logic that executes them. This allows for easy scalability when adding new commands in the future.
* **Strategy Pattern:** Implemented in the ticket printing system (`PrinterStrategy`). It allows changing the receipt format at runtime (e.g., `CommonPrinter`, `ServicePrinter`) without modifying the core ticket logic.
* **Layered Architecture:** Clear separation of concerns between the presentation/input layer (`Handlers`), the services layer (`Services`), the domain models (`User`, `Product`, `Ticket`), and data persistence (`Persistence`).

## 📊 Class Diagram (UML)

For a better understanding of the project's structure, here is the complete UML diagram:

![Alt text](https://raw.githubusercontent.com/Nick-codes-47/tienda_upm/master/assets/UML_Tercera_Entrega.svg)

## 🛠️ Technologies Used

* **Language:** Java
* **Tools:** Maven
* **Core Concepts:** OOP, SOLID principles, GoF Design Patterns.

