package main;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Starter {

    public static void main(String[] args){
        String toRead = "A!!!!!!";
        Map<String, Boolean> values = new HashMap<>();

        values.put("A", true);
        values.put("B", false);
        values.put("C", false);
        values.put("D", true);
        values.put("E", true);
        values.put("F", false);

        BooleanExpression bla = BooleanExpression.parseExpression(toRead).toDNF();
            System.out.println(bla.toPostfixString());

        System.out.println(BooleanExpression.parseExpression((toRead)).toPostfixString());
    }
}
