package bool;

public class BooleanEvaluator {

    public static boolean evaluate(BooleanNode expression) {
        return expression.evaluate();
    }

    public static String prettyPrint(BooleanNode expression) {
        return expression.prettyPrint();
    }

}