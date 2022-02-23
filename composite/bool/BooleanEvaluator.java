package bool;

public class BooleanEvaluator {

    public static boolean evaluate(Expression expression) {
        return expression.evaluate();
    }

    public static String prettyPrint(Expression expression) {
        return expression.prettyPrint();
    }

}