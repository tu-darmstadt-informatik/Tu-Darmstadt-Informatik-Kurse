package ex02;

import java.util.Map;

public class Not implements BooleanExpression {
    private final BooleanExpression op;

    public Not(BooleanExpression _op) {
        op = _op;
    }

    public BooleanExpression getOp() {
        return op;
    }

    @Override
    public String toPostfixString() {
        return String.format("%s !", op.toPostfixString());
    }

    /**
     * @return returns the BooleanExpression-Object in Prefix-Format as String
     */
    @Override
    public String toPrefixString(){
        return String.format("! %s", op.toPrefixString());
    }

    /**
     * if the operand is an And/Or Object, the operand will be in parenthesis
     * @return returns the BooleanExpression-Object in Infix-Format as String
     */
    @Override
    public String toInfixString(){
        if(op instanceof Or || op instanceof And)
            return String.format("! %s", String.format("(%s)", op.toInfixString()));
        else return String.format("! %s", op.toInfixString());
    }

    @Override
    public boolean evaluate(Map<String, Boolean> argumentMap) {
        return !op.evaluate(argumentMap);
    }

    @Override
    public BooleanExpression toDNF() {
        if (op.isVar()) {
            return this;
        } else if (op.isNot()) {
            return op.asNot().op.toDNF();
        } else if (op.isAnd()) {
            And op1AsAnd = op.asAnd();

            BooleanExpression orLeftOp = new Not(op1AsAnd.getLeftOp()).toDNF();
            BooleanExpression orRightOp = new Not(op1AsAnd.getRightOp()).toDNF();

            return new Or(orLeftOp, orRightOp);
        } else if (op.isOr()) {
            Or op1AsOr = op.asOr();

            BooleanExpression andLeftOp = new Not(op1AsOr.getLeftOp());
            BooleanExpression andRightOp = new Not(op1AsOr.getRightOp());

            return new And(andLeftOp, andRightOp).toDNF();
        } else {
            // This block should not be reached, but anyway
            throw new IllegalArgumentException("Op1 is neither of type Var, Not, And nor Or!");
        }
    }

    @Override
    public boolean isNot() {
        return true;
    }

    @Override
    public Not asNot() {
        return this;
    }
}
