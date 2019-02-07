package ex10.search;


import ex10.annotations.Algorithm;
import ex10.annotations.Strategy;

@Strategy
public interface Searching {

    @Algorithm
    <T> int search(String[] a, String s);
}
