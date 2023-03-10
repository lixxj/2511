package bool;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

public class BoolTest {
    
    @Test
    public void externaltest1() {

        JSONObject and = new JSONObject();
        and.put("node", "and");
        JSONObject or = new JSONObject();
        or.put("node", "or");
        JSONObject innerSubnode1 = new JSONObject();
        innerSubnode1.put("node", "value");
        innerSubnode1.put("value", true);
        JSONObject innerSubnode2 = new JSONObject();
        innerSubnode2.put("node", "value");
        innerSubnode2.put("value", false);
        or.put("subnode1", innerSubnode1);
        or.put("subnode2", innerSubnode2);
        and.put("subnode1", or);
        JSONObject outerSubnode2 = new JSONObject();
        outerSubnode2.put("node", "value");
        outerSubnode2.put("value", true);
        and.put("subnode2", outerSubnode2);
        //System.out.println(and.toString());

        assertDoesNotThrow(() -> {
            assertEquals("(AND(OR true false) true)", BooleanEvaluator.prettyPrint(NodeFactory.createBooleanCompositeExpression(and)));
        });
    }

    @Test
    public void internaltestEvaluateAndNode() {
        assertEquals(true, BooleanEvaluator.evaluate(new BooleanAnd(new BooleanLeaf(true), new BooleanLeaf(true))));
        assertEquals(false, BooleanEvaluator.evaluate(new BooleanAnd(new BooleanLeaf(false), new BooleanLeaf(false))));
        assertEquals(false, BooleanEvaluator.evaluate(new BooleanAnd(new BooleanLeaf(true), new BooleanLeaf(false))));
    }

    @Test
    public void internaltestEvaluateOrNode() {
        assertEquals(true, BooleanEvaluator.evaluate(new BooleanOr(new BooleanLeaf(true), new BooleanLeaf(true))));
        assertEquals(false, BooleanEvaluator.evaluate(new BooleanOr(new BooleanLeaf(false), new BooleanLeaf(false))));
        assertEquals(true, BooleanEvaluator.evaluate(new BooleanOr(new BooleanLeaf(true), new BooleanLeaf(false))));
    }

    @Test
    public void internaltestEvaluateNotNode() {
        assertEquals(false, BooleanEvaluator.evaluate(new BooleanNot(new BooleanAnd(new BooleanLeaf(true), new BooleanLeaf(true)))));
        assertEquals(true, BooleanEvaluator.evaluate(new BooleanNot(new BooleanAnd(new BooleanLeaf(false), new BooleanLeaf(false)))));
        assertEquals(true, BooleanEvaluator.evaluate(new BooleanNot(new BooleanAnd(new BooleanLeaf(true), new BooleanLeaf(false)))));

        assertEquals(false, BooleanEvaluator.evaluate(new BooleanNot(new BooleanOr(new BooleanLeaf(true), new BooleanLeaf(true)))));
        assertEquals(true, BooleanEvaluator.evaluate(new BooleanNot(new BooleanOr(new BooleanLeaf(false), new BooleanLeaf(false)))));
        assertEquals(false, BooleanEvaluator.evaluate(new BooleanNot(new BooleanOr(new BooleanLeaf(true), new BooleanLeaf(false)))));
    }

    @Test
    public void internaltestPrintAndNode() {
        assertEquals("(AND true true)", BooleanEvaluator.prettyPrint(new BooleanAnd(new BooleanLeaf(true), new BooleanLeaf(true))));
        assertEquals("(AND false false)", BooleanEvaluator.prettyPrint(new BooleanAnd(new BooleanLeaf(false), new BooleanLeaf(false))));
        assertEquals("(AND true false)", BooleanEvaluator.prettyPrint(new BooleanAnd(new BooleanLeaf(true), new BooleanLeaf(false))));
    }

    @Test
    public void internaltestPrintOrNode() {
        assertEquals("(OR true true)", BooleanEvaluator.prettyPrint(new BooleanOr(new BooleanLeaf(true), new BooleanLeaf(true))));
        assertEquals("(OR false false)", BooleanEvaluator.prettyPrint(new BooleanOr(new BooleanLeaf(false), new BooleanLeaf(false))));
        assertEquals("(OR true false)", BooleanEvaluator.prettyPrint(new BooleanOr(new BooleanLeaf(true), new BooleanLeaf(false))));
    }

    @Test
    public void internaltestPrintNotNode() {
        assertEquals("(NOT(AND true true))", BooleanEvaluator.prettyPrint(new BooleanNot(new BooleanAnd(new BooleanLeaf(true), new BooleanLeaf(true)))));
        assertEquals("(NOT(AND false false))", BooleanEvaluator.prettyPrint(new BooleanNot(new BooleanAnd(new BooleanLeaf(false), new BooleanLeaf(false)))));
        assertEquals("(NOT(AND true false))", BooleanEvaluator.prettyPrint(new BooleanNot(new BooleanAnd(new BooleanLeaf(true), new BooleanLeaf(false)))));

        assertEquals("(NOT(OR true true))", BooleanEvaluator.prettyPrint(new BooleanNot(new BooleanOr(new BooleanLeaf(true), new BooleanLeaf(true)))));
        assertEquals("(NOT(OR false false))", BooleanEvaluator.prettyPrint(new BooleanNot(new BooleanOr(new BooleanLeaf(false), new BooleanLeaf(false)))));
        assertEquals("(NOT(OR true false))", BooleanEvaluator.prettyPrint(new BooleanNot(new BooleanOr(new BooleanLeaf(true), new BooleanLeaf(false)))));
    }
    
}
