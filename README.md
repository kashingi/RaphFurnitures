# Expense Tracker RESTful API

A Spring Boot RESTful API for managing personal expenses and categories with JWT authentication.

## Features

- User registration and authentication with JWT
- Category management (CRUD operations)
- Product management (CRUD operations)
- Cart management (CRUD operations)
- Order management (CRUD operations)
- MySQL database integration

## Tech Stack

- Java 17
- Spring Boot 3.2.0
- Spring Security
- Spring Data JPA
- MySQL
- JWT (JSON Web Tokens)
- JUnit for testing
- Postman as the testing tool

## Project Structure

```
src/main/java/com/expensetracker/
├── constants/          # Application constants
├── dto/               # Data Transfer Objects
├── jwt/               # JWT utilities and filters
├── controllers/       # REST controllers
├── services/          # Service interfaces
├── servicesimpl/      # Service implementations
├── utils/             # Utility classes
├── model/             # JPA entities
├── repository/        # JPA repositories
└── config/            # Configuration classes
```

## Database Setup

1. Create MySQL database:
```sql
CREATE DATABASE furnitureDB;
```

2. Update `application.properties` with your MySQL credentials:
```properties
spring.datasource.username=your_username
spring.datasource.password=your_password
```

## API Endpoints

### Authentication
- `POST /api/v1/auth/register` - Register new user
- `POST /api/v1/auth/login` - Login user
- `GET /api/v1/auth/getAllUsers` - Get all users
- `PUT /api/v1/auth/updateUser` - Update a user
- `PUT /api/v1/auth/updateRole` - Update user role
- `DELETE /api/v1/auth/deleteUser` - Delete a user

### Categories (Requires JWT token)
- `POST /api/v1//category/addCategory` - add new category
- `GET /api/v1/category/getAllCategories` - Get all categories
- `POST /api/v1/category/updateCategory{id}` - Update category
- `PUT /api/v1/category/updateCategoryStatus/{id}` - Update category status
- `DELETE /api/category/deleteCategory/{id}` - Delete category

### Expenses (Requires JWT token)
- `POST /api/v1/product/addProduct` - Add a new product
- `GET /api/v1/product/getAllProducts` - Add new expense
- `PUT /api/v1/product/updateProduct/{id}` - Update a product
- `GET /api/v1/product/getProductByCategory/{id}` - Get a product br category id
- `PUT /api/v1/product/updateProductStatus` - Update product status
- `DELETE /api/v1/product/deleteProduct` - Delete a product

## Sample Requests


### Register User

POST http://localhost:8081/api/v1/auth/register
Content-Type: application/json
```Json
{
    "name" : "Test User",
    "email" : "user@test.com",
    "contact" : "0790487504",
    "password" : "123456"
}
```

### Login
POST http://localhost:8081/api/v1/auth/login
```json
{
    "email": "john@example.com",
    "password": "password123"
}
```

### Get all users
GET http://localhost:8081/api/v1/auth/getAllUsers
Authorization: Bearer <jwt_token>
```json
[
  {
    "id": 2,
    "name": "User User",
    "email": "user@test.com",
    "contact": "0990487504",
    "role": "User"
  }
]
```

### Update user
PUT http://localhost:8081/api/v1/auth/updateUser/2
Authorization: Bearer <jwt_token>
```json
{
  "contact" : "0746350811"
}
```

### Update user role
PUT http://localhost:8081/api/v1/auth/updateRole/id
Authorization: Bearer <jwt_token>
```json
{
  "role" : "admin"
}
```
### Delete user
DELETE http://localhost:8081/api/v1/auth/deleteUser/id
Authorization: Bearer <jwt_token>
```json
{
  "Message":"User deleted successfully."
}
```

## Running the Application

1. Clone the repository
2. Set up MySQL database
3. Update database credentials in `application.properties`
4. Run the application:
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## Testing

Run unit tests:
```bash
mvn test
```

## Security

- JWT tokens are required for all category and expense operations
- Users can only access their own data
- Passwords are encrypted using BCrypt
- CORS is configured for cross-origin requests

## Postman Testing

Import the API endpoints into Postman and test:
1. Register a new user
2. Login to get JWT token
3. Use the token in Authorization header for protected endpoints
4. Test all CRUD operations for categories and expenses