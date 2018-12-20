package main;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public interface BooleanExpression {

    static BooleanExpression parseExpression(String input){
        Stack<BooleanExpression> stack = new Stack<>();

        char[] inputChar = input.toCharArray();

        for(char chars : inputChar){

            switch(chars) {

                case 32:
                    continue;

                case 38:
                    if(stack.size() < 2) throw new IllegalArgumentException();
                    stack.push(new And(stack.pop(), stack.pop()));
                    break;

                case 124:
                    if(stack.size() < 2) throw new IllegalArgumentException();
                    stack.push(new Or(stack.pop(), stack.pop()));
                    break;

                case 33:
                    if(stack.size() < 1) throw new IllegalArgumentException();
                    stack.push(new Not(stack.pop()));
                    break;

                default:
                    stack.push(new Var(Character.toString(chars)));
            }
        }

        if(stack.size() != 1) throw new IllegalArgumentException();
        else return stack.get(0);
    }

    default List<BooleanExpression> disjunctiveTerms() {
        if(this instanceof Or){
            List<BooleanExpression> left = new ArrayList<>();
            List<BooleanExpression> right = new ArrayList<>();

            left.addAll((((Or) this).getLeftOp()).disjunctiveTerms());
            right.addAll((((Or) this).getRightOp()).disjunctiveTerms());

            left.addAll(right);

            return left;
        }
        else {
            List<BooleanExpression> output = new ArrayList<>();
            output.add(this);
            return output;
        }
    }

    String toPostfixString();

    boolean evaluate(Map<String, Boolean> value);

    BooleanExpression toDNF();

}