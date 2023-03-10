package bool;

import org.json.JSONObject;

public class NodeFactory {

    public static BooleanNode createBooleanCompositeExpression(JSONObject expression) throws Exception {

        String condition = expression.get("node").toString();

        // Leaf node
        if (expression.has("value")) {
            BooleanNode value = new BooleanLeaf(expression.getBoolean("value"));
            return value;
        } 

        if (expression.has("subnode1")) {
            JSONObject condition1 = expression.getJSONObject("subnode1");
            BooleanNode value1 = createBooleanCompositeExpression(condition1);
            JSONObject value2 = null;
            switch (condition) {
                case "not":
                    return new BooleanNot(value1);
                case "and":
                    if (!expression.has("subnode2")) break;
                    value2 = expression.getJSONObject("subnode2");
                    return new BooleanAnd(value1, createBooleanCompositeExpression(value2));
                case "or":
                    if (!expression.has("subnode2")) break;
                    value2 = expression.getJSONObject("subnode2");
                    return new BooleanOr(value1, createBooleanCompositeExpression(value2));                   
            }
        }
        throw new Exception("ERROR: Invalid JSONObject!");
    }

}