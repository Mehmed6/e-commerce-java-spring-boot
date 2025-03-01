# e-Commerce Application with Spring Boot

This project is an e-commerce platform developed with Java Spring Boot. It provides a system where users can securely register, log in, and use JWT-based authentication and authorization mechanisms. Once logged in, users can perform actions with a JWT token. If the token expires, a new token can be obtained using a refresh token. Only admin users can perform CRUD operations on categories and products.

The project enhances user experience by using Thymeleaf for HTML-based frontend and JSON for API-based backend communication. Users can save multiple addresses and cards, add products to their cart, and place orders. Payment processing is done via Iyzico integration.

The project leverages tools like MapStruct and Lombok to simplify coding and increase productivity.


## 💻  Technologies Used


* Java 17
* Spring Boot
* Spring Security
* JWT Token Authentication and Authorization for user operations
* Thymeleaf
* Lombok
* PostgreSQL
* Iyzico Payment Integration
* Mapstruct

## 🔗  Services


* **RegisterService:** Handles user registration operations, including creating new user accounts and saving user data to the database.
* **LoginService:** Manages user login functionality by validating credentials and generating JWT tokens upon successful authentication.
* **JwtTokenService:** Responsible for creating and validating JWT tokens, ensuring secure user authentication and authorization in the system.
* **RefreshTokenService:** Handles the generation and validation of refresh tokens, enabling users to obtain new JWT tokens when their current token expires.
* **ProductService:** Manages product-related operations, including creating, reading, updating, and deleting products from the database.
* **CategoryService:** Manages category-related operations, including creating, reading, updating, and deleting categories from the database.
* **AccountService:** Handles user account operations, including account creation, balance management, and account deletion.
* **AddressService:** Responsible for handling user address information, including adding, updating, and retrieving multiple addresses for a user.
* **CardInfoService:** Manages user card information, allowing users to add, update, and retrieve card details for payment processing.
* **CartService:** Handles the shopping cart operations, including adding/removing items, updating quantities, and retrieving cart details.
* **OrderService:** Responsible for managing order processing, including creating, updating, and tracking user orders through their lifecycle.
* **PaymentService:** Handles payment-related operations, integrating with external payment gateways like Iyzico, and processing payments for completed orders.

# 🌐 API Endpoints


## RegisterController - JSON

* `Post /register/json`
  {
  "username": "username",
  "password": "password",
  "email": "email"
  } 
The user submits this request body to perform the registration process.

## LoginController - JSON


* `Post /login/json`
  {
  "username": "username",
  "password": "password"
  }
  The user submits this request body to perform the login process.

## RefreshTokenController - JSON


* `Post /refreshToken/json`
Generates a new JWT and refresh token for an expired JWT token using the refresh token.

## CategoryController - JSON


* `Post /json/api/category/save`
  Only admins can use this to create a new category with a category name.
* `Get /json/api/category/find`
  Finds the related category using the `categorName` parameter and throws an error if not found.
* `Get /json/api/category/find/all` Finds all existing categories.
* `Delete /json/api/category/delete/id` Only admins can delete the category with the given `id`.
* `Delete /json/api/category/delete/name` Only admins can delete the category with the given `categoryName`.

## ProductController - JSON


* `Post /json/api/product/save` {
  "name" : "name",
  "description" : "description",
  "price" : price,
  "stock" : stock,
  "categoryName" : "categoryName"
  }
  Only admins can use this request body to create a new product.
* `Get /json/api/product/find/id`
  Finds the related product using the `id` parameter and throws an error if not found.
* `Get /json/api/product/find/name`
  Finds the related products using the `name` parameter.
* `Get /json/api/product/find/all`
  Finds all existing products.
* `Put /json/api/product/update/id`
  Only admins can use the request body and the `id` parameter to update a product
* `Delete /json/api/product/delete/id`
  Only admins can delete the product with the given `id`.
* `Delete /json/api/product/delete/all`
  Only admins can delete all products.

## CartController - JSON


* `Post /json/api/cart/show/my-cart` {
  "username" : "username",
  "productId" : productId,
  "quantity" : quantity
  }
  The user can add a product to their cart using this request body.
* `Get /json/api/cart/add/cart`
  The user can view their cart using the `username`.
* `Delete /json/api/cart/delete/from/cart`
  The user can remove a product from their cart using the `username` and `productId`.
* `Delete /json/api/cart/clear/cart`
  The user can empty their cart using the `username`.

  
## OrderController - JSON


* `Post /json/api/order/create`
  The user creates an order from the items in the cart using their `username` and `password`.
* `Post /json/api/order/complete`
  Only admins can approve the order using the `orderId`.
* `Get /json/api/order/find`
  The user can view all their orders using their `username`.
* `Delete /json/api/order/delete/id`
  The user can delete their order using the `username` and `orderId` if the order is not completed.
* `Delete /json/api/order/delete/status`
  The user can delete their order using the `username` and `status`.
* `Delete /json/api/order/delete/all`
  The user can delete all orders using the `username`.

## AddressController - JSON


* `Post /json/api/address/save` {
  "username" : "username",
  "firstName" : "firstName",
  "lastName" : "lastName",
  "phone" : "phone",
  "email" : "email",
  "registrationAddress" : "registrationAddress",
  "zipCode" : "zipCode",
  "city" : "city",
  "country" : "country",
  "addressType" : "addressType"
  }
  The user can save an address using this request body.
* `Get /json/api/address/find`
  The user can view their addresses using the `username`.
* `Put /json/api/address/find`
  The user can update their address using the `addressId` and request body.
* `Delete /json/api/address/delete`
  The user can delete their address using the request body.
* `Delete /json/api/address/delete/id`
  The user can delete their address using the `addressId`

## CardController - JSON


* `Post /json/api/card/save` {
  "username" : "username",
  "cardHolderName" : "cardHolderName",
  "cardNumber" : "cardNumber",
  "expiryMonth" : "expiryMonth",
  "expiryYear" : "expiryYear",
  "cvc" : "cvc"
  }
  The user can save a card using this request body.
* `Get /json/api/card/find`
  The user can view their cards using the `username`.
* `Delete /json/api/card/delete`
  The user can delete their card using the `username` and `cardNumber`.

## AccountController - JSON


* `Post /json/api/account/save` {
  "username" : "username",
  "password" : "password",
  "accountNo" : "accountNo",
  "balance" : balance,
  "iban" : "iban"
  }
  The user can create an account using this request body.
* `Get /json/api/account/find/no`
  The user can view their account using the `accountNo`.
* `Get /json/api/account/find/username`
  The user can view their account using the `username`.
* `Get /json/api/account/find/all`
  Only admins can view all accounts.
* `Post /json/api/account/add/balance`
  The user can deposit balance into their account using the `username` and `balance` amount.
* `Delete /json/api/account/delete`
  The user can delete their account using their `username`.
* `Delete /json/api/account/delete/all`
  Only admins can delete all accounts.

## PaymentController - JSON


* `Post /json/api/payment/pay`
  The user can initiate the payment process with a request body like this. After performing several checks, if all values are correct and complete, the payment will be processed. If any value is incorrect or missing, an error will be thrown.

## ⚠️ JWT Token Usage


All JSON-based operations require the use of **JWT (JSON Web Token)**. After successfully logging in, users must include their JWT token in the `Authorization` header of every API request. If the token expires, a new JWT token can be obtained using the **Refresh Token**.

Example:

* `Authorization: Bearer <JWT_TOKEN_HERE>`

## RegisterController - Thymeleaf

* URL: `http://localhost:6767/register`
  Users can register on this page by filling out the form here.


## LoginController - Thymeleaf

* URL: `http://localhost:6767/auth/login`
  Users can log in with their username and password on this page.


## DashboardController - Thymeleaf

* URL: `http://localhost:6767/dashboard`
  Users can view the JWT token and refresh token information on this page.


## CategoryController - Thymeleaf

* URL: `http://localhost:6767/admin/category/save/form`
  Only admins can create a category using the form on this page.

* URL: `http://localhost:6767/admin/show/all/categories`
  Users can view all categories on this page.

* URL: `http://localhost:6767/admin/category/delete/form`
  Only admins can delete a category using the form on this page.


## ProductController - Thymeleaf

* URL: `http://localhost:6767/admin/product/save/form`
  Only admins can create a product using the form on this page.

* URL: `http://localhost:6767/admin/show/all/products`
  Users can view all products on this page.

* URL: `http://localhost:6767/admin/product/delete/form`
  Only admins can delete a product using the form on this page.
