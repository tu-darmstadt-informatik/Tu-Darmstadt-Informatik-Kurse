package ex10.comparators;

public class LengthComparator implements Comparator<String> {
    @Override
    public int compare(String o1, String o2) {
        return o1.length() - o2.length();
    }

    @Override
    public String toString() {
        return "Comparator Length";
    }
}
