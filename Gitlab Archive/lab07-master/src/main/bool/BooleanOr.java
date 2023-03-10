package bool;

public class BooleanOr extends BooleanNode {
    private BooleanNode a;
    private BooleanNode b;

    public BooleanOr(BooleanNode a, BooleanNode b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public boolean evaluate() {
        return (a.evaluate() || b.evaluate());
    }

    @Override
    public String prettyPrint() {
        return "(OR" + a.prettyPrint() + b.prettyPrint() + ")";
    }
}