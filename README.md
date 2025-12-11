# CS301 Group Project

---

# Trans Safety & Aid

A JavaFX + MongoDB desktop application that helps transgender and gender-diverse individuals locate safe, inclusive, and affirming real-world businesses and community services.

---

## Overview

Trans Safety & Aid is a community-driven application designed to help transgender and gender-diverse people identify safe, affirming, and supportive real-world spaces.

In an environment where political hostility, discrimination, and digital platform limitations create significant barriers, many transgender individuals struggle to access gender-affirming care, safe businesses, and community resources.
This application aims to bridge that gap by allowing users to search for businesses, healthcare providers, and community services that are verified or highlighted as “safe-to-go” for members of the LGBTQ+ community.

Users can browse listings by service type, search by keywords or location, and view community-contributed reviews describing affirming experiences. By putting visibility and safety information directly into the hands of transgender users, the app supports informed decision-making and fosters a safer community network.

---

## Target Users

The target users are:

* transgender people
* gender-nonconforming individuals
* broader LGBTQ+ community members

Allies, caregivers, and supportive organizations may also use the app to identify affirming services.

---

## Motivation

The motivation behind this project is grounded in real-world need: transgender people are facing increasing obstacles to accessing safe spaces, gender-affirming care, and respectful businesses. With discrimination on the rise and limited visibility of affirming services, community-sourced tools like this app can significantly improve safety, accessibility, and overall quality of life.

By enabling people to share supportive resources and mark affirming environments, **Trans Safety & Aid** strengthens mutual aid networks and amplifies the presence of safe, inclusive spaces.

---

## **Features Implemented**

### **MVP Features (Completed)**

* **User Account Creation & Login**
  * Register new users
  * Authenticate with stored credentials

* **Search Functionality**
  * Search by business name, location, or keywords

* **Filtering**
  * Filter businesses by service type

* **View Reviews**
  * Preloaded reviews display for each business

* **MongoDB Integration**
  * Stores users, businesses, and reviews
  * All DB communication handled in `Database.java`

---

### **Stretch Features (Not Implemented)**

* Safe-space verification & moderation
* Review reporting / anti-griefing features
* Mobile app version
* Proximity-based search / advanced filters
* Saving new reviews to MongoDB

---

## **System Requirements (WSL Only)**

### **Operating System**
* **Windows 10 / 11 using WSL (Ubuntu recommended)**

### **Languages**
* **Java 17+**

### **Frameworks / Libraries**
* **JavaFX 17+**
* **MongoDB Java Driver** (via Maven)
* **MongoDB Java Driver JAR** *(Only needed if running `DBTest.java` manually)*

### **Build Tool**
* **Maven**

### **Database (Inside WSL)**
You will need all of the following:
* **MongoDB Community Server running in WSL**
* **MongoDB Compass** (Windows GUI connecting to WSL)
* **MongoDB VSCode Extension** (Required for browsing/editing DB from VSCode)

### **Dependencies (Automatically Installed via Maven)**
* JavaFX Controls
* JavaFX FXML
* MongoDB Java Driver

---

## **Clone & Run Instructions (WSL Only)**

### **Step 1 — Install Required Software in WSL**

```bash
sudo apt update
sudo apt install openjdk-17-jdk maven -y
```

### Install MongoDB in WSL

```bash
sudo apt install -y mongodb
```

---

### **Step 2 — Start MongoDB (WSL Only)**

```bash
sudo service mongod start
```

Check status:

```bash
sudo service mongod status
```

MongoDB **must be running** before launching the app.

---

### **Step 3 — Clone the Repository**

```bash
git clone <YOUR_REPOSITORY_URL>
cd transgender-safety-and-aid-platform
```

---

### **Step 4 — Install Maven Dependencies**

```bash
mvn clean install
```

---

### **Step 5 — Restore the MongoDB Database (WSL)**

```bash
mongorestore --db transsafety ./backup_transsafety/transsafety
```

### OR using MongoDB Compass (Windows App)

Connect Compass to:

```
mongodb://localhost:27017
```

Then:

1. Create database: **transsafety**
2. Import `.bson` into:

   * `users`
   * `businesses`
   * `reviews`

---

### **Step 6 — Environment Configuration**

No external config or `.env` file is required.

Database settings are inside `Database.java`:

```java
  // Database.java
  private final String connectionString;
  private final String databaseName;

  // inside Database()
  this.connectionString = "mongodb://localhost:27017";
  this.databaseName = "transsafety";
```

---

## **Step 7 — Run the Application**

```bash
mvn clean javafx:run
```

---

## **Step 8 — Using the Application**

### **Login**
* Use an existing user
* Or register a new account

### **Main Features**
Users can:
* Search for businesses
* Filter by service type
* View business details
* Read preloaded reviews
* Submit a review (**printed to console only; not stored**)

The application is now fully running under **WSL**.

---
