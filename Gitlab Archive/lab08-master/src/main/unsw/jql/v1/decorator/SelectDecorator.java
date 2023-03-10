package unsw.jql.v1.decorator;

import java.util.function.Function;

import unsw.jql.v1.TableView;

/*
 * For the SelectDecorator consider that the types of the input inner and output 
 * (next for iteration, and toTable for aggregation) will not necessarily be of the same type. 
 * You will need to modify the generic parameters of OperationDecorator 
 * and hence the other Decorators for this to work.
 */

public class SelectDecorator<E, R> extends OperationDecorator<E, R> {

    private Function<E, R> s;
    /**
     * Map a table view to another table view.
     * 
     * Each item/record is mapped through the provided selector.
     * 
     * An example would be `new SelectDecorator(view, (fruit) -> fruit.age()))`
     */
    public SelectDecorator(TableView<E> inner, Function<E, R> selector) {
        super(inner);
        s = selector;
    }

    @Override
    public boolean hasNext() {
        return super.hasNext();
    }

    @Override
    public E next() {
        return super.nextElement();
    }

    public R next(int i) {
        return s.apply(super.nextElement());
    }
}