package main;
/**
 * @author Quang Duy Nguyen, Egemen Ulut�rk, Leonard Bongard
 * @version 1.3.31012018
 */
import java.util.Map;
/*
 * Die KLasse Var ein BooleanExpression Literal
 */
public class Var implements BooleanExpression {

    private final String name;

    public Var(String name){
        this.name = name;
    }
    
    /**
     * 
     * @return Gibt den Namen der Variable(String) zur�ck
     */
    public String getName(){
        return name;
    }

    /**
     * 
     * @return Gibt den Namen als Variable zur�ck
     */
    @Override
    public String toPostfixString() {
        return name;
    }

    /**
     * 
     * @param gibt den Namen der value zur�ck
     */
    @Override
    public boolean evaluate(Map<String, Boolean> value) {
        return value.get(name);
    }

    /**
     * 
     * @returngibt den aktuellen Ausdruck zur�ck
     */
    @Override
    public BooleanExpression toDNF(){
        return this;
    }

}
