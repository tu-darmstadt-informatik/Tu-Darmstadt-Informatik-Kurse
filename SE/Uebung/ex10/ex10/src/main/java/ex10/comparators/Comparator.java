package ex10.comparators;

public interface Comparator<T> {

    int compare(T o1, T o2);

    static Comparator<String> getStringComparatorByName(String comp) {
        comp = comp.toLowerCase();
        switch (comp) {
            case "lexicographical":
                return new LexicographicalComparator();

            case "length":
                return new LengthComparator();

            case "ordinal":
                return new OrdinalComparator();

            default:
                return new LexicographicalComparator();
        }
    }
}
