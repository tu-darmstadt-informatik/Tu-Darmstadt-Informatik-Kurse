package main;

import java.util.Map;

public class Var implements BooleanExpression {

    private final String name;

    public Var(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    @Override
    public String toPostfixString() {
        return name;
    }

    @Override
    public boolean evaluate(Map<String, Boolean> value) {
        return value.get(name);
    }

    @Override
    public BooleanExpression toDNF(){
        return this;
    }

}
