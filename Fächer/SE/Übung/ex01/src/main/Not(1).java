package main;

import java.util.Map;

public class Not implements BooleanExpression {

    private final BooleanExpression op;

    public Not(BooleanExpression op){
        this.op = op;
    }

    public BooleanExpression getOp() {
        return op;
    }


    @Override
    public String toPostfixString() {
        return (op.toPostfixString() + " !");
    }

    @Override
    public boolean evaluate(Map<String, Boolean> value) {
        return !op.evaluate(value);
    }

    @Override
    public BooleanExpression toDNF(){
        if(op instanceof Var) return this;

        else if(op instanceof Not) return ((Not) op).getOp().toDNF();

        else if(op instanceof And) {
            Or out = new Or(new Not(((And) op).getLeftOp()), new Not(((And) op).getRightOp()));
            return out;
        }
        else {
            And out = new And(new Not(((Or) op).getLeftOp()), new Not(((Or) op).getRightOp()));
            return out;
        }
    }

}
