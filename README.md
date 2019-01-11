# Package Service

The package service is responsible for managing packages which aggregate multiple products.

## API

### Create a new Package  
**URL** : `/v1/package`  
**Method** : `POST`  
**Success code** : `201 CREATED`  
**Example**  
POST `/v1/package`  
```json
{
	"name": "test",
	"description" : "DESC",
	"products" : ["VqKb4tyj9V6i"]
}
```
Returns 201  

### Update a Package  
**URL** : `/v1/package/{packageId}`  
**Method** : `PUT`  
**Success code** : `202 ACCEPTED`  
**Example**  
PUT `/v1/package/{packageId}`  
```json
{
	"name": "test 2",
	"description" : "DESC",
	"products" : ["xxxxxx"]
}
```
Returns 202  

### Retrieve a package  
**URL** : `/v1/package/{packageId}`  
**Method** : `GET`  
**Parameters** : currencyCode (defaults to USD)  
**Success code** : `200 OK`  
**Example**  
GET `/v1/package/1?currencyCode=GBP`  
Returns 200  
```json
{
    "id": 1,
    "name": "test",
    "description": "DESC",
    "products": [
        "VqKb4tyj9V6i"
    ],
    "currency": "EUR",
    "totalPriceInCurrency": 997
}
```

### Retrieve all packages  
**URL** : `/v1/package`  
**Method** : `GET`  
**Parameters** : currencyCode (defaults to USD)  
**Success code** : `200 OK`  
**Example**  
GET `/v1/package?currencyCode=GBP`  
Returns 200  
```json
[
     {
         "id": 4,
         "name": "test",
         "description": null,
         "products": [],
         "currency": "USD",
         "totalPriceInCurrency": 0
     },
     {
         "id": 1,
         "name": "test",
         "description": "DESC",
         "products": [
             "VqKb4tyj9V6i"
         ],
         "currency": "USD",
         "totalPriceInCurrency": 1149
     }
 ]
```
 
### Delete a package  
**URL** : `/v1/package/{packageId}`  
**Method** : `DELETE`  
**Success code** : `202 ACCEPTED`  
**Example**  
DELETE `/v1/package/1`  
Returns 202  

## Running the service  
To build run `mvn clean install`  
To run locally run `mvn -pl application spring-boot:run`  
To run in production `java -jar application/target/application-GIT-SNAPSHOT.jar  

