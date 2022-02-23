package part3q3.enums;

public enum BusinessRuleGroupTypes {
    /**
     * Evaluates the two business rules supplied and if both are true evaluates to true
     * else it evaluates to false.
     */
    AND,

    /**
     * Evaluates the two business rules supplied and if either are true evaluates to true
     * else it evaluates to false.
     */
    OR;
}
