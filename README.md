# perfect-number
A RESTful application provides two REST services to check if a given number is perfect and find all perfect numbers in a given range.
## Technology stack

- JDK 8
- Spring Boot latest version
- JUnit 5.x
- Maven
- Jetty

## Installation

### Requirements

To build, test, and run this project, you need to have JDK 8 and Maven on your machine. 

### Build, Test, And Run

-  Clone the project into a directory on your machine (e.g. /somewhere) :
```
	# git clone https://github.com/mohsen-khaxar/perfect-number.git
```

-  Change directory to the project directory :
```
	# cd /somewhere/perfect-number
```

-  Run the following commands to build, test, and run :
```	
	# mvn clean compile
	# mvn test 
	# mvn spring-boot:run
```	

- Also you can run the following commands to run the application
```
	# mvn clean package
	# java -jar target/perfect-number-0.0.1-SNAPSHOT.jar
```
	
-	Open a browser and check the REST APIs :
```
	http://localhost:8080/rest/perfectNumber/29
	http://localhost:8080/rest/perfectNumbersInRange/1/1000
```
