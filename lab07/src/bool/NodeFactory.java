package bool;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONArray;
import org.json.JSONObject;

public class NodeFactory {
    
    private static final String OR = "or";
    private static final String AND = "and";
    private static final String NOT = "not";

    // public static JSONObject getFileContent(String fileName) {
        
    //     String filePath = "src/bool/resources/" + fileName + ".json";
        
    //     try {
    //         String json = new String(Files.readAllBytes(Paths.get(filePath)));
    //         JSONObject json = (JSONParser)parser.parse(stringToParse);
    //         return json;
    //     } catch (Exception ex) {
    //         ex.printStackTrace();
    //     }

    //     // JSONParser parser = new JSONParser();
    //     // try {
    //     //     JsonElement jsonElement = 
    //     //     JsonParser.parseReader(new FileReader(filename));
    //     //     return jsonElement.getAsJsonObject();
    //     // } catch (FileNotFoundException e) {
    //     //     throw new Exception("Invalid filename");
    //     // }
    // }


    public static BooleanNode createBooleanCompositeExpression(JSONObject expression) throws Exception {

        if (expression == null) return null;

        String condition = expression.get("node").toString();

        // Leaf node
        if (expression.has("value")) {
            BooleanNode value = new Condition(expression.getBoolean("value"));
            return value;
        } 

        if (expression.has("subnode1")) {
            JSONObject condition1 = expression.getJSONObject("subnode1");
            BooleanNode value1 = createBooleanCompositeExpression(condition1);
            JSONObject value2 = null;
            switch (condition) {
                case NOT:
                    return new NotNode(value1);
                case AND:
                    if (!expression.has("subnode2")) break;
                    value2 = expression.getJSONObject("subnode2");
                    return new AndNode(value1, createBooleanCompositeExpression(value2));
                case OR:
                    if (!expression.has("subnode2")) break;
                    value2 = expression.getJSONObject("subnode2");
                    return new OrNode(value1, createBooleanCompositeExpression(value2));                   
            }
        }

        throw new Exception("ERROR: Invalid Conditions. Please enter a correct JSONObject!");
    }
}