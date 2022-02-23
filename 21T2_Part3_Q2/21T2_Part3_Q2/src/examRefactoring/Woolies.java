package examRefactoring;

import java.util.List;

public class Woolies extends SupermarketCheckoutSystem {
    
    @Override
    public String getCardName() { return "Everyday Rewards"; }

    @Override
    public void scanItems(List<Item> items) {
        // Supermarkets have restrictions on the number of items allowed
        if (items.size() >= 55) {
            System.out.println("Sorry, that's more than we can handle in a single order!");
        }
        super.scanItems(items);
    }

    @Override
    public void printReceipt(List<Item> items) {
        System.out.print("Your purchase: ");
        for (int i = 0; i < items.size() - 1; i++) {
            System.out.print(items.get(i).getName() + ", ($" + items.get(i).getPrice() + "), ");
        }
        System.out.println(items.get(items.size() - 1).getName() + " ($" + items.get(items.size() - 1).getPrice() + ").");
    }

}
