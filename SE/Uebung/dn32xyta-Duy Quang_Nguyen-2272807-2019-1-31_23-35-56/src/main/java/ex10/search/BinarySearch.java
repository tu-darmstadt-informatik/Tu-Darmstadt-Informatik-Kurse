package ex10.search;

import ex10.annotations.Algorithm;
import ex10.annotations.ConcreteStrategy;

@ConcreteStrategy
public class BinarySearch implements Searching {

    @Algorithm
    @Override
    public int search(String[] a, String s) {
        int left = 0;
        int right = a.length - 1;

        while(left <= right) {
            int mid = (left + right) / 2;
            if(a[mid].compareTo(s) < 0)
                left = mid + 1;
            else if(a[mid].compareTo(s) > 0)
                right = mid - 1;
            else return mid;
        }

        return -1;
    }
}
