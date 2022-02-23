package bool;

public class AndNode extends BooleanNode {

    public AndNode(BooleanNode e1, BooleanNode e2) {
        super(e1, e2);
    }

    @Override
    public boolean evaluate() {
        return getLeft().evaluate() && getRight().evaluate();
    }

    @Override
    public String prettyPrint() {
        return "(AND" + getLeft().prettyPrint() + getRight().prettyPrint() + ")";
    }
}