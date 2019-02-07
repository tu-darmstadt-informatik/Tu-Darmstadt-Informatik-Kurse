package ex10;

import ex10.comparators.Comparator;

import java.util.Arrays;

public class Main {
    public static void main(String args[]) {
        String[] compareArrays = new String[args.length - 1];
        System.arraycopy(args, 1, compareArrays, 0, args.length - 1);
        sort(compareArrays, args[0]);

        System.out.println("Array sorted: " + Arrays.toString(compareArrays));
    }

    private static void sort(String[] compareArrays, String arg) {
        Comparator<String> comparator = Comparator.getStringComparatorByName(arg);
        MergeSortArrays sorter = new MergeSortArrays();
        sorter.sort(compareArrays, comparator);
    }
}
