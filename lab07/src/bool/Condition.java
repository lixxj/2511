package bool;

public class Condition implements BooleanNode {
    private boolean condition;

    public Condition(boolean condition) {
        this.condition = condition;
    }

    @Override
    public boolean evaluate() {
        return condition;
    }

    @Override
    public String prettyPrint() {
        if (condition) return " true";
        return " false";
    }
}
