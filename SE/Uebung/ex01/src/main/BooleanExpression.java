package main;
/**
 * @author Quang Duy Nguyen, Egemen Ulutürk, Leonard Bongard
 * @version 1.3.31012018
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
/*
 * Implementierung des Interfaces wie es in Aufabe 2 vorgegeben wird
 * 
 */
public interface BooleanExpression {

	/**
	 * Parsed den Char Array hintereinander aus und wertet die Ausdrücke aus
	 * @param input ein boolscher Ausdruck in Postfixnotation
	 * @return Stack aus Ausdrücken(BooleanExpression) zurück
	 */
    static BooleanExpression parseExpression(String input){
        Stack<BooleanExpression> stack = new Stack<>();

        char[] inputChar = input.toCharArray();

        for(char chars : inputChar){

        	 
            switch(chars) { // Es wird zwischen And Or Not Var Leerzeichen Fall unterschieden

                case 32:
                    continue; // Aktuelles Leerzeichen übersprungen

                case 38: // stack >= 2. Dann werden die 2 obersten Elemente genommen. diese werden als AndExpression wieder auf den Stack zurückgegeben.
                		// Falls nicht genug Elemente im Stack vorhanden sind wird eine Exception geworfen
                    if(stack.size() < 2) throw new IllegalArgumentException();
                    stack.push(new And(stack.pop(), stack.pop()));
                    break;

                case 124: // Stack >= 2. Dann werden die 2 obersten Elemente genommen. diese werden als ORExpression wieder auf den Stack zurückgegeben. 
                	      // Falls nicht genug Elemente im Stack vorhanden sind wird eine Exception geworfen
                    if(stack.size() < 2) throw new IllegalArgumentException();
                    stack.push(new Or(stack.pop(), stack.pop()));
                    break;

                case 33:// Das oberste Element des Stacks wird als NotExpression wieder auf den Stack zurückgegeben
                		// Falls nicht genug Elemente im Stack vorhanden sind, wird eine Exception geworfen
                    if(stack.size() < 1) throw new IllegalArgumentException();
                    stack.push(new Not(stack.pop()));
                    break;

                default: //Das Zeichen wird auf den Stack gelegt
                    stack.push(new Var(Character.toString(chars)));
            }
        }

        if(stack.size() != 1) throw new IllegalArgumentException();// Am ende der Operation, ist die falsche Anzahl an Ausdrücken auf den Stack und deswegen wird eine Exception geworfen
        else return stack.get(0); // das letzte Element des Stacks wird zurückgegeben
    }

    /**
     * 
     * @return eine Liste die den aktuellen Ausdruck zurückgibt
     */
    default List<BooleanExpression> disjunctiveTerms() {
            List<BooleanExpression> output = new ArrayList<>();
            output.add(this);
            return output;
    }

    /**
     * 
     * @return Gibt das BooleanExpression als Postfix im Typ String zurück
     */
    String toPostfixString();

    /**
     * virtuelle Methode 
     * @param eine Map mit variablennamen und der jeweiligen BooleanExpression
     * @return liefert den aktuellen Ausdruck zurück
     */
    boolean evaluate(Map<String, Boolean> value);

    /**
     * 
     * @return gibt die BooleanExpression in DNF zurück
     */
    BooleanExpression toDNF();

}