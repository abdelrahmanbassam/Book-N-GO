# README

# Book’n Go – Hall Reservation System

## 📌 Project Overview

**Book’n Go** is a web-based hall reservation platform designed to simplify the process of booking spaces for student organizations, technical teams, and individuals. The platform enables users to browse available halls, check real-time availability, and make instant reservations with payment integration.

## 🚀 Features

### 👥 User Roles

- **Admin**: Manages hall provider accounts, system oversight.
- **Hall Providers**: Offer halls for reservation, manage availability and schedules.
- **Users**: Browse, book halls, and provide feedback post-reservation.

### 🔧 Core Functionalities

- **Authentication**: Secure sign-up and login for users and admins.
- **Admin Dashboard**: Review and approve hall provider applications.
- **User Profiles**: Manage personal data and reservation history.
- **Hall Listings**: Detailed information including amenities, capacity, and schedules.
- **Real-time Scheduling**: Interactive timetable for checking availability.
- **Payment Integration**: Process payments during booking.
- **Feedback System**: Users can leave reviews after reservations.
- **AI Chat Support**: Customer service assistant to help users navigate the system.

## 📂 Project Structure

### Backend

- Built with Java and Spring Boot
- RESTful APIs for hall, location, workday, booking, authentication, etc.
- Unit-tested for robustness
    
    ### **Overall system design**
    
    ![assests/image.png](image.png)
    
    ![image.png](image%201.png)
    
    ![image.png](image%202.png)
    

### Frontend

- Developed using modern frameworks (React or similar)
- Responsive UI for user interactions

![image.png](image%203.png)

![image.png](image%204.png)

![image.png](image%205.png)

![image.png](image%206.png)

![image.png](image%207.png)

![image.png](image%208.png)

![image.png](image%209.png)

### Database

- Relational database with schemas for users, halls, bookings, schedules, etc.

![image.png](image%2010.png)

## Design and Architecture

### UML

![image.png](image%2011.png)

## 🧪 Testing

Extensive unit testing was conducted on services and controllers:
- **BookingServiceTest**: Ensures correct behavior for booking creation, updates, and deletions.
- **HallControllerTest**: Verifies hall management endpoints.
- **AuthControllerTest**: Covers user registration, login, logout scenarios.
- Additional tests for Workday, Location, and Workspace controllers.

## 🛠️ Setup Instructions

1. Clone the repository:
    
    ```bash
    git clone https://github.com/Gang-of-CSED/Book-N-GO.git
    ```
    
2. Set up the backend:
    - Install Java 17+ and Maven.
    - Navigate to the backend directory:
        
        ```bash
        cd backend
        mvn spring-boot:run
        ```
        
3. Set up the frontend:
    - Install Node.js and npm.
    - Navigate to the frontend directory:
        
        ```bash
        cd frontend
        npm install
        npm start
        ```
        
4. Access the app in your browser at `http://localhost:3000`.
