package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class And implements BooleanExpression {

    private final BooleanExpression left;
    private final BooleanExpression right;

    public And(BooleanExpression left, BooleanExpression right){
        this.left = left;
        this.right = right;
    }

    public BooleanExpression getLeftOp(){
        return left;
    }

    public BooleanExpression getRightOp(){
        return right;
    }

    @Override
    public String toPostfixString() {
        return (left.toPostfixString() + " " + right.toPostfixString() + " &");
    }

    @Override
    public boolean evaluate(Map<String, Boolean> value) {
        return (left.evaluate(value) && right.evaluate(value));
    }

    @Override
    public BooleanExpression toDNF(){
        BooleanExpression left = this.left.toDNF();
        BooleanExpression right = this.right.toDNF();

        if(!(left instanceof Or || right instanceof Or)) {
            return new And(left, right);
        }

        else {
            List<BooleanExpression> leftList = left.disjunctiveTerms();
            List<BooleanExpression> rightList = right.disjunctiveTerms();
            List<BooleanExpression> andList = new ArrayList<>();

            Or out;

            for(int i = 0; i < leftList.size(); i++){
                for(int j = 0; j < rightList.size(); j++) {
                    andList.add(new And(leftList.get(i), rightList.get(j)));
                }
            }

            out = new Or(andList.get(0), andList.get(0));

            for(int i = 1; i < andList.size(); i++){
                out = new Or(out, andList.get(i));
            }

            return out;
        }
    }
}
