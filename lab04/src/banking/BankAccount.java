package banking;

/**
 * @invariant balance >= 0 
 */

public class BankAccount {

    private int balance;

    public BankAccount(int initialBalance) {
        this.balance = initialBalance;
    }

    public BankAccount() {
        this.balance = 0;
    }

    /**
     * @pre amount > 0
     * @post balance = old balance + amount
     */
    public void addDeposit(int amount) {
        this.balance += amount;
    }

    /**
     * @param amount - money that is being withdrawn from the bank account
     * @pre 0 < amount <= balance
     * @post balance = old balance - amount    
     */
    public void makeWithdrawal(int amount) {
        this.balance -= amount;
    }
}