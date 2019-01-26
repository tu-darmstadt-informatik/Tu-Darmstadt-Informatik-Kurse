package frame;

import java.util.ArrayList;

import lab.SortingItem;

/**
 * Do NOT change anything in this class!
 * 
 * The SortArray class provides simple basic functions, to store a list of
 * sortingItems to track the number of operations.
 * 
 * This class contains two members (readingOperations and writingOperations)
 * that act as counters for the number of accesses to the arrays to be sorted.
 * These are used by the JUnit tests to construct the output. The methods
 * provided in this class should be sufficient for you to sort the records of
 * the input files.
 * 
 * @author Stefan Kropp
 */

public class SortArray {

	private int numberOfItems;

	private ArrayList<SortingItem> listOfItems;

	private int readingOperations;
	private int writingOperations;

	/**
	 * @param numberOfItems
	 *            number of items to hold
	 */
	public SortArray(ArrayList<String[]> items) {
		numberOfItems = items.size();
		readingOperations = 0;
		writingOperations = 0;
		listOfItems = new ArrayList<>();

		for (String[] element : items) {
			SortingItem s = new SortingItem();
			s.BookSerialNumber = element[0];
			s.ReaderID = element[1];
			s.Status = element[2];
			listOfItems.add(s);
		}
	}

	/**
	 * sets the elements at index. if index is >= numberOfItems or less then
	 * zero an IndexOutOfBoundException will occur.
	 * 
	 * @param index
	 *            the index of the Elements to set
	 * @param record
	 *            a 3-dimensional record which holds: BookSerialNumber,
	 *            ReaderID, Status
	 */
	public void setElementAt(int index, SortingItem record) {
		this.listOfItems.set(index, record);

		writingOperations++;
	}

	/**
	 * Retrieves the information stored at position Index. if index is >=
	 * numberOfItems or less then zero an IndexOutOfBoundException will occur.
	 * 
	 * @param index
	 *            Index defines which elements to retrieve from the SortArray
	 * @return Returns a 3-dimensional String array with following format:
	 *         BookSerialNumber, ReaderID, Status.
	 * 
	 */
	public SortingItem getElementAt(int index) {

		SortingItem result = new SortingItem(this.listOfItems.get(index));
		readingOperations++;
		return result;
	}

	/**
	 * @return Returns the number of reading operations.
	 */
	public int getReadingOperations() {
		return readingOperations;
	}

	/**
	 * @return Returns the number of writing operations.
	 */
	public int getWritingOperations() {
		return writingOperations;
	}

	/**
	 * @return Returns the numberOfItems.
	 */
	public int getNumberOfItems() {
		return numberOfItems;
	}
}
