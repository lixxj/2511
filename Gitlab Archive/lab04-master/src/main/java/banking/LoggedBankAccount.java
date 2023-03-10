package banking;

public class LoggedBankAccount extends BankAccount {
    // Every deposit and withdrawal must make a log of the action
    private String log;

    public LoggedBankAccount(Double balance) {
        super(balance);
        this.log = "";
    }

    /**
     * @pre amount > 0
     * @post currentBalance = old currentBalance + amount
     */
    public void deposit(Double amount) {
        super.deposit(amount);
        log += "deposited " + amount + "\n";
    }

    /**
     * @param amount - money that is being withdrawn from the bank account
     * @pre 0 < amount <= currentBalance
     * @post currentBalance = old currentBalance - amount    
     */
    public void withdraw(int amount) {
        super.withdraw(amount);
        log += "Withdrawn " + amount + "\n";
    }

    public String getLog() {
        return log;
    }

}
