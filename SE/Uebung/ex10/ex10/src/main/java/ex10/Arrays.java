package ex10;

import ex10.comparators.Comparator;

public abstract class Arrays{
    /**
     * Sort an array of Objects according to a Comparator. The sort is
     * guaranteed to be stable, that is, equal elements will not be reordered.
     * The sort algorithm is a mergesort with the merge omitted if the last
     * element of one half comes before the first element of the other half. This
     * algorithm gives guaranteed O(n*log(n)) time, at the expense of making a
     * copy of the array.
     *
     * @param a the array to be sorted
     * @param c a Comparator to use in sorting the array; or null to indicate
     *        the elements' natural order
     * @throws ClassCastException if any two elements are not mutually
     *         comparable by the Comparator provided
     * @throws NullPointerException if a null element is compared with natural
     *         ordering (only possible when c is null)
     */
    public <T> void sort(T[] a, Comparator<? super T> c) {
    	if(a == null) 
    		throw new NullPointerException("null is not a valid parameter for array a");
    	else if(c == null)
    		throw new NullPointerException("null is not a valid parameter for comaprator c");
    	if(a.length >= 2) {
    		int toIndex = a.length-1;
    		sort(a, 0, toIndex, c);
    	}
    }

    public abstract <T> void sort(T[] a, int fromIndex, int toIndex, Comparator<? super T> c);
}