package main;

/**
 * @author Quang Duy Nguyen, Egemen Ulutürk, Leonard Bongard
 * @version 1.3.31012018
 */
import java.util.Map;
/*
 * Klasse Not beschreibt eine negierte BooleanExpression
 * 
 */
public class Not implements BooleanExpression {

    private final BooleanExpression op;

    public Not(BooleanExpression op){
        this.op = op;
    }

    /**
     * 
     * @return Gibt den Operanden(BooleanExpression) zurück
     */
    public BooleanExpression getOp() {
        return op;
    }

    /**
     * 
     * @return Verwandelt die BooleanExpression in einen String in Postfix Notation
     */
    @Override
    public String toPostfixString() {
        return (op.toPostfixString() + " !");
    }

    /**
     * @return negiert die BooleanExpression
     */
    @Override
    public boolean evaluate(Map<String, Boolean> value) {
        return !op.evaluate(value);
    }

    
    /**
     * Fallunterscheidung
     * @return Booleanexpression im code einzeln behandelt
     */
    @Override
    public BooleanExpression toDNF(){
        if(op instanceof Var) return this; // gibt den aktuellen Ausdruck zurück

        else if(op instanceof Not) 
        	return ((Not) op).getOp().toDNF();//toDNF wird aufgerufen und der Wert zurückgegeben

        else if(op instanceof And) {
            Or out = new Or(new Not(((And) op).getLeftOp()), new Not(((And) op).getRightOp()));
            return out; //Es wird ein neues OR Objekt erzeugt aus der Negierung der linken und rechten Boolean Expression und zurückgegeben
        }
           else {
            And out = new And(new Not(((Or) op).getLeftOp()), new Not(((Or) op).getRightOp()));
            return out;// Es wird ein neues AND Objekt erzeugt aus der Negierung der linken und rechten Boolean Expression und zurückgegeben
        }
    }

}
