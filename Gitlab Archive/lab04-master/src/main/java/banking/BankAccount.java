package banking;

/**
 * @invariant balance >= 0 
 */
public class BankAccount {
    private Double balance;

    public BankAccount(Double balance) {
        this.balance = balance;
    }

    public BankAccount() {
        this.balance = (double)0;
    }

    // Each bank account should have a current balance and methods implementing deposits and withdrawals
    
    /**
     * @pre amount > 0
     * @post balance = old balance + amount
     */
    public void deposit(Double amount) {
        this.balance += amount;
    }

    // Money can only be withdrawn from an account if there are sufficient funds
    
    /**
     * @param amount - money that is being withdrawn from the bank account
     * @pre 0 < amount <= balance
     * @post balance = old balance - amount    
     */
    public void withdraw(int amount) {
        this.balance -= amount;
    }

    public double getBalance() {
        return balance;
    }

}