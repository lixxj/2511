package bool;

public class OrNode extends BooleanNode {

    public OrNode(BooleanNode e1, BooleanNode e2) {
        super(e1, e2);
    }

    @Override
    public boolean evaluate() {
        return getLeft().evaluate() || getRight().evaluate();
    }

    @Override
    public String prettyPrint() {
        return "(OR" + getLeft().prettyPrint() + getRight().prettyPrint() + ")";
    }
}