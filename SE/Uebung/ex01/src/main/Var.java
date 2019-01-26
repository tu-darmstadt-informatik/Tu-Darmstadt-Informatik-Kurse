package main;
/**
 * @author Quang Duy Nguyen, Egemen Ulutürk, Leonard Bongard
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
     * @return Gibt den Namen der Variable(String) zurück
     */
    public String getName(){
        return name;
    }

    /**
     * 
     * @return Gibt den Namen als Variable zurück
     */
    @Override
    public String toPostfixString() {
        return name;
    }

    /**
     * 
     * @param gibt den Namen der value zurück
     */
    @Override
    public boolean evaluate(Map<String, Boolean> value) {
        return value.get(name);
    }

    /**
     * 
     * @returngibt den aktuellen Ausdruck zurück
     */
    @Override
    public BooleanExpression toDNF(){
        return this;
    }

}
