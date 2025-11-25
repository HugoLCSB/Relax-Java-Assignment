/**
 * Relax Gaming - Java clean code test
 *
 * For the purposes of this test, the candidate can assume that the code
 * compiles and that references
 * to other classes do what you would expect them to.
 *
 * The objective is for the candidate to list down the things in plain text
 * which can be improved in this class
 *
 * Good luck!
 *
 */

public class account {
    public String accountNumber;

    public account(String accountNumber) {
        // Constructor
        this.accountNumber = accountNumber;
    }

    public String getAccountNumber() {
        return accountNumber; // return the account number
    }

    public ArrayList getTransactions() throws Exception {
        try {
            List dbTransactionList = Db.getTransactions(accountNumber.trim()); // Get the list of transactions
            ArrayList transactionList = new ArrayList();
            int i;
            for (i = 0; i < dbTransactionList.size(); i++) {
                DbRow dbRow = (DbRow) dbTransactionList.get(i);
                Transaction trans = makeTransactionFromDbRow(dbRow);
                trans.setTimestamp(createTimestampAndExpiryDate(trans)[0]);
                trans.setExpiryDate(createTimestampAndExpiryDate(trans)[1]);
                transactionList.add(trans);
            }
            return transactionList;

        } catch (SQLException ex) {
            // There was a database error
            throw new Exception("Can't retrieve transactions from the database");
        }
    }

    public Transaction makeTransactionFromDbRow(DbRow row) {
        double currencyAmountInPounds = Double.parseDouble(row.getValueForField("amt"));
        float currencyAmountInEuros = new Float(currencyAmountInPounds * 1.10);
        String description = row.getValueForField("desc");
        // description = fixDescription(description);
        return new Transaction(description, currencyAmountInEuros); // return the new Transaction object
    }

    public String[] createTimestampAndExpirydate(Transaction trans) {
        String[] return1 = new String[] {};
        LocalDateTime now = LocalDateTime.now();
        return1[0] = now.toString();
        return1[1] = LocalDateTime.now().plusDays(60).toString();

        return return1;

    }

    public String fixDescription(String desc) {
        String newDesc = "Transaction [" + desc + "]";
        return newDesc;
    }

    // Override the equals method
    public boolean equals(Account o) {
        return o.getAccountNumber() == getAccountNumber(); // check account numbers are the same
    }
}

/*
 * What I see:
 * 
 * line 0: stating the obvious but the formatting is all wrong, any java
 * formatter changes right away the whole file, indents, comments spacing etc...
 * 
 * line 15: this class should be "Account" as per java naming conventions
 * 
 * line 16: this should be private for safety and final for immutability
 * we already have the public getter and everything
 * 
 * line 16: this might be nit-picky but i feel weird about this var name, i
 * think if its really a number it should be something like int, otherwise
 * maybe say accountId or something, but it could make sense in context
 * 
 * line 19: public methods should have a javadoc style comment, documenting
 * what the method does and its parameters return values and/or exceptions
 * instead of having a random comment inside the method
 * 
 * line 24: should always use "this." with class variables, both in terms of
 * readability and to ensure you are referencing the right var
 * 
 * line 24: this comment should be removed in favor of a javadoc for the method
 * 
 * line 27: should return the generic List<> istead of a specific implementation
 * the client of this contract shouldn't be concerned with which implementation
 * we choose
 * 
 * line 29: List should be declared with the type of the elements, say
 * "List<Transaction> list = (...)"
 * 
 * line 29: strange to have to trim an account number
 * 
 * line 29: aren't we missing some null checks here
 * 
 * line 32: variable i should be declared inside the for, its more ideomatic and
 * we don't use it anywhere else
 * 
 * line 33: this variable dbRow is only used once hence there's no need to
 * specifically declare it
 * 
 * line 33: if we had declared this list with the right type we wouldn't need to
 * cast here
 * 
 * line 35: if we have lots of setters we need to call to construct an object
 * maybe we should have a builder instead.
 * 
 * line 35: its a bad practive to have a function that does two different things
 * 
 * line 35: it looks like "createTimestampAndExpiryDate()", doesnt actually use
 * the "trans" parameter for anything so should be removed, but even if it did,
 * it's not at all a good practice
 * to send as a parameter the object of the setter you're calling, if
 * setTimeStamp() needs anything from the Transaction object it should
 * itself access that information otherwise its double reference and could
 * lead to all kinds of issues and human errors
 * like: trans1.setTimestampAndExpiryDate(trans2)
 * 
 * line 35: There should never be a function with multiple return types in java
 * either have a return object or separate it in two functions:
 * createTimestamp() and createExpiryDate()
 * 
 * line 41: This Catch ins't doing anything, and even worse its
 * silently swallowing the actual SQLException and throwing regular
 * Exception instead, when using a try catch either do some exception
 * handling inside it, log it, or otherwise it would potentially be
 * better to just have a throw SQLException on the method declaration
 * 
 * line 43: then rethrowing an exception you need to at least include the
 * original one otherwise you're loosing the stack trace and the information
 * like:
 * catch(Exception e){
 * throw new Exception("your message", e);
 * }
 * 
 * line 47: This should potentially be private or even an overloaded constructor
 * of the Transaction object itself
 * 
 * line 48: check for null return of row.getValueForField()
 * 
 * line 49: ultimately casting unsafely to double and float seems like a
 * potential propblem especially when dealing with money values, after a
 * quick search, saw that another solution could be to use BigDecimal
 * when doing getValueForField("amt")
 * and then use BigDecimal.floatValue() directly.
 * 
 * line 49: Here we are unboxing Float into float which could be an option but
 * seems unnecessary and deprecated, just cast directly (float) or use a static
 * method like Float.valueOf() and then do the unboxing.
 * 
 * line 49: I think multiplying by a static hardcoded value like 1.10 here is a
 * big problem, currency exchange rates are constantly changing, this should be
 * using some external api or another alternative to the value right.
 * 
 * line 50: description here is only used once, no need to declare variable
 * 
 * lini 51: don't save unused code with comments, creates clutter and decreases
 * readability
 * 
 * line 52: I've been noticing unnecessary comments, better use a javadoc
 * 
 * line 55: like i said before should refactor this into two separate functions
 * or if absolutely necessary return a dedicated type, with these two fields
 * 
 * line 56: declaring a zero length array and then trying to define elements
 * on it would throw Exception
 * 
 * line 57: variable now is being declared and only used once, either don't
 * declare it or use it on both places that need the current timestamp
 * 
 * line 59: hardcoded values like this .plusDays(60) are not at all a good
 * practice, they hurt readability and debugging, wither make it configurable
 * through a parameter on the function declaration or if absolutely constant
 * make a static final field at the top of the class and name it all caps
 * like:
 * private static final int EXPIRATION_AMOUNT_DAYS = 60;
 * 
 * Line 66: no need to declare newDesc since its only used once, would be
 * simpler like:
 * return "Transaction [" + desc + "]";
 * 
 * Line 66: concat like this is fine for small things, if bigger should use
 * StringBuilder or another solution
 * 
 * line 70: always include the annotation @Override which would catch things
 * like in this case what is happening is overloading and not overriding
 * because the parameter should be (Object o), and then we'd check the class
 * of that other object is what we want inside.
 * Also while checking for overriding the equals method I got reminded that
 * its also important to override the hashCode(), since two objects that
 * return true on the equals method should also have the same hashCode,
 * so in this case include the accountNumber in the hashcode somehow
 * maybe:
 * return this.getAccountNumber().hashCode();
 * 
 * line 72: should include this.getAccountNumber(); for readability and
 * correctness.
 * 
 * line 72: using == for string comparison is wrong, == compares reference
 * not value, should use .equals()
 * 
 */