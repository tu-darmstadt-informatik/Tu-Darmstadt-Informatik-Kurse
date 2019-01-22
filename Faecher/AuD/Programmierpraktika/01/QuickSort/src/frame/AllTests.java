package frame;

import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import lab.SortingItem;

/**
 * Do NOT change anything in this class!
 * 
 * The test cases defined by this class are used to test if the input file was
 * correctly sorted. This class is also responsible for outputting to the
 * console.
 * 
 */

@DisplayName("QuickSort tests")
class AllTests {

	protected static int NrOfTestFiles;
	protected int correct = 0;
	protected Duration timeout = Duration.ofSeconds(3);

	static class TestFileProvider implements ArgumentsProvider {

		@Override
		public Stream<Arguments> provideArguments(ExtensionContext context) throws Exception {
			File dir = new File(System.getProperty("user.dir"));
			FilenameFilter filter;
			if (context.getParent().get().getDisplayName().startsWith("QuickSortB Complexity")) {
				filter = new FilenameFilter() {
					public boolean accept(File dir, String name) {
						return name.startsWith("TestFile") && !name.contains("_d_");
					}
				};
			} else {
				filter = new FilenameFilter() {
					public boolean accept(File dir, String name) {
						return name.startsWith("TestFile");
					}
				};
			}
			String[] inputFiles = dir.list(filter);
			if (inputFiles == null) {
				throw new FileNotFoundException("Error: No TestFiles found!");
			} else {
				Arrays.sort(inputFiles);
				NrOfTestFiles = inputFiles.length;
				List<Arguments> tests = new ArrayList<Arguments>();
				for (int i = 0; i < inputFiles.length; i++) {
					tests.add(Arguments.of(inputFiles[i], SortingLab.readFile(inputFiles[i])));
				}
				return tests.stream();
			}
		}
	}

	protected boolean sortingTester(SortArray records) {
		boolean sorted = true;
		SortingItem lastRecord = records.getElementAt(0);
		for (int i = 1; i < records.getNumberOfItems() && sorted; i++) {
			SortingItem actualRecord = records.getElementAt(i);
			sorted = (actualRecord.BookSerialNumber.compareTo(lastRecord.BookSerialNumber) > 0)
					|| ((actualRecord.BookSerialNumber.compareTo(lastRecord.BookSerialNumber) == 0)
							&& ((actualRecord.ReaderID.compareTo(lastRecord.ReaderID) > 0)
									|| ((actualRecord.ReaderID.compareTo(lastRecord.ReaderID) == 0))));
			lastRecord = actualRecord;
		}
		return sorted;
	}

	@Nested
	@TestInstance(Lifecycle.PER_CLASS)
	@DisplayName("QuickSortA Sorting")
	class QuickSortASortingTest {

		@BeforeAll
		public void init() {
			correct = 0;
			System.out.println("Starting QuicksortA tests!");
		}

		@AfterAll
		public void tearDown() {
			System.out.println("Correct QuicksortA sortings: " + correct + " out of " + NrOfTestFiles + " tests\n");
		}

		@DisplayName("Tests")
		@ParameterizedTest(name = "QuicksortA sorting test with input: {0}")
		@ArgumentsSource(TestFileProvider.class)
		public void testQuicksortA(String inputFile, SortArray records) {
			assertTimeoutPreemptively(timeout, () -> {
				SortingLab.QuicksortA(records, 0, records.getNumberOfItems() - 1);
			}, () -> {
				System.out.println("QuicksortA [" + inputFile + "]: Execution timed out after: " + timeout.getSeconds()
						+ " seconds");
				return "Test failed!";
			});
			int readOps = records.getReadingOperations();
			int writeOps = records.getWritingOperations();
			assertTrue(sortingTester(records), () -> {
				System.out.println("QuicksortA [" + inputFile + "]: Wrong order!");
				return "Test failed!";
			});
			System.out.println(
					"QuicksortA [" + inputFile + "]: Correct order! Read Ops: " + readOps + "; Write Ops: " + writeOps);
			correct++;
		}
	}

	@Nested
	@TestInstance(Lifecycle.PER_CLASS)
	@DisplayName("QuickSortB Sorting")
	class QuickSortBSortingTest {

		@BeforeAll
		public void init() {
			correct = 0;
			System.out.println("Starting QuicksortB tests!");
		}

		@AfterAll
		public void tearDown() {
			System.out.println("Correct QuicksortB sortings: " + correct + " out of " + NrOfTestFiles + " tests\n");
		}

		@DisplayName("Tests")
		@ParameterizedTest(name = "QuicksortB  sorting test with input: {0}")
		@ArgumentsSource(TestFileProvider.class)
		public void testQuicksortB(String inputFile, SortArray records) {
			assertTimeoutPreemptively(timeout, () -> {
				SortingLab.QuicksortB(records, 0, records.getNumberOfItems() - 1);
			}, () -> {
				System.out.println("QuicksortB [" + inputFile + "]: Execution timed out after: " + timeout.getSeconds()
						+ " seconds");
				return "Test failed!";
			});
			int readOps = records.getReadingOperations();
			int writeOps = records.getWritingOperations();
			assertTrue(sortingTester(records), () -> {
				System.out.println("QuicksortB [" + inputFile + "]: Wrong order!");
				return "Test failed!";
			});
			System.out.println(
					"QuicksortB [" + inputFile + "]: Correct order! Read Ops: " + readOps + "; Write Ops: " + writeOps);
			correct++;
		}
	}

	@Nested
	@TestInstance(Lifecycle.PER_CLASS)
	@DisplayName("QuickSortA Complexity")
	class QuickSortAComplexityTest {

		@BeforeAll
		public void init() {
			correct = 0;
			System.out.println("Starting QuicksortA complexity tests!");
		}

		@AfterAll
		public void tearDown() {
			System.out.println(
					"Passed complexity tests for QuicksortA: " + correct + " out of " + NrOfTestFiles + " tests\n");
		}

		private void complexityTesterA(SortArray records, String inputFile, int readOps) {
			int n = records.getNumberOfItems();
			double nlogn = n * (Math.log(n) / Math.log(2)) * 5;
			assertTrue(readOps > 0);
			if (inputFile.contains("_r_")) {
				assertTrue(readOps < nlogn, () -> {
					System.out.println("QuickSortA complexity test failed for file: " + inputFile
							+ " - complexity out of allowed range: O(nlog(n)) required!");
					return "Test failed!";
				});
			} else if (inputFile.contains("_a_") || inputFile.contains("_d_")) {
				assertTrue(readOps > Math.pow(n, 2) / 2, () -> {
					System.out.println("QuickSortA complexity test failed for file: " + inputFile
							+ " - complexity out of allowed range: O(n^2) required!");
					return "Test failed!";
				});
			}
			correct++;
		}

		@DisplayName("Tests")
		@ParameterizedTest(name = "QuicksortA complexity test with input: {0}")
		@ArgumentsSource(TestFileProvider.class)
		public void testQuicksortAComplexity(String inputFile, SortArray records) {
			assertTimeoutPreemptively(timeout, () -> {
				SortingLab.QuicksortA(records, 0, records.getNumberOfItems() - 1);
			}, () -> {
				System.out.println("Complexity QuicksortA [" + inputFile + "]: Execution timed out after: "
						+ timeout.getSeconds() + " seconds");
				return "Test failed!";
			});
			int readOps = records.getReadingOperations();
			complexityTesterA(records, inputFile, readOps);
			System.out.println("Complexity QuicksortA [" + inputFile + "]: Complexity within allowed range!");
		}
	}

	@Nested
	@TestInstance(Lifecycle.PER_CLASS)
	@DisplayName("QuickSortB Complexity")
	class QuickSortBComplexityTest {

		@BeforeAll
		public void init() {
			correct = 0;
			System.out.println("Starting QuicksortB complexity tests!");
		}

		@AfterAll
		public void tearDown() {
			System.out.println(
					"Passed complexity tests for QuicksortB: " + correct + " out of " + NrOfTestFiles + " tests\n");
		}

		private void complexityTesterB(SortArray records, String inputFile, int readOps) {
			int n = records.getNumberOfItems();
			double nlogn = n * (Math.log(n) / Math.log(2)) * 5;

			assertTrue(readOps > 0);
			assertTrue(readOps < nlogn, () -> {
				System.out.println("QuickSortB complexity test failed for file: " + inputFile
						+ " - complexity out of allowed range: O(nlog(n)) required!");
				return "Test failed!";
			});
			correct++;
		}

		@DisplayName("Tests")
		@ParameterizedTest(name = "QuicksortB complexity test with input: {0}")
		@ArgumentsSource(TestFileProvider.class)
		public void testQuicksortBComplexity(String inputFile, SortArray records) {
			assertTimeoutPreemptively(timeout, () -> {
				SortingLab.QuicksortB(records, 0, records.getNumberOfItems() - 1);
			}, () -> {
				System.out.println("Complexity QuicksortB [" + inputFile + "]: Execution timed out after: "
						+ timeout.getSeconds() + " seconds");
				return "Test failed!";
			});
			int readOps = records.getReadingOperations();
			complexityTesterB(records, inputFile, readOps);
			System.out.println("Complexity QuicksortB [" + inputFile + "]: Complexity within allowed range!");
		}
	}
}