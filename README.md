## Detail Description
1. In the below repository 
1.1 Main class name to run the application was give as JavaAssignmentApplication 

2. The flow so basically I have created 2 API's one is get and another is put
2.1 where using get I will fetch the details of the customer and the totalRewardsPoints recieved
2.2 And using Post I will insert the records in the database

3. Here the database I have used is h2-console database and table name is transactions and database name is rewardpoints
3.1 Database credentials are mentioned in the application.properties file as the h2 database if the class is rerun again we have to login every time so the password is not an issue

4. Here we have used the entity as Transaction which helps us to connect to database and to use the functions like save, find for that I have created Repository Interface -> TransactionRepository

5. I have also created a service and service implementation class to maintain the business logic

6. Now the logic that has been used for generating the reward points 
6.1 The logic is written in method -> calculateRewardPoints(Double transactionAmount)
6.2 First, we check if the transaction amount is over $100. If it is, we calculate the amount spent over $100 by subtracting 100 from the transaction amount.
6.3 We then multiply this amount by 2 to get the points for the amount spent over $100.
6.4 Next, we check if the transaction amount is over $50. If it is, we calculate the amount spent between $50 and $100 by subtracting 50 from the minimum of the transaction amount and 100 (to ensure we don't go over $100).
6.5 We then add this amount to the points to get the total points for the transaction.
Finally, we return the total points.
6.6 After returning we are using this method in two places one is while creating the records and one is for fetching the records
6.7 In fetching the records we are finding it using customer id and iterating all the customerId and displaying the response according 

7. Now the logic for inserting the records
7.1 Here we are normally taking the inputs such as customerId, amount and the transactiondate
7.2 After that we are using the calculateRewardPoints method to generate the reward points 
7.3 After that we are storing it in the transactions table 
7.4 the logic is implemented in controller class -> createTransaction method which has the service implementation to store the records in the database.


8. For handling the response for get request we have created a model class RewardPointResponse which used to display the map of string and integer for the monthly points and the total point for that particular customer

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
url -> http://localhost:8080/h2-console/

## Get Request 
url -> http://localhost:8080/api/reward-points/1

response ->
```
{
    "monthlyPoints": {
        "feb": 50,
        "jan": 120
    },
    "totalPoints": 170
}
```

## Post request 
Note -> for the post request I used an application like postman to enter the request input and send the details 
url -> http://localhost:8080/api/reward-points/createTransactions

raw json request ->
```
{
  "customerId": 1,
  "amount": 100.0,
  "transactionDate": "2022-01-01"
}
```
response ->
Transaction created successfully

## Validations/test scenarios
4.1.1 In get Request
4.1.2 If we pass input which is customer ID lesser than 1

url -> http://localhost:8080/api/reward-points/-1

response ->
Customer ID must be a positive number

4.1.3 If we pass input as string 

url -> http://localhost:8080/api/reward-points/as

response ->
Invalid customer ID format

4.2.1 In Post Request 
4.2.2  If we pass customer Id as negative value
url - http://localhost:8080/api/reward-points/createTransactions

request ->
```
{
  "customerId": -1,
  "amount": 100,
  "transactionDate": "2001-02-20"
}
```
response ->
Customer ID cannot be a negative value

4.2.3 If we pass amount lesser than zero
url -> http://localhost:8080/api/reward-points/createTransactions

request ->
```
{
  "customerId": 1,
  "amount": -1,
  "transactionDate": "08-22-2001"
}
```
response ->
Amount cannot be less than zero

4.2.4 If we pass invalid format date
url -> http://localhost:8080/api/reward-points/createTransactions
Note -> correct format of date is -> 2001-06-12 (year-month-date [yyyy-MM-dd])

request ->
```
{
  "customerId": 1,
  "amount": 20,
  "transactionDate": "08-22-2001"
}
```

response ->
Transaction date must be in the format yyyy-MM-dd
