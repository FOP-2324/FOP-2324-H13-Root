package h13.utils;

/**
 * Utility class for String operations.
 *
 * @author Nhan Huynh
 */
public final class Strings {

    /**
     * Don't let anyone instantiate this class.
     */
    private Strings() {
    }

    /**
     * Capitalizes a string.
     *
     * @param string the string to capitalise
     * @return the capitalised string
     */
    public static String capitalize(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase();
    }

}
