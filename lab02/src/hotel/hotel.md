# Code Smells in Hotel (UNFINISHED)

- Several sections of the code in the subclasses for Room have repeated sections of code:
    - Methods book, removeBooking, changeBooking and toJSON are identical in each room subclass and can be abstracted to the Booking class since they are not relevant to the rooms
- Booking attributes do not have an access modifier 

## CHANGES
### Why the Room interface is better as an abstract class?
Since many of the methods defined in the Room interface are used identically in its subclasses, to reduce repetition, the Room interface is better suited as a superclass.
However since we do not want objects to be instantiated using the Room class, using an abstract superclass helps us ensure this does not happen.
### Other
- Polymorphism enables us to reuse large sections of the toJSON method in each room subclass
