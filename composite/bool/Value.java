package bool;

public class Value implements Expression {
    private boolean condition;

    public Value(boolean condition) {
        this.condition = condition;
    }

    @Override
    public boolean evaluate() {
        return condition;
    }

    @Override
    public String prettyPrint() {
        return " " + condition;
    }
}
