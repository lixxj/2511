package bool;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

public class NodeFactoryTest {
    @Test
    public void testNodeFactory() {

        // { "node": "and", "subnode1" : {
        //     "node": "or", "subnode1": {
        //         "node": "value", "value": true},
        //                 "subnode2": {
        //         "node": "value", "value": false}},
        //                 "subnode2": {
        //     "node": "value", "value": true} }

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

        System.out.println(and.toString());

        assertDoesNotThrow(() -> {
            assertEquals("(AND(OR true false) true)", 
            BooleanEvaluator.prettyPrint(NodeFactory.createBooleanCompositeExpression(and)));
        });
    }

    public static void main(String[] args) {
        JSONObject and = new JSONObject();
        and.put("node", "and");
        JSONObject or = new JSONObject();
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

        System.out.println(and.toString());
    }
}
