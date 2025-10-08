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
PUT http://localhost:8081/api/v1/auth/updateUser/id
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
### Add category
POST http://localhost:8081/api/v1/category/addCategory
Authorization: Bearer <jwt_token>
```json
{
    "name" : "Legumes"
}
```

### Get all categories
Authorization: <jwt_token>
```json
[
    {
        "id": 1,
        "name": "Stools",
        "status": "true"
    },
    {
        "id": 2,
        "name": "Beds",
        "status": "true"
    }
]
```

### Update category
Authorization: <jwt_token>
PUT http://localhost:8081/api/v1/category/updateCategory/id
```json
{
  "name" : "Legumes"
}
```

### Update category status
PUT http://localhost:8081/api/v1/category/updateCategoryStatus/id
Authorization: <jwt_token>
```json
{
    "status" : "true"
}
```

### Delete category
DELETE http://localhost:8081/api/v1/category/deleteCategory/id
Authorization: <jwt_token>
```json
{
  "Message":"Category deleted successfully."
}
```

### Add to cart
POST http://localhost:8081/api/v1/cart/addToCart
Authorization: <jwt_token>
```json
{
    "productId" : 9,
    "quantity" : 5
}
```

### Get all cart items
GET http://localhost:8081/api/v1/cart/getCart
```json
[
  {
    "id": 6,
    "userName": "Admin Admin",
    "userEmail": "admin@test.com",
    "productName": "Kitchen Toolz",
    "productDescription": "Confortable seat",
    "productPrice": 15000.0,
    "quantity": 5
  }
]
```
### Update cart item
PUT http://localhost:8081/api/v1/cart/updateCart/id
Authorization: <jwt_token>
```json
{
    "quantity" : 2
}
```

### Remove from cart
DELETE http://localhost:8081/api/v1/cart/removeFromCart/id
Authorization: <jwt_token>
```json
{
  "Message":"Cart item deleted successfully"
}
```
### Place order
POST http://localhost:8081/api/v1/order/placeOrder
Authorization: <jwt_token>
```json
{
    "cartId" : 6,
    "paymentMethod" : "CASH"
}
```

### Get orders
GET http://localhost:8081/api/v1/order/getOrders
Authorization: <jwt_token>
```json
[
    {
        "id": 5,
        "userName": "Admin Admin",
        "userEmail": "admin@test.com",
        "productName": "Kitchen Toolz",
        "productDescription": "Confortable seat",
        "productPrice": 15000.0,
        "quantity": 2,
        "totalAmount": 30000.0,
        "paymentMethod": "CASH",
        "paymentStatus": "PENDING",
        "orderStatus": "PENDING",
        "orderDate": "2025-09-26T22:10:33"
    }
]
```
### Update order status
PUT http://localhost:8081/api/v1/order/updateOrderStatus/id
Authorization: <jwt_token>
```json
{
  "status" : "CONFIRMED"
}
```
### Lipa na mpesa
Install ngrok, unzip and paste it in C drive
Run it using this command
ngrok http 8081
Copy and paste Forwarding in call.back-url before 
/api/v1/payment/callback

### stk push
POST http://localhost:8081/api/v1/payment/stkpush
Authorization: <jwt_token>
```json

{
  "orderId": 5,
  "phoneNumber": "254790487504"
}

```

### Call back url
POST https://c438eabaf72c.ngrok-free.app/api/v1/payment/callback
Authorization: <no token required>
```json
{
  "Body": {
    "stkCallback": {
      "MerchantRequestID": "2e9c-429c-a95c-b4cd653531a322302",
      "CheckoutRequestID": "ws_CO_05102025193733441713408025",
      "ResultCode": 1032,
      "ResultDesc": "Request cancelled by user"
    }
  }
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