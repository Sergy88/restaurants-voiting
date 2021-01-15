REST "restaurant-voting" Project
===============================
Simple REST service with extensive domain model with authentication and authorization (Spring Security).
Persistence into DB implemented with JPA Hibernate.

Tech stack: Maven, Spring MVC, Security, JPA(Hibernate), REST(Jackson), 
Java 8 Stream and Time API, storage -> HSQLDB.

Application implements restaurants ranking functionality.

Application supports 2 types of users: admin and regular users.

Admin can input a restaurant and it's lunch menu of the day (2-5 items usually, just a dish name and price).
Users can leave their vote for each restaurant (with value of 1 to 10.). They can change their voce same day but only before 11 a.m.
Users can check particular restaurant and it`s average rank by rest request.

# A relative path for admin types of operations: "/rest/admin"

## Update menu for particular restaurant:

curl --location --request PUT 'http://localhost:8080/rest/admin/restaurant/10020/menu' \
--header 'Authorization: Basic ZXVnZW5lQG1haWwuY29tOnBhc3N3b3Jk' \
--header 'Content-Type: application/json' \
--data-raw '    {
"Винегрет": 70400,
"Кабаби": 135000,
"Бигос": 120400,
"Бисквит": 50600
}'

10020 - id of restaurant to change menu in.

## Create a new restaurant:

curl --location --request POST 'http://localhost:8080/rest/admin/restaurants' \
--header 'Authorization: Basic ZXVnZW5lQG1haWwuY29tOnBhc3N3b3Jk' \
--header 'Content-Type: application/json' \
--data-raw '{
"name": "StarDucks",
"menu": {
"Latte": 22500,
"Espresso": 15000,
"Цикорий": 13200,
"Паса эль Карбонара": 50000
}
}'

## Update restaurant that already exists:

curl --location --request PUT 'http://localhost:8080/rest/admin/restaurants/10048' \
--header 'Authorization: Basic ZXVnZW5lQG1haWwuY29tOnBhc3N3b3Jk' \
--header 'Content-Type: application/json' \
--data-raw '{
"name": "StarDucks-coffe555",
"menu": {
"Latte": 22500,
"Espresso": 15000,
"Цикорий": 13200,
"Паса эль Карбонара": 40000
}
}'

# A relative path for admin types of operations: "/rest/votes"

## Check restaurant with its rank:

curl --location --request GET 'http://localhost:8080/rest/restaurants/10020' \
--header 'Authorization: Basic ZnJhbmtAbWFpbC5jb206cGFzc3dvcmQ='

## Get all your votes as an authorized user

curl --location --request GET 'http://localhost:8080/rest/votes/' \
--header 'Authorization: Basic ZnJhbmtAbWFpbC5jb206cGFzc3dvcmQ='

## Leave vote for a restaurant:

curl --location --request POST 'http://localhost:8080/rest/votes/10020/4' \
--header 'Authorization: Basic ZnJhbmtAbWFpbC5jb206cGFzc3dvcmQ='

Explanation:
http://localhost:8080/rest/votes/{restaurant id}/{restaurant rank}

Response Headers
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Pragma: no-cache
Expires: 0
X-XSS-Protection: 1; mode=block
X-Frame-Options: DENY
X-Content-Type-Options: nosniff
Content-Type: application/json
Transfer-Encoding: chunked
Date: Fri, 15 Jan 2021 16:32:03 GMT
Keep-Alive: timeout=20
Connection: keep-alive
Response Body
{
"id":10036,"rating":4,
"date":"2021-01-15 16:32",
"restaurant":{"id":10020}
}

## Delete your voce by id as authorized user:
curl --location --request DELETE 'http://localhost:8080/rest/votes/10053' \
--header 'Authorization: Basic ZnJhbmtAbWFpbC5jb206cGFzc3dvcmQ='

## Update your voce by id as authorized user (before 11AM same day):
curl --location --request PUT 'http://localhost:8080/rest/votes/10027' \
--header 'Authorization: Basic ZnJhbmtAbWFpbC5jb206cGFzc3dvcmQ=' \
--header 'Content-Type: application/json' \
--data-raw '{
"id": 10027,
"rating": 0,
"restaurant": {
"id": 10023,
"name": "Пляж"
}
}'

# A registration example

## Request
curl --location --request POST 'http://localhost:8080/rest/profile/register' \
--header 'Content-Type: application/json' \
--data-raw '{
"name": "alexey",
"email": "alexey@mail.com",
"password": "password"
}'

## Response
HTTP/1.1 201 Created  
Location: http://localhost:8080/rest/profile  
Cache-Control: no-cache, no-store, max-age=0, must-revalidate  
Pragma: no-cache  
Expires: 0  
X-XSS-Protection: 1; mode=block  
X-Frame-Options: DENY  
X-Content-Type-Options: nosniff  
Content-Type: application/json  
Transfer-Encoding: chunked  
Date: Tue, 05 Jan 2021 08:57:42 GMT  
Keep-Alive: timeout=20  
Connection: keep-alive  
{"id":10055,"name":"alexey","email":"alexey@mail.com","password":"{bcrypt}$2a$10$j20sFWSjSnaGggCscYIv0O7D6JdAMzT6jkFKPyhb1zI4ZkKMrtg/y","enabled":true,"registered":"2021-01-05 11:57","roles":["USER"]}
