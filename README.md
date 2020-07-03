# Afterpay - Fraud Detector
The repository contains the solution for the programming assignment of detecting Card Transactions Fraud
The application accepts a comma separated file that contains list of transactions.
A credit card transaction comprises the following elements.
- hashed credit card number
- timestamp - of format year-month-dayThour:minute:second
- amount - of format dollars.cents
Transactions are to be received in a file as a comma separated string of elements, one per line,
eg: 10d7ce2f43e35fa57d1bbf8b1e2, 2014-04-29T13:15:54, 10.00
A credit card will be identified as fraudulent if the sum of amounts for a unique hashed credit
card number over a 24-hour sliding window period exceeds the price threshold.
### Prerequisites

Java 1.8

Maven


### Build
open a command console into the afterpay folder and run the following
```
mvn clean package
```
this will run all the unit tests then compile the class file and build the jar file under /target directory

### Running the tests

automated tests are under /src/test/java/com/afterpay/frauddetector 
```
mvn test
```
## Running the application

the fraud-detector-0.1.0.jar under target folder could be run with 2 arguments :
an amount threshold
a file containing a list of transaction ( see transactions_sample file under src/test/resources folder)

for example :
open a command console into the cardFraudDetector folder and run the following
```
java -jar ./target/fraud-detector-0.1.0.jar 150.00 src/test/resources/transactions_sample.csv
```

main class is Main.java

Project is built using Maven, in order to run the project you need to have maven installed, please run:
mvn package
to create the jar, then run 
java -jar target/fraud-detector-0.1.0.jar <params>