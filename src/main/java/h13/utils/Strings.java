package h13.utils;

public final class Strings {

    private Strings() {
    }

    public static String capitalize(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase();
    }

}
