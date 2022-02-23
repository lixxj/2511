package bool;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;

public class BooleanTest {
    
    @Test
    public void testEvaluateAndNode() {
        assertEquals(true, BooleanEvaluator.evaluate(new AndNode(new Condition(true), new Condition(true))));
        assertEquals(false, BooleanEvaluator.evaluate(new AndNode(new Condition(false), new Condition(false))));
        assertEquals(false, BooleanEvaluator.evaluate(new AndNode(new Condition(true), new Condition(false))));
    }

    @Test
    public void testEvaluateOrNode() {
        assertEquals(true, BooleanEvaluator.evaluate(new OrNode(new Condition(true), new Condition(true))));
        assertEquals(false, BooleanEvaluator.evaluate(new OrNode(new Condition(false), new Condition(false))));
        assertEquals(true, BooleanEvaluator.evaluate(new OrNode(new Condition(true), new Condition(false))));
    }

    @Test
    public void testEvaluateNotNode() {
        assertEquals(false, BooleanEvaluator.evaluate(new NotNode(new AndNode(new Condition(true), new Condition(true)))));
        assertEquals(true, BooleanEvaluator.evaluate(new NotNode(new AndNode(new Condition(false), new Condition(false)))));
        assertEquals(true, BooleanEvaluator.evaluate(new NotNode(new AndNode(new Condition(true), new Condition(false)))));

        assertEquals(false, BooleanEvaluator.evaluate(new NotNode(new OrNode(new Condition(true), new Condition(true)))));
        assertEquals(true, BooleanEvaluator.evaluate(new NotNode(new OrNode(new Condition(false), new Condition(false)))));
        assertEquals(false, BooleanEvaluator.evaluate(new NotNode(new OrNode(new Condition(true), new Condition(false)))));
    }

    @Test
    public void testPrintAndNode() {
        assertEquals("(AND true true)", BooleanEvaluator.prettyPrint(new AndNode(new Condition(true), new Condition(true))));
        assertEquals("(AND false false)", BooleanEvaluator.prettyPrint(new AndNode(new Condition(false), new Condition(false))));
        assertEquals("(AND true false)", BooleanEvaluator.prettyPrint(new AndNode(new Condition(true), new Condition(false))));
    }

    @Test
    public void testPrintOrNode() {
        assertEquals("(OR true true)", BooleanEvaluator.prettyPrint(new OrNode(new Condition(true), new Condition(true))));
        assertEquals("(OR false false)", BooleanEvaluator.prettyPrint(new OrNode(new Condition(false), new Condition(false))));
        assertEquals("(OR true false)", BooleanEvaluator.prettyPrint(new OrNode(new Condition(true), new Condition(false))));
    }

    @Test
    public void testPrintNotNode() {
        assertEquals("(NOT(AND true true))", BooleanEvaluator.prettyPrint(new NotNode(new AndNode(new Condition(true), new Condition(true)))));
        assertEquals("(NOT(AND false false))", BooleanEvaluator.prettyPrint(new NotNode(new AndNode(new Condition(false), new Condition(false)))));
        assertEquals("(NOT(AND true false))", BooleanEvaluator.prettyPrint(new NotNode(new AndNode(new Condition(true), new Condition(false)))));

        assertEquals("(NOT(OR true true))", BooleanEvaluator.prettyPrint(new NotNode(new OrNode(new Condition(true), new Condition(true)))));
        assertEquals("(NOT(OR false false))", BooleanEvaluator.prettyPrint(new NotNode(new OrNode(new Condition(false), new Condition(false)))));
        assertEquals("(NOT(OR true false))", BooleanEvaluator.prettyPrint(new NotNode(new OrNode(new Condition(true), new Condition(false)))));
    }

}
