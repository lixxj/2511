package bool;

public class BooleanLeaf extends BooleanNode{
    private boolean condition;

    public BooleanLeaf(boolean condition) {
        this.condition = condition;
    }

    @Override
    public boolean evaluate() {
        return condition;
    }

    @Override
    public String prettyPrint() {
        if (condition) {
            return " true";
        }
        else {
            return " false";
        }
    }
}
