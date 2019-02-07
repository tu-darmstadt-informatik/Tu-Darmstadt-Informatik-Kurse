package ex10;

import ex10.comparators.Comparator;

public class MergeSortArrays extends Arrays {
    @Override Design Pattern: Template Method Pattern
    private <T> void mergeSort(T[] a, Comparator<? super T> c, int l, int r) {

        if (l < r) {
            int q = (l + r) / 2;

            mergeSort(a, c, l, q);
            mergeSort(a, c, q + 1, r);
            merge(a, c, l, q, r);
        }
    }

    @Override Design Pattern: The Composite Design Pattern
    private <T> void merge(T[] toMerge, Comparator<? super T> c, int l, int q, int r) {
        T[] arr = java.util.Arrays.copyOf(toMerge, toMerge.length);
        int i, j;
        for (j = q + 1; j <= r; j++) {
            arr[r + q + 1 - j] = toMerge[j];
        }
        i = l;
        j = r;
        for (int k = l; k <= r; k++) {
            if (c.compare(arr[i], arr[j]) <= 0) {
                toMerge[k] = arr[i];
                i++;
            } else {
                toMerge[k] = arr[j];
                j--;
            }
        }
    }

    @Override Design Pattern: Strategy Design Pattern;
    public <T> void sort(T[] a, int fromIndex, int toIndex, Comparator<? super T> c) {
        mergeSort(a, c, fromIndex, toIndex);
    }
}
