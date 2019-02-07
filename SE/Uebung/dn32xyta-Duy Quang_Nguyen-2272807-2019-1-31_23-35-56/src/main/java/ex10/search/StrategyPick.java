package ex10.search;

import ex10.annotations.Context;

@Context
public class StrategyPick {
    private Searching sorting;

    public StrategyPick(Searching sorting) {
        this.sorting = sorting;
    }

    public int execute(String[] a, String s) {
        return sorting.search(a, s);
    }
}
