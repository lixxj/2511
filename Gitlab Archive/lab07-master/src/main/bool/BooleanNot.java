package bool;

public class BooleanNot extends BooleanNode {
    private BooleanNode b;

    public BooleanNot(BooleanNode b) {
        this.b = b;
    }

    @Override
    public boolean evaluate() {
        return !b.evaluate();
    }

    @Override
    public String prettyPrint() {
        return "(NOT"+ b.prettyPrint() + ")";
    }
}
