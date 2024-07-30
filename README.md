## Detail Description
1. In the below repository <br>
1.1 Main class name to run the application was give as JavaAssignmentApplication 

2. The flow so basically I have created 2 API's one is get and another is post <br>
2.1 where using get I will fetch the details of the customer and the totalRewardsPoints recieved <br>
2.2 And using Post I will insert the records in the database

3. Here the database I have used is h2-console database and table name is transactions and database name is rewardpoints <br>
3.1 Database credentials are mentioned in the application.properties file as the h2 database if the class is rerun again we have to login every time so the password is not an issue

4. Here we have used the entity as Transaction which helps us to connect to database and to use the functions like save, find for that I have created Repository Interface -> TransactionRepository

5. I have also created a service and service implementation class to maintain the business logic

6. Now the logic that has been used for generating the reward points <br>
6.1 The logic is written in method -> calculateRewardPoints(Double transactionAmount) <br>
6.2 First, we check if the transaction amount is over $100. If it is, we calculate the amount spent over $100 by subtracting 100 from the transaction amount. <br>
6.3 We then multiply this amount by 2 to get the points for the amount spent over $100. <br>
6.4 Next, we check if the transaction amount is between $50 and $100. If it is, we calculate the amount spent between $50 and $100 by subtracting 50 from the minimum of the transaction amount and 100 (to ensure we don't go over $100).<br>
6.5 We then add this amount to the points to get the total points for the transaction.
Finally, we return the total points. <br>
6.6 After returning we are using this method in two places one is while creating the records and one is for fetching the records <br>
6.7 In fetching the records we are finding it using customer id and iterating all the customerId and displaying the response according <br>

7. Now the logic for inserting the records <br>
7.1 Here we are normally taking the inputs such as customerId, amount and the transactiondate <br>
7.2 After that we are using the calculateRewardPoints method to generate the reward points <br>
7.3 After that we are storing it in the transactions table <br>
7.4 the logic is implemented in controller class -> createTransaction method which has the service implementation to store the records in the database. 

8. For handling the response for get request we have created a model class RewardPointResponse which used to display the map of string and integer for the monthly points and the total point for that particular customer

9. The API's that are Implemented <br>
9.1 getAllCustomerTransactions -> this GET API will return all the customerId along with the rewardPoint earned in monthly basis <br>
9.2 getRewardPoints -> this GET API takes customerId as a input parameter and fetch the records for that particular customer <br>
9.3 createTransaction -> this POST API will take customerId, transactionDate and Amount and will return the response as records/ transaction successfully inserted.


## Below is the Examples
## Sample input data records 
```
insert into transactions(customer_id,transaction_date,amount) values(1,'2020-01-01',120);
insert into transactions(customer_id,transaction_date,amount) values(1,'2020-01-15',80);
insert into transactions(customer_id,transaction_date,amount) values(1,'2020-02-15',100);
insert into transactions(customer_id,transaction_date,amount) values(2,'2020-02-01',150);
insert into transactions(customer_id,transaction_date,amount) values(3,'2020-03-01',200);
insert into transactions(customer_id,transaction_date,amount) values(3,'2020-03-01',150);
insert into transactions(customer_id,transaction_date,amount) values(4,'2020-04-01',200);
```
1.1 H2 console which I have used for database 
url -> http://localhost:8080/h2-console/ <br>

## Get Request 
<b> Get all Records </b>
url -> http://localhost:8080/api/reward-points/getAllCustomer <br>
response ->
```
[
    {
        "customerId": 1,
        "monthlyPoints": {
            "feb": 50,
            "jan": 70
        },
        "totalPoints": 120
    },
    {
        "customerId": 2,
        "monthlyPoints": {
            "feb": 100
        },
        "totalPoints": 100
    },
    {
        "customerId": 3,
        "monthlyPoints": {
            "mar": 300
        },
        "totalPoints": 300
    },
    {
        "customerId": 4,
        "monthlyPoints": {
            "apr": 200
        },
        "totalPoints": 200
    }
]
```
<b> Get Using Single customerID </b><br>
url -> http://localhost:8080/api/reward-points/1 <br>
response ->
```
{
    "monthlyPoints": {
        "feb": 50,
        "jan": 70
    },
    "totalPoints": 120
}
```


## Post request 
<i> Note -> for the post request I used an application like postman to enter the request input and send the details </i><br>
url -> http://localhost:8080/api/reward-points/createTransactions <br>
raw json request ->
```
{
  "customerId": 1,
  "amount": 100.0,
  "transactionDate": "2022-01-01"
}
```
response
```
Transaction created successfully
```
## Validations/test scenarios
- <b> Get Request Validations </b> <br>
  2.1.1 If we pass input which is customer ID lesser than 1 <br>
  request url -> http://localhost:8080/api/reward-points/-1 <br>
  response
  ```
  Customer ID must be a positive number
  ```
  2.1.2 If we pass input as string <br>
  request url -> http://localhost:8080/api/reward-points/as <br>
  response 
  ```
  Invalid customer ID format
  ```
- <b>  Post Request Validations </b> <br>
    2.2.1 If we pass customer Id as negative value <br>
    request url - <a href="http://localhost:8080/api/reward-points/createTransactions">http://localhost:8080/api/reward-points/createTransactions</a> <br>
    request ->
    ```
    {
      "customerId": -1,
      "amount": 100,
      "transactionDate": "2001-02-20"
    }
    ```
    response 
    ```
    Customer ID cannot be a negative value
    ```
    2.2.2 If we pass amount lesser than zero <br>
    request url -> http://localhost:8080/api/reward-points/createTransactions <br>
    request ->
    ```
    {
      "customerId": 1,
      "amount": -1,
      "transactionDate": "08-22-2001"
    }
    ```
    response 
    ```
    Amount cannot be less than zero
    ```
    2.2.3 If we pass invalid format date <br>
    url -> http://localhost:8080/api/reward-points/createTransactions <br/>
    <i>Note -> correct format of date is -> 2001-06-12 (year-month-date [yyyy-MM-dd])</i><br>
    request ->
    ```
    {
      "customerId": 1,
      "amount": 20,
      "transactionDate": "08-22-2001"
    }
    ```
    response 
    ```
    Transaction date must be in the format yyyy-MM-dd
    ```
