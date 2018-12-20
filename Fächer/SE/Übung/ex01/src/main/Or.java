package main;
/**
 * @author Quang Duy Nguyen, Egemen Ulutürk, Leonard Bongard
 * @version 1.3.31012018
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/*
 * Klasse OR beschreibt die Disjunktion von 2 BooleanExpression
 */
public class Or implements BooleanExpression {

    private final BooleanExpression left;
    private final BooleanExpression right;

    public Or(BooleanExpression left, BooleanExpression right) {
        this.left = left;
        this.right = right;
    }
    
    /**
     * 
     * @return Gibt den linken Operanden(BooleanExpression) zurück
     */
    public BooleanExpression getLeftOp() {
        return left;
    }

    /**
     * 
     * @return Gibt den rechten Operanden(BooleanExpression) zurück
     */
    public BooleanExpression getRightOp() {
        return right;
    }

    /**
     * 
     * @return Verwandelt die BooleanExpression in einen String in Postfix Notation
     */
    @Override
    public String toPostfixString() {
        return (left.toPostfixString() + " " + right.toPostfixString() + " |");
    }

    /**
     * 
     * @param führt ein logisches ODER aus und gibt das Ergebnis zurück
     */
    @Override
    public boolean evaluate(Map<String, Boolean> value) {
        return (left.evaluate(value) || right.evaluate(value));
    }
    
    /**
     * @return Gibt eine Liste zurück, die die Verkettung der Ergebnisse des Aufrufs von disjunctiveTerms auf beiden Teilausdrücken darstellt
     */
    @Override
    public List<BooleanExpression> disjunctiveTerms(){
        List<BooleanExpression> left = new ArrayList<>();
        List<BooleanExpression> right = new ArrayList<>();

        left.addAll((((Or) this).getLeftOp()).disjunctiveTerms());
        right.addAll((((Or) this).getRightOp()).disjunctiveTerms());

        left.addAll(right);

        return left;
    }

    /**
     * 
     * @return Gibt ein neues OR zurück 
     */
    @Override
    public BooleanExpression toDNF() {
        return new Or(left.toDNF(), right.toDNF());
    }


}


