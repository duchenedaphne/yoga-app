# Yoga-app by Savasana

🧎‍♂️ Reserve your next yoga session.

## 🛠 Software tools

- [Angular CLI](https://github.com/angular/angular-cli) version 14.2.1
- [Java 11](https://www.oracle.com/java/technologies/downloads/)
- Spring Boot
- [Maven](https://maven.apache.org/download.cgi)
- [MySQL](https://www.mysql.com/fr/downloads/)

## Start the project

Clone this repository :
> git clone https://github.com/duchenedaphne/yoga-app

### MySQL :

Create a database with the name `test`.

SQL script for creating the schema is available `ressources/sql/script.sql`

By default the admin account is:
- login: yoga@studio.com
- password: test!1234

### Back-end :  

Go inside folder :
> cd back

Add your database credentials in the application.properties file   
( Warning : environment variables won't work. ) :

>spring.datasource.username=${DB_USER}

>spring.datasource.password=${DB_PASSWORD}

Install the dependencies :
> mvn clean install

Launch the backend server with Spring Boot and Maven :  
> mvn spring-boot:run

#### Test
Launch and generate the jacoco code coverage:
> mvn clean test

The report is available in the `target` folder :
> target/site/jacoco/index.html

### Front-end :

Go inside folder:
> cd front

Install the dependencies :
> npm install

Launch the frontend :
> npm run start

Navigate to http://localhost:4200/

#### Tests
- Unit

Launch and generate the jest code coverage:
> npx jest --coverage
 
or

Launching test:

> npm run test

for following change:

> npm run test:watch

- E2E

Launching e2e test:

> npm run e2e

Generate coverage report (you should launch e2e test before):

> npm run e2e:coverage

#### Issues 

If you've got the 'Command failed Time Out' issue   
or if the Cypress app window doesn't show-up on your screen :

1- go to your cypress directory here :

> C:\Users\\{YOUR_USERNAME}\AppData\Local\Cypress\Cache\10.4.0\Cypress

2- Execute the Cypress.exe application,

3- Add this project's `front` folder to the cypress application,

4- For Windows : in your terminal execute those 2 next command lines :

> set CYPRESS_VERIFY_TIMEOUT=100000

> npx cypress verify

5- Execute the `npm run e2e` command line once again,

6- In the Cypress application window :  
Choose a Browser and Start E2E Testing.

## Ressources

### Postman collection

For Postman import the collection
> ressources/postman/yoga.postman_collection.json 

by following the documentation: 

https://learning.postman.com/docs/getting-started/importing-and-exporting-data/#importing-data-into-postman

## ✍ Testing scripts author
Daphné Duchêne
