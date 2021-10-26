# ATM Emulator
## Usage
You will need openjdk 17 installed to compile and run this app.

First, in terminal navigate to directory containing this file.

You can create jar file with command
gradlew build

```console
gradlew build
```
This will generate atm-emulator-0.0.1-SNAPSHOT.jar file under build/libs directory.
Now you can run this jar with command


```console
java -jar build/libs/atm-emulator-0.0.1-SNAPSHOT.jar
```

This command will run application cli, which you can start investigating by typing `help`

##Implementation Details
* h2 in memory database for persistence
* Spring shell library for cli interaction
* Spring boot for dependency management
* Spring validation for fast and easy constraint validation
* Junit 5 with mockito for unit testing - not happy with mockito, but it's late to change

##Design Choices
* balance is not stored within User, but in a separated Account objects
* Currently, any user has only one account, by decoupling allows this relationship to grow to one-to-many
* Deposits and transfers are modeled as Transactions between accounts
  * Deposits are transactions from bank account, to user account
  * Transfers are transactions from sender user account, to receiver user account
* All transactions are done via TransactionService by creating and persisting Transaction object.
  The idea is to make transactions trackable by persisting history of transactions.
* Notice that transaction are not coupled to users but user accounts, making users _non-financial_ entities.
* Only service layer is fully covered by unit tests.
* Integration/Functional test are needed by not provided...