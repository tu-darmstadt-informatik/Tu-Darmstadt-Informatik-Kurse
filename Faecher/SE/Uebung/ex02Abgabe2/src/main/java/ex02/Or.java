package ex02;

import java.util.List;
import java.util.Map;

public class Or implements BooleanExpression {
    private final BooleanExpression leftOp;
    private final BooleanExpression rightOp;

    public Or(BooleanExpression _leftOp, BooleanExpression _rightOp) {
        leftOp = _leftOp;
        rightOp = _rightOp;
    }

    public BooleanExpression getLeftOp() {
        return leftOp;
    }

    public BooleanExpression getRightOp() {
        return rightOp;
    }

    @Override
    public String toPostfixString() {
        return String.format("%s %s |", leftOp.toPostfixString(), rightOp.toPostfixString());
    }

    /**
     * @return returns the BooleanExpression-Object in Prefix-Format as String
     */
    @Override
    public String toPrefixString(){
        return String.format("| %s %s", leftOp.toPrefixString(), rightOp.toPrefixString());

    }

    /**
     * @return returns the BooleanExpression-Object in Infix-Format as String
     */
    @Override
    public String toInfixString(){
        return String.format("%s | %s", leftOp.toInfixString(), rightOp.toInfixString());
    }

    @Override
    public boolean evaluate(Map<String, Boolean> argumentMap) {
        return leftOp.evaluate(argumentMap) | rightOp.evaluate(argumentMap);
    }

    @Override
    public List<BooleanExpression> disjunctiveTerms() {
        List<BooleanExpression> listLeftOp = leftOp.disjunctiveTerms();
        List<BooleanExpression> listRightOp = rightOp.disjunctiveTerms();

        // All terms of operand 2 will be added at the bottom of the
        // list of operand 1
        listLeftOp.addAll(listRightOp);
        return listLeftOp;
    }

    @Override
    public BooleanExpression toDNF() {
        return new Or(leftOp.toDNF(), rightOp.toDNF());
    }

    @Override
    public boolean isOr() {
        return true;
    }

    @Override
    public Or asOr() {
        return this;
    }
}
