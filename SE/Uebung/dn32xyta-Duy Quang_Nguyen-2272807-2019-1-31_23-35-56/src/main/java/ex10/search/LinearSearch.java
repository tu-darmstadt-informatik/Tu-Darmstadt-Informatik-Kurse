package ex10.search;

import ex10.annotations.Algorithm;
import ex10.annotations.ConcreteStrategy;

@ConcreteStrategy
public class LinearSearch implements Searching {

    @Algorithm
    @Override
    public int search(String[] a, String s) {
        int index = -1;
        for(int i = 0; i < a.length; i++) {
            if(a[i].compareTo(s) == 0){
                index = i;
                break;
            }
        }
        return index;
    }
}
