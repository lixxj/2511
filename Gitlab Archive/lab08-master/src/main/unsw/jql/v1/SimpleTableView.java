package unsw.jql.v1;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/*
 * Task: Modify the SimpleTableView class so that it stores a copy of the records as well as the iterator. 
 * You will need to update the records copy as the iterator is updated in order for the records to remain consistent with the state of the iterator. 
 * Once you have done this, use your updated code to resolve the bug and pass the tests.
 */

public class SimpleTableView<E> implements TableView<E> {
    private Iterator<E> it;
    private List<E> records;
    int ir = 0;

    public SimpleTableView(Iterator<E> iterator, List<E> records) {
        this.it = iterator;
        this.records = records;
    }

    /*public void recordRemove(int index) {
        records.remove(index);
    }*/

    @Override
    public boolean hasNext() {
        /*if (records.size() > 0) return true;
        else return false;*/
        return it.hasNext();
    }

    @Override
    public E next() {
        //records.remove(0);
        //ir++;
        return it.next();
    }

    @Override
    public Table<E> toTable() {
        List<E> list = new ArrayList<E>();
        this.forEachRemaining(list::add);
        return new Table<E>(list);
    }

    @Override
    public Iterator<E> iterator() {
        // *technically* this is non standard
        // since this should reproduce a unique iterator each time
        // but for our sakes it's fine, since any operation on an
        // iterator will implicitly invalidate the inner iterators
        // invalidating its original context anyways.
        return this;
    }

    @Override
    public int count() {
        int count = 0;
        
        // TODO Task3 
        // the iterator has been used up
        /*while (it.hasNext()) {
            it.next();
            count++;
        }*/

        int irt = ir;
        while (irt < records.size()) {
            irt++;
            //it.next();
            count++;
        }

        return count;
    }
}
