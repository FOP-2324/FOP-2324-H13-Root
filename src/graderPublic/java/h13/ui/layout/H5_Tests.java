package h13.ui.layout;

import h13.rubric.H13_Tests;
import h13.util.Links;
import org.testfx.framework.junit5.ApplicationTest;
import org.tudalgo.algoutils.tutor.general.reflections.PackageLink;
import org.tudalgo.algoutils.tutor.general.reflections.TypeLink;

import java.util.Map;

public abstract class H5_Tests extends ApplicationTest implements H13_Tests {

    protected static final PackageLink PACKAGE_LINK = Links.getPackage(BASE_PACKAGE_LINK, "ui", "layout");

    @Override
    public PackageLink getPackageLink() {
        return PACKAGE_LINK;
    }

    public abstract TypeLink getTypeLink();

    @Override
    public Map<String, String> getContextInformation() {
        return Map.of();
    }
}
