package ex02;

import java.util.*;


public class And implements BooleanExpression {
    private final BooleanExpression leftOp;
    private final BooleanExpression rightOp;

    public And(BooleanExpression _leftOp, BooleanExpression _rightOp) {
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
        // equivalent to:
        // leftOp.toPostfixString() + " " + rightOp.toPostfixString() + " &"
        return String.format("%s %s &", leftOp.toPostfixString(), rightOp.toPostfixString());
    }

    /**
     * @return returns the BooleanExpression-Object in Prefix-Format as String
     */
    @Override
    public String toPrefixString(){
        return String.format("& %s %s", leftOp.toPrefixString(), rightOp.toPrefixString());
    }

    /**
     * if an operand is an Or-Object, the operand will be in parethesis
     * @return returns the BooleanExpression-Object in Infix-Format as String
     */
    @Override
    public String toInfixString(){
        String left;
        String right;

        if(leftOp instanceof Or) left = String.format("(%s)", leftOp.toInfixString());
        else left = leftOp.toInfixString();

        if(rightOp instanceof  Or) right = String.format("(%s)", rightOp.toInfixString());
        else right = rightOp.toInfixString();

        return String.format("%s & %s", left, right);
    }

    @Override
    public boolean evaluate(Map<String, Boolean> argumentMap) {
        return leftOp.evaluate(argumentMap) & rightOp.evaluate(argumentMap);
    }

    @Override
    public BooleanExpression toDNF() {
        BooleanExpression dnfLeftOp = leftOp.toDNF();
        BooleanExpression dnfRightOp = rightOp.toDNF();

        // If no dnf is of type Or
        if (!(dnfLeftOp instanceof Or || dnfRightOp instanceof Or)) {
            return new And(dnfLeftOp, dnfRightOp);
        }

        // Minimum one dnf is of type Or
        List<BooleanExpression> disjDnfLeftOp = dnfLeftOp.disjunctiveTerms();
        List<BooleanExpression> disjDnfRightOp = dnfRightOp.disjunctiveTerms();

        // Points to the head of the list
        // We don't care about the order of the result (it's computationally equivalent)
        BooleanExpression head = null;

        // Calculating all possible terms
        for (BooleanExpression term : disjDnfLeftOp) {
            for (BooleanExpression term2 : disjDnfRightOp) {
                And and = new And(term, term2);

                // In the first iteration current is null,
                // we set it to the last entry.
                if (head == null) {
                    head = and;

                // In all other iterations we chain the entries via an Or.
                } else {
                    head = new Or(and, head);
                }
            }
        }

        return head;
    }

    @Override
    public boolean isAnd() {
        return true;
    }

    @Override
    public And asAnd() {
        return this;
    }
}
