package ex10.comparators;

public class OrdinalComparator implements Comparator<String> {
    private boolean isValidInteger(String str) {
        boolean isValidInteger;
        try {
            Integer.parseInt(str);
            isValidInteger = true;
        } catch (NumberFormatException ex) {
            isValidInteger = false;
        }

        return isValidInteger;
    }

    @Override
    public int compare(String o1, String o2) {

        boolean o1HasValidInteger = isValidInteger(o1);
        boolean o2HasValidInteger = isValidInteger(o2);

        if (!o1HasValidInteger && !o2HasValidInteger) {
            return o1.compareToIgnoreCase(o2);
        }

        if (o1HasValidInteger && !o2HasValidInteger) {
           return -1;
        }

        if (!o1HasValidInteger && o2HasValidInteger) {
           return 1;
        }

        int o1Number = Integer.parseInt(o1);

        int o2Number = Integer.parseInt(o2);

        return o1Number - o2Number;
    }

    @Override
    public String toString() {
        return "Comparator Ordinal";
    }
}
