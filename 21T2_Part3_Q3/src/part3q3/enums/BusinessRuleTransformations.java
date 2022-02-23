package part3q3.enums;

public enum BusinessRuleTransformations {
    /**
     * Given A, and B are either integers or doubles evaluates to A / B,
     * the result is always a double.
     * 
     * Should throw BusinessRuleException("Both arguments have to be numeric")
     * if either A or B isn't an integer or a double or if B isn't supplied.
     */
    DIVISION,

    /**
     * Given A, and B are either integers or doubles evaluates to A * B.
     * the result is always a double.
     * 
     * Should throw BusinessRuleException("Both arguments have to be numeric")
     * if either A or B isn't an integer or a double or if B isn't supplied.
     */
    MULTIPLICATION;
}
