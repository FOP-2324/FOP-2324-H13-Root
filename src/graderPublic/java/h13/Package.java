package h13;

public enum Package {

    H13("h13"),
    NOISE(H13, "noise"),
    ;
    private final String name;

    Package(String name) {
        this.name = name;
    }

    Package(Package p, String name) {
        this.name = p.name + "." + name;
    }

    @Override
    public String toString() {
        return name;
    }
}
