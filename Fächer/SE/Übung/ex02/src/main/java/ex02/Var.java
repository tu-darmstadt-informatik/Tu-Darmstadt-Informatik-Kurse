package ex02;

import java.util.Map;

public class Var implements BooleanExpression {
    private final String name;

    public Var(String _name) {
        name = _name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toPostfixString() {
        return name;
    }

    /**
     * @return returns the variable name
     */
    @Override
    public String toPrefixString() {return name;}

    /**
     * @return returns the variable name
     */
    @Override
    public String toInfixString(){
        return name;
    }

    @Override
    public boolean evaluate(Map<String, Boolean> argumentMap) {
        return argumentMap.get(name);
    }

    @Override
    public BooleanExpression toDNF() {
        return this;
    }

    @Override
    public boolean isVar() {
        return true;
    }
}
