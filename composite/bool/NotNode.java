package bool;

public class NotNode extends BooleanNode {

    public NotNode(BooleanNode e1) {
        super(e1, null);
    }

    @Override
    public boolean evaluate() {
        return !getLeft().evaluate();
    }

    @Override
    public String prettyPrint() {
        return "(NOT"+ getLeft().prettyPrint() + ")";
    }
}
