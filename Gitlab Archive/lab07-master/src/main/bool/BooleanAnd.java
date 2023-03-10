package bool;

public class BooleanAnd extends BooleanNode {
    private BooleanNode a;
    private BooleanNode b;

    public BooleanAnd(BooleanNode a, BooleanNode b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public boolean evaluate() {
        return (a.evaluate() && b.evaluate());
    }

    @Override
    public String prettyPrint() {
        return "(AND" + a.prettyPrint() + b.prettyPrint() + ")";
    }
}