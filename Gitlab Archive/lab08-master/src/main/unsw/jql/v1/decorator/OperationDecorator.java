package unsw.jql.v1.decorator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

import unsw.jql.v1.Table;
import unsw.jql.v1.TableView;

public abstract class OperationDecorator<E, R> implements TableView<E> {

    private TableView<E> inner;

    public OperationDecorator(TableView<E> inner) {
        this.inner = inner;
    }

    @Override
    public boolean hasNext() {
        return inner.hasNext();
    }

    @Override
    public abstract E next();

    //public abstract R next(int i);

    public E nextElement() {
        //if (this instanceof SelectDecorator) return inner.next(1);
        return inner.next();
    }

    @Override
    public Iterator<E> iterator() {
        return this;
    }

    @Override
    public int count() {
        return inner.count();
    }

    @Override
    public Table<E> toTable() {
        List<E> list = new ArrayList<E>();
        this.forEachRemaining(list::add);
        return new Table<E>(list);
    }
    
}