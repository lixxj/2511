package bool;

public class NotNode implements BooleanNode {
    private BooleanNode e1;

    public NotNode(BooleanNode e1) {
        this.e1 = e1;
    }

    @Override
    public boolean evaluate() {
        return !e1.evaluate();
    }

    @Override
    public String prettyPrint() {
        return "(NOT"+ e1.prettyPrint() + ")";
    }
}
