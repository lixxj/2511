package examRefactoring;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class CheckoutSystem {
    
    private SupermarketCheckoutSystem supermarket;

    // Could create a factory class here to read user input and create a corresponding supermarket
    // checkout system
    private CheckoutSystem(SupermarketCheckoutSystem supermarket) {
        this.supermarket = supermarket;
    }

    public static CheckoutSystem instance(SupermarketCheckoutSystem supermarket) {
        return new CheckoutSystem(supermarket);
    }

    public void checkout(List<Item> items, String paymentMethod, int paymentAmount, boolean receipt) {
        supermarket.checkout(items, paymentMethod, paymentAmount, receipt);
    }

    public static void main(String[] args) {
        List<Item> items = new ArrayList<Item>(Arrays.asList(
            new Item("Apple", 1),
            new Item("Orange", 1),
            new Item("Avocado", 5)
        ));

        SupermarketCheckoutSystem supermarket = new Coles();
        CheckoutSystem checkout = new CheckoutSystem(supermarket);
        checkout.checkout(items, "cash", 200, true);
    }

}