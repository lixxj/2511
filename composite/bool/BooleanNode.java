package bool;

/**
 * A boolean BooleanNode.
 * @author Nick Patrikeos + Yash Raj
 * 
 * Feel free to change this to an abstract class/interface as you see fit.
 */
public abstract class BooleanNode implements Expression {
    private Expression e1;
    private Expression e2;

    public BooleanNode(Expression e1, Expression e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    public Expression getLeft() {
        return e1;
    }
    
    public Expression getRight() {
        return e2;
    }
}