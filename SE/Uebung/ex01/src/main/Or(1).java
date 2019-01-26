package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Or implements BooleanExpression {

    private final BooleanExpression left;
    private final BooleanExpression right;

    public Or(BooleanExpression left, BooleanExpression right) {
        this.left = left;
        this.right = right;
    }

    public BooleanExpression getLeftOp() {
        return left;
    }

    public BooleanExpression getRightOp() {
        return right;
    }

    @Override
    public String toPostfixString() {
        return (left.toPostfixString() + " " + right.toPostfixString() + " |");
    }

    @Override
    public boolean evaluate(Map<String, Boolean> value) {
        return (left.evaluate(value) || right.evaluate(value));
    }

    @Override
    public BooleanExpression toDNF() {
        return new Or(left.toDNF(), right.toDNF());
    }


}


