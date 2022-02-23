package examRefactoring;

import java.util.List;

public abstract class SupermarketCheckoutSystem {

    private int amountPurchased = 0;

    /**
     * Template pattern --> sub methods either have default implementations 
     * or must be overriden in subclasses.
     * @param items
     * @param paymentMethod
     * @param paymentAmount
     * @param receipt
     */
    public void checkout(List<Item> items, String paymentMethod, int paymentAmount, boolean receipt) {
        // Welcome the user
        System.out.println("Welcome! Please scan your first item. If you have a " + getCardName() + " card, please scan it at any time.");
        
        // Scan the items
        scanItems(items);

        // Take the user's payment
        if (paymentAmount < amountPurchased) {
            System.out.println("Not enough $$$.");
            return;
        }

        if (paymentMethod.equals("cash")) {
            System.out.println("Paid $" + paymentAmount + " with $" + (paymentAmount - amountPurchased) + " change.");
        } else {
            paymentAmount = amountPurchased;
            System.out.println("Paid $" + paymentAmount + ".");
        }

        if (receipt) printReceipt(items);
        
    }

    public String getCardName() { return "rewards"; }

    public void scanItems(List<Item> items) {
        if (items.size() == 0) {
            System.out.println("You do not have any items to purchase.");
            return;
        }
        for (Item item : items) {
            amountPurchased += item.getPrice();
        }
    }

    public abstract void printReceipt(List<Item> items);

}
