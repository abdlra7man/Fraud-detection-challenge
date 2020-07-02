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
