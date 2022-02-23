# Synchronisation

How does your code protect against the following cases;

## Double Processing Items & Skipping Items
Items are split evenly amongst threads and loop is only accessed by one thread at a time so no risk of double processing / skipping items.

## Races (the third case)
Thread synchronisation on the iterator (which is shared amongst all threads) allows the code block to be executed atomically. All other threads attempting to enter the synchronized block are blocked until the thread inside the synchronized block exits the block

# Test Case 

How does your test case show off the bug.

# Performance

Is it faster?  If so explain why, if it isn't faster explain why it isn't.
In both cases consider cheap reduction functions like add (`x, y -> x + y`) and more complicated reduction functions that might take 1-2s to complete each time (i.e. a sleep inside the reduction function).

    Creates more work for OS / increased runtime due to the overhead of creating the threads and switching between them. 


# My Questions

- How can thread safety be implemented using a Singleton pattern in this case?