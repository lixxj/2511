package part3q3.enums;

public enum BusinessRuleOperators {
    /**
     * Given A, and B are either integers or doubles
     * evalutes to true if A > B else false.
     * 
     * Should throw BusinessRuleException("Both arguments have to be numeric")
     * if either A or B isn't an integer or a double or if B isn't supplied.
     */
    GREATER_THAN,

    /**
     * Is a unary operator returns false if the argument given is either
     * null or a string consisting purely of spaces (or is empty) otherwise it returns true.
     * 
     * Hint: `string.isBlank()` will tell you if a string is empty/consists purely of spaces.
     * 
     * If the type is an integer/boolean/double it should always return true.
     * 
     * Ignores second argument if supplied.
     */
    IS_NOT_BLANK;
}
