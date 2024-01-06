package h13;

/**
 * Represents all relevant packages for the grader.
 *
 * @author Nhan Huynh
 */
public enum Package {

    /**
     * The root package.
     */
    H13("h13"),

    /**
     * The noise package which is relevant for task H1 and H2.
     */
    NOISE(H13, "noise"),
    ;

    /**
     * The name of the package.
     */
    private final String name;

    /**
     * Constructs a package with the given name.
     *
     * @param name the name of the package
     */
    Package(String name) {
        this.name = name;
    }

    /**
     * Constructs a package with the given name in the given package.
     *
     * @param p    the package in which the new package is located
     * @param name the name of the package
     */
    Package(Package p, String name) {
        this.name = p.name + "." + name;
    }

    @Override
    public String toString() {
        return name;
    }

}
