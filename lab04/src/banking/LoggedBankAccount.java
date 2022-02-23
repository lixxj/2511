package banking;

import java.util.ArrayList;
import java.util.List;

public class LoggedBankAccount extends BankAccount {
    
    private List<String> accountLogBook;
    public LoggedBankAccount(int initialBalance) {
        super(initialBalance);
        this.accountLogBook = new ArrayList<>();
    }

    /**
     * @pre amount > 0
     * @post currentBalance = old currentBalance + amount
     */
    public void addDeposit(int amount) {
        super.addDeposit(amount);
        String entry = String.valueOf(accountLogBook.size());
        String money = String.valueOf(amount);
        accountLogBook.add("#" + entry + " : CR $" + money);
    }

    /**
     * @param amount - money that is being withdrawn from the bank account
     * @pre 0 < amount <= currentBalance
     * @post currentBalance = old currentBalance - amount    
     */
    public void makeWithdrawal(int amount) {
        super.makeWithdrawal(amount);
        String entry = String.valueOf(accountLogBook.size());
        String money = String.valueOf(amount);
        accountLogBook.add("#" + entry + " : DR $" + money);
    }
}
