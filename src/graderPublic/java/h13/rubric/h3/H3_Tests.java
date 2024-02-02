package h13.rubric.h3;

import h13.rubric.H13_Tests;
import h13.util.Links;
import org.tudalgo.algoutils.tutor.general.reflections.PackageLink;

public abstract class H3_Tests implements H13_Tests {

    protected static final PackageLink PACKAGE_LINK = Links.getPackage(BASE_PACKAGE_LINK, "ui", "controls");

    @Override
    public PackageLink getPackageLink() {
        return PACKAGE_LINK;
    }
}
