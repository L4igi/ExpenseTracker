DROP TABLE IF EXISTS User;
CREATE TABLE User (
    id INT AUTO_INCREMENT,
    username VARCHAR(250) NOT NULL,

    CONSTRAINT PK_User PRIMARY KEY (id)
)AUTO_INCREMENT=0;

DROP TABLE IF EXISTS Account;
CREATE TABLE Account (
    accountID INT AUTO_INCREMENT,
    userID INT NOT NULL,
    accountType VARCHAR(250) NOT NULL,
    accountName VARCHAR(250) NOT NULL,

    CONSTRAINT PK_Account PRIMARY KEY (accountID),
    CONSTRAINT FK_Account_UserID FOREIGN KEY (userID) REFERENCES User(id)
);
DROP TABLE IF EXISTS Transaction;
CREATE TABLE Transaction (
    transactionID INT AUTO_INCREMENT,
    fromUser INT NOT NULL,
    fromAccount INT NOT NULL,
    toAccount INT NOT NULL,
    amount FLOAT NOT NULL,
    transactionType VARCHAR(250) NOT NULL,
    transactiondate DATE,
    transactiondescription VARCHAR(250),
    transactioncategory VARCHAR(250),

    CONSTRAINT PK_Transaction PRIMARY KEY (transactionID),
    CONSTRAINT FK_fromAccount FOREIGN KEY (fromAccount) REFERENCES Account(accountID),
    CONSTRAINT FK_toAccount FOREIGN KEY (toAccount) REFERENCES Account(accountID)
);

