package unsw.jql.v1.decorator;

import java.util.NoSuchElementException;

import unsw.jql.v1.TableView;

public class TakeDecorator<E> extends OperationDecorator<E, E> {

    private int itemsLeft;

    /**
     * Grab a subset of the table view
     * @param numberOfItems The number of items to take, the rest are ignored
     */
    public TakeDecorator(TableView<E> inner, int numberOfItems) {
        super(inner);
        this.itemsLeft = numberOfItems;
    }

    @Override
    public boolean hasNext() {
        return itemsLeft > 0 && super.hasNext();
    }

    @Override
    public E next() {
        if (hasNext()) {
            itemsLeft--;
            return super.nextElement();
        } else
            throw new NoSuchElementException();
    }

    @Override
    public int count() {
        return itemsLeft;
    }
}