package ex10.comparators;

public class LexicographicalComparator implements Comparator<String> {
    @Override
    public int compare(String o1, String o2) {
        return o1.compareToIgnoreCase(o2);
    }

    @Override
    public String toString() {
        return "Comparator Lexicographical";
    }
}
