package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/*
 * @author Quang Duy Nguyen, Egemen Ulutürk, Leonard Bongard
 * @version 1.3.31012018
 * Klasse And beschreibt die Konjunktion von 2 BooleanExpression 
 * 
 */
public class And implements BooleanExpression {

    private final BooleanExpression left;
    private final BooleanExpression right;

    public And(BooleanExpression left, BooleanExpression right){
        this.left = left;
        this.right = right;
    }
    
    /**
     * 
     * @return Gibt den linken Operanden(BooleanExpression) zurück
     */
    public BooleanExpression getLeftOp(){
        return left;
    }
    /**
     * 
     * @return Gibt den rechten Operanden(BooleanExpression) zurück
     */
    public BooleanExpression getRightOp(){
        return right;
    }

    /**
     * 
     * @return Verwandelt die BooleanExpression in einen String in Postfix Notation
     */
    @Override
    public String toPostfixString() {
        return (left.toPostfixString() + " " + right.toPostfixString() + " &");
    }

    /**
     * 
     * @param führt ein logisches UND aus und gibt das Ergebnis zurück
     */
    @Override
    public boolean evaluate(Map<String, Boolean> value) {
        return (left.evaluate(value) && right.evaluate(value));
    }

    /**
     * 
     * @return eine BooleanExpression. Im code mehr spezifiert
     */
    @Override
    public BooleanExpression toDNF(){
        if(!(left instanceof Or || right instanceof Or)) { // Überprüft ob mindestens 1 Element vom typ OR ist
            return new And(left, right); // gibt ein neues AND zurück
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

            return out; //gibt ein neues OR zurück, welches aus ANDs besteht
        }
    }
}
