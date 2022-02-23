## Authentication

1. Why do we need authentication/a login for applications such as gmail/myunsw/facebook?

> For network security reasons. User authentication verifies user credentials before the user is allowed into their account and helps protect sensitive information from falling into the wrong hands.

## SSO

1. What patterns can you see in the flow.

> State Pattern and Strategy Pattern

## State Pattern

1. What could you represent as a state system in this example?

> Each page represents a state of the system, which can be modelled using a state diagram.

2. Fill in the following state table

> Note: the cleaner way to represent the below table is through the use of a 'sub-state' (to handle locking/invalid login) but that just makes the below table even longer and we don't really talk about sub-states in enough detail in this course.  Also we could make the table a lot more compact but for your first table separating it out like this will be easier to think about.

For details on how InstaHam works check out Task 3)'s description.

```
+----------------------+----------------------+---------------------+---------------------+---------------------+-----------------+
|       Action         |   Condition          |      Home Page      |   Provider Select   |   Provider Login    |      Locked     |
+----------------------+----------------------+---------------------+---------------------+---------------------+-----------------+
| Visits App           | Cache Token Valid    |  Home (no movement) |                     |                     |                 |
|                      | Cache Token Invalid  |  Redirect to Home   |                     |                     |                 |
|                      | Cached but Locked    |    Go to Locked     |                     |                     |                 |
|----------------------|----------------------|---------------------|---------------------|---------------------|-----------------|
| 'Back Button' (null) |     Used Cache       |    No page (null)   |                     |                     |                 |
| 'Back Button' (null) |                      |                     |    No page (null)   |                     |                 |
| 'Back Button' (null) |                      |                     |                     |   Provider Select   |                 |
| 'Back Button' (null) |     Used Login       |    Provider Login   |                     |                     |                 |
| 'Back Button' (null) |                      |                     |                     |                     | Provider Select |
|----------------------|----------------------|---------------------|---------------------|---------------------|-----------------|
|  Decide on provider  |                      |                     |    Provider Login   |                     |                 |
|----------------------|----------------------|---------------------|---------------------|---------------------|-----------------|
|     Login attempt    |    Using Hoogle      |                     |                     |   Go to Home Page   |                 |
|     Login attempt    |   Using InstaHam     |                     |                     | Stay, but send email|                 |
|  Code from email     |   Validate Token     |                     |                     |   Go to Home Page   |                 |
|  Invalid login used  | Third time incorrect |                     |                     |  Go to Locked Page  |                 |
|  Correct login used  | *but* user is locked |                     |                     |  Go to Locked Page  |                 |
|----------------------|----------------------|---------------------|---------------------|---------------------|-----------------|
```

Login attempt actions assume that Hoogle and InstaHam are registered providers.

## Strategy Pattern

1. How could you use the strategy pattern in this example?

> Perhaps in organising third-party providers and how they interact with the application.

2. Give an example of why that's useful here, and the benefits of the strategy pattern over simply using `if `statements.

> Enables greater extensibility of software and future additions of providers become much simpler. Also, since a strategy pattern uses composition over inheritance, it allows for better decoupling between behaviour and the context class using its behaviour.

## Task 1) Cache

1. What did you modify to implement this?

> Added a map for each app that has been validated by a token. Helps specify the current browser state. 

## Task 2) Providers/Pages

1. What pattern/approach did you do to refactor `Hoogle` and providers in general.

> Strategy Pattern

2. What pattern/approach did you do to refactor how pages are managed / transitioned from/to.

> State Pattern

## Task 3) Testing your design 1: InstaHam

1. What pattern/approach did you do to finish the implementation of `InstaHam` and link it up to Browser.

> Again keep this short, and try to write it before you start coding.

## Task 4) Testing your design 2: Locking

1. What approach did you take here to implement locking, did you modify an existing pattern, did you use a new one?

> Again keep this short, and try to write it before you start coding.  Don't overcomplicate this!!  If the pattern is significantly longer than the simpler version of the code it's probably not the right fit.

2. How did you change your code to handle the complex case.

> Again keep this short, and try to write it before you start coding.
