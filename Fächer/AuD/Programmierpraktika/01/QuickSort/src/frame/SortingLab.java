package frame;

import lab.QuickSort;
import lab.QuickSortA;
import lab.QuickSortB;

public class SortingLab {

	/**
	 * Reads the file specified in filename and returns the data as a SortArray
	 * 
	 * @param filename
	 *            the path and name of the file to read
	 * @return Returns a SortArray filled with the data of the input file
	 */
	public static SortArray readFile(String filename) {
		LibraryFileReader fileReader = new LibraryFileReader(filename);
		return new SortArray(fileReader.readFile());
	}

	static QuickSort sortingMethod;

	/**
	 * Uses the Quicksort A algorithm to sort the records (the pivot is the
	 * first element in the list)
	 * 
	 * @param records
	 *            unsorted SortArray data
	 * @param left
	 *            the left bound for the algorithm
	 * @param right
	 *            the right bound for the algorithm
	 * @return Returns the sorted SortArray
	 */
	public static SortArray QuicksortA(SortArray records, int left, int right) {
		
		sortingMethod = new QuickSortA();
		sortingMethod.Quicksort(records, left, right);
		return records;
	}

	/**
	 * Uses the Quicksort B algorithm to sort the records (the pivot is the
	 * median between first, last and middle element in the list)
	 * 
	 * @param records
	 *            unsorted SortArray data
	 * @param left
	 *            the left bound for the algorithm
	 * @param right
	 *            the right bound for the algorithm
	 * @return Returns the sorted SortArray
	 */
	public static SortArray QuicksortB(SortArray records, int left, int right) {

		sortingMethod = new QuickSortB();
		sortingMethod.Quicksort(records, left, right);
		return records;
	}

}
