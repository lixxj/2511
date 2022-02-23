package examRefactoring;

import java.util.List;

public class Coles extends SupermarketCheckoutSystem {
    
    @Override
    public String getCardName() { return "flybuys"; }

    @Override
    public void scanItems(List<Item> items) {
        // Supermarkets have restrictions on the number of items allowed
        if (items.size() > 20) {
            System.out.println("Too many items.");
        }
        super.scanItems(items);
    }

    @Override
    public void printReceipt(List<Item> items) {
        System.out.println("Today at Coles you purchased the following:");   
        for (Item item : items) {
            System.out.println("- " + item.getName() + " : $" + item.getPrice());
        }        
    }
}
