## Explain why the code is consistent with the preconditions and postconditions.
Given that the preconditions are satisfied, the methods ensure that the postconditions are met. Responsibilities have been clearly assigned to different methods such that the design prevents redundant checks for errors when balance < 0 or the amount of deposit/withdrawal < 0. If the required conditions are not satisfied, the program can expect unexpected behaviour that would otherwise be captured in defensive programming (through additional error checking for pre-conditions). 

## Explain why balance >= 0 is a class invariant for both classes.
balance >= 0 is the class invariant for the BankAccount class since it is established during construction and constantly maintained by all calls to its public methods. Also, class invariants are inherited so the subclass LoggedBankAccount must satisfy the invariant of the parent class. 

## Are your class definitions consistent with the Liskov Substitution Principle?
Yes, since the LoggedBankAccount class is substitutable with its base class (BankAccount) and merely adds additional methods.